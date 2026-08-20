package io.allonsy.kokoro.monde

import androidx.compose.runtime.Composable
import io.allonsy.kokoro.R
import io.allonsy.kokoro.programme.Carte
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.Quand
import io.allonsy.kokoro.programme.Rubrique
import io.allonsy.kokoro.programme.bilans
import io.allonsy.kokoro.programme.cartesDe
import io.allonsy.kokoro.programme.documents
import io.allonsy.kokoro.programme.faite
import io.allonsy.kokoro.programme.quand
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.Teinte

fun videsDe(programme: Programme): Set<Ecran> = buildSet {
    if (programme.cartesDe(Rubrique.THERAPIE).isEmpty()) add(Ecran.THERAPIE)
    if (programme.bilans().isEmpty()) add(Ecran.BILAN)
    if (programme.documents().isEmpty()) add(Ecran.DOCUMENTATION)
}

fun toutFaitAujourdhui(programme: Programme, faites: Faites): Boolean =
    programme.cartesDe(Rubrique.THERAPIE)
        .filter { it.quand == Quand.AUJOURDHUI }
        .all { faites.faite(it) }

// Tout ce qu'un bouton du monde peut ouvrir dans le panneau de dialogue — une seule forme, plusieurs contenus.
// 🔴 Tension et phrase restent bâties dans l'app : l'écran de crise doit tenir sans programme et sans Drive.
sealed interface Contexte {
    data class Panneau(val carte: Carte.Panneau) : Contexte

    data object Reglages : Contexte

    data object Tension : Contexte

    data object Phrase : Contexte
}

data class Section(
    val quand: Quand,
    val libelle: Int,
    val couleur: Teinte,
    val perchoir: Perchoir?,
)

@Composable
fun sectionsDuProgramme(): List<Section> {
    val palette = LocalPaletteKokoro.current
    return listOf(
        Section(Quand.AUJOURDHUI, R.string.monde_quand_aujourdhui, palette.peche, Perchoir.AUJOURDHUI),
        Section(Quand.AU_BESOIN, R.string.monde_quand_au_besoin, palette.beurre, null),
        Section(Quand.SANS_DATE, R.string.monde_quand_sans_date, palette.azur, Perchoir.SANS_DATE),
    )
}
