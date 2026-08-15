package io.allonsy.kokoro.crise

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.allonsy.kokoro.R
import io.allonsy.kokoro.reglages.Reglages
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val FORMAT_HEURE: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun ContenuCrise(
    ecran: EcranCrise,
    reglages: Reglages,
    envoi: ResultatEnvoi,
    onAller: (EcranCrise) -> Unit,
    onEnvoyer: () -> Unit,
    onSecours: () -> Unit,
    onFermer: () -> Unit,
) {
    when (ecran) {
        EcranCrise.Accueil -> EcranAccueil(
            reglages = reglages,
            onAller = onAller,
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
        EcranCrise.Phrase -> ContenuTension(onFermer = onFermer, ouvrirSurLaPhrase = true)
    }
}

@Composable
private fun EcranAccueil(
    reglages: Reglages,
    onAller: (EcranCrise) -> Unit,
    onFermer: () -> Unit,
) {
    PageCrise(titre = stringResource(R.string.monde_crise_titre)) {
        GrandBouton(
            libelle = stringResource(R.string.crise_bouton_mot_code, reglages.contactNom),
            repere = stringResource(R.string.crise_repere_mot_code),
            onClick = { onAller(EcranCrise.MotCode) },
        )
        GrandBouton(
            libelle = stringResource(R.string.crise_bouton_tension),
            repere = stringResource(R.string.crise_repere_tension),
            onClick = { onAller(EcranCrise.Tension) },
        )
        Fermer(onFermer)
    }
}

/**
 * ⭐ **Cet écran n'est plus le chemin ordinaire du mot-code** *(15/08/2026)*. Depuis l'écran
 * **Crise** du monde, le message part d'un seul appui. On n'arrive plus ici que par la notification
 * de l'écran verrouillé, **ou parce que l'envoi direct n'a pas pu se faire** — pas de numéro,
 * autorisation refusée, échec du réseau.
 */
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

    PageCrise(titre = stringResource(R.string.mot_code_titre)) {
        Explication(stringResource(R.string.mot_code_destinataire, reglages.contactNom))
        EnGrand(reglages.motCode)

        when {
            !reglages.contactRenseigne -> Explication(stringResource(R.string.mot_code_sans_numero))

            envoi == ResultatEnvoi.ENVOYE -> EnGrand(stringResource(R.string.mot_code_envoye, heure))

            envoi == ResultatEnvoi.EN_COURS -> Explication(stringResource(R.string.mot_code_en_cours))

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
            )

            else -> GrandBouton(
                libelle = stringResource(R.string.mot_code_action_messages),
                repere = stringResource(R.string.mot_code_repere_messages),
                onClick = onSecours,
            )
        }

        Fermer(onFermer)
    }
}
