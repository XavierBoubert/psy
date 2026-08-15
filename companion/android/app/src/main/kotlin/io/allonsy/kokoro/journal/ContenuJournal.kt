package io.allonsy.kokoro.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.corps.Expression
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.ChampTexte
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PageKokoro
import io.allonsy.kokoro.ui.TypoKokoro
import java.util.Locale

/**
 * Le check-in, **dans la matière du monde** *(15/08/2026)* — `companion/INTERFACE.md` §4.
 *
 * ⭐ **Une question par écran, et rien d'autre à l'écran.** Le rang est écrit en petit, il ne compte
 * rien d'un jour à l'autre. 🔴 **Aucune réponse n'est commentée** : Kokoro enregistre, il
 * n'interprète pas — l'interprétation appartient à la séance.
 *
 * 🔴 **Le vert est réservé à ce qui avance d'un pas** : une réponse, un enregistrement. **Passer et
 * arrêter sont neutres, jamais gris-triste ni barrés** — il n'y a pas de retard dans ce dispositif,
 * donc pas de couleur pour en parler.
 */

sealed interface EtapeJournal {
    data object DossierAbsent : EtapeJournal
    data object DejaEcrit : EtapeJournal
    data class Repondre(val index: Int) : EtapeJournal
    data object Note : EtapeJournal
    data class Enregistre(val nom: String) : EtapeJournal
    data class Echoue(val cause: String) : EtapeJournal
}

/**
 * L'expression du locuteur, **choisie par le contenu** (`PRESENCE.md` §1.1, **E12**).
 *
 * 🔴 **`chaleureux` réagit à un fait accompli et n'a pas de contraire** (§4.4) : il ne paraît que
 * lorsque le check-in du jour **est écrit**, et il n'existe aucune expression pour dire qu'il ne
 * l'est pas. Une question à laquelle on n'a pas répondu, un dossier qu'on n'a pas choisi, une
 * écriture qui a échoué : **l'expression est celle de tous les jours**, exactement comme si de rien
 * n'était.
 *
 * ⭐ **Un échec d'écriture n'est pas un échec de Xavier** — c'est un dossier introuvable, et le
 * texte de la page le dit déjà. Le visage n'a rien à ajouter.
 */
fun expressionDuJournal(etape: EtapeJournal): Expression = when (etape) {
    EtapeJournal.DejaEcrit, is EtapeJournal.Enregistre -> Expression.CHALEUREUX
    EtapeJournal.DossierAbsent, EtapeJournal.Note -> Expression.SEREIN
    is EtapeJournal.Repondre, is EtapeJournal.Echoue -> Expression.SEREIN
}

@Composable
fun ContenuJournal(
    etape: EtapeJournal,
    checkin: Checkin,
    repris: Map<Champ, Double>,
    onRepondre: (Champ, Double?) -> Unit,
    onNote: (String?) -> Unit,
    onChoisirDossier: () -> Unit,
    onArreter: () -> Unit,
    onFermer: () -> Unit,
) {
    PageKokoro(
        titre = stringResource(R.string.journal_titre),
        couleur = LocalPaletteKokoro.current.peche,
        ecart = 16.dp,
        locuteur = expressionDuJournal(etape),
        onFermer = onFermer,
    ) {
        when (etape) {
            EtapeJournal.DossierAbsent -> EcranDossierAbsent(onChoisirDossier)
            EtapeJournal.DejaEcrit -> EcranDejaEcrit(checkin.date)
            is EtapeJournal.Repondre -> EcranQuestion(
                question = QUESTIONS[etape.index],
                rang = etape.index,
                depart = departDe(QUESTIONS[etape.index], repris),
                onRepondre = onRepondre,
                onArreter = onArreter,
            )
            EtapeJournal.Note -> EcranNote(onNote, onArreter)
            is EtapeJournal.Enregistre -> EcranEnregistre(etape.nom)
            is EtapeJournal.Echoue -> EcranEchoue(etape.cause)
        }
    }
}

@Composable
private fun EcranDossierAbsent(onChoisirDossier: () -> Unit) {
    Explication(stringResource(R.string.journal_dossier_explication))
    Principal(stringResource(R.string.journal_action_dossier), onChoisirDossier)
}

@Composable
private fun EcranDejaEcrit(date: String) {
    Explication(stringResource(R.string.journal_deja_ecrit, date))
}

