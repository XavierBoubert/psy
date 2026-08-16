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
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import io.allonsy.kokoro.MainActivity
import io.allonsy.kokoro.R
import io.allonsy.kokoro.crise.ACTION_MOT_CODE_ENVOYE
import io.allonsy.kokoro.crise.CriseActivity
import io.allonsy.kokoro.crise.ECRAN_MOT_CODE
import io.allonsy.kokoro.crise.ECRAN_PHRASE
import io.allonsy.kokoro.crise.ECRAN_TENSION
import io.allonsy.kokoro.crise.EXTRA_ECHEC
import io.allonsy.kokoro.crise.EXTRA_ECRAN
import io.allonsy.kokoro.crise.creerCanalAcces
import io.allonsy.kokoro.crise.publierAccesCrise
import io.allonsy.kokoro.crise.tenterMotCode
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.journal.JournalActivity
import io.allonsy.kokoro.journal.checkinDuJourExiste
import io.allonsy.kokoro.journal.jourCourant
import io.allonsy.kokoro.reglages.REGLAGES_INITIAUX
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.ThemeMonde
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

/**
 * Le monde — l'écran où Kokoro habite, et **l'interface principale de l'app** depuis la v2 de
 * `companion/INTERFACE.md`.
 *
 * ⭐ **Bord à bord** : le décor passe sous la barre d'état et sous la barre de navigation. Les
 * surfaces qui portent du texte, elles, respectent les encoches — **le décor ne porte jamais de
 * texte** (**P3**).
 *
 * ⭐ **L'heure est lue à l'arrivée, et à ce moment-là seulement** *(14/08/2026)*. Si la nuit tombe
 * pendant que le monde est ouvert, il ne change pas : Xavier retrouvera la nuit à sa prochaine
 * venue. Un décor qui vire sous les yeux serait un mouvement à interpréter, et le dispositif n'en
 * provoque aucun. **Le thème de l'interface suit la même heure que le décor** — jamais le thème
 * système.
 *
 * 🔴 **Le monde ne s'affiche jamais par-dessus le verrouillage** *(15/08/2026)*. On l'a essayé pour
 * la notification, **et le téléphone a demandé le déverrouillage** : le monde vit dans la tâche du
 * lanceur, et un `showWhenLocked` posé à l'exécution arrive après la décision du keyguard. **La
 * porte du verrouillage reste `CriseActivity`**, qui le déclare dans le manifeste — voir
 * `crise/AccesCrise.kt`.
 *
 * ⭐ **C'est lui qui republie la notification d'accès** *(15/08/2026)* : elle était accrochée à
 * l'écran de contrôle, qui n'est plus le point d'entrée depuis **D10**. **Une porte de crise qui
 * n'existe que si Xavier a pensé à ouvrir les réglages n'est pas une porte.**
 */
class MondeActivity : ComponentActivity() {
    private val nuit = mutableStateOf(false)
    private val reglages = mutableStateOf(REGLAGES_INITIAUX)
    private val accuse = mutableStateOf<String?>(null)
    private val envoiEnCours = mutableStateOf(false)
    private val accesPerdu = mutableStateOf(false)
    private val sejour = mutableStateOf(Sejour(heure = 0, checkinFait = false))

    /**
     * 🧪 Les bascules de test de l'affichage — **jamais montrées hors build debug**
     * ([BuildConfig.DEBUG]). Elles ne touchent à rien du dossier : elles forcent ce que Kokoro
     * montre, le temps de comparer ses affichages à l'écran sans attendre l'heure ou une vraie liste.
     *
     * ⭐ `null` dans [affichageForce] veut dire *l'heure réelle décide*, comme toujours.
     */
    private val affichageForce = mutableStateOf<Boolean?>(null)
    private val documentationVide = mutableStateOf(true)
    private val bilanVide = mutableStateOf(true)

