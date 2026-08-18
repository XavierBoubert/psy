package io.allonsy.kokoro.monde

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import io.allonsy.kokoro.R
import io.allonsy.kokoro.alerte.creerCanalAlerte
import io.allonsy.kokoro.crise.ACTION_MOT_CODE_ENVOYE
import io.allonsy.kokoro.crise.CriseActivity
import io.allonsy.kokoro.crise.ECRAN_MOT_CODE
import io.allonsy.kokoro.crise.EXTRA_ECHEC
import io.allonsy.kokoro.crise.EXTRA_ECRAN
import io.allonsy.kokoro.crise.creerCanalAcces
import io.allonsy.kokoro.crise.publierAccesCrise
import io.allonsy.kokoro.crise.tenterMotCode
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.journal.Champ
import io.allonsy.kokoro.journal.Checkin
import io.allonsy.kokoro.journal.EtapeJournal
import io.allonsy.kokoro.journal.QUESTIONS
import io.allonsy.kokoro.journal.ResultatEcriture
import io.allonsy.kokoro.journal.checkinDuJourExiste
import io.allonsy.kokoro.journal.cheminAffichable
import io.allonsy.kokoro.journal.ecrireCheckin
import io.allonsy.kokoro.journal.enregistrerDossier
import io.allonsy.kokoro.journal.intentChoisirDossier
import io.allonsy.kokoro.journal.jourCourant
import io.allonsy.kokoro.journal.lireDossier
import io.allonsy.kokoro.journal.ecrireReponse
import io.allonsy.kokoro.journal.listerReponses
import io.allonsy.kokoro.journal.pdfDeLaBibliotheque
import io.allonsy.kokoro.journal.texteDuProgramme
import io.allonsy.kokoro.journal.valeursReprises
import io.allonsy.kokoro.programme.AUCUNE_FAITE
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.Fonction
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.programme.PROGRAMME_ABSENT
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.lireProgramme
import io.allonsy.kokoro.programme.reponseDe
import io.allonsy.kokoro.reglages.EtatAutorisations
import io.allonsy.kokoro.reglages.REGLAGES_INITIAUX
import io.allonsy.kokoro.reglages.ecrireReglages
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireAutorisations
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.ThemeMonde
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

// Depuis CriseActivity (JournalActivity n'existe plus) : demande d'ouvrir directement le panneau check-in.
const val EXTRA_OUVRIR_CHECKIN = "ouvrir_checkin"

// Ne s'affiche jamais par-dessus le verrouillage : c'est CriseActivity qui porte cette déclaration dans le manifeste.
class MondeActivity : ComponentActivity() {
    private val nuit = mutableStateOf(false)
    private val reglages = mutableStateOf(REGLAGES_INITIAUX)
    private val accuse = mutableStateOf<String?>(null)
    private val envoiEnCours = mutableStateOf(false)
    private val accesPerdu = mutableStateOf(false)
    private val sejour = mutableStateOf(Sejour(heure = 0, checkinFait = false))
    private val programme = mutableStateOf(PROGRAMME_ABSENT)
    private val faites = mutableStateOf(AUCUNE_FAITE)

    // Ex-MainActivity : la roue dentée ouvre désormais un panneau interne, plus une Activity.
    private val autorisations = mutableStateOf(EtatAutorisations(false, false, false))
    private val dossier = mutableStateOf<String?>(null)

    // Ex-JournalActivity : même raison — le check-in est un panneau interne.
    private val etapeCheckin = mutableStateOf<EtapeJournal>(EtapeJournal.Repondre(0))
    private val checkin = mutableStateOf(Checkin.vide(""))
    private val repris = mutableStateOf<Map<Champ, Double>>(emptyMap())
    private val ouvrirCheckinDemande = mutableStateOf(false)

    private val choixDossier = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { resultat ->
        resultat.data?.data?.let { arbre ->
            enregistrerDossier(this, arbre)
            relire()
            demarrerCheckin()
        }
    }

