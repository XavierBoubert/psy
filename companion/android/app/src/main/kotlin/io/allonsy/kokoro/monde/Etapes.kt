package io.allonsy.kokoro.monde

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.Fonction
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.Quand
import io.allonsy.kokoro.programme.Rubrique
import io.allonsy.kokoro.programme.bilans
import io.allonsy.kokoro.programme.etapesDe
import io.allonsy.kokoro.programme.faite
import io.allonsy.kokoro.programme.fiches
import io.allonsy.kokoro.programme.quand
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PanneauDialogue
import io.allonsy.kokoro.ui.Teinte
import io.allonsy.kokoro.ui.TypoKokoro

fun videsDe(programme: Programme): Set<Ecran> = buildSet {
    if (programme.etapesDe(Rubrique.THERAPIE).isEmpty()) add(Ecran.THERAPIE)
    if (programme.bilans().isEmpty()) add(Ecran.BILAN)
    if (programme.fiches().isEmpty()) add(Ecran.DOCUMENTATION)
}

// Le check-in n'écrit pas de réponse mais un fichier de journal : son état vient du séjour, jamais des faites.
fun rendue(etape: Etape, faites: Faites, checkinFait: Boolean): Boolean =
    if (etape is Etape.Ecran && etape.fonction == Fonction.CHECK_IN) checkinFait else faites.faite(etape)

fun toutFaitAujourdhui(programme: Programme, faites: Faites, checkinFait: Boolean): Boolean =
    programme.etapesDe(Rubrique.THERAPIE)
        .filter { it.quand == Quand.AUJOURDHUI }
        .all { etape -> rendue(etape, faites, checkinFait) }

// Tout ce qu'un bouton du monde peut ouvrir dans le panneau de dialogue — une seule forme, plusieurs contenus.
sealed interface Contexte {
    data class Lecture(val titre: String, val texte: String) : Contexte

    data class Demarche(val etape: Etape.Demarche, val faite: Boolean) : Contexte

    data class Exercice(val etape: Etape.Exercice) : Contexte

    data class Questionnaire(val etape: Etape.Questionnaire) : Contexte

    data class SeanceDuo(val etape: Etape.SeanceDuo, val entraine: Boolean) : Contexte

    data object Reglages : Contexte

    data object Checkin : Contexte

    data object Tension : Contexte

    data object Phrase : Contexte
}

data class Section(
    val quand: Quand,
    val libelle: Int,
    val couleur: Teinte,
    val perchoir: Perchoir?,
)

@Composable
fun sectionsDuProgramme(): List<Section> {
    val palette = LocalPaletteKokoro.current
    return listOf(
        Section(Quand.AUJOURDHUI, R.string.monde_quand_aujourdhui, palette.peche, Perchoir.AUJOURDHUI),
        Section(Quand.AU_BESOIN, R.string.monde_quand_au_besoin, palette.beurre, null),
        Section(Quand.SANS_DATE, R.string.monde_quand_sans_date, palette.azur, Perchoir.SANS_DATE),
    )
}

@Composable
fun PanneauLecture(titre: String, texte: String, onFermer: () -> Unit) {
    PanneauDialogue(titre = titre, onFermer = onFermer) {
        Text(text = texte, style = TypoKokoro.lecture, color = LocalPaletteKokoro.current.encre)
    }
}

// Une démarche déjà faite n'offre plus son bouton : elle reste lisible jusqu'à ce que le psy la retire du programme.
@Composable
fun PanneauDemarche(
    etape: Etape.Demarche,
    faite: Boolean,
    onFait: () -> Unit,
    onFermer: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    PanneauDialogue(titre = etape.reperes.titre, onFermer = onFermer) {
        Text(text = etape.detail, style = TypoKokoro.lecture, color = palette.encre)

        if (faite) {
            Text(
                text = stringResource(R.string.etape_deja_faite),
                style = TypoKokoro.discret,
                color = palette.encreDouce,
            )
            return@PanneauDialogue
        }

        BoutonEpais(
            libelle = stringResource(R.string.etape_action_fait),
            onClic = onFait,
            couleur = palette.menthe,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}
