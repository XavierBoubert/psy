package io.allonsy.kokoro

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import io.allonsy.kokoro.alerte.creerCanalAlerte
import io.allonsy.kokoro.alerte.programmerAlerteTest
import io.allonsy.kokoro.corps.AtelierActivity
import io.allonsy.kokoro.corps.Expression
import io.allonsy.kokoro.crise.CriseActivity
import io.allonsy.kokoro.crise.creerCanalAcces
import io.allonsy.kokoro.crise.publierAccesCrise
import io.allonsy.kokoro.decor.capteurInclinaisonPresent
import io.allonsy.kokoro.journal.cheminAffichable
import io.allonsy.kokoro.journal.enregistrerDossier
import io.allonsy.kokoro.journal.intentChoisirDossier
import io.allonsy.kokoro.journal.lireDossier
import io.allonsy.kokoro.reglages.Parallaxe
import io.allonsy.kokoro.reglages.PlageNuit
import io.allonsy.kokoro.reglages.REGLAGES_INITIAUX
import io.allonsy.kokoro.reglages.Reglages
import io.allonsy.kokoro.reglages.ecrireHeure
import io.allonsy.kokoro.reglages.ecrireHeures
import io.allonsy.kokoro.reglages.ecrireMinutes
import io.allonsy.kokoro.reglages.ecrireReglages
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireBorne
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.ChampTexte
import io.allonsy.kokoro.ui.Interrupteur
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PageKokoro
import io.allonsy.kokoro.ui.Pancarte
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.Separateur
import io.allonsy.kokoro.ui.ThemeMonde
import io.allonsy.kokoro.ui.TypoKokoro

private const val DELAI_TEST_MILLIS = 20_000L

/** Deux chiffres, pas plus — c'est tout ce qu'une moitié d'heure peut valoir. */
private const val CHIFFRES_BORNE = 2

data class EtatAutorisations(
    val notificationsAutorisees: Boolean,
    val pleinEcranAutorise: Boolean,
    val smsAutorise: Boolean,
)

/**
 * L'écran de réglages — **la matière du monde, et la forme d'un écran de paramètres**
 * *(15/08/2026)*.
 *
 * ⭐ **Rien ici ne se règle en situation : tout se prépare à froid.** C'est ce qui autorise cet
 * écran à être long et à porter des explications, là où un écran de crise ne porte que des boutons.
 *
 * 🔴 **Le monde et le check-in n'y ont plus de bouton** *(demande de Xavier)* : l'icône du lanceur
 * ouvre le monde (D10), et le check-in est une étape de la thérapie. **Une porte par chose, pas
 * deux.**
 */
class MainActivity : ComponentActivity() {
    private val autorisations = mutableStateOf(EtatAutorisations(false, false, false))
    private val reglages = mutableStateOf(REGLAGES_INITIAUX)
    private val dossier = mutableStateOf<String?>(null)
    private val nuit = mutableStateOf(false)

