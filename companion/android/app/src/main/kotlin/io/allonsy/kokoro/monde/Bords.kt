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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.crise.PortesDeCrise
import io.allonsy.kokoro.ui.BandeTitre
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.CadreVide
import io.allonsy.kokoro.ui.Carte
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.Pancarte
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.Teinte
import io.allonsy.kokoro.ui.TypoKokoro

private val ECART_CARTES = 20.dp

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
    accesPerdu: Boolean,
    onReglages: () -> Unit,
    onOuvrir: (Etape) -> Unit,
    fige: Boolean = false,
) {
    EcranDeBord(
        titre = stringResource(R.string.monde_therapie_titre),
        couleur = LocalPaletteKokoro.current.menthe,
        defilant = true,
        onReglages = onReglages,
        perchoirs = perchoirs,
        fige = fige,
    ) {
        if (accesPerdu) {
            AvisAcces(onReglages = onReglages, modifier = Modifier.padding(top = 18.dp))
        }
        sectionsTherapie().forEach { section ->
            BandeDeSection(perchoirs = perchoirs, perchoir = section.perchoir) {
                Pancarte(
                    texte = section.quand,
                    couleur = section.couleur,
                    modifier = Modifier.padding(start = 2.dp),
                )
            }
            section.etapes.forEach { etape ->
                Carte(
                    titre = etape.titre,
                    duree = etape.duree,
                    onClic = { onOuvrir(etape) },
                    modifier = Modifier.padding(bottom = ECART_CARTES),
                )
            }
        }
    }
}

@Composable
private fun BandeDeSection(
    perchoirs: Perchoirs,
    perchoir: Perchoir,
    contenu: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 16.dp)
            .perchoir(perchoirs, perchoir),
        contentAlignment = Alignment.CenterStart,
        content = { contenu() },
    )
}

@Composable
private fun BandeDeTete(perchoirs: Perchoirs, perchoir: Perchoir) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(CADRE_HABITANT.height)
            .perchoir(perchoirs, perchoir),
    )
}

@Composable
fun ContenuDocumentation(perchoirs: Perchoirs) {
    EcranDeBord(
        titre = stringResource(R.string.monde_documentation_titre),
        couleur = LocalPaletteKokoro.current.lavande,
        defilant = false,
    ) {
        BandeDeTete(perchoirs = perchoirs, perchoir = Perchoir.DOCUMENTATION)
        CadreVide(texte = stringResource(R.string.monde_documentation_vide))
    }
}

@Composable
fun ContenuBilan(perchoirs: Perchoirs) {
    EcranDeBord(
        titre = stringResource(R.string.monde_bilan_titre),
        couleur = LocalPaletteKokoro.current.beurre,
        defilant = false,
    ) {
        BandeDeTete(perchoirs = perchoirs, perchoir = Perchoir.BILAN)
        CadreVide(texte = stringResource(R.string.monde_bilan_vide))
    }
}

@Composable
fun ContenuCriseDuMonde(
    perchoirs: Perchoirs,
    contactNom: String,
    envoiEnCours: Boolean,
    onFonction: (Fonction) -> Unit,
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
