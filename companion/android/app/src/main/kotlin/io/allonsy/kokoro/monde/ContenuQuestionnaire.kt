package io.allonsy.kokoro.monde

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.programme.QuestionFermee
import io.allonsy.kokoro.programme.ReponseItem
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PanneauDialogue
import io.allonsy.kokoro.ui.TypoKokoro

private enum class VuePassation { AVANT, PENDANT, APRES }

// Une question par écran, des choix fermés, aucune saisie de texte. Kokoro renvoie les items ; le score se calcule en séance.
@Composable
fun PanneauQuestionnaire(
    etape: Etape.Questionnaire,
    onRendu: (Issue, List<ReponseItem>) -> Unit,
    onFermer: () -> Unit,
) {
    var vue by remember(etape.reperes.id) { mutableStateOf(VuePassation.AVANT) }
    var rang by remember(etape.reperes.id) { mutableIntStateOf(0) }
    var items by remember(etape.reperes.id) { mutableStateOf(emptyList<ReponseItem>()) }

    val rendre = { issue: Issue, rendus: List<ReponseItem> ->
        onRendu(issue, rendus)
        vue = VuePassation.APRES
    }

    val repondre = { valeur: Int? ->
        val rendus = items + ReponseItem(question = etape.questions[rang].id, valeur = valeur)
        items = rendus
        if (rang + 1 < etape.questions.size) rang += 1 else rendre(Issue.TERMINE, rendus)
    }

    PanneauDialogue(titre = etape.reperes.titre, ecart = 16.dp, onFermer = onFermer) {
        when (vue) {
            VuePassation.AVANT -> Avant(
                questions = etape.questions.size,
                onCommencer = { vue = VuePassation.PENDANT },
            )

            VuePassation.PENDANT -> Question(
                question = etape.questions[rang],
                rang = rang,
                total = etape.questions.size,
                onRepondre = repondre,
                onArreter = { rendre(Issue.ARRETE, items) },
            )

            VuePassation.APRES -> Apres(onFermer = onFermer)
        }
    }
}

@Composable
private fun Avant(questions: Int, onCommencer: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(
        text = stringResource(R.string.questionnaire_annonce, questions),
        style = TypoKokoro.lecture,
        color = palette.encre,
    )
    Text(
        text = stringResource(R.string.questionnaire_sortie_libre),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    BoutonEpais(
        libelle = stringResource(R.string.questionnaire_action_commencer),
        onClic = onCommencer,
        couleur = palette.menthe,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun Question(
    question: QuestionFermee,
    rang: Int,
    total: Int,
    onRepondre: (Int?) -> Unit,
    onArreter: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current

    Text(
        text = stringResource(R.string.questionnaire_rang, rang + 1, total),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    Text(text = question.enonce, style = TypoKokoro.titre, color = palette.encre)

    question.choix.forEach { choix ->
        BoutonEpais(
            libelle = choix.libelle,
            onClic = { onRepondre(choix.valeur) },
            couleur = palette.menthe,
            hauteurMinimale = 72.dp,
        )
    }

    BoutonEpais(
        libelle = stringResource(R.string.questionnaire_action_passer),
        onClic = { onRepondre(null) },
        hauteurMinimale = 60.dp,
    )
    BoutonEpais(
        libelle = stringResource(R.string.questionnaire_action_arreter),
        onClic = onArreter,
        hauteurMinimale = 60.dp,
    )
}

@Composable
private fun Apres(onFermer: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(text = stringResource(R.string.questionnaire_fini), style = TypoKokoro.corps, color = palette.encre)
    BoutonEpais(
        libelle = stringResource(R.string.questionnaire_action_fermer),
        onClic = onFermer,
        modifier = Modifier.padding(top = 6.dp),
    )
}
