package io.allonsy.kokoro.monde

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.programme.Carte
import io.allonsy.kokoro.programme.Compteur
import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.programme.Porteur
import io.allonsy.kokoro.programme.ReponseItem
import io.allonsy.kokoro.programme.Saisie
import io.allonsy.kokoro.programme.Unite
import io.allonsy.kokoro.programme.rend
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.ChampTexte
import io.allonsy.kokoro.ui.Interrupteur
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PanneauDialogue
import io.allonsy.kokoro.ui.TypoKokoro
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.abs

private const val TICK_MILLIS = 200L

private enum class Passage { ACCUEIL, DEROULE, APRES }

private data class Marche(
    val rang: Int,
    val blanc: Boolean,
    val coches: Set<Int>,
    val commence: Boolean,
    val confirme: Boolean,
)

// Une carte, un panneau, des étapes dans l'ordre. Kokoro n'interprète rien : il déroule ce qu'on lui a donné,
// et renvoie ce que Xavier en a fait. 🔴 Le minuteur vit dans le panneau : quitter l'app l'arrête sans rien écrire.
@Composable
fun PanneauCarte(
    carte: Carte.Panneau,
    faite: Boolean,
    entraine: Boolean,
    reprises: Map<String, Double>,
    onRendu: (Issue, List<ReponseItem>) -> Unit,
    onEntrainementMene: () -> Unit,
    onFermer: () -> Unit,
) {
    val aidant = carte.porteur == Porteur.AIDANT
    val cle = carte.reperes.id

    var passage by remember(cle) { mutableStateOf(if (aidant) Passage.ACCUEIL else Passage.DEROULE) }
    var marche by remember(cle) {
        mutableStateOf(Marche(rang = 0, blanc = false, coches = emptySet(), commence = false, confirme = false))
    }
    var items by remember(cle) { mutableStateOf(emptyList<ReponseItem>()) }
    var reste by remember(cle) { mutableIntStateOf(0) }
    var menee by remember(cle) { mutableStateOf(false) }

    val rendue = carte.etapes.any { it.rend }
    val etape = carte.etapes.getOrNull(marche.rang)

    val clore = { issue: Issue, rendus: List<ReponseItem> ->
        if (rendue) onRendu(issue, rendus)
        passage = Passage.APRES
    }

    val terminer = {
        menee = true
        if (marche.blanc) onEntrainementMene()
        clore(
            when {
                marche.blanc -> Issue.ENTRAINEMENT
                marche.confirme -> Issue.FAIT
                else -> Issue.TERMINE
            },
            items,
        )
    }

    val avancer = {
        if (marche.rang + 1 < carte.etapes.size) {
            marche = marche.copy(rang = marche.rang + 1, coches = emptySet())
        } else {
            terminer()
        }
    }

    // Rien n'a encore été produit : arrêter ferme le panneau sans écrire. Pas encore fait n'est pas une donnée.
    val arreter = {
        if (!marche.commence) {
            onFermer()
        } else {
            clore(if (marche.blanc) Issue.ENTRAINEMENT else Issue.ARRETE, items)
        }
    }

    val repondre = { item: ReponseItem ->
        items = items + item
        marche = marche.copy(commence = true)
        avancer()
    }

    // Aucun son, aucune vibration à zéro : l'écran passe seul à l'étape suivante, et il ne la commente pas.
    // 🔴 L'entraînement ne chronomètre pas : l'aidant passe à la main, le temps affiché reste celui de la séance.
    LaunchedEffect(passage, marche.rang, marche.blanc) {
        val minuteur = etape as? Etape.Minuteur ?: return@LaunchedEffect
        if (passage != Passage.DEROULE) return@LaunchedEffect
        reste = minuteur.secondes
        if (marche.blanc) return@LaunchedEffect
        marche = marche.copy(commence = true)
        val depuis = SystemClock.elapsedRealtime()
        while (reste > 0) {
            reste = resteSecondes(minuteur.secondes, SystemClock.elapsedRealtime() - depuis)
            if (reste > 0) delay(TICK_MILLIS)
        }
        avancer()
    }

    PanneauDialogue(
        titre = carte.reperes.titre,
        ecart = 16.dp,
        remonteSur = passage to marche.rang,
        onFermer = onFermer,
    ) {
        when {
            passage == Passage.ACCUEIL -> AccueilDeLAidant(
                entraine = entraine,
                onEntrer = { blanc ->
                    marche = marche.copy(blanc = blanc)
                    passage = Passage.DEROULE
                },
            )

            passage == Passage.APRES -> Apres(
                enregistre = rendue,
                menee = menee,
                aidant = aidant,
                onFermer = onFermer,
            )

            etape == null -> Apres(enregistre = false, menee = false, aidant = aidant, onFermer = onFermer)

            else -> VueEtape(
                carte = carte,
                etape = etape,
                marche = marche,
                reste = reste,
                faite = faite,
                reprises = reprises,
                onCocher = { ligne, coche ->
                    marche = marche.copy(
                        coches = if (coche) marche.coches + ligne else marche.coches - ligne,
                    )
                },
                onRepondre = repondre,
                onConfirmer = {
                    marche = marche.copy(commence = true, confirme = true)
                    avancer()
                },
                onAvancer = avancer,
                onArreter = arreter,
                onFermer = onFermer,
            )
        }
    }
}

