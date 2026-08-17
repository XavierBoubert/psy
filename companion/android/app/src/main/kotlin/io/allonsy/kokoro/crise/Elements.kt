package io.allonsy.kokoro.crise

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.monde.Fonction
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PageKokoro
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.PileDeBoutons
import io.allonsy.kokoro.ui.TypoKokoro
import io.allonsy.kokoro.ui.grave

private val HAUTEUR_CRISE = 88.dp
private val ECART_PORTES = 26.dp

@Composable
internal fun PageCrise(
    titre: String,
    onFermer: () -> Unit,
    defilant: Boolean = true,
    alignement: Alignment.Vertical = Alignment.Top,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    PageKokoro(
        titre = titre,
        couleur = LocalPaletteKokoro.current.azur,
        ecart = 20.dp,
        defilant = defilant,
        alignement = alignement,
        onFermer = onFermer,
        contenu = contenu,
    )
}

@Composable
fun PortesDeCrise(
    contactNom: String,
    envoiEnCours: Boolean,
    onFonction: (Fonction) -> Unit,
    modifier: Modifier = Modifier,
    motCode: Modifier = Modifier,
) {
    val palette = LocalPaletteKokoro.current
    PileDeBoutons(modifier = modifier, ecart = ECART_PORTES) {
        BoutonEpais(
            libelle = stringResource(R.string.crise_bouton_mot_code, contactNom),
            onClic = { onFonction(Fonction.MOT_CODE) },
            modifier = motCode,
            couleur = palette.azur,
            actif = !envoiEnCours,
            hauteurMinimale = HAUTEUR_CRISE,
            style = TypoKokoro.boutonCrise,
        )
        BoutonEpais(
            libelle = stringResource(R.string.crise_bouton_tension),
            onClic = { onFonction(Fonction.TENSION) },
            couleur = palette.azur,
            hauteurMinimale = HAUTEUR_CRISE,
            style = TypoKokoro.boutonCrise,
        )
        BoutonEpais(
            libelle = stringResource(R.string.phrase_titre),
            onClic = { onFonction(Fonction.PHRASE) },
            couleur = palette.azur,
            hauteurMinimale = HAUTEUR_CRISE,
            style = TypoKokoro.boutonCrise,
        )
    }
}

// Le repère est toujours un fait extérieur, jamais une sensation : déficit intéroceptif de Xavier.
@Composable
internal fun GrandBouton(
    libelle: String,
    repere: String,
    onClick: () -> Unit,
    actif: Boolean = true,
) {
    val palette = LocalPaletteKokoro.current
    PanneauExtrude(
        modifier = Modifier.fillMaxWidth(),
        couleur = palette.azur,
        contenuPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        onClic = if (actif) onClick else null,
    ) {
        Text(
            text = libelle,
            style = when {
                actif -> grave(TypoKokoro.boutonCrise, opacite = 0.20f)
                else -> TypoKokoro.boutonCrise.copy(color = palette.encreDouce)
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = repere,
            style = when {
                actif -> grave(TypoKokoro.repere, opacite = 0.16f)
                else -> TypoKokoro.repere.copy(color = palette.encreDouce)
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        )
    }
}

@Composable
internal fun Explication(texte: String) {
    Text(
        text = texte,
        style = TypoKokoro.lecture,
        color = LocalPaletteKokoro.current.encreDouce,
    )
}

@Composable
internal fun Enonce(texte: String) {
    Text(text = texte, style = TypoKokoro.corps, color = LocalPaletteKokoro.current.encre)
}

@Composable
internal fun EnGrand(texte: String) {
    Text(text = texte, style = TypoKokoro.fort, color = LocalPaletteKokoro.current.encre)
}

@Composable
internal fun Lien(libelle: String, onClick: () -> Unit) {
    BoutonEpais(libelle = libelle, onClic = onClick, hauteurMinimale = 62.dp)
}

@Composable
internal fun Action(libelle: String, onClick: () -> Unit) {
    BoutonEpais(
        libelle = libelle,
        onClic = onClick,
        couleur = LocalPaletteKokoro.current.azur,
        hauteurMinimale = HAUTEUR_CRISE,
        style = TypoKokoro.boutonCrise,
    )
}