    private val choixDossier = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { resultat ->
        resultat.data?.data?.let { arbre ->
            enregistrerDossier(this, arbre)
            relire()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        creerCanalAlerte(this)
        creerCanalAcces(this)
        relire()
        publierAccesCrise(this)
        setContent {
            ThemeMonde(nuit = nuit.value) {
                EcranReglages(
                    autorisations = autorisations.value,
                    reglages = reglages.value,
                    dossier = dossier.value,
                    onRelire = { relire() },
                    onEnregistrer = {
                        ecrireReglages(this, it)
                        relire()
                    },
                    onChoisirDossier = { choixDossier.launch(intentChoisirDossier()) },
                    onFermer = { finish() },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        relire()
        publierAccesCrise(this)
    }

    private fun relire() {
        autorisations.value = lireAutorisations(this)
        reglages.value = lireReglages(this)
        dossier.value = cheminAffichable(this, lireDossier(this))
        nuit.value = estNuit(reglages.value.nuit, minuteCourante())
    }
}

private fun lireAutorisations(context: Context): EtatAutorisations {
    val gestionnaire = context.getSystemService(NotificationManager::class.java)
    val pleinEcran = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        gestionnaire.canUseFullScreenIntent()
    } else {
        true
    }
    return EtatAutorisations(
        notificationsAutorisees = NotificationManagerCompat.from(context).areNotificationsEnabled(),
        pleinEcranAutorise = pleinEcran,
        smsAutorise = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) ==
            PackageManager.PERMISSION_GRANTED,
    )
}

private fun ouvrirReglagePleinEcran(context: Context) {
    val action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT
    } else {
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    }
    context.startActivity(
        Intent(action, Uri.fromParts("package", context.packageName, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
    )
}

private fun ouvrirReglageNotifications(context: Context) {
    context.startActivity(
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
    )
}

@Composable
private fun EcranReglages(
    autorisations: EtatAutorisations,
    reglages: Reglages,
    dossier: String?,
    onRelire: () -> Unit,
    onEnregistrer: (Reglages) -> Unit,
    onChoisirDossier: () -> Unit,
    onFermer: () -> Unit,
) {
    val context = LocalContext.current
    val demandeNotifications = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { onRelire() },
    )
    val demandeSms = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { onRelire() },
    )

    PageKokoro(
        titre = stringResource(R.string.controle_titre),
        couleur = LocalPaletteKokoro.current.beurre,
        ecart = 14.dp,
        locuteur = Expression.SEREIN,
        onFermer = onFermer,
    ) {
        Explication(stringResource(R.string.controle_sous_titre))

        Section(stringResource(R.string.controle_section_contact))
        ChampsContact(reglages = reglages, onEnregistrer = onEnregistrer)

        Section(stringResource(R.string.controle_section_autorisations))
        Groupe {
            LigneEtat(
                libelle = stringResource(R.string.controle_etat_notifications),
                accorde = autorisations.notificationsAutorisees,
            )
            Separateur()
            LigneEtat(
                libelle = stringResource(R.string.controle_etat_plein_ecran),
                accorde = autorisations.pleinEcranAutorise,
            )
            Separateur()
            LigneEtat(
                libelle = stringResource(R.string.controle_etat_sms),
                accorde = autorisations.smsAutorise,
            )
        }

        if (!autorisations.notificationsAutorisees) {
            Action(stringResource(R.string.controle_action_notifications)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    demandeNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    ouvrirReglageNotifications(context)
                }
            }
        }

        if (!autorisations.pleinEcranAutorise) {
            Explication(stringResource(R.string.controle_guidage_plein_ecran))
            Action(stringResource(R.string.controle_action_plein_ecran)) {
                ouvrirReglagePleinEcran(context)
            }
        }

        if (!autorisations.smsAutorise) {
            Explication(stringResource(R.string.controle_guidage_sms))
            Action(stringResource(R.string.controle_action_sms)) {
                demandeSms.launch(Manifest.permission.SEND_SMS)
            }
        }

        Section(stringResource(R.string.controle_section_acces))
        Explication(stringResource(R.string.controle_acces_explication))
        Action(stringResource(R.string.controle_action_acces)) { publierAccesCrise(context) }
        Action(stringResource(R.string.controle_action_ouvrir_crise)) {
            context.startActivity(Intent(context, CriseActivity::class.java))
        }

        Section(stringResource(R.string.controle_section_journal))
        Groupe {
            Valeur(
                when (dossier) {
                    null -> stringResource(R.string.controle_dossier_absent)
                    else -> stringResource(R.string.controle_dossier_choisi, dossier)
                },
            )
        }
        Explication(stringResource(R.string.controle_dossier_explication))
        Action(stringResource(R.string.controle_action_dossier), onClick = onChoisirDossier)

        Section(stringResource(R.string.controle_section_nuit))
        Explication(stringResource(R.string.controle_nuit_explication))
        ChampsNuit(nuit = reglages.nuit, onEnregistrer = { onEnregistrer(reglages.copy(nuit = it)) })

        Section(stringResource(R.string.controle_section_parallaxe))
        Explication(stringResource(R.string.controle_parallaxe_explication))
        ChampsParallaxe(
            parallaxe = reglages.parallaxe,
            capteurPresent = remember(context) { capteurInclinaisonPresent(context) },
            onEnregistrer = { onEnregistrer(reglages.copy(parallaxe = it)) },
        )

        Section(stringResource(R.string.controle_section_corps))
        Explication(stringResource(R.string.controle_corps_explication))
        Action(stringResource(R.string.controle_action_corps)) {
            context.startActivity(Intent(context, AtelierActivity::class.java))
        }

        Section(stringResource(R.string.controle_section_test))
        Explication(stringResource(R.string.controle_consigne_test))
        Action(
            libelle = stringResource(R.string.controle_action_test),
            actif = autorisations.notificationsAutorisees,
        ) {
            programmerAlerteTest(context, DELAI_TEST_MILLIS)
        }
    }
}

/**
 * Le destinataire du mot-code, et **le message lui-même** *(15/08/2026, demande de Xavier)*.
 *
 * 🔴 **Le changer ici ne prévient personne.** Un mot-code n'a de valeur que parce que la personne qui
 * le reçoit sait ce qu'il veut dire : le réglage sert à suivre un accord qui a bougé, **il ne le
 * remplace pas**.
 */
