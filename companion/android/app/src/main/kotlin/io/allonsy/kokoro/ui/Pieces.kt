package io.allonsy.kokoro.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Les pièces du thème — **le jeu de composables maison** annoncé par `companion/INTERFACE.md` §4.4.
 *
 * 🔴 **Ce style ne se pose pas sur les composants Material** : contours épais, épaisseur portée,
 * rubans crantés et creux internes ne s'obtiennent pas en réglant un `Card`. Tout est dessiné ici,
 * une fois, sur la recette unique de [matiere].
 *
 * ⭐ **Le retour au toucher est l'enfoncement du panneau** : il descend de ses 7 dp d'épaisseur en
 * 90 ms et **s'arrête net**. 🔴 **Aucun rebond, aucun dépassement, aucune onde** — un ressort qui
 * repart au-delà de sa position est exactement l'animation brusque que les hypersensibilités
 * interdisent. C'est aussi pourquoi il n'y a **aucune ondulation Material** : l'indication d'appui
 * est le volume, pas une tache qui s'étale.
 */

private const val ENFONCEMENT_MS = 90

/** Le cran taillé dans chaque bout du ruban. */
private val CRAN = 18.dp

/** L'épaisseur portée sous un ruban — plus mince que celle d'un panneau, il est plus petit. */
private val EPAISSEUR_RUBAN = 6.dp

private val PADDING_PANNEAU = PaddingValues(horizontal = 22.dp, vertical = 20.dp)

/**
 * La pièce de base : un panneau opaque à gros contour, posé sur le paysage.
 *
 * @param couleur `null` pour la matière neutre — c'est le cas de **toutes les cartes de liste**.
 *   ⭐ **Toutes les cartes sont identiques** : aucune n'est plus grande, plus vive ni marquée.
 * @param onClic `null` pour une surface qui ne répond pas au doigt — elle ne s'enfonce alors jamais.
 */
@Composable
fun PanneauExtrude(
    modifier: Modifier = Modifier,
    couleur: Teinte? = null,
    rayon: Dp = RAYON,
    epaisseur: Dp = EPAISSEUR,
    ombre: Boolean = true,
    contenuPadding: PaddingValues = PADDING_PANNEAU,
    onClic: (() -> Unit)? = null,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    val interactions = remember { MutableInteractionSource() }
    val appuye by interactions.collectIsPressedAsState()
    val descente by animateDpAsState(
        targetValue = if (appuye && onClic != null) epaisseur else 0.dp,
        animationSpec = tween(durationMillis = ENFONCEMENT_MS, easing = LinearOutSlowInEasing),
        label = "enfoncement",
    )

    Column(
        modifier = modifier
            .then(
                if (onClic == null) Modifier
                else Modifier.clickable(
                    interactionSource = interactions,
                    indication = null,
                    onClick = onClic,
                ),
            )
            .matiere(
                palette = palette,
                couleur = couleur,
                rayon = rayon,
                epaisseur = epaisseur,
                enfoncement = descente,
                ombre = ombre,
            )
            .padding(bottom = epaisseur)
            .offset { IntOffset(x = 0, y = descente.roundToPx()) }
            .padding(contenuPadding),
        content = contenu,
    )
}

/**
 * Une carte de liste : **le titre, la durée si elle est connue, rien d'autre**
 * (`companion/INTERFACE.md` §3.1).
 *
 * 🔴 **Pas de chevron, pas d'aperçu, pas de compteur, pas d'ornement.** Une carte ne dit jamais où
 * elle en est : il n'y a **ni progression, ni historique, ni palier atteint** dans cette app.
 */
@Composable
fun Carte(
    titre: String,
    modifier: Modifier = Modifier,
    duree: String? = null,
    onClic: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    PanneauExtrude(modifier = modifier.fillMaxWidth(), onClic = onClic) {
        Text(text = titre, style = TypoKokoro.corps, color = palette.encre)
        if (duree != null) {
            Text(
                text = duree,
                style = TypoKokoro.discret,
                color = palette.encreDouce,
                modifier = Modifier.padding(top = 5.dp),
            )
        }
    }
}

/**
 * Un bouton — **pleine largeur, un par ligne, libellé en toutes lettres** (§4.3).
 *
 * `couleur` **plein** quand il agit, `null` quand il ferme. Aucune icône seule : la seule du
 * dispositif est la roue dentée de l'écran central (D4), et elle est justifiée à part.
 */
@Composable
fun BoutonEpais(
    libelle: String,
    onClic: () -> Unit,
    modifier: Modifier = Modifier,
    couleur: Teinte? = null,
    hauteurMinimale: Dp = 66.dp,
    style: TextStyle = TypoKokoro.bouton,
) {
    val palette = LocalPaletteKokoro.current
    PanneauExtrude(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = hauteurMinimale + EPAISSEUR),
        couleur = couleur,
        contenuPadding = PaddingValues(horizontal = 22.dp, vertical = 18.dp),
        onClic = onClic,
    ) {
        Text(
            text = libelle,
            style = if (couleur == null) style.copy(color = palette.encre) else grave(style, opacite = 0.20f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        )
    }
}

