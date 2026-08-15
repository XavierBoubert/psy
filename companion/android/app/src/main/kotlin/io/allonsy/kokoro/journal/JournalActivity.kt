package io.allonsy.kokoro.journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.ThemeMonde

/**
 * Le check-in quotidien — sept questions du noyau puis les champs de campagne déclarés
 * dans `etat.md` §4, dans l'ordre du skill `psy-journal`. Aucune question n'attend de
 * texte ; « passer » écrit `null`, qui n'est pas `0` ; « arrêter » enregistre ce qui a
 * été rempli et s'arrête sans relance.
 *
 * Rien n'est commenté, rien n'est comparé, rien n'est compté d'un jour à l'autre.
 */
class JournalActivity : ComponentActivity() {
    private val etape = mutableStateOf<EtapeJournal>(EtapeJournal.Repondre(0))
    private val checkin = mutableStateOf(Checkin.vide(""))
    private val repris = mutableStateOf<Map<Champ, Double>>(emptyMap())
    private val nuit = mutableStateOf(false)

    private val choixDossier = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { resultat ->
        resultat.data?.data?.let { arbre ->
            enregistrerDossier(this, arbre)
            demarrer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        nuit.value = estNuit(lireReglages(this).nuit, minuteCourante())
        demarrer()
        setContent {
            ThemeMonde(nuit = nuit.value) {
                ContenuJournal(
                    etape = etape.value,
                    checkin = checkin.value,
                    repris = repris.value,
                    onRepondre = { champ, valeur -> repondre(champ, valeur) },
                    onNote = { enregistrer(checkin.value.copy(notes = it)) },
                    onChoisirDossier = { choixDossier.launch(intentChoisirDossier()) },
                    onArreter = { enregistrer(checkin.value) },
                    onFermer = { finish() },
                )
            }
        }
    }

    private fun demarrer() {
        val jour = jourCourant()
        checkin.value = Checkin.vide(jour)
        etape.value = when {
            lireDossier(this) == null -> EtapeJournal.DossierAbsent
            checkinDuJourExiste(this, jour) -> EtapeJournal.DejaEcrit
            else -> EtapeJournal.Repondre(0)
        }
        repris.value = if (etape.value is EtapeJournal.Repondre) valeursReprises(this, jour) else emptyMap()
    }

    private fun repondre(champ: Champ, valeur: Double?) {
        checkin.value = checkin.value.avec(champ, valeur)
        val suivant = (etape.value as? EtapeJournal.Repondre)?.index?.plus(1) ?: return
        etape.value = if (suivant < QUESTIONS.size) EtapeJournal.Repondre(suivant) else EtapeJournal.Note
    }

    private fun enregistrer(aEcrire: Checkin) {
        etape.value = when (val resultat = ecrireCheckin(this, aEcrire)) {
            is ResultatEcriture.Ecrit -> EtapeJournal.Enregistre(resultat.nom)
            ResultatEcriture.DossierAbsent -> EtapeJournal.DossierAbsent
            ResultatEcriture.DejaEcritAujourdhui -> EtapeJournal.DejaEcrit
            is ResultatEcriture.Echec -> EtapeJournal.Echoue(resultat.cause)
        }
    }
}
