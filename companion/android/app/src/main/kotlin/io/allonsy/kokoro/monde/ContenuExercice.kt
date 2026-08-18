package io.allonsy.kokoro.monde

import android.os.SystemClock
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.PanneauDialogue
import io.allonsy.kokoro.ui.TypoKokoro
import kotlinx.coroutines.delay

private const val PERIODE_TICK_MILLIS = 200L

private enum class VueExercice { AVANT, PENDANT, APRES }

// Le minuteur vit dans le panneau : quitter Kokoro l'arrête sans rien écrire, et c'est dit avant de commencer.
@Composable
fun PanneauExercice(etape: Etape.Exercice, onIssue: (Issue) -> Unit, onFermer: () -> Unit) {
    var vue by remember(etape.reperes.id) { mutableStateOf(VueExercice.AVANT) }
    var debut by remember(etape.reperes.id) { mutableLongStateOf(0L) }
    var reste by remember(etape.reperes.id) { mutableIntStateOf(etape.minuteurSecondes) }

    LaunchedEffect(vue, debut) {
        if (vue != VueExercice.PENDANT) return@LaunchedEffect
        while (reste > 0) {
            reste = resteSecondes(etape.minuteurSecondes, SystemClock.elapsedRealtime() - debut)
            if (reste > 0) delay(PERIODE_TICK_MILLIS)
        }
        onIssue(Issue.TERMINE)
        vue = VueExercice.APRES
    }

    PanneauDialogue(titre = etape.reperes.titre, onFermer = onFermer) {
        when (vue) {
            VueExercice.AVANT -> Avant(
                etape = etape,
                onCommencer = {
                    debut = SystemClock.elapsedRealtime()
                    reste = etape.minuteurSecondes
                    vue = VueExercice.PENDANT
                },
            )

            VueExercice.PENDANT -> Pendant(
                reste = reste,
                onArreter = {
                    onIssue(Issue.ARRETE)
                    vue = VueExercice.APRES
                },
            )

            VueExercice.APRES -> Apres(onFermer = onFermer)
        }
    }
}

@Composable
private fun Avant(etape: Etape.Exercice, onCommencer: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(text = etape.consigne, style = TypoKokoro.lecture, color = palette.encre)
    Text(
        text = stringResource(R.string.exercice_sortie_libre),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    Text(
        text = stringResource(R.string.exercice_minuteur_ouvert),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    BoutonEpais(
        libelle = stringResource(R.string.exercice_action_commencer),
        onClic = onCommencer,
        couleur = palette.menthe,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun Pendant(reste: Int, onArreter: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(
        text = libelleDuReste(reste),
        style = TypoKokoro.compte,
        color = palette.encre,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
    )
    Text(
        text = stringResource(R.string.exercice_sortie_libre),
        style = TypoKokoro.discret,
        color = palette.encreDouce,
    )
    BoutonEpais(
        libelle = stringResource(R.string.exercice_action_arreter),
        onClic = onArreter,
        modifier = Modifier.padding(top = 6.dp),
    )
}

@Composable
private fun Apres(onFermer: () -> Unit) {
    val palette = LocalPaletteKokoro.current

    Text(text = stringResource(R.string.exercice_fini), style = TypoKokoro.corps, color = palette.encre)
    BoutonEpais(
        libelle = stringResource(R.string.exercice_action_fermer),
        onClic = onFermer,
        modifier = Modifier.padding(top = 6.dp),
    )
}