@Composable
private fun ChampsContact(reglages: Reglages, onEnregistrer: (Reglages) -> Unit) {
    var nom by remember(reglages) { mutableStateOf(reglages.contactNom) }
    var numero by remember(reglages) { mutableStateOf(reglages.contactNumero) }
    var motCode by remember(reglages) { mutableStateOf(reglages.motCode) }

    Groupe {
        Valeur(
            when {
                reglages.contactRenseigne -> stringResource(
                    R.string.controle_contact_enregistre,
                    reglages.contactNom,
                    reglages.contactNumero,
                )
                else -> stringResource(R.string.controle_contact_absent)
            },
        )
        Separateur()
        Champ(libelle = stringResource(R.string.controle_champ_nom), valeur = nom, onValeur = { nom = it })
        Separateur()
        Champ(
            libelle = stringResource(R.string.controle_champ_numero),
            valeur = numero,
            onValeur = { numero = it },
            clavier = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
        Separateur()
        Champ(
            libelle = stringResource(R.string.controle_champ_mot_code),
            valeur = motCode,
            onValeur = { motCode = it },
        )
    }
    Explication(stringResource(R.string.controle_mot_code_explication))
    Action(
        libelle = stringResource(R.string.controle_action_enregistrer),
        actif = motCode.isNotBlank(),
    ) {
        onEnregistrer(reglages.copy(contactNom = nom, contactNumero = numero, motCode = motCode))
    }
}

/**
 * Le réglage de la nuit — un interrupteur et deux heures, **chacune en deux champs de deux
 * chiffres** *(15/08/2026)*.
 *
 * ⭐ **Le bouton d'enregistrement reste inerte tant qu'une des deux heures ne se lit pas.** Rien
 * n'est corrigé en silence : un réglage qu'on croit posé et qui ne l'est pas serait pire qu'un
 * réglage qui refuse de partir.
 */
@Composable
private fun ChampsNuit(nuit: PlageNuit, onEnregistrer: (PlageNuit) -> Unit) {
    var debutHeures by remember(nuit) { mutableStateOf(ecrireHeures(nuit.debut)) }
    var debutMinutes by remember(nuit) { mutableStateOf(ecrireMinutes(nuit.debut)) }
    var finHeures by remember(nuit) { mutableStateOf(ecrireHeures(nuit.fin)) }
    var finMinutes by remember(nuit) { mutableStateOf(ecrireMinutes(nuit.fin)) }

    Groupe {
        Valeur(
            when {
                nuit.active -> stringResource(
                    R.string.controle_nuit_reglee,
                    ecrireHeure(nuit.debut),
                    ecrireHeure(nuit.fin),
                )
                else -> stringResource(R.string.controle_nuit_coupee)
            },
        )
        Separateur()
        Ligne(stringResource(R.string.controle_nuit_active)) {
            Interrupteur(actif = nuit.active, onChange = { onEnregistrer(nuit.copy(active = it)) })
        }
        Separateur()
        Ligne(stringResource(R.string.controle_nuit_debut)) {
            Borne(
                heures = debutHeures,
                minutes = debutMinutes,
                onHeures = { debutHeures = it },
                onMinutes = { debutMinutes = it },
            )
        }
        Separateur()
        Ligne(stringResource(R.string.controle_nuit_fin)) {
            Borne(
                heures = finHeures,
                minutes = finMinutes,
                onHeures = { finHeures = it },
                onMinutes = { finMinutes = it },
            )
        }
    }
    Action(
        libelle = stringResource(R.string.controle_action_nuit),
        actif = lireBorne(debutHeures, debutMinutes) != null && lireBorne(finHeures, finMinutes) != null,
    ) {
        val ouverture = lireBorne(debutHeures, debutMinutes) ?: return@Action
        val fermeture = lireBorne(finHeures, finMinutes) ?: return@Action
        onEnregistrer(nuit.copy(debut = ouverture, fin = fermeture))
    }
}

/**
 * Le mouvement du décor — **deux interrupteurs, et ils prennent effet tout de suite**
 * *(15/08/2026, demande de Xavier)*.
 *
 * ⭐ **Pas de bouton *Enregistrer* ici, contrairement à la nuit.** Un interrupteur n'a rien à
 * valider : il n'y a pas de saisie qui pourrait ne pas se lire, donc rien qui puisse partir de
 * travers en silence. C'est déjà la règle de l'interrupteur de la nuit, juste au-dessus.
 *
 * 🔴 **La ligne de l'inclinaison disparaît quand la parallaxe est coupée** — elle ne ferait alors
 * rien du tout — **et quand le téléphone n'a pas le capteur**, où une phrase dit pourquoi. Un
 * interrupteur inerte est un réglage qu'on croit posé.
 */
@Composable
private fun ChampsParallaxe(
    parallaxe: Parallaxe,
    capteurPresent: Boolean,
    onEnregistrer: (Parallaxe) -> Unit,
) {
    val suivi = parallaxe.inclinaison && capteurPresent

    Groupe {
        Valeur(
            stringResource(
                when {
                    !parallaxe.actif -> R.string.controle_parallaxe_coupee
                    suivi -> R.string.controle_parallaxe_complete
                    else -> R.string.controle_parallaxe_doigt
                },
            ),
        )
        Separateur()
        Ligne(stringResource(R.string.controle_parallaxe_active)) {
            Interrupteur(
                actif = parallaxe.actif,
                onChange = { onEnregistrer(parallaxe.copy(actif = it)) },
            )
        }
        if (parallaxe.actif && capteurPresent) {
            Separateur()
            Ligne(stringResource(R.string.controle_parallaxe_inclinaison)) {
                Interrupteur(
                    actif = parallaxe.inclinaison,
                    onChange = { onEnregistrer(parallaxe.copy(inclinaison = it)) },
                )
            }
        }
    }
    when {
        !capteurPresent -> Explication(stringResource(R.string.controle_parallaxe_sans_capteur))
        parallaxe.actif -> Explication(
            stringResource(R.string.controle_parallaxe_inclinaison_explication),
        )
    }
}

/**
 * Une heure : deux champs de deux chiffres, séparés par un deux-points **écrit à l'écran et non à
 * saisir** — le clavier numérique d'Android ne le porte pas.
 */
@Composable
private fun Borne(
    heures: String,
    minutes: String,
    onHeures: (String) -> Unit,
    onMinutes: (String) -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        ChampChiffres(valeur = heures, onValeur = onHeures, repere = stringResource(R.string.controle_nuit_heures))
        Text(
            text = ":",
            style = TypoKokoro.corps,
            color = palette.encreDouce,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        ChampChiffres(valeur = minutes, onValeur = onMinutes, repere = stringResource(R.string.controle_nuit_minutes))
    }
}

@Composable
private fun ChampChiffres(valeur: String, onValeur: (String) -> Unit, repere: String) {
    ChampTexte(
        valeur = valeur,
        onValeur = { saisie ->
            if (saisie.length <= CHIFFRES_BORNE && saisie.all(Char::isDigit)) onValeur(saisie)
        },
        modifier = Modifier.width(74.dp),
        repere = repere,
        clavier = KeyboardOptions(keyboardType = KeyboardType.Number),
        alignement = TextAlign.Center,
    )
}

/** Le titre d'une section — une pancarte, la même que celles des écrans du monde. */
@Composable
private fun Section(libelle: String) {
    Pancarte(texte = libelle, couleur = LocalPaletteKokoro.current.peche, modifier = Modifier.padding(top = 16.dp))
}

/** Ce qui range les lignes d'une section : **un seul panneau, des traits dedans.** */
@Composable
private fun Groupe(contenu: @Composable () -> Unit) {
    PanneauExtrude(modifier = Modifier.fillMaxWidth()) { contenu() }
}

/** Une ligne de réglage : ce qu'elle règle à gauche, de quoi le régler à droite. */
@Composable
private fun Ligne(libelle: String, valeur: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = libelle,
            style = TypoKokoro.corps,
            color = LocalPaletteKokoro.current.encre,
            modifier = Modifier.padding(end = 14.dp),
        )
        Box(contentAlignment = Alignment.CenterEnd) { valeur() }
    }
}