@Composable
private fun VueEtape(
    carte: Carte.Panneau,
    etape: Etape,
    marche: Marche,
    reste: Int,
    faite: Boolean,
    reprises: Map<String, Double>,
    onCocher: (Int, Boolean) -> Unit,
    onRepondre: (ReponseItem) -> Unit,
    onConfirmer: () -> Unit,
    onAvancer: () -> Unit,
    onArreter: () -> Unit,
    onFermer: () -> Unit,
) {
    when (etape) {
        is Etape.Info -> VueInfo(etape = etape, onAvancer = onAvancer)

        is Etape.Checklist -> VueChecklist(
            carte = carte,
            etape = etape,
            blanc = marche.blanc,
            coches = marche.coches,
            onCocher = onCocher,
            onAvancer = onAvancer,
        )

        is Etape.Question -> VueQuestion(
            etape = etape,
            rang = rangDe(carte, marche.rang),
            depart = depart(etape, reprises),
            onRepondre = onRepondre,
            onArreter = onArreter,
        )

        is Etape.Note -> VueNote(etape = etape, onRepondre = onRepondre, onArreter = onArreter)

        is Etape.Minuteur -> VueMinuteur(
            etape = etape,
            rang = rangDe(carte, marche.rang),
            reste = reste,
            blanc = marche.blanc,
            aidant = carte.porteur == Porteur.AIDANT,
            onAvancer = onAvancer,
            onArreter = onArreter,
        )

        is Etape.Confirmation -> VueConfirmation(
            etape = etape,
            faite = faite,
            onConfirmer = onConfirmer,
            onFermer = onFermer,
        )
    }
}

