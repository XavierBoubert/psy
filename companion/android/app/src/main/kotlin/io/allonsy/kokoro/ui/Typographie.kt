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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.allonsy.kokoro.R

val VARELA = FontFamily(Font(R.font.varela_round))

// Varela Round n'a qu'une seule graisse : tout gras ici est synthétisé par Android, jamais dans un corps de texte.
private fun corps(taille: Int, interligne: Float, graisse: FontWeight = FontWeight.Normal) = TextStyle(
    fontFamily = VARELA,
    fontSize = taille.sp,
    lineHeight = (taille * interligne).sp,
    fontWeight = graisse,
    lineHeightStyle = LineHeightStyle(
        // Alignment.Center répartit l'interligne des deux côtés : par défaut Compose ne l'ajoute qu'en dessous, ce qui désaxe un libellé seul.
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
)

object TypoKokoro {
    val corps = corps(18, 1.5f)
    val lecture = corps(18, 1.7f)
    val discret = corps(15, 1.5f)
    val titre = corps(23, 1.4f, FontWeight.Bold)
    val ruban = corps(25, 1.25f, FontWeight.Bold)
    val pancarte = corps(16, 1.4f, FontWeight.Bold)
    val bouton = corps(18, 1.4f, FontWeight.SemiBold)
    val boutonCrise = corps(21, 1.35f, FontWeight.SemiBold)

    // Ce que le repère déclenche, jamais un ressenti (R6).
    val repere = corps(16, 1.35f)

    val fort = corps(30, 1.2f, FontWeight.Bold)

    // Un compte (minuteur), jamais un score ou une série.
    val compte = corps(52, 1.1f, FontWeight.Bold)

    val vide = corps(18, 1.6f, FontWeight.SemiBold)
}

@Composable
fun grave(style: TextStyle, opacite: Float = 0.22f): TextStyle {
    val chute = with(LocalDensity.current) { 2.dp.toPx() }
    return style.copy(
        color = Color.White,
        shadow = Shadow(color = Color.Black.copy(alpha = opacite), offset = Offset(0f, chute), blurRadius = 0f),
    )
}
