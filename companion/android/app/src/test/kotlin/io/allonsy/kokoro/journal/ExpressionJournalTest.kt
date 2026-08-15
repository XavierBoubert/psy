package io.allonsy.kokoro.journal

import io.allonsy.kokoro.corps.Expression
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Tous les états du check-in — un test qui en oublierait un ne dirait rien de l'invariant. */
private val ETAPES = listOf(
    EtapeJournal.DossierAbsent,
    EtapeJournal.DejaEcrit,
    EtapeJournal.Repondre(0),
    EtapeJournal.Repondre(QUESTIONS.lastIndex),
    EtapeJournal.Note,
    EtapeJournal.Enregistre("2026-08-15.json"),
    EtapeJournal.Echoue("dossier introuvable"),
)

/**
 * L'expression du locuteur pendant le check-in — `PRESENCE.md` §1.1 et §4.4, étape **E12**.
 *
 * 🔴 **`chaleureux` réagit à un fait accompli et n'a pas de contraire.** Le vérifier état par état
 * est la seule façon d'affirmer qu'aucun visage ne dit *tu ne l'as pas fait* — un reproche se glisse
 * dans un détail, pas dans une déclaration.
 */
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

    /**
     * ⭐ **Un échec d'écriture n'est pas un échec de Xavier** : c'est un dossier introuvable, et la
     * page le dit déjà en toutes lettres. Le visage reste celui de tous les jours.
     */
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

    /** Le jeu reste fermé : aucune expression du check-in n'invente une septième forme de visage. */
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
