package io.allonsy.kokoro.crise

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PageKokoro
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.TypoKokoro
import io.allonsy.kokoro.ui.grave

/**
 * Les pièces des écrans de crise — **la matière du monde, appliquée ici aussi**
 * *(`companion/INTERFACE.md` §4.5, écrit le 15/08/2026)*.
 *
 * 🔴 **C'est l'écran le moins décoré du dispositif.** Aucun ornement, aucune étincelle, aucun cœur,
 * une seule couleur de bouton, texte plus grand et boutons plus hauts qu'ailleurs. **En crise, la
 * mignonnerie est du bruit** — il se distingue en étant plus grand et plus vide, **pas plus vif**.
 *
 * 🔴 **Aucun rouge, ici moins qu'ailleurs.** La palette n'en contient pas.
 */

/** La hauteur d'un bouton de crise. Plus haut que partout ailleurs, et c'est le sujet. */
private val HAUTEUR_CRISE = 88.dp

@Composable
internal fun PageCrise(titre: String, contenu: @Composable ColumnScope.() -> Unit) {
    PageKokoro(
        titre = titre,
        couleur = LocalPaletteKokoro.current.azur,
        ecart = 20.dp,
        contenu = contenu,
    )
}

/**
 * Le grand bouton d'un écran de crise : **le libellé, et le repère qui dit quand s'en servir**.
 *
 * ⭐ **Le repère est un fait extérieur** — *la parole est coupée*, *aiguille, geste médical, sang*.
 * 🔴 **Jamais une sensation** : le déficit intéroceptif rend inutilisable tout déclenchement posé
 * sur ce que Xavier est censé percevoir.
 */
@Composable
internal fun GrandBouton(libelle: String, repere: String, onClick: () -> Unit) {
    val palette = LocalPaletteKokoro.current
    PanneauExtrude(
        modifier = Modifier.fillMaxWidth(),
        couleur = palette.azur,
        contenuPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        onClic = onClick,
    ) {
        Text(
            text = libelle,
            style = grave(TypoKokoro.boutonCrise, opacite = 0.20f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = repere,
            style = grave(TypoKokoro.repere, opacite = 0.16f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        )
    }
}

/** Ce qu'il y a à lire — jamais une consigne à interpréter, jamais un encouragement. */
@Composable
internal fun Explication(texte: String) {
    Text(
        text = texte,
        style = TypoKokoro.lecture,
        color = LocalPaletteKokoro.current.encreDouce,
    )
}

/** Un fait posé à l'encre pleine — ce qui est attendu maintenant, et rien de plus. */
@Composable
internal fun Enonce(texte: String) {
    Text(text = texte, style = TypoKokoro.corps, color = LocalPaletteKokoro.current.encre)
}

/** Ce qui se lit d'un coup d'œil : le mot-code, la phase, l'heure d'envoi. */
@Composable
internal fun EnGrand(texte: String) {
    Text(text = texte, style = TypoKokoro.fort, color = LocalPaletteKokoro.current.encre)
}

/**
 * Un chemin de côté — la phrase pour le soignant, les critères d'arrêt, le retour.
 *
 * ⭐ **Un panneau neutre : il mène ailleurs, il n'agit pas.** Le plein est réservé à ce qui fait
 * quelque chose.
 */
@Composable
internal fun Lien(libelle: String, onClick: () -> Unit) {
    BoutonEpais(libelle = libelle, onClic = onClick, hauteurMinimale = 62.dp)
}

/** L'action de l'écran, dans la couleur de la crise. */
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

@Composable
internal fun Fermer(onFermer: () -> Unit) {
    Lien(stringResource(R.string.crise_fermer), onFermer)
}
