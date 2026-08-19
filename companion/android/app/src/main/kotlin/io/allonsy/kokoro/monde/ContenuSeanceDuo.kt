package io.allonsy.kokoro.monde

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.Interrupteur
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PanneauDialogue
import io.allonsy.kokoro.ui.TypoKokoro
import kotlinx.coroutines.delay

private const val TICK_MILLIS = 200L

private enum class VueDuo { ACCUEIL, AVANT, PENDANT, APRES }

// Le déroulé passe dans les mains de l'aidant : Kokoro tient la cadence, il ne lui demande jamais de juger.
@Composable
fun PanneauSeanceDuo(
    etape: Etape.SeanceDuo,
    entraine: Boolean,
    onIssue: (Issue) -> Unit,
    onEntrainementMene: () -> Unit,
    onFermer: () -> Unit,
) {
    var vue by remember(etape.reperes.id) { mutableStateOf(VueDuo.ACCUEIL) }
    var blanc by remember(etape.reperes.id) { mutableStateOf(!entraine) }
    var coches by remember(etape.reperes.id) { mutableStateOf(emptySet<Int>()) }
    var rang by remember(etape.reperes.id) { mutableIntStateOf(0) }
    var debut by remember(etape.reperes.id) { mutableLongStateOf(0L) }
    var reste by remember(etape.reperes.id) { mutableIntStateOf(0) }
    var menee by remember(etape.reperes.id) { mutableStateOf(false) }

    val rendre = { issue: Issue ->
        onIssue(issue)
        vue = VueDuo.APRES
    }

    val entrer = { aBlanc: Boolean ->
        blanc = aBlanc
        vue = VueDuo.AVANT
    }

    val avancer = {
        if (rang + 1 < etape.sequence.size) {
            rang += 1
            debut = SystemClock.elapsedRealtime()
        } else {
            menee = true
            if (blanc) onEntrainementMene()
            rendre(if (blanc) Issue.ENTRAINEMENT else Issue.TERMINE)
        }
    }

    // Aucun son, aucune vibration à la bascule : l'écran passe seul à la consigne suivante, et il ne la commente pas.
    // 🔴 L'entraînement ne chronomètre pas : l'aidant passe à la main, le temps affiché reste celui que la consigne durera.
    LaunchedEffect(vue, rang, debut) {
        if (vue != VueDuo.PENDANT) return@LaunchedEffect
        val tenue = etape.sequence[rang].secondes
        reste = tenue
        if (blanc) return@LaunchedEffect
        while (reste > 0) {
            reste = resteSecondes(tenue, SystemClock.elapsedRealtime() - debut)
            if (reste > 0) delay(TICK_MILLIS)
        }
        avancer()
    }

    PanneauDialogue(titre = etape.reperes.titre, remonteSur = vue to rang, onFermer = onFermer) {
        when (vue) {
            VueDuo.ACCUEIL -> Accueil(
                entraine = entraine,
                onEntrainement = { entrer(true) },
                onSeance = { entrer(false) },
            )

            VueDuo.AVANT -> Avant(
                etape = etape,
                blanc = blanc,
                coches = coches,
                onCocher = { ligne, coche -> coches = if (coche) coches + ligne else coches - ligne },
                onCommencer = {
                    rang = 0
                    debut = SystemClock.elapsedRealtime()
                    vue = VueDuo.PENDANT
                },
            )

            VueDuo.PENDANT -> Pendant(
                consigne = etape.sequence[rang].consigne,
                rang = rang,
                total = etape.sequence.size,
                reste = reste,
                blanc = blanc,
                onSuivante = avancer,
                onArreter = { rendre(if (blanc) Issue.ENTRAINEMENT else Issue.ARRETE) },
            )

            VueDuo.APRES -> Apres(menee = menee, onFermer = onFermer)
        }
    }
}

