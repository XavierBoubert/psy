package io.allonsy.kokoro.crise

import android.os.SystemClock
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.annotation.StringRes
import io.allonsy.kokoro.R
import io.allonsy.kokoro.tension.EtapeSoins
import io.allonsy.kokoro.tension.NOMBRE_CYCLES
import io.allonsy.kokoro.tension.PhaseTension
import io.allonsy.kokoro.tension.SECONDES_ASSIS_APRES
import io.allonsy.kokoro.tension.SEQUENCE_SOINS
import io.allonsy.kokoro.tension.cyclesDe
import io.allonsy.kokoro.tension.etapeAttendue
import io.allonsy.kokoro.tension.etatTension
import io.allonsy.kokoro.tension.fractionPhase
import io.allonsy.kokoro.tension.secondesDuBloc
import kotlinx.coroutines.delay

private const val PERIODE_TICK_MILLIS = 200L
private const val DUREE_TRANSITION_MILLIS = 800

private enum class VueTension { ACCUEIL, SEQUENCE, BLOC, ASSIS, PHRASE, ARRET }

private data class BlocEnCours(
    val debut: Long,
    val cycles: Int?,
    val etape: EtapeSoins?,
)

/**
 * @param ouvrirSurLaPhrase entre directement sur **la phrase pour le soignant**, sans passer par
 *   l'accueil de la tension appliquée. ⭐ **Le retour reste l'accueil** : venir la lire ne doit pas
 *   enfermer, et repartir de là est le chemin normal.
 */
@Composable
fun ContenuTension(onFermer: () -> Unit, ouvrirSurLaPhrase: Boolean = false) {
    var vue by remember {
        mutableStateOf(if (ouvrirSurLaPhrase) VueTension.PHRASE else VueTension.ACCUEIL)
    }
    var retour by remember { mutableStateOf(VueTension.ACCUEIL) }
    var bloc by remember { mutableStateOf<BlocEnCours?>(null) }
    var dernierFait by remember { mutableStateOf<EtapeSoins?>(null) }
    var debutAssis by remember { mutableLongStateOf(0L) }

    val allerVers: (VueTension) -> Unit = { destination ->
        retour = vue
        vue = destination
    }

    val demarrer: (EtapeSoins?) -> Unit = { etape ->
        bloc = BlocEnCours(
            debut = SystemClock.elapsedRealtime(),
            cycles = etape?.let { cyclesDe(it) } ?: NOMBRE_CYCLES,
            etape = etape,
        )
        if (etape != null) dernierFait = etape
        vue = VueTension.BLOC
    }

    val quitterBloc: () -> Unit = {
        val depuisSequence = bloc?.etape != null
        bloc = null
        vue = if (depuisSequence) VueTension.SEQUENCE else VueTension.ACCUEIL
    }

    val enCours = bloc
    when (if (vue == VueTension.BLOC && enCours == null) VueTension.ACCUEIL else vue) {
        VueTension.ACCUEIL -> VueAccueil(
            onDemarrer = { demarrer(null) },
            onSequence = { vue = VueTension.SEQUENCE },
            onPhrase = { allerVers(VueTension.PHRASE) },
            onArret = { allerVers(VueTension.ARRET) },
            onFermer = onFermer,
        )

        VueTension.SEQUENCE -> VueSequence(
            attendue = etapeAttendue(dernierFait),
            onRepere = { demarrer(it) },
            onPhrase = { allerVers(VueTension.PHRASE) },
            onArret = { allerVers(VueTension.ARRET) },
            onFermer = onFermer,
        )

        VueTension.BLOC -> VueBloc(
            bloc = checkNotNull(enCours),
            onQuitter = quitterBloc,
            onAssis = {
                debutAssis = SystemClock.elapsedRealtime()
                bloc = null
                vue = VueTension.ASSIS
            },
            onPhrase = { allerVers(VueTension.PHRASE) },
            onFermer = onFermer,
        )

        VueTension.ASSIS -> VueAssis(
            debut = debutAssis,
            onQuitter = { vue = VueTension.SEQUENCE },
            onFermer = onFermer,
        )

        VueTension.PHRASE -> VuePhrase(onRetour = { vue = retour }, onFermer = onFermer)

        VueTension.ARRET -> VueArret(onRetour = { vue = retour }, onFermer = onFermer)
    }
}

