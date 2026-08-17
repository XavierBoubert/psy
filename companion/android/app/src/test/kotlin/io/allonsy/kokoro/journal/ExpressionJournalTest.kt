package io.allonsy.kokoro.journal

import io.allonsy.kokoro.corps.Expression
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private val ETAPES = listOf(
    EtapeJournal.DossierAbsent,
    EtapeJournal.DejaEcrit,
    EtapeJournal.Repondre(0),
    EtapeJournal.Repondre(QUESTIONS.lastIndex),
    EtapeJournal.Note,
    EtapeJournal.Enregistre("2026-08-15.json"),
    EtapeJournal.Echoue("dossier introuvable"),
)

class ExpressionJournalTest {

    @Test
    fun `le visage ne dit jamais qu'une chose n'est pas faite`() {
        val accomplis = setOf<EtapeJournal>(
            EtapeJournal.DejaEcrit,
            EtapeJournal.Enregistre("2026-08-15.json"),
        )
        ETAPES.forEach { etape ->
            val attendue = if (etape in accomplis) Expression.CHALEUREUX else Expression.SEREIN
            assertEquals("L'expression de $etape", attendue, expressionDuJournal(etape))
        }
    }

    // Un échec d'écriture n'est pas un échec de Xavier : le visage reste celui de tous les jours.
    @Test
    fun `un echec technique ne change pas le visage`() {
        assertEquals(
            expressionDuJournal(EtapeJournal.Repondre(0)),
            expressionDuJournal(EtapeJournal.Echoue("dossier introuvable")),
        )
        assertEquals(
            expressionDuJournal(EtapeJournal.Repondre(0)),
            expressionDuJournal(EtapeJournal.DossierAbsent),
        )
    }

    @Test
    fun `le check in n'utilise que deux expressions du jeu`() {
        val utilisees = ETAPES.map { expressionDuJournal(it) }.toSet()
        assertEquals(setOf(Expression.SEREIN, Expression.CHALEUREUX), utilisees)
        assertTrue(
            "Aucune expression de veille ni de clignement en pleine page",
            Expression.VEILLE !in utilisees && Expression.CLIGNEMENT !in utilisees,
        )
    }
}