@Composable
private fun Accueil(entraine: Boolean, onEntrainement: () -> Unit, onSeance: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(text = stringResource(R.string.duo_accueil), style = TypoKokoro.lecture, color = palette.encre)
    Text(
        text = stringResource(if (entraine) R.string.duo_entrainement_mene else R.string.duo_accueil_entrainement),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )

    BoutonEpais(
        libelle = stringResource(R.string.duo_action_entrainement),
        onClic = onEntrainement,
        couleur = if (entraine) null else palette.menthe,
        modifier = Modifier.padding(top = 6.dp),
    )
    BoutonEpais(
        libelle = stringResource(R.string.duo_action_seance),
        onClic = onSeance,
        couleur = if (entraine) palette.menthe else null,
    )
}

// L'aidant coche, ou n'entre pas dans la séquence — et les critères d'arrêt sont une case comme les autres :
// c'est la seule page qui les porte, donc la seule manière de garantir qu'ils ont été lus est de les faire cocher.
@Composable
private fun Avant(
    etape: Etape.SeanceDuo,
    blanc: Boolean,
    coches: Set<Int>,
    onCocher: (Int, Boolean) -> Unit,
    onCommencer: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    val tout = coches.size == etape.avant.size + 1

    if (blanc) {
        Text(
            text = stringResource(R.string.duo_entrainement_en_cours),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
    }
    Text(text = stringResource(R.string.duo_avant_titre), style = TypoKokoro.lecture, color = palette.encre)

    etape.avant.forEachIndexed { ligne, texte ->
        ACocher(coche = ligne in coches, onCocher = { onCocher(ligne, it) }) {
            Text(text = texte, style = TypoKokoro.corps, color = palette.encre)
        }
    }

    val criteres = etape.avant.size
    ACocher(coche = criteres in coches, onCocher = { onCocher(criteres, it) }) {
        Text(text = etape.signalArret, style = TypoKokoro.corps, color = palette.encre)
        etape.arret.forEach { critere ->
            Text(text = critere, style = TypoKokoro.corps, color = palette.encre)
        }
    }

    Text(
        text = stringResource(R.string.duo_sortie_libre),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    Text(
        text = stringResource(
            if (blanc) R.string.duo_avant_cadence_entrainement else R.string.duo_minuteur_ouvert,
        ),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    BoutonEpais(
        libelle = stringResource(if (tout) R.string.duo_action_commencer else R.string.duo_avant_reste),
        onClic = onCommencer,
        couleur = if (tout) palette.menthe else null,
        actif = tout,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun ACocher(
    coche: Boolean,
    onCocher: (Boolean) -> Unit,
    contenu: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = { contenu() },
        )
        Interrupteur(actif = coche, onChange = onCocher)
    }
}

// La consigne dit elle-même à qui elle s'adresse : rien ne s'ajoute autour d'elle, et rien ne se répète d'un écran à l'autre.
@Composable
private fun Pendant(
    consigne: String,
    rang: Int,
    total: Int,
    reste: Int,
    blanc: Boolean,
    onSuivante: () -> Unit,
    onArreter: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    val derniere = rang + 1 == total

    Text(
        text = stringResource(R.string.duo_rang, rang + 1, total),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    Text(text = consigne, style = TypoKokoro.titre, color = palette.encre)
    Text(
        text = libelleDuReste(reste),
        style = TypoKokoro.compte,
        color = palette.encre,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
    )

    if (blanc) {
        Text(
            text = stringResource(R.string.duo_duree_annonce),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
        BoutonEpais(
            libelle = stringResource(if (derniere) R.string.duo_action_finir else R.string.duo_action_suivante),
            onClic = onSuivante,
            couleur = palette.menthe,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
    BoutonEpais(
        libelle = stringResource(R.string.duo_action_arreter),
        onClic = onArreter,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun Apres(menee: Boolean, onFermer: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(
        text = stringResource(if (menee) R.string.duo_mene else R.string.duo_arrete),
        style = TypoKokoro.corps,
        color = palette.encre,
    )
    Text(text = stringResource(R.string.duo_fini), style = TypoKokoro.corps, color = palette.encre)
    BoutonEpais(
        libelle = stringResource(R.string.duo_action_fermer),
        onClic = onFermer,
        modifier = Modifier.padding(top = 6.dp),
    )
}
