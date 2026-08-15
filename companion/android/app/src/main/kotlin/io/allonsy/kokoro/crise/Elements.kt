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

/** L'écart entre deux portes de crise — le même que dans le monde, puisque c'est le même écran. */
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

/**
 * 🔴 **Les trois portes de la crise — un seul contenu, deux entrées** (`companion/INTERFACE.md`
 * §6.2). L'écran **BAS** du monde et l'écran ouvert hors du monde affichent **ce composable-ci**, et
 * non chacun sa version : *(15/08/2026, demande de Xavier)* deux écrans qui font la même chose et ne
 * se ressemblent pas obligent à vérifier lequel on a sous les yeux, **au moment précis où on n'a rien
 * à vérifier**.
 *
 * @param envoiEnCours grise le mot-code le temps que le SMS parte. **Un bouton qu'on peut retoucher
 *   pendant l'envoi envoie deux fois**, et rien à l'écran ne dit qu'il travaille.
 */
@Composable
fun PortesDeCrise(
    contactNom: String,
    envoiEnCours: Boolean,
    onFonction: (Fonction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalPaletteKokoro.current
    PileDeBoutons(modifier = modifier, ecart = ECART_PORTES) {
        BoutonEpais(
            libelle = stringResource(R.string.crise_bouton_mot_code, contactNom),
            onClic = { onFonction(Fonction.MOT_CODE) },
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

/**
 * Le grand bouton d'un écran de crise : **le libellé, et le repère qui dit quand s'en servir**.
 *
 * ⭐ **Le repère est un fait extérieur** — *par SMS, aucun réseau de données requis*. 🔴 **Jamais une
 * sensation** : le déficit intéroceptif rend inutilisable tout déclenchement posé sur ce que Xavier
 * est censé percevoir.
 */
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