/** Un champ nommé : le nom au-dessus, la saisie en dessous, jamais l'un dans l'autre. */
@Composable
private fun Champ(
    libelle: String,
    valeur: String,
    onValeur: (String) -> Unit,
    clavier: KeyboardOptions = KeyboardOptions.Default,
) {
    Text(
        text = libelle,
        style = TypoKokoro.discret,
        color = LocalPaletteKokoro.current.encreDouce,
        modifier = Modifier.padding(bottom = 8.dp),
    )
    ChampTexte(
        valeur = valeur,
        onValeur = onValeur,
        modifier = Modifier.fillMaxWidth(),
        clavier = clavier,
    )
}

/** Ce qui est enregistré aujourd'hui — un constat, jamais un commentaire. */
@Composable
private fun Valeur(texte: String) {
    Text(text = texte, style = TypoKokoro.corps, color = LocalPaletteKokoro.current.encre)
}

@Composable
private fun Explication(texte: String) {
    Text(
        text = texte,
        style = TypoKokoro.lecture,
        color = LocalPaletteKokoro.current.encreDouce,
        modifier = Modifier.padding(horizontal = 4.dp),
    )
}

@Composable
private fun Action(libelle: String, actif: Boolean = true, onClick: () -> Unit) {
    BoutonEpais(libelle = libelle, onClic = onClick, actif = actif, hauteurMinimale = 62.dp)
}

@Composable
private fun LigneEtat(libelle: String, accorde: Boolean) {
    val marque = stringResource(
        if (accorde) R.string.controle_accorde else R.string.controle_refuse,
    )
    Ligne(libelle) {
        Text(text = marque, style = TypoKokoro.corps, color = LocalPaletteKokoro.current.encreDouce)
    }
}
