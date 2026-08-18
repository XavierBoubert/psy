package io.allonsy.kokoro.monde

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.allonsy.kokoro.R
import io.allonsy.kokoro.programme.Bibliotheque
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PanneauDialogue
import io.allonsy.kokoro.ui.Teinte
import io.allonsy.kokoro.ui.TypoKokoro

// Contenu écrit en dur, reprend companion/inputs/programme.json v1 ; K5 le remplacera par une lecture du dossier.
enum class Fonction { CHECK_IN, MOT_CODE, TENSION, PHRASE }

sealed interface Ouverture {
    data class Ecran(val fonction: Fonction) : Ouverture

    data class Detail(val texte: String) : Ouverture
}

data class Etape(
    val titre: String,
    val ouverture: Ouverture,
    val duree: String? = null,
)

data class Section(
    val quand: String,
    val couleur: Teinte,
    val perchoir: Perchoir,
    val etapes: List<Etape>,
)

val ECRANS_VIDES = setOf(Ecran.DOCUMENTATION, Ecran.BILAN)

// La documentation cesse d'être vide dès qu'une fiche arrive du dossier ; le bilan, lui, n'a pas encore de liste.
fun videsDe(bibliotheque: Bibliotheque): Set<Ecran> =
    if (bibliotheque.fiches.isEmpty()) ECRANS_VIDES else ECRANS_VIDES - Ecran.DOCUMENTATION

// Tout ce qu'un bouton du monde peut ouvrir dans le panneau de dialogue — une seule forme, cinq contenus.
sealed interface Contexte {
    data class Lecture(val titre: String, val texte: String) : Contexte
    data object Reglages : Contexte
    data object Checkin : Contexte
    data object Tension : Contexte
    data object Phrase : Contexte
}

@Composable
fun sectionsTherapie(): List<Section> {
    val palette = LocalPaletteKokoro.current
    return listOf(
        Section(
            quand = stringResource(R.string.monde_quand_aujourdhui),
            couleur = palette.peche,
            perchoir = Perchoir.AUJOURDHUI,
            etapes = listOf(
                Etape(
                    titre = stringResource(R.string.journal_titre),
                    ouverture = Ouverture.Ecran(Fonction.CHECK_IN),
                    duree = stringResource(R.string.monde_duree_minutes, 2),
                ),
            ),
        ),
        Section(
            quand = stringResource(R.string.monde_quand_sans_date),
            couleur = palette.azur,
            perchoir = Perchoir.SANS_DATE,
            etapes = listOf(
                demarche(R.string.etape_ppc_releve, R.string.etape_ppc_releve_detail),
                demarche(R.string.etape_ppc_origine_fuite, R.string.etape_ppc_origine_fuite_detail),
                demarche(R.string.etape_ppc_interfaces, R.string.etape_ppc_interfaces_detail),
                demarche(R.string.etape_ppc_prise_en_charge, R.string.etape_ppc_prise_en_charge_detail),
                demarche(R.string.etape_ppc_roisman, R.string.etape_ppc_roisman_detail),
                demarche(R.string.etape_ppc_voyage, R.string.etape_ppc_voyage_detail),
                demarche(R.string.etape_email_isorni, R.string.etape_email_isorni_detail),
            ),
        ),
    )
}

@Composable
private fun demarche(titre: Int, detail: Int): Etape =
    Etape(titre = stringResource(titre), ouverture = Ouverture.Detail(stringResource(detail)))

// Simple appelant du panneau de dialogue partagé (ui/Pieces.kt) : une démarche n'est qu'un texte à lire.
@Composable
fun PanneauEtape(titre: String, detail: String, onFermer: () -> Unit) {
    PanneauDialogue(titre = titre, onFermer = onFermer) {
        Text(text = detail, style = TypoKokoro.lecture, color = LocalPaletteKokoro.current.encre)
    }
}