@Composable
private fun VueAccueil(
    onDemarrer: () -> Unit,
    onSequence: () -> Unit,
    onPhrase: () -> Unit,
    onArret: () -> Unit,
    onFermer: () -> Unit,
) {
    Titre(stringResource(R.string.tension_titre))
    Explication(stringResource(R.string.tension_geste))
    GrandBouton(
        libelle = stringResource(R.string.tension_action_demarrer),
        repere = stringResource(R.string.tension_repere_demarrer),
        onClick = onDemarrer,
    )
    GrandBouton(
        libelle = stringResource(R.string.tension_action_sequence),
        repere = stringResource(R.string.tension_repere_sequence),
        onClick = onSequence,
    )
    Lien(stringResource(R.string.tension_lien_phrase), onPhrase)
    Lien(stringResource(R.string.tension_lien_arret), onArret)
    Fermer(onFermer)
}

@Composable
private fun VueSequence(
    attendue: EtapeSoins,
    onRepere: (EtapeSoins) -> Unit,
    onPhrase: () -> Unit,
    onArret: () -> Unit,
    onFermer: () -> Unit,
) {
    Titre(stringResource(R.string.sequence_titre))
    Explication(stringResource(R.string.sequence_consigne))
    Text(
        text = stringResource(R.string.sequence_attendu, stringResource(libelleDe(attendue))),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    SEQUENCE_SOINS.forEach { etape ->
        GrandBouton(
            libelle = stringResource(libelleDe(etape)),
            repere = stringResource(blocDe(etape)),
            onClick = { onRepere(etape) },
        )
    }
    Lien(stringResource(R.string.tension_lien_phrase), onPhrase)
    Lien(stringResource(R.string.tension_lien_arret), onArret)
    Fermer(onFermer)
}

@Composable
private fun VueBloc(
    bloc: BlocEnCours,
    onQuitter: () -> Unit,
    onAssis: () -> Unit,
    onPhrase: () -> Unit,
    onFermer: () -> Unit,
) {
    val finMillis = secondesDuBloc(bloc.cycles)?.times(1000L)
    var millis by remember(bloc) { mutableLongStateOf(SystemClock.elapsedRealtime() - bloc.debut) }

    LaunchedEffect(bloc) {
        while (true) {
            millis = SystemClock.elapsedRealtime() - bloc.debut
            if (finMillis != null && millis >= finMillis) return@LaunchedEffect
            delay(PERIODE_TICK_MILLIS)
        }
    }

    val etat = etatTension((millis / 1000L).toInt(), bloc.cycles)
    val termine = etat.phase == PhaseTension.TERMINE
    val couleur by animateColorAsState(
        targetValue = when (etat.phase) {
            PhaseTension.CONTRACTE -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(DUREE_TRANSITION_MILLIS),
        label = "phase",
    )

    bloc.etape?.let {
        Text(
            text = stringResource(libelleDe(it)),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    Text(
        text = stringResource(
            when (etat.phase) {
                PhaseTension.CONTRACTE -> R.string.tension_phase_contracte
                PhaseTension.RELACHE -> R.string.tension_phase_relache
                PhaseTension.TERMINE -> R.string.tension_phase_termine
            },
        ),
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Text(
        text = when (etat.phase) {
            PhaseTension.CONTRACTE -> stringResource(R.string.tension_consigne_contracte)
            PhaseTension.RELACHE -> stringResource(R.string.tension_consigne_relache)
            PhaseTension.TERMINE -> stringResource(R.string.tension_consigne_termine, etat.cycle)
        },
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (!termine) {
        Text(
            text = etat.secondesRestantes.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Barre(fraction = fractionPhase(millis, bloc.cycles), couleur = couleur)
        Text(
            text = when (bloc.cycles) {
                null -> stringResource(R.string.tension_cycle_enchaine, etat.cycle)
                else -> stringResource(R.string.tension_cycle, etat.cycle, bloc.cycles)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (termine && bloc.etape == EtapeSoins.APRES_GESTE) {
        GrandBouton(
            libelle = stringResource(R.string.assis_action),
            repere = stringResource(R.string.assis_repere),
            onClick = onAssis,
        )
    }

    OutlinedButton(onClick = onQuitter, modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(
                when {
                    termine && bloc.etape != null -> R.string.tension_action_revenir
                    termine -> R.string.tension_action_recommencer
                    else -> R.string.tension_action_arreter
                },
            ),
        )
    }
    Lien(stringResource(R.string.tension_lien_phrase), onPhrase)
    Fermer(onFermer)
}

@Composable
private fun VueAssis(debut: Long, onQuitter: () -> Unit, onFermer: () -> Unit) {
    var secondes by remember(debut) {
        mutableLongStateOf((SystemClock.elapsedRealtime() - debut) / 1000L)
    }

    LaunchedEffect(debut) {
        while (true) {
            secondes = (SystemClock.elapsedRealtime() - debut) / 1000L
            if (secondes >= SECONDES_ASSIS_APRES) return@LaunchedEffect
            delay(PERIODE_TICK_MILLIS)
        }
    }

    val restantes = (SECONDES_ASSIS_APRES - secondes).coerceAtLeast(0L)
    Titre(stringResource(R.string.assis_titre))

    if (restantes > 0L) {
        Explication(stringResource(R.string.assis_consigne))
        Text(
            text = stringResource(R.string.assis_restant, restantes / 60L, restantes % 60L),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Barre(
            fraction = secondes.toFloat() / SECONDES_ASSIS_APRES,
            couleur = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        Explication(stringResource(R.string.assis_termine))
    }

    OutlinedButton(onClick = onQuitter, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.tension_action_revenir))
    }
    Fermer(onFermer)
}

@Composable
private fun VuePhrase(onRetour: () -> Unit, onFermer: () -> Unit) {
    Titre(stringResource(R.string.phrase_titre))
    Text(
        text = stringResource(R.string.phrase_texte),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Explication(stringResource(R.string.phrase_montrer))
    Explication(stringResource(R.string.phrase_appuis))
    OutlinedButton(onClick = onRetour, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.crise_retour))
    }
    Fermer(onFermer)
}

@Composable
private fun VueArret(onRetour: () -> Unit, onFermer: () -> Unit) {
    Titre(stringResource(R.string.arret_titre))
    Explication(stringResource(R.string.arret_douleur))
    Explication(stringResource(R.string.arret_syncope))
    Explication(stringResource(R.string.arret_discriminant))
    OutlinedButton(onClick = onRetour, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.crise_retour))
    }
    Fermer(onFermer)
}

@Composable
private fun Barre(fraction: Float, couleur: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(couleur),
        )
    }
}

@StringRes
private fun libelleDe(etape: EtapeSoins): Int = when (etape) {
    EtapeSoins.PORTE -> R.string.sequence_porte
    EtapeSoins.FAUTEUIL -> R.string.sequence_fauteuil
    EtapeSoins.PLATEAU -> R.string.sequence_plateau
    EtapeSoins.APRES_GESTE -> R.string.sequence_apres
}

@StringRes
private fun blocDe(etape: EtapeSoins): Int = when (etape) {
    EtapeSoins.PORTE -> R.string.sequence_porte_bloc
    EtapeSoins.FAUTEUIL -> R.string.sequence_fauteuil_bloc
    EtapeSoins.PLATEAU -> R.string.sequence_plateau_bloc
    EtapeSoins.APRES_GESTE -> R.string.sequence_apres_bloc
}