@Composable
private fun AccueilDeLAidant(entraine: Boolean, onEntrer: (Boolean) -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(text = stringResource(R.string.duo_accueil), style = TypoKokoro.lecture, color = palette.encre)
    Text(
        text = stringResource(if (entraine) R.string.duo_entrainement_mene else R.string.duo_accueil_entrainement),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    BoutonEpais(
        libelle = stringResource(R.string.duo_action_entrainement),
        onClic = { onEntrer(true) },
        couleur = if (entraine) null else palette.menthe,
        modifier = Modifier.padding(top = 6.dp),
    )
    BoutonEpais(
        libelle = stringResource(R.string.duo_action_seance),
        onClic = { onEntrer(false) },
        couleur = if (entraine) palette.menthe else null,
    )
}

@Composable
private fun VueInfo(etape: Etape.Info, onAvancer: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(
        text = etape.texte,
        style = if (etape.montrable) TypoKokoro.titre else TypoKokoro.lecture,
        color = palette.encre,
    )
    if (etape.montrable) {
        Text(
            text = stringResource(R.string.carte_montrable),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
    }
    BoutonEpais(
        libelle = stringResource(R.string.carte_action_suivant),
        onClic = onAvancer,
        couleur = palette.menthe,
        modifier = Modifier.padding(top = 6.dp),
    )
}

// L'aidant coche, ou n'entre pas dans le déroulé : cocher est la seule preuve que les critères d'arrêt ont été lus.
@Composable
private fun VueChecklist(
    carte: Carte.Panneau,
    etape: Etape.Checklist,
    blanc: Boolean,
    coches: Set<Int>,
    onCocher: (Int, Boolean) -> Unit,
    onAvancer: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    val arret = listOfNotNull(carte.signalArret) + carte.arret
    val cases = etape.lignes.size + if (arret.isEmpty()) 0 else 1
    val tout = coches.size == cases

    if (blanc) {
        Text(
            text = stringResource(R.string.duo_entrainement_en_cours),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
    }
    Text(text = etape.enonce, style = TypoKokoro.lecture, color = palette.encre)

    etape.lignes.forEachIndexed { ligne, texte ->
        ACocher(coche = ligne in coches, onCocher = { onCocher(ligne, it) }) {
            Text(text = texte, style = TypoKokoro.corps, color = palette.encre)
        }
    }

    if (arret.isNotEmpty()) {
        val rang = etape.lignes.size
        ACocher(coche = rang in coches, onCocher = { onCocher(rang, it) }) {
            arret.forEach { texte ->
                Text(text = texte, style = TypoKokoro.corps, color = palette.encre)
            }
        }
    }

    Text(
        text = stringResource(R.string.carte_sortie_libre),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    Text(
        text = stringResource(if (blanc) R.string.duo_cadence_entrainement else R.string.carte_minuteur_ouvert),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    BoutonEpais(
        libelle = stringResource(if (tout) R.string.carte_action_commencer else R.string.carte_avant_reste),
        onClic = onAvancer,
        couleur = if (tout) palette.menthe else null,
        actif = tout,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun ACocher(coche: Boolean, onCocher: (Boolean) -> Unit, contenu: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = { contenu() },
        )
        Interrupteur(actif = coche, onChange = onCocher)
    }
}

// Un choix fermé ou un compteur, jamais une note sur dix : on cote un comportement observable, pas un ressenti.
@Composable
private fun VueQuestion(
    etape: Etape.Question,
    rang: Rang?,
    depart: Double,
    onRepondre: (ReponseItem) -> Unit,
    onArreter: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current

    if (rang != null) {
        Text(
            text = stringResource(R.string.carte_rang_question, rang.rang, rang.total),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
    }
    Text(text = etape.enonce, style = TypoKokoro.titre, color = palette.encre)
    etape.precision?.let {
        Text(text = it, style = TypoKokoro.lecture, color = palette.encreDouce)
    }

    when (val saisie = etape.saisie) {
        is Saisie.Fermee -> saisie.choix.forEach { choix ->
            BoutonEpais(
                libelle = choix.libelle,
                onClic = { onRepondre(ReponseItem.Nombre(etape.id, choix.valeur)) },
                couleur = palette.menthe,
                hauteurMinimale = 72.dp,
            )
        }

        is Saisie.Reglee -> VueCompteur(
            compteur = saisie.compteur,
            depart = depart,
            onValider = { onRepondre(ReponseItem.Nombre(etape.id, it)) },
        )
    }

    BoutonEpais(
        libelle = stringResource(R.string.carte_action_passer),
        onClic = { onRepondre(ReponseItem.Nombre(etape.id, null)) },
        hauteurMinimale = 60.dp,
    )
    BoutonEpais(
        libelle = stringResource(R.string.carte_action_arreter),
        onClic = onArreter,
        hauteurMinimale = 60.dp,
    )
}

@Composable
private fun VueCompteur(compteur: Compteur, depart: Double, onValider: (Double) -> Unit) {
    var valeur by remember(depart, compteur) { mutableStateOf(depart) }
    val ajuster = { delta: Double -> valeur = maxOf(compteur.minimum, arrondir(valeur + delta)) }

    Text(
        text = afficher(valeur, compteur.unite),
        style = TypoKokoro.compte,
        color = LocalPaletteKokoro.current.encre,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Pas(-compteur.grandPas, compteur.unite, Modifier.weight(1f), ajuster)
        Pas(-compteur.pas, compteur.unite, Modifier.weight(1f), ajuster)
        Pas(compteur.pas, compteur.unite, Modifier.weight(1f), ajuster)
        Pas(compteur.grandPas, compteur.unite, Modifier.weight(1f), ajuster)
    }
    BoutonEpais(
        libelle = stringResource(R.string.carte_action_suivant),
        onClic = { onValider(valeur) },
        couleur = LocalPaletteKokoro.current.menthe,
        hauteurMinimale = 72.dp,
    )
}

@Composable
private fun Pas(delta: Double, unite: Unite, modifier: Modifier, onClic: (Double) -> Unit) {
    BoutonEpais(
        libelle = (if (delta > 0) "+" else "−") + afficher(abs(delta), unite),
        onClic = { onClic(delta) },
        modifier = modifier,
        hauteurMinimale = 60.dp,
        style = TypoKokoro.discret,
    )
}

// Le seul endroit du dispositif où une saisie de texte est proposée — et elle peut rester vide.
@Composable
private fun VueNote(etape: Etape.Note, onRepondre: (ReponseItem) -> Unit, onArreter: () -> Unit) {
    val palette = LocalPaletteKokoro.current
    var texte by remember(etape.id) { mutableStateOf("") }

    Text(text = etape.enonce, style = TypoKokoro.titre, color = palette.encre)
    etape.precision?.let {
        Text(text = it, style = TypoKokoro.lecture, color = palette.encreDouce)
    }
    ChampTexte(
        valeur = texte,
        onValeur = { texte = it },
        modifier = Modifier.fillMaxWidth(),
        uneSeuleLigne = false,
    )
    BoutonEpais(
        libelle = stringResource(R.string.carte_action_enregistrer),
        onClic = { onRepondre(ReponseItem.Texte(etape.id, texte.trim().ifBlank { null })) },
        couleur = palette.menthe,
        hauteurMinimale = 72.dp,
    )
    BoutonEpais(
        libelle = stringResource(R.string.carte_action_arreter),
        onClic = onArreter,
        hauteurMinimale = 60.dp,
    )
}

@Composable
private fun VueMinuteur(
    etape: Etape.Minuteur,
    rang: Rang?,
    reste: Int,
    blanc: Boolean,
    aidant: Boolean,
    onAvancer: () -> Unit,
    onArreter: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current

    if (rang != null) {
        Text(
            text = stringResource(R.string.carte_rang_consigne, rang.rang, rang.total),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
    }
    etape.consigne?.let {
        Text(text = it, style = TypoKokoro.titre, color = palette.encre)
    }
    Text(
        text = libelleDuReste(reste),
        style = TypoKokoro.compte,
        color = palette.encre,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
    )

    if (blanc) {
        Text(
            text = stringResource(R.string.duo_duree_annonce),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
        BoutonEpais(
            libelle = stringResource(R.string.carte_action_suivant),
            onClic = onAvancer,
            couleur = palette.menthe,
            modifier = Modifier.padding(top = 6.dp),
        )
    } else {
        Text(
            text = stringResource(if (aidant) R.string.duo_sortie_libre else R.string.carte_sortie_libre),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
    }

    BoutonEpais(
        libelle = stringResource(if (aidant) R.string.duo_action_arreter else R.string.carte_action_arreter),
        onClic = onArreter,
        modifier = Modifier.padding(top = 6.dp),
    )
}

// Une carte rendue garde sa place et son texte : Kokoro montre l'état, il ne décide pas de la sortie du programme.
@Composable
private fun VueConfirmation(
    etape: Etape.Confirmation,
    faite: Boolean,
    onConfirmer: () -> Unit,
    onFermer: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current

    if (faite) {
        Text(
            text = stringResource(R.string.carte_deja_faite),
            style = TypoKokoro.discret,
            color = palette.encreDouce,
        )
        BoutonEpais(
            libelle = stringResource(R.string.carte_action_fermer),
            onClic = onFermer,
            modifier = Modifier.padding(top = 6.dp),
        )
        return
    }

    BoutonEpais(
        libelle = etape.libelle,
        onClic = onConfirmer,
        couleur = palette.menthe,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun Apres(enregistre: Boolean, menee: Boolean, aidant: Boolean, onFermer: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    if (aidant) {
        Text(
            text = stringResource(if (menee) R.string.duo_mene else R.string.duo_arrete),
            style = TypoKokoro.corps,
            color = palette.encre,
        )
    }
    if (enregistre) {
        Text(text = stringResource(R.string.carte_fini), style = TypoKokoro.corps, color = palette.encre)
    }
    BoutonEpais(
        libelle = stringResource(R.string.carte_action_fermer),
        onClic = onFermer,
        modifier = Modifier.padding(top = 6.dp),
    )
}

data class Rang(val rang: Int, val total: Int)

// Le rang ne se dit que s'il y a plusieurs étapes du même genre — sinon il n'apprend rien et encombre.
fun rangDe(carte: Carte.Panneau, rang: Int): Rang? {
    val etape = carte.etapes.getOrNull(rang) ?: return null
    val total = carte.etapes.count { it::class == etape::class }
    if (total < 2) return null

    return Rang(rang = carte.etapes.take(rang + 1).count { it::class == etape::class }, total = total)
}

fun depart(etape: Etape.Question, reprises: Map<String, Double>): Double = when (val saisie = etape.saisie) {
    is Saisie.Reglee -> if (etape.reprise) reprises[etape.id] ?: saisie.compteur.depart else saisie.compteur.depart
    is Saisie.Fermee -> saisie.choix.first().valeur
}

private fun arrondir(valeur: Double): Double = Math.round(valeur * 10.0) / 10.0

fun afficher(valeur: Double, unite: Unite): String = when (unite) {
    Unite.BRUTE -> valeur.toLong().toString()
    Unite.MINUTES -> "${valeur.toLong()} min"
    Unite.HEURES -> "${decimale(valeur)} h"
    Unite.KILOS -> "${decimale(valeur)} kg"
}

private fun decimale(valeur: Double): String =
    String.format(Locale.FRANCE, "%.1f", valeur).removeSuffix(",0")