    /**
     * 🔴 **Le seul retour que l'envoi direct donne.** Un SMS parti n'affiche rien de lui-même : sans
     * cet accusé, l'écran resterait exactement tel qu'avant l'appui, et **rien ne dirait si le
     * message est parti** — le doute conduirait à re-taper.
     */
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
        relire()
        setContent {
            ThemeMonde(nuit = nuit.value) {
                MondeKokoro(
                    palette = paletteDuMoment(nuit.value),
                    contactNom = reglages.value.contactNom,
                    sejour = sejour.value.copy(
                        heure = when (affichageForce.value) {
                            null -> sejour.value.heure
                            true -> HEURE_DU_CHECKIN
                            false -> 0
                        },
                        vides = setOfNotNull(
                            Ecran.DOCUMENTATION.takeIf { documentationVide.value },
                            Ecran.BILAN.takeIf { bilanVide.value },
                        ),
                    ),
                    onFonction = { ouvrir(it) },
                    onReglages = { startActivity(Intent(this, MainActivity::class.java)) },
                    parallaxe = reglages.value.parallaxe,
                    envoiEnCours = envoiEnCours.value,
                    accesPerdu = accesPerdu.value,
                    accuse = accuse.value,
                    onAccuseFini = {
                        accuse.value = null
                        envoiEnCours.value = false
                    },
                    debug = DebugMonde(
                        documentationVide = documentationVide.value,
                        bilanVide = bilanVide.value,
                        onBasculerAffichageTherapie = { affichageForce.value = it },
                        onBasculerDocumentationVide = { documentationVide.value = !documentationVide.value },
                        onBasculerBilanVide = { bilanVide.value = !bilanVide.value },
                    ),
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

    /**
     * 🔴 **La notification d'accès est republiée à chaque venue** *(15/08/2026)*. Elle ne l'était
     * qu'à l'ouverture de l'écran de contrôle — donc **jamais**, depuis que l'icône du lanceur ouvre
     * le monde (**D10**) : Xavier a dû aller la chercher dans les réglages.
     *
     * ⚠️ **Ce n'est pas une relance, et ça ne peut pas en devenir une** : [publierAccesCrise] réécrit
     * une notification permanente, muette, au même identifiant. **Rien de neuf ne paraît, rien ne
     * sonne, rien ne compte.** Elle refuse d'elle-même tant que l'autorisation n'est pas accordée —
     * **et aucun écran ne la réclame en ouverture** : la demande vit dans les réglages, à froid.
     *
     * ⭐ **Quand elle refuse, l'écran d'entrée le dit en toutes lettres** *(15/08/2026, tranché par
     * Xavier)* — voir [AvisAcces]. 🔴 **Un défaut silencieux serait le pire des deux mondes** : la
     * porte du verrouillage aurait disparu, et rien ne l'aurait signalé.
     */
    private fun relire() {
        nuit.value = nuitDuMoment(this)
        reglages.value = lireReglages(this)
        accesPerdu.value = !publierAccesCrise(this)
        relireLeSejour()
    }

    /**
     * Ce que l'habitant sait du monde (`PRESENCE.md` §2) — **l'heure, et si le check-in du jour est
     * écrit.**
     *
     * ⭐ **L'heure est lue à l'arrivée, comme celle du décor** : si 18 h passent pendant que le monde
     * est ouvert, Kokoro ne change pas de place sous les yeux de Xavier. Il aura bougé à la
     * prochaine venue, et **la prévisibilité est une fonctionnalité**.
     *
     * 🔴 **L'existence du fichier se lit hors du fil principal.** Le fournisseur de documents peut
     * mettre une seconde à répondre — Google Drive le fait —, et une interface figée à l'ouverture
     * du monde serait pire que l'expression qu'on attend.
     *
     * ⭐ **Le défaut est *pas fait*, et il ne se voit pas** : tant que la réponse n'est pas revenue,
     * l'expression est celle de tous les jours. 🔴 **Aucun état intermédiaire ne s'affiche** — ni
     * attente, ni point, ni grisé : il n'y a rien à attendre, et rien à interpréter.
     */
    private fun relireLeSejour() {
        sejour.value = sejour.value.copy(heure = LocalTime.now().hour)
        lifecycleScope.launch {
            val fait = withContext(Dispatchers.IO) { checkinDuJourExiste(this@MondeActivity, jourCourant()) }
            sejour.value = sejour.value.copy(checkinFait = fait)
        }
    }

    /**
     * 🔴 **Le monde ne réimplémente aucune fonction de crise** : il ouvre celles qui existent, et
     * qui ont été éprouvées pour de vrai. **Deux portes, un seul contenu** (§6.2).
     *
     * ⭐ **Sauf le mot-code, qui part d'un seul appui** *(15/08/2026, demande de Xavier)* — voir
     * [envoyerLeMotCode].
     */
    private fun ouvrir(fonction: Fonction) {
        when (fonction) {
            Fonction.CHECK_IN -> startActivity(Intent(this, JournalActivity::class.java))
            Fonction.MOT_CODE -> envoyerLeMotCode()
            Fonction.TENSION -> startActivity(intentCrise(ECRAN_TENSION))
            Fonction.PHRASE -> startActivity(intentCrise(ECRAN_PHRASE))
        }
    }

    /**
     * ⭐ **Un appui, le message part.** Il n'y a plus d'écran de confirmation entre le bouton et
     * l'envoi : **demander « es-tu sûr ? » à quelqu'un qui vient de perdre la parole, c'est lui
     * demander un tap de plus au moment précis où il n'en a plus.** Le geste est déjà volontaire —
     * il faut traverser le monde jusqu'à l'écran du bas pour l'atteindre.
     *
     * 🔴 **Les deux cas où l'envoi direct est impossible gardent l'ancien écran** — pas de numéro
     * enregistré, ou autorisation SMS refusée. Il explique, et il propose l'application Messages.
     * **Un bouton qui n'envoie rien en silence serait pire que l'écran de trop.**
     *
     * ⭐ **L'accusé paraît à l'appui, avant même la réponse du réseau**, et **le bouton se grise le
     * temps que le message parte** *(15/08/2026, demande de Xavier)*. Le SMS met parfois deux
     * secondes à s'acquitter : sans ces deux-là, l'écran resterait figé assez longtemps pour qu'on
     * re-tape, **et le message partirait deux fois**.
     */
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
