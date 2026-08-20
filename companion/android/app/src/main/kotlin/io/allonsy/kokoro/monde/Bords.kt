package io.allonsy.kokoro.monde

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.crise.PorteDeCrise
import io.allonsy.kokoro.crise.PortesDeCrise
import io.allonsy.kokoro.programme.Carte
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.Rubrique
import io.allonsy.kokoro.programme.bilans
import io.allonsy.kokoro.programme.cartesDe
import io.allonsy.kokoro.programme.documents
import io.allonsy.kokoro.programme.faite
import io.allonsy.kokoro.programme.moisDe
import io.allonsy.kokoro.programme.quand
import io.allonsy.kokoro.ui.BandeTitre
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.CadreVide
import io.allonsy.kokoro.ui.Vignette
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.Pancarte
import io.allonsy.kokoro.ui.PictoDehors
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.Teinte
import io.allonsy.kokoro.ui.TypoKokoro

private val ECART_CARTES = 20.dp

private val PERCHOIRS_THERAPIE = listOf(Perchoir.AUJOURDHUI, Perchoir.SANS_DATE)

@Composable
fun EcranDeBord(
    titre: String,
    couleur: Teinte,
    defilant: Boolean,
    modifier: Modifier = Modifier,
    onReglages: (() -> Unit)? = null,
    // Non-null seulement hors du monde : là, l'écran est une Activity, et rien ne l'emporte en glissant.
    onFermer: (() -> Unit)? = null,
    // Non-null seulement pour Thérapie : pose un plafond fixe (hors scroll) où Kokoro se cale en défilant la liste.
    perchoirs: Perchoirs? = null,
    // Un panneau recouvre l'écran : le scrim bloque l'appui, pas le glissement — le défilement se coupe donc ici.
    fige: Boolean = false,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        BandeTitre(
            titre = titre,
            couleur = couleur,
            onFermer = onFermer,
            onReglages = onReglages,
            modifier = if (perchoirs == null) Modifier else Modifier.perchoir(perchoirs, Perchoir.PLAFOND),
        )

        val bas = Modifier
            .weight(1f)
            .windowInsetsPadding(WindowInsets.navigationBars)

        if (defilant) {
            Column(
                modifier = bas
                    .verticalScroll(rememberScrollState(), enabled = !fige)
                    .padding(horizontal = 20.dp)
                    .padding(top = 6.dp, bottom = 52.dp),
                content = contenu,
            )
        } else {
            Column(
                modifier = bas.padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 26.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterVertically),
                content = contenu,
            )
        }
    }
}

@Composable
fun ContenuTherapie(
    perchoirs: Perchoirs,
    programme: Programme,
    faites: Faites,
    accesPerdu: Boolean,
    onReglages: () -> Unit,
    onOuvrir: (Carte) -> Unit,
    fige: Boolean = false,
) {
    val aFaire = programme.cartesDe(Rubrique.THERAPIE)

    EcranDeBord(
        titre = stringResource(R.string.monde_therapie_titre),
        couleur = LocalPaletteKokoro.current.menthe,
        defilant = aFaire.isNotEmpty(),
        onReglages = onReglages,
        perchoirs = perchoirs,
        fige = fige,
    ) {
        if (accesPerdu) {
            AvisAcces(onReglages = onReglages, modifier = Modifier.padding(top = 18.dp))
        }

        if (aFaire.isEmpty()) {
            BandeDeTete(perchoirs = perchoirs, poses = PERCHOIRS_THERAPIE)
            CadreVide(texte = stringResource(R.string.monde_therapie_vide))
            return@EcranDeBord
        }

        val rendues = sectionsDuProgramme().filter { section -> aFaire.any { it.quand == section.quand } }
        val orphelins = PERCHOIRS_THERAPIE - rendues.mapNotNull { it.perchoir }.toSet()

        rendues.forEachIndexed { rang, section ->
            BandeDeSection(
                perchoirs = perchoirs,
                poses = listOfNotNull(section.perchoir) + if (rang == 0) orphelins else emptyList(),
            ) {
                Pancarte(
                    texte = stringResource(section.libelle),
                    couleur = section.couleur,
                    modifier = Modifier.padding(start = 2.dp),
                )
            }
            aFaire.filter { it.quand == section.quand }.forEach { carte ->
                CarteDuProgramme(
                    carte = carte,
                    faite = faites.faite(carte),
                    onClic = { onOuvrir(carte) },
                )
            }
        }
    }
}

@Composable
private fun CarteDuProgramme(carte: Carte, faite: Boolean, onClic: () -> Unit) {
    Vignette(
        titre = carte.reperes.titre,
        duree = carte.reperes.dureeMinutes?.let { stringResource(R.string.monde_duree_minutes, it) },
        picto = if (carte is Carte.Pdf) ({ PictoDehors() }) else null,
        faite = faite,
        onClic = onClic,
        modifier = Modifier.padding(bottom = ECART_CARTES),
    )
}

@Composable
private fun BandeDeSection(
    perchoirs: Perchoirs,
    poses: List<Perchoir>,
    contenu: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 16.dp)
            .poser(perchoirs, poses),
        contentAlignment = Alignment.CenterStart,
        content = { contenu() },
    )
}

@Composable
private fun BandeDeTete(perchoirs: Perchoirs, poses: List<Perchoir>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(CADRE_HABITANT.height)
            .poser(perchoirs, poses),
    )
}

