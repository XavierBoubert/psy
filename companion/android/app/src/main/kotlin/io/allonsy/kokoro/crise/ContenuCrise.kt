package io.allonsy.kokoro.crise

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.allonsy.kokoro.R
import io.allonsy.kokoro.monde.Fonction
import io.allonsy.kokoro.reglages.Reglages
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.TypoKokoro
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val FORMAT_HEURE: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun ContenuCrise(
    ecran: EcranCrise,
    reglages: Reglages,
    envoi: ResultatEnvoi,
    onFonction: (Fonction) -> Unit,
    onEnvoyer: () -> Unit,
    onSecours: () -> Unit,
    onFermer: () -> Unit,
) {
    when (ecran) {
        EcranCrise.Accueil -> EcranAccueil(
            reglages = reglages,
            envoi = envoi,
            onFonction = onFonction,
            onFermer = onFermer,
        )
        EcranCrise.MotCode -> EcranMotCode(
            reglages = reglages,
            envoi = envoi,
            onEnvoyer = onEnvoyer,
            onSecours = onSecours,
            onFermer = onFermer,
        )
        EcranCrise.Tension -> ContenuTension(onFermer = onFermer)
        EcranCrise.Phrase -> EcranPhrase(onFermer = onFermer)
    }
}

@Composable
private fun EcranAccueil(
    reglages: Reglages,
    envoi: ResultatEnvoi,
    onFonction: (Fonction) -> Unit,
    onFermer: () -> Unit,
) {
    PageCrise(
        titre = stringResource(R.string.monde_crise_titre),
        onFermer = onFermer,
        defilant = false,
        alignement = Alignment.CenterVertically,
    ) {
        PortesDeCrise(
            contactNom = reglages.contactNom,
            envoiEnCours = envoi == ResultatEnvoi.EN_COURS,
            onFonction = onFonction,
        )
    }
}

@Composable
private fun EcranMotCode(
    reglages: Reglages,
    envoi: ResultatEnvoi,
    onEnvoyer: () -> Unit,
    onSecours: () -> Unit,
    onFermer: () -> Unit,
) {
    val context = LocalContext.current
    val direct = remember { envoiDirectDisponible(context) }
    val heure = remember(envoi) { LocalTime.now().format(FORMAT_HEURE) }

    PageCrise(titre = stringResource(R.string.mot_code_titre), onFermer = onFermer) {
        Explication(stringResource(R.string.mot_code_destinataire, reglages.contactNom))
        EnGrand(reglages.motCode)

        when {
            !reglages.contactRenseigne -> Explication(stringResource(R.string.mot_code_sans_numero))

            envoi == ResultatEnvoi.ENVOYE -> EnGrand(stringResource(R.string.mot_code_envoye, heure))

            envoi == ResultatEnvoi.ECHEC -> {
                Explication(stringResource(R.string.mot_code_echec))
                Action(
                    libelle = stringResource(R.string.mot_code_action_messages),
                    onClick = onSecours,
                )
                Explication(stringResource(R.string.mot_code_repere_messages))
            }

            direct -> GrandBouton(
                libelle = stringResource(R.string.mot_code_action_envoyer),
                repere = stringResource(R.string.mot_code_repere_envoyer),
                onClick = onEnvoyer,
                actif = envoi != ResultatEnvoi.EN_COURS,
            )

            else -> GrandBouton(
                libelle = stringResource(R.string.mot_code_action_messages),
                repere = stringResource(R.string.mot_code_repere_messages),
                onClick = onSecours,
            )
        }
    }
}

@Composable
private fun EcranPhrase(onFermer: () -> Unit) {
    val palette = LocalPaletteKokoro.current
    PageCrise(titre = stringResource(R.string.phrase_titre), onFermer = onFermer) {
        PanneauExtrude(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.phrase_texte),
                style = TypoKokoro.titre,
                color = palette.encre,
            )
        }
        Explication(stringResource(R.string.phrase_montrer))
        Explication(stringResource(R.string.phrase_appuis))
    }
}