    private val accuseEnvoi = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            envoiEnCours.value = false
            when (resultCode) {
                Activity.RESULT_OK -> accuse.value =
                    getString(R.string.monde_mot_code_envoye, reglages.value.contactNom)
                else -> ouvrirMotCode(echec = true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ContextCompat.registerReceiver(
            this,
            accuseEnvoi,
            IntentFilter(ACTION_MOT_CODE_ENVOYE),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        creerCanalAcces(this)
        creerCanalAlerte(this)
        relire()
        demarrerCheckin()
        lireExtras(intent)
        setContent {
            ThemeMonde(nuit = nuit.value) {
                MondeKokoro(
                    palette = paletteDuMoment(nuit.value),
                    contactNom = reglages.value.contactNom,
                    sejour = sejour.value,
                    onFonction = { ouvrir(it) },
                    donneesReglages = DonneesReglages(
                        autorisations = autorisations.value,
                        reglages = reglages.value,
                        dossier = dossier.value,
                        onRelire = { relire() },
                        onEnregistrer = {
                            ecrireReglages(this, it)
                            relire()
                        },
                        onChoisirDossier = { choixDossier.launch(intentChoisirDossier()) },
                    ),
                    donneesCheckin = DonneesCheckin(
                        etape = etapeCheckin.value,
                        checkin = checkin.value,
                        repris = repris.value,
                        onRepondre = { champ, valeur -> repondreCheckin(champ, valeur) },
                        onNote = { enregistrerCheckin(checkin.value.copy(notes = it)) },
                        onChoisirDossier = { choixDossier.launch(intentChoisirDossier()) },
                        onArreter = { enregistrerCheckin(checkin.value) },
                        onOuverture = { demarrerCheckin() },
                    ),
                    programme = programme.value,
                    faites = faites.value,
                    onIssue = { etape, issue -> enregistrerReponse(etape, issue) },
                    onPdf = { document -> ouvrirLeDocument(document) },
                    ouvrirCheckin = ouvrirCheckinDemande.value,
                    onCheckinOuvert = { ouvrirCheckinDemande.value = false },
                    parallaxe = reglages.value.parallaxe,
                    envoiEnCours = envoiEnCours.value,
                    accesPerdu = accesPerdu.value,
                    accuse = accuse.value,
                    onAccuseFini = {
                        accuse.value = null
                        envoiEnCours.value = false
                    },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        lireExtras(intent)
    }

    override fun onResume() {
        super.onResume()
        relire()
    }

    override fun onDestroy() {
        unregisterReceiver(accuseEnvoi)
        super.onDestroy()
    }

    private fun relire() {
        nuit.value = nuitDuMoment(this)
        reglages.value = lireReglages(this)
        accesPerdu.value = !publierAccesCrise(this)
        autorisations.value = lireAutorisations(this)
        dossier.value = cheminAffichable(this, lireDossier(this))
        relireLeSejour()
        relireLeProgramme()
    }

    // Le programme est lu hors fil principal : c'est un fichier de Drive, sa lecture peut prendre le temps qu'elle veut.
    private fun relireLeProgramme() {
        lifecycleScope.launch {
            val lu = withContext(Dispatchers.IO) { programmeDuDossier() }
            val rendues = withContext(Dispatchers.IO) { listerReponses(this@MondeActivity) }
            programme.value = lu
            faites.value = Faites(jour = jourCourant(), reponses = rendues)
            sejour.value = sejour.value.copy(vides = videsDe(lu))
        }
    }

    private fun programmeDuDossier(): Programme =
        texteDuProgramme(this)?.let(::lireProgramme) ?: PROGRAMME_ABSENT

    // Le nom écrit rejoint la liste sans relire Drive : la carte se grise au tap, pas à la prochaine synchronisation.
    private fun enregistrerReponse(etape: String, issue: Issue) {
        lifecycleScope.launch {
            val resultat = withContext(Dispatchers.IO) {
                ecrireReponse(this@MondeActivity, reponseDe(etape, issue))
            }
            accuse.value = when (resultat) {
                is ResultatEcriture.Ecrit -> {
                    faites.value = faites.value.copy(reponses = faites.value.reponses + resultat.nom)
                    null
                }

                ResultatEcriture.DossierAbsent -> getString(R.string.reponse_dossier_absent)
                ResultatEcriture.DejaEcritAujourdhui -> null
                is ResultatEcriture.Echec -> getString(R.string.reponse_echec)
            }
        }
    }

    // Deux échecs distincts, deux phrases distinctes : le document n'est pas arrivé, ou le téléphone n'a pas de lecteur.
    private fun ouvrirLeDocument(document: String) {
        lifecycleScope.launch {
            val pdf = withContext(Dispatchers.IO) { pdfDeLaBibliotheque(this@MondeActivity, document) }
            accuse.value = when {
                pdf == null -> getString(R.string.bibliotheque_document_absent)
                ouvrirLePdf(this@MondeActivity, pdf) -> null
                else -> getString(R.string.bibliotheque_sans_lecteur)
            }
        }
    }

    // Lu hors fil principal, sans état intermédiaire affiché : le défaut est « pas fait », et ça ne se voit pas.
    private fun relireLeSejour() {
        sejour.value = sejour.value.copy(heure = LocalTime.now().hour)
        lifecycleScope.launch {
            val fait = withContext(Dispatchers.IO) { checkinDuJourExiste(this@MondeActivity, jourCourant()) }
            sejour.value = sejour.value.copy(checkinFait = fait)
        }
    }

    // demarrerCheckin() n'est pas rejoué ici : MondeKokoro le fait déjà via onOuverture en ouvrant le panneau.
    private fun lireExtras(depuis: Intent) {
        if (depuis.getBooleanExtra(EXTRA_OUVRIR_CHECKIN, false)) {
            ouvrirCheckinDemande.value = true
        }
    }

    // Même logique que l'ex-JournalActivity.demarrer() : rejouée à chaque ouverture, plus une seule fois au lancement.
    private fun demarrerCheckin() {
        val jour = jourCourant()
        checkin.value = Checkin.vide(jour)
        etapeCheckin.value = when {
            lireDossier(this) == null -> EtapeJournal.DossierAbsent
            checkinDuJourExiste(this, jour) -> EtapeJournal.DejaEcrit
            else -> EtapeJournal.Repondre(0)
        }
        repris.value = if (etapeCheckin.value is EtapeJournal.Repondre) valeursReprises(this, jour) else emptyMap()
    }

    private fun repondreCheckin(champ: Champ, valeur: Double?) {
        checkin.value = checkin.value.avec(champ, valeur)
        val suivant = (etapeCheckin.value as? EtapeJournal.Repondre)?.index?.plus(1) ?: return
        etapeCheckin.value = if (suivant < QUESTIONS.size) EtapeJournal.Repondre(suivant) else EtapeJournal.Note
    }

    private fun enregistrerCheckin(aEcrire: Checkin) {
        etapeCheckin.value = when (val resultat = ecrireCheckin(this, aEcrire)) {
            is ResultatEcriture.Ecrit -> EtapeJournal.Enregistre(resultat.nom)
            ResultatEcriture.DossierAbsent -> EtapeJournal.DossierAbsent
            ResultatEcriture.DejaEcritAujourdhui -> EtapeJournal.DejaEcrit
            is ResultatEcriture.Echec -> EtapeJournal.Echoue(resultat.cause)
        }
    }

    // Seul le mot-code remonte ici : check-in, tension et phrase sont des panneaux internes à MondeKokoro.
    private fun ouvrir(fonction: Fonction) {
        if (fonction == Fonction.MOT_CODE) envoyerLeMotCode()
    }

    // Accusé et grisage posés avant la réponse du réseau : sans ça, un second tap enverrait le message deux fois.
    private fun envoyerLeMotCode() {
        if (!tenterMotCode(this, reglages.value)) {
            ouvrirMotCode(echec = false)
            return
        }
        envoiEnCours.value = true
        accuse.value = getString(R.string.mot_code_en_cours)
    }

    private fun ouvrirMotCode(echec: Boolean) {
        startActivity(intentCrise(ECRAN_MOT_CODE).putExtra(EXTRA_ECHEC, echec))
    }

    private fun intentCrise(ecran: String): Intent =
        Intent(this, CriseActivity::class.java).putExtra(EXTRA_ECRAN, ecran)
}

private fun nuitDuMoment(context: Context): Boolean =
    estNuit(lireReglages(context).nuit, minuteCourante())

private fun paletteDuMoment(nuit: Boolean): PaletteDecor = if (nuit) DECOR_NUIT else DECOR_JOUR