/**
 * Le sous-titre d'un `quand` — *Aujourd'hui* · *Quand j'en ai besoin* · *Sans date*.
 *
 * ⭐ **La couleur distingue les sections, elle ne les classe jamais** — §6.5 tranché par Xavier le
 * 14/08/2026, option B. 🔴 **Il reste interdit d'aligner la palette sur une échelle d'urgence** :
 * aucune teinte ne doit pouvoir se lire comme *urgent*, *en retard* ou *important*, et il n'existe
 * ni pastille ni badge nulle part.
 */
@Composable
fun Pancarte(texte: String, couleur: Teinte, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Box(
        modifier = modifier
            .matiere(
                palette = palette,
                couleur = couleur,
                rayon = 999.dp,
                epaisseur = 5.dp,
                ombre = false,
                epaisseurReflet = 3.dp,
                epaisseurCreux = 0.dp,
            )
            .padding(bottom = 5.dp)
            .padding(horizontal = 22.dp, vertical = 10.dp),
    ) {
        Text(text = texte, style = grave(TypoKokoro.pancarte, opacite = 0.20f))
    }
}

/**
 * Le titre d'un écran, sur sa bannière crantée.
 *
 * 🔴 **Il ne défile pas** (D11) : savoir où l'on est ne doit pas dépendre d'où l'on en est dans la
 * liste.
 */
@Composable
fun Ruban(texte: String, couleur: Teinte, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Box(
        modifier = modifier
            .drawBehind {
                val ep = EPAISSEUR_RUBAN.toPx()
                val cran = CRAN.toPx()
                val corps = size.copy(height = size.height - ep)
                if (corps.height <= 0f) return@drawBehind
                val forme = cheminRuban(corps, cran)

                translate(top = ep) { drawPath(forme, palette.contour) }
                drawPath(
                    path = forme,
                    color = palette.contour,
                    style = Stroke(width = EPAISSEUR_RUBAN.toPx(), join = StrokeJoin.Round),
                )
                drawPath(
                    path = forme,
                    brush = Brush.verticalGradient(
                        colors = listOf(couleur.haut, couleur.bas),
                        startY = 0f,
                        endY = corps.height,
                    ),
                )
            }
            .padding(bottom = EPAISSEUR_RUBAN)
            .padding(horizontal = 40.dp, vertical = 13.dp),
    ) {
        Text(text = texte, style = grave(TypoKokoro.ruban))
    }
}

/**
 * La bande de titre d'un écran de bord — un panneau pleine largeur **qui sort de l'écran par le
 * haut**, portant le ruban et ses deux rivets.
 *
 * ⭐ **Elle déborde exprès** : sans ce débord, deux coins arrondis flotteraient sous la barre
 * d'état et la bande aurait l'air posée de travers. Le décor passe **sous** elle, comme partout —
 * jamais à travers (**P3**, **P5**).
 */
@Composable
fun BandeTitre(titre: String, couleur: Teinte, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .matiere(palette = palette, debordHaut = RAYON)
            .padding(bottom = EPAISSEUR)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 20.dp, end = 20.dp, top = 26.dp, bottom = 26.dp),
        contentAlignment = Alignment.Center,
    ) {
        Ruban(texte = titre, couleur = couleur)
        Rivet(Modifier.align(Alignment.CenterStart))
        Rivet(Modifier.align(Alignment.CenterEnd))
    }
}

/**
 * L'état vide d'un écran — **une plaque creuse**, la recette retournée : le dégradé remonte, le
 * creux passe en haut, l'épaisseur disparaît. Elle a l'air enfoncée dans le monde, pas posée dessus.
 *
 * ⭐ **Un écran vide dit qu'il n'y a rien, et ne s'en excuse pas.** Il ne propose rien d'autre, ne
 * relance sur rien, et ne compte pas les jours depuis la dernière fois.
 */
@Composable
fun CadreVide(texte: String, modifier: Modifier = Modifier, ornements: Boolean = true) {
    val palette = LocalPaletteKokoro.current
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp)
                .matiere(palette = palette, creuse = true)
                .padding(horizontal = 26.dp, vertical = 40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = texte,
                style = TypoKokoro.vide,
                color = palette.encreDouce,
                textAlign = TextAlign.Center,
            )
        }
        if (ornements) {
            Etincelle(Modifier.align(Alignment.TopStart).offset(x = 22.dp, y = 2.dp))
            Etincelle(Modifier.align(Alignment.BottomEnd).offset(x = (-30).dp, y = (-2).dp), taille = 12.dp)
            Coeur(Modifier.align(Alignment.TopEnd).offset(x = (-24).dp))
        }
    }
}

/** Une pile de boutons pleine largeur, un par ligne, à écart constant. */
@Composable
fun PileDeBoutons(
    modifier: Modifier = Modifier,
    ecart: Dp = 18.dp,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(ecart), content = contenu)
}
