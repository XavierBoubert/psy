package io.allonsy.kokoro.monde

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
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
import io.allonsy.kokoro.crise.PorteDeCrise
import io.allonsy.kokoro.crise.creerCanalAcces
import io.allonsy.kokoro.crise.publierAccesCrise
import io.allonsy.kokoro.crise.tenterMotCode
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.dossier.ResultatEcriture
import io.allonsy.kokoro.dossier.cheminAffichable
import io.allonsy.kokoro.dossier.derniereReponse
import io.allonsy.kokoro.dossier.ecrireReponse
import io.allonsy.kokoro.dossier.enregistrerDossier
import io.allonsy.kokoro.dossier.entrainementsMenes
import io.allonsy.kokoro.dossier.intentChoisirDossier
import io.allonsy.kokoro.dossier.jourCourant
import io.allonsy.kokoro.dossier.lireDossier
import io.allonsy.kokoro.dossier.listerReponses
import io.allonsy.kokoro.dossier.marquerEntrainement
import io.allonsy.kokoro.dossier.pdfDeLaBibliotheque
import io.allonsy.kokoro.dossier.pdfDuBilan
import io.allonsy.kokoro.dossier.texteDuProgramme
import io.allonsy.kokoro.programme.AUCUNE_FAITE
import io.allonsy.kokoro.programme.Carte
import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.programme.PROGRAMME_ABSENT
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.ReponseItem
import io.allonsy.kokoro.programme.lireProgramme
import io.allonsy.kokoro.programme.reponseDe
import io.allonsy.kokoro.programme.valeursDeLaReponse
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

// Ne s'affiche jamais par-dessus le verrouillage : c'est CriseActivity qui porte cette déclaration dans le manifeste.
class MondeActivity : ComponentActivity() {
    private val nuit = mutableStateOf(false)
    private val reglages = mutableStateOf(REGLAGES_INITIAUX)
    private val accuse = mutableStateOf<String?>(null)
    private val envoiEnCours = mutableStateOf(false)
    private val accesPerdu = mutableStateOf(false)
    private val sejour = mutableStateOf(Sejour(heure = 0))
    private val programme = mutableStateOf(PROGRAMME_ABSENT)
    private val faites = mutableStateOf(AUCUNE_FAITE)

    // Ex-MainActivity : la roue dentée ouvre un panneau interne, plus une Activity.
    private val autorisations = mutableStateOf(EtatAutorisations(false, false, false))
    private val dossier = mutableStateOf<String?>(null)

    private val choixDossier = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { resultat ->
        resultat.data?.data?.let { arbre ->
            enregistrerDossier(this, arbre)
            relire()
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
        setContent {
            ThemeMonde(nuit = nuit.value) {
                MondeKokoro(
                    palette = paletteDuMoment(nuit.value),
                    contactNom = reglages.value.contactNom,
                    sejour = sejour.value,
                    onPorteDeCrise = { ouvrir(it) },
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
                    programme = programme.value,
                    faites = faites.value,
                    onRendu = { carte, issue, items -> enregistrerReponse(carte, issue, items) },
                    onEntrainement = { carte -> retenirLEntrainement(carte) },
                    onPdf = { carte -> ouvrirLeDocument(carte) },
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
        sejour.value = sejour.value.copy(heure = LocalTime.now().hour)
        relireLeProgramme()
    }

    // Le programme est lu hors fil principal : c'est un fichier de Drive, sa lecture peut prendre le temps qu'elle veut.
    private fun relireLeProgramme() {
        lifecycleScope.launch {
            val lu = withContext(Dispatchers.IO) { programmeDuDossier() }
            val rendues = withContext(Dispatchers.IO) { listerReponses(this@MondeActivity) }
            val reprises = withContext(Dispatchers.IO) { reprisesDuProgramme(lu) }
            programme.value = lu
            faites.value = Faites(
                jour = jourCourant(),
                reponses = rendues,
                entrainements = entrainementsMenes(this@MondeActivity),
                reprises = reprises,
            )
            sejour.value = sejour.value.copy(vides = videsDe(lu))
        }
    }

    private fun programmeDuDossier(): Programme =
        texteDuProgramme(this)?.let(::lireProgramme) ?: PROGRAMME_ABSENT

    // Une question qui se reprend repart de la dernière valeur donnée : Xavier ne redonne pas ce qu'il a déjà donné.
    private fun reprisesDuProgramme(programme: Programme): Map<String, Double> =
        programme.cartes
            .filterIsInstance<Carte.Panneau>()
            .filter { carte -> carte.etapes.any { it is Etape.Question && it.reprise } }
            .flatMap { carte ->
                derniereReponse(this, carte.reperes.id)?.let(::valeursDeLaReponse)?.toList().orEmpty()
            }
            .toMap()

    // Le nom écrit rejoint la liste sans relire Drive : la carte se grise au tap, pas à la prochaine synchronisation.
    private fun enregistrerReponse(carte: String, issue: Issue, items: List<ReponseItem>) {
        lifecycleScope.launch {
            val resultat = withContext(Dispatchers.IO) {
                ecrireReponse(this@MondeActivity, reponseDe(carte, issue, items = items))
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

    private fun retenirLEntrainement(carte: String) {
        marquerEntrainement(this, carte)
        faites.value = faites.value.copy(entrainements = faites.value.entrainements + carte)
    }

    // Deux échecs distincts, deux phrases distinctes : le document n'est pas arrivé, ou le téléphone n'a pas de lecteur.
    private fun ouvrirLeDocument(carte: Carte.Pdf) {
        lifecycleScope.launch {
            val pdf = withContext(Dispatchers.IO) { resoudreLePdf(carte) }
            accuse.value = when {
                pdf == null -> getString(R.string.bibliotheque_document_absent)
                ouvrirLePdf(this@MondeActivity, pdf) -> null
                else -> getString(R.string.bibliotheque_sans_lecteur)
            }
        }
    }

    // 🔴 Un bilan ne passe pas par la bibliothèque : canal distinct au dépôt, dossier distinct dans le transit.
    private fun resoudreLePdf(carte: Carte.Pdf): Uri? =
        if (carte.date == null) {
            pdfDeLaBibliotheque(this, carte.document)
        } else {
            pdfDuBilan(this, carte.document)
        }

    private fun ouvrir(porte: PorteDeCrise) {
        if (porte == PorteDeCrise.MOT_CODE) envoyerLeMotCode()
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