@Composable
private fun EcranQuestion(
    question: Question,
    rang: Int,
    depart: Double,
    onRepondre: (Champ, Double?) -> Unit,
    onArreter: () -> Unit,
) {
    Rang(stringResource(R.string.journal_rang, rang + 1, QUESTIONS.size))
    Enonce(stringResource(question.enonce))
    question.precision?.let { Explication(stringResource(it)) }

    when (val saisie = question.saisie) {
        is Saisie.Choix -> saisie.options.forEach { option ->
            Principal(stringResource(option.libelle)) { onRepondre(question.champ, option.valeur) }
        }
        is Saisie.Compteur -> Compteur(
            saisie = saisie,
            depart = depart,
            onValider = { onRepondre(question.champ, it) },
        )
    }

    Discret(stringResource(R.string.journal_action_passer)) { onRepondre(question.champ, null) }
    Discret(stringResource(R.string.journal_action_arreter), onArreter)
}

@Composable
private fun Compteur(saisie: Saisie.Compteur, depart: Double, onValider: (Double) -> Unit) {
    var valeur by remember(depart, saisie) { mutableStateOf(depart) }
    val ajuster = { delta: Double -> valeur = maxOf(saisie.minimum, arrondir(valeur + delta)) }

    Text(
        text = afficher(valeur, saisie.unite),
        style = TypoKokoro.compte,
        color = LocalPaletteKokoro.current.encre,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Pas(delta = -saisie.grandPas, unite = saisie.unite, modifier = Modifier.weight(1f), onClick = ajuster)
        Pas(delta = -saisie.pas, unite = saisie.unite, modifier = Modifier.weight(1f), onClick = ajuster)
        Pas(delta = saisie.pas, unite = saisie.unite, modifier = Modifier.weight(1f), onClick = ajuster)
        Pas(delta = saisie.grandPas, unite = saisie.unite, modifier = Modifier.weight(1f), onClick = ajuster)
    }
    Principal(stringResource(R.string.journal_action_suivant)) { onValider(valeur) }
}

@Composable
private fun Pas(delta: Double, unite: Unite, modifier: Modifier, onClick: (Double) -> Unit) {
    BoutonEpais(
        libelle = (if (delta > 0) "+" else "−") + afficher(kotlin.math.abs(delta), unite),
        onClic = { onClick(delta) },
        modifier = modifier,
        hauteurMinimale = 60.dp,
        style = TypoKokoro.discret,
    )
}

@Composable
private fun EcranNote(onNote: (String?) -> Unit, onArreter: () -> Unit) {
    var texte by remember { mutableStateOf("") }

    Enonce(stringResource(R.string.journal_q_notes))
    Explication(stringResource(R.string.journal_p_notes))
    ChampTexte(
        valeur = texte,
        onValeur = { texte = it },
        modifier = Modifier.fillMaxWidth(),
        uneSeuleLigne = false,
    )
    Principal(stringResource(R.string.journal_action_enregistrer)) {
        onNote(texte.trim().ifBlank { null })
    }
    Discret(stringResource(R.string.journal_action_arreter), onArreter)
}

@Composable
private fun EcranEnregistre(nom: String) {
    Enonce(stringResource(R.string.journal_enregistre))
    Explication(nom)
}

@Composable
private fun EcranEchoue(cause: String) {
    Enonce(stringResource(R.string.journal_echec))
    Explication(cause)
    Explication(stringResource(R.string.journal_echec_suite))
}

@Composable
private fun Rang(texte: String) {
    Text(text = texte, style = TypoKokoro.discret, color = LocalPaletteKokoro.current.encreDouce)
}

@Composable
private fun Enonce(texte: String) {
    Text(text = texte, style = TypoKokoro.titre, color = LocalPaletteKokoro.current.encre)
}

@Composable
private fun Explication(texte: String) {
    Text(text = texte, style = TypoKokoro.lecture, color = LocalPaletteKokoro.current.encreDouce)
}

@Composable
private fun Principal(libelle: String, onClick: () -> Unit) {
    BoutonEpais(
        libelle = libelle,
        onClic = onClick,
        couleur = LocalPaletteKokoro.current.menthe,
        hauteurMinimale = 72.dp,
    )
}

@Composable
private fun Discret(libelle: String, onClick: () -> Unit) {
    BoutonEpais(libelle = libelle, onClic = onClick, hauteurMinimale = 60.dp)
}

private fun arrondir(valeur: Double): Double = Math.round(valeur * 10.0) / 10.0

private fun afficher(valeur: Double, unite: Unite): String = when (unite) {
    Unite.BRUTE -> valeur.toLong().toString()
    Unite.MINUTES -> "${valeur.toLong()} min"
    Unite.HEURES -> "${decimale(valeur)} h"
    Unite.KILOS -> "${decimale(valeur)} kg"
}

private fun decimale(valeur: Double): String =
    String.format(Locale.FRANCE, "%.1f", valeur).removeSuffix(",0")