// Sans perchoir posé, Kokoro n'a nulle part où se tenir et l'écran se dessine sans lui : une bande en porte donc
// plusieurs quand la section correspondante est absente du programme.
private fun Modifier.poser(perchoirs: Perchoirs, poses: List<Perchoir>): Modifier =
    poses.fold(this) { modifier, pose -> modifier.perchoir(perchoirs, pose) }

@Composable
fun ContenuDocumentation(
    perchoirs: Perchoirs,
    programme: Programme,
    onDocument: (Carte.Pdf) -> Unit,
    fige: Boolean = false,
) {
    val palette = LocalPaletteKokoro.current
    val fiches = programme.documents()

    EcranDeBord(
        titre = stringResource(R.string.monde_documentation_titre),
        couleur = palette.lavande,
        defilant = fiches.isNotEmpty(),
        fige = fige,
    ) {
        if (fiches.isEmpty()) {
            BandeDeTete(perchoirs = perchoirs, poses = listOf(Perchoir.DOCUMENTATION))
            CadreVide(texte = stringResource(R.string.monde_documentation_vide))
            return@EcranDeBord
        }

        val rendues = RUBRIQUES_LUES.filter { rubrique -> fiches.any { it.reperes.rubrique == rubrique } }

        rendues.forEachIndexed { rang, rubrique ->
            BandeDeSection(
                perchoirs = perchoirs,
                poses = if (rang == 0) listOf(Perchoir.DOCUMENTATION) else emptyList(),
            ) {
                Pancarte(
                    texte = stringResource(libelleDe(rubrique)),
                    couleur = palette.lavande,
                    modifier = Modifier.padding(start = 2.dp),
                )
            }
            fiches.filter { it.reperes.rubrique == rubrique }.forEach { fiche ->
                CarteDuProgramme(carte = fiche, faite = false, onClic = { onDocument(fiche) })
            }
        }
    }
}

// Documentation : groupé par rubrique, la bibliothèque entière y vit quelle que soit la sienne.
private val RUBRIQUES_LUES = listOf(Rubrique.CRISE, Rubrique.THERAPIE, Rubrique.DOCUMENTATION)

private fun libelleDe(rubrique: Rubrique): Int = when (rubrique) {
    Rubrique.CRISE -> R.string.monde_rubrique_crise
    Rubrique.THERAPIE -> R.string.monde_rubrique_therapie
    Rubrique.DOCUMENTATION -> R.string.monde_rubrique_dispositif
    Rubrique.BILAN -> R.string.monde_bilan_titre
}

// Groupé par mois du document, du plus récent au plus ancien : c'est la date du bilan, jamais une progression.
@Composable
fun ContenuBilan(
    perchoirs: Perchoirs,
    programme: Programme,
    onBilan: (Carte.Pdf) -> Unit,
    fige: Boolean = false,
) {
    val palette = LocalPaletteKokoro.current
    val bilans = programme.bilans()

    EcranDeBord(
        titre = stringResource(R.string.monde_bilan_titre),
        couleur = palette.beurre,
        defilant = bilans.isNotEmpty(),
        fige = fige,
    ) {
        if (bilans.isEmpty()) {
            BandeDeTete(perchoirs = perchoirs, poses = listOf(Perchoir.BILAN))
            CadreVide(texte = stringResource(R.string.monde_bilan_vide))
            return@EcranDeBord
        }

        bilans.groupBy { moisDe(it.date.orEmpty()) }.toList().forEachIndexed { rang, (mois, duMois) ->
            BandeDeSection(
                perchoirs = perchoirs,
                poses = if (rang == 0) listOf(Perchoir.BILAN) else emptyList(),
            ) {
                Pancarte(
                    texte = libelleDuMois(mois),
                    couleur = palette.beurre,
                    modifier = Modifier.padding(start = 2.dp),
                )
            }
            duMois.forEach { bilan ->
                CarteDuProgramme(carte = bilan, faite = false, onClic = { onBilan(bilan) })
            }
        }
    }
}

@Composable
private fun libelleDuMois(mois: String): String {
    val noms = stringArrayResource(R.array.monde_mois)
    val rang = mois.takeLast(2).toIntOrNull() ?: return mois

    return noms.getOrNull(rang - 1)?.let { "$it ${mois.take(4)}" } ?: mois
}

@Composable
fun ContenuCriseDuMonde(
    perchoirs: Perchoirs,
    contactNom: String,
    envoiEnCours: Boolean,
    onFonction: (PorteDeCrise) -> Unit,
    onFermer: (() -> Unit)? = null,
) {
    EcranDeBord(
        titre = stringResource(R.string.monde_crise_titre),
        couleur = LocalPaletteKokoro.current.azur,
        defilant = false,
        onFermer = onFermer,
    ) {
        PortesDeCrise(
            contactNom = contactNom,
            envoiEnCours = envoiEnCours,
            onFonction = onFonction,
            motCode = Modifier.perchoir(perchoirs, Perchoir.CRISE),
        )
    }
}

@Composable
fun AvisAcces(onReglages: () -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        PanneauExtrude(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.monde_acces_perdu),
                style = TypoKokoro.corps,
                color = palette.encre,
            )
            Text(
                text = stringResource(R.string.monde_acces_effet),
                style = TypoKokoro.lecture,
                color = palette.encreDouce,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        BoutonEpais(
            libelle = stringResource(R.string.monde_acces_action),
            onClic = onReglages,
            hauteurMinimale = 56.dp,
        )
    }
}
