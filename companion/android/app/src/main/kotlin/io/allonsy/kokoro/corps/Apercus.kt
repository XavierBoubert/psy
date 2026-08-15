package io.allonsy.kokoro.corps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview(name = "Kokoro — repos", widthDp = 220, heightDp = 220)
@Composable
private fun ApercuRepos() {
    Vignette(RigKokoro.pose(Posture.Repos), PALETTE_CLAIRE, 200.dp)
}

@Preview(name = "Kokoro — repos, palette sombre", widthDp = 220, heightDp = 220)
@Composable
private fun ApercuReposSombre() {
    Vignette(RigKokoro.pose(Posture.Repos), PALETTE_SOMBRE, 200.dp)
}

@Preview(name = "Kokoro — le jeu des expressions", widthDp = 720, heightDp = 140)
@Composable
private fun ApercuExpressions() {
    Row(
        modifier = Modifier
            .background(PALETTE_CLAIRE.fond)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Expression.entries.forEach { expression ->
            CorpsKokoro(
                rig = RigKokoro(visage = Visage.de(expression)),
                modifier = Modifier.size(116.dp),
                palette = PALETTE_CLAIRE,
            )
        }
    }
}

/** Le regard ne vient plus d'une expression : il se règle par-dessus, la même partout. */
@Preview(name = "Kokoro — les trois regards", widthDp = 400, heightDp = 140)
@Composable
private fun ApercuRegards() {
    Row(
        modifier = Modifier
            .background(PALETTE_CLAIRE.fond)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        listOf(-REGARD_DESIGNATION, 0f, REGARD_DESIGNATION).forEach { regard ->
            CorpsKokoro(
                rig = RigKokoro(visage = Visage.de(Expression.SEREIN), regard = regard),
                modifier = Modifier.size(116.dp),
                palette = PALETTE_CLAIRE,
            )
        }
    }
}

@Preview(name = "Kokoro — les postures de départ", widthDp = 720, heightDp = 200)
@Composable
private fun ApercuPostures() {
    Planche(
        listOf(
            Posture.Repos,
            Posture.Present,
            Posture.Montre(Cote.GAUCHE),
            Posture.CoteACote,
            Posture.Retrait,
        ),
    )
}

/** Les quatre postures immobiles. Le geste de `notes` ne se voit qu'à l'atelier — il est animé. */
@Preview(name = "Kokoro — les postures immobiles", widthDp = 720, heightDp = 200)
@Composable
private fun ApercuPosturesImmobiles() {
    Planche(listOf(Posture.Pensif, Posture.Lecture, Posture.Notes, Posture.Attente, Posture.Sommeil))
}

@Composable
private fun Planche(postures: List<Posture>) {
    Row(
        modifier = Modifier
            .background(PALETTE_CLAIRE.fond)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        postures.forEach { posture ->
            CorpsKokoro(
                rig = RigKokoro.pose(posture),
                modifier = Modifier.size(140.dp),
                palette = PALETTE_CLAIRE,
            )
        }
    }
}

@Preview(name = "Kokoro — respiration au sommet", widthDp = 220, heightDp = 220)
@Composable
private fun ApercuInspiration() {
    Vignette(RigKokoro.pose(Posture.Repos).copy(respiration = 1f), PALETTE_CLAIRE, 200.dp)
}

@Composable
private fun Vignette(rig: RigKokoro, palette: PaletteCorps, taille: Dp) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(taille)
            .background(palette.fond),
    ) {
        CorpsKokoro(rig = rig, modifier = Modifier.size(taille), palette = palette)
    }
}
