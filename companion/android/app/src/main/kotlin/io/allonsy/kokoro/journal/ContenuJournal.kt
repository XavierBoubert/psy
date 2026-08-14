package io.allonsy.kokoro.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import java.util.Locale

sealed interface EtapeJournal {
    data object DossierAbsent : EtapeJournal
    data object DejaEcrit : EtapeJournal
    data class Repondre(val index: Int) : EtapeJournal
    data object Note : EtapeJournal
    data class Enregistre(val nom: String) : EtapeJournal
    data class Echoue(val cause: String) : EtapeJournal
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (etape) {
                EtapeJournal.DossierAbsent -> EcranDossierAbsent(onChoisirDossier, onFermer)
                EtapeJournal.DejaEcrit -> EcranDejaEcrit(checkin.date, onFermer)
                is EtapeJournal.Repondre -> EcranQuestion(
                    question = QUESTIONS[etape.index],
                    rang = etape.index,
                    depart = departDe(QUESTIONS[etape.index], repris),
                    onRepondre = onRepondre,
                    onArreter = onArreter,
                )
                EtapeJournal.Note -> EcranNote(onNote, onArreter)
                is EtapeJournal.Enregistre -> EcranEnregistre(etape.nom, onFermer)
                is EtapeJournal.Echoue -> EcranEchoue(etape.cause, onFermer)
            }
        }
    }
}

@Composable
private fun EcranDossierAbsent(onChoisirDossier: () -> Unit, onFermer: () -> Unit) {
    Titre(stringResource(R.string.journal_titre))
    Explication(stringResource(R.string.journal_dossier_explication))
    Principal(stringResource(R.string.journal_action_dossier), onChoisirDossier)
    Discret(stringResource(R.string.journal_action_fermer), onFermer)
}

@Composable
private fun EcranDejaEcrit(date: String, onFermer: () -> Unit) {
    Titre(stringResource(R.string.journal_titre))
    Explication(stringResource(R.string.journal_deja_ecrit, date))
    Principal(stringResource(R.string.journal_action_fermer), onFermer)
}

@Composable
private fun EcranQuestion(
    question: Question,
    rang: Int,
    depart: Double,
    onRepondre: (Champ, Double?) -> Unit,
    onArreter: () -> Unit,
) {
    Text(
        text = stringResource(R.string.journal_rang, rang + 1, QUESTIONS.size),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Titre(stringResource(question.enonce))
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
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
    OutlinedButton(
        onClick = { onClick(delta) },
        modifier = modifier.heightIn(min = 64.dp),
    ) {
        Text(
            text = (if (delta > 0) "+" else "−") + afficher(kotlin.math.abs(delta), unite),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun EcranNote(onNote: (String?) -> Unit, onArreter: () -> Unit) {
    var texte by remember { mutableStateOf("") }

    Titre(stringResource(R.string.journal_q_notes))
    Explication(stringResource(R.string.journal_p_notes))
    OutlinedTextField(
        value = texte,
        onValueChange = { texte = it },
        modifier = Modifier.fillMaxWidth(),
    )
    Principal(stringResource(R.string.journal_action_enregistrer)) {
        onNote(texte.trim().ifBlank { null })
    }
    Discret(stringResource(R.string.journal_action_arreter), onArreter)
}

@Composable
private fun EcranEnregistre(nom: String, onFermer: () -> Unit) {
    Titre(stringResource(R.string.journal_enregistre))
    Explication(nom)
    Principal(stringResource(R.string.journal_action_fermer), onFermer)
}

@Composable
private fun EcranEchoue(cause: String, onFermer: () -> Unit) {
    Titre(stringResource(R.string.journal_echec))
    Explication(cause)
    Explication(stringResource(R.string.journal_echec_suite))
    Principal(stringResource(R.string.journal_action_fermer), onFermer)
}

@Composable
private fun Titre(texte: String) {
    Text(
        text = texte,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun Explication(texte: String) {
    Text(
        text = texte,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun Principal(libelle: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp),
    ) {
        Text(libelle, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
    }
}

@Composable
private fun Discret(libelle: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(libelle, textAlign = TextAlign.Center)
    }
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
