package io.allonsy.kokoro.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.allonsy.kokoro.R

/**
 * La typographie — **une famille arrondie, embarquée dans l'APK** (`companion/INTERFACE.md` §4.3).
 *
 * ⚠️ **Android n'en garantit aucune** : sans fichier embarqué, le rendu retomberait sur la Roboto du
 * système et il manquerait la moitié de l'effet. **Varela Round** est retenue le 14/08/2026 —
 * licence SIL OFL 1.1, texte complet dans `app/licences/varela-round-OFL.txt`.
 *
 * ⭐ **Une police embarquée n'enfreint pas §1.4** : la règle interdit qu'une ressource soit *allée
 * chercher ailleurs pendant que Xavier s'en sert*. Celle-ci est figée à la compilation, donc hors
 * ligne par construction.
 *
 * ⚠️ **Varela Round n'a qu'une graisse.** Le gras des rubans, des pancartes et des boutons pleins
 * est donc **synthétisé** par Android. C'est acceptable — le gras fait partie de la matière, pas du
 * texte courant — et **jamais dans un corps de texte** (§4.3).
 *
 * Les corps sont grands et les interlignes larges : **lisible en shutdown**, c'est-à-dire lisible
 * quand on ne peut plus faire d'effort.
 */
val VARELA = FontFamily(Font(R.font.varela_round))

private fun corps(taille: Int, interligne: Float, graisse: FontWeight = FontWeight.Normal) = TextStyle(
    fontFamily = VARELA,
    fontSize = taille.sp,
    lineHeight = (taille * interligne).sp,
    fontWeight = graisse,
)

object TypoKokoro {
    /** Le texte courant, et le titre d'une carte. 18 sp. */
    val corps = corps(18, 1.5f)

    /** Le corps d'une étape ouverte — le plus long à lire, donc le plus aéré. */
    val lecture = corps(18, 1.7f)

    /** Sous le titre d'une carte : la durée, quand elle est connue. Rien d'autre n'y va. */
    val discret = corps(15, 1.5f)

    /** Le titre d'une étape ouverte. 23 sp. */
    val titre = corps(23, 1.4f, FontWeight.Bold)

    /** Le mot posé sur le ruban d'un écran. 25 sp. */
    val ruban = corps(25, 1.25f, FontWeight.Bold)

    /** Le sous-titre d'un `quand`, sur sa pancarte. */
    val pancarte = corps(16, 1.4f, FontWeight.Bold)

    /** Le libellé d'un bouton — toujours en toutes lettres, jamais une icône. */
    val bouton = corps(18, 1.4f, FontWeight.SemiBold)

    /**
     * 🔴 **L'écran de crise lit plus gros** (§4.5). En crise, la mignonnerie est du bruit : il se
     * distingue en étant plus grand et plus vide, **pas plus vif**.
     */
    val boutonCrise = corps(21, 1.35f, FontWeight.SemiBold)

    /** L'état vide d'un écran. */
    val vide = corps(18, 1.6f, FontWeight.SemiBold)
}

/**
 * Le texte blanc **gravé** des rubans, pancartes et boutons pleins : une ombre nette d'un point,
 * sans flou. Elle creuse la lettre dans la matière au lieu de la poser dessus.
 */
@Composable
fun grave(style: TextStyle, opacite: Float = 0.22f): TextStyle {
    val chute = with(LocalDensity.current) { 2.dp.toPx() }
    return style.copy(
        color = Color.White,
        shadow = Shadow(color = Color.Black.copy(alpha = opacite), offset = Offset(0f, chute), blurRadius = 0f),
    )
}
