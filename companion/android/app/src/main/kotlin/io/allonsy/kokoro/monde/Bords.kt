package io.allonsy.kokoro.monde

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.crise.PortesDeCrise
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.CadreVide
import io.allonsy.kokoro.ui.BandeTitre
import io.allonsy.kokoro.ui.Carte
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.Pancarte
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.Teinte
import io.allonsy.kokoro.ui.TypoKokoro
import io.allonsy.kokoro.ui.matiere
import kotlin.math.cos
import kotlin.math.sin

/**
 * Les quatre écrans de bord (`companion/INTERFACE.md` §3) — **une rubrique par écran**.
 *
 * ```
 *                  HAUT
 *               ┌─────────┐
 *               │  Bilan  │
 *               └─────────┘
 *  ┌──────────┐ ┌─────────┐ ┌───────────────┐
 *  │ Thérapie │ │ Kokoro  │ │ Documentation │
 *  └──────────┘ └─────────┘ └───────────────┘
 *               ┌─────────┐
 *               │  Crise  │
 *               └─────────┘
 *                   BAS
 * ```
 *
 * ⭐ **Un contenu ne change jamais de place** : la rubrique est écrite dans le programme et ne
 * bouge pas, alors que `quand` bouge tous les jours. **L'interface n'arbitre rien.**
 *
 * 🔴 **P1 commande le rangement.** Le glissement vertical est pris par la traversée du monde : un
 * écran du haut ou du bas **ne peut pas contenir de liste qui défile**. Les deux contenus longs —
 * la thérapie et la documentation — vont donc à gauche et à droite, où la butée est franche et où
 * le geste vertical ne sert à rien d'autre.
 */

/** L'écart qui laisse passer le monde entre les cartes. Ce n'est pas de la respiration graphique. */
private val ECART_CARTES = 20.dp

@Composable
fun EcranDeBord(
    titre: String,
    couleur: Teinte,
    defilant: Boolean,
    modifier: Modifier = Modifier,
    alignement: Alignment.Vertical = Alignment.CenterVertically,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        BandeTitre(titre = titre, couleur = couleur)

        val bas = Modifier
            .weight(1f)
            .windowInsetsPadding(WindowInsets.navigationBars)

        if (defilant) {
            Column(
                modifier = bas
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 6.dp, bottom = 52.dp),
                content = contenu,
            )
        } else {
            Column(
                modifier = bas.padding(horizontal = 20.dp).padding(top = 20.dp, bottom = 26.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp, alignement),
                content = contenu,
            )
        }
    }
}

/**
 * **GAUCHE — Thérapie.** Les prochaines actions de la thérapie en cours, groupées par `quand`.
 *
 * ⭐ **L'écran est long avant d'être riche** : sept démarches administratives sans date. C'est ce
 * qui a commandé **P1**, et c'est l'état réel du chantier n° 1.
 */
@Composable
fun ContenuTherapie(onOuvrir: (Etape) -> Unit) {
    EcranDeBord(
        titre = stringResource(R.string.monde_therapie_titre),
        couleur = LocalPaletteKokoro.current.menthe,
        defilant = true,
    ) {
        sectionsTherapie().forEach { section ->
            Pancarte(
                texte = section.quand,
                couleur = section.couleur,
                modifier = Modifier.padding(top = 20.dp, bottom = 16.dp, start = 2.dp),
            )
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

/**
 * **DROITE — Documentation.** La bibliothèque, une fiche par ligne.
 *
 * Elle est vide : `companion/inputs/bibliotheque/` ne contient aujourd'hui que son README.
 * 🔴 **Les protocoles de `psy/docs/protocoles/` ne s'y copient pas** — ils se réécrivent pour
 * Xavier (contrôle **C9**), et ça se décide en séance.
 */
@Composable
fun ContenuDocumentation() {
    EcranDeBord(
        titre = stringResource(R.string.monde_documentation_titre),
        couleur = LocalPaletteKokoro.current.lavande,
        defilant = true,
    ) {
        CadreVide(texte = stringResource(R.string.monde_documentation_vide))
    }
}

/**
 * **HAUT — Bilan.** Les questionnaires à passer et les comptes rendus écrits en séance.
 *
 * 🔴 **Aucun chiffre, aucune courbe, aucun score** : la cotation n'est pas dans Kokoro
 * (`companion/PROGRAMME.md` §3), et un score mal lu est pire qu'un score absent.
 *
 * ⭐ **Le contenu part du haut, comme à la documentation.** Cet écran ne défile pas (**P1**), mais un
 * état vide posé au milieu de la page se lit comme un message adressé ; posé en haut, il se lit comme
 * une liste qui n'a rien dedans — ce qu'il est. **Les deux écrans qui n'ont rien doivent se
 * ressembler.**
 */
@Composable
fun ContenuBilan() {
    EcranDeBord(
        titre = stringResource(R.string.monde_bilan_titre),
        couleur = LocalPaletteKokoro.current.beurre,
        defilant = false,
        alignement = Alignment.Top,
    ) {
        CadreVide(texte = stringResource(R.string.monde_bilan_vide))
    }
}

/**
 * **BAS — Crise.** Trois grands boutons, **jamais de défilement** — c'est une exigence en soi, pas
 * une conséquence de P1.
 *
 * 🔴 **L'écart assumé : c'est l'écran le moins décoré du monde** (§4.5). Aucun ornement, aucune
 * étincelle, aucun cœur, une seule couleur, texte à 21 sp et boutons à 88 dp. **En crise, la
 * mignonnerie est du bruit** — il se distingue en étant plus grand et plus vide, **pas plus vif**.
 *
 * ⭐ **Cet écran est la première porte, pas la seule.** La notification y mène aussi, et depuis le
 * 15/08/2026 **elle mène ici même** : les boutons sont ceux de [PortesDeCrise], partagés à la
 * lettre avec l'écran de crise ouvert hors du monde (§6.2).
 */
@Composable
fun ContenuCriseDuMonde(
    contactNom: String,
    envoiEnCours: Boolean,
    onFonction: (Fonction) -> Unit,
) {
    EcranDeBord(
        titre = stringResource(R.string.monde_crise_titre),
        couleur = LocalPaletteKokoro.current.azur,
        defilant = false,
    ) {
        PortesDeCrise(
            contactNom = contactNom,
            envoiEnCours = envoiEnCours,
            onFonction = onFonction,
        )
    }
}

/**
 * **CENTRE — l'avis de porte fermée.** Il ne paraît que quand la notification d'accès n'a pas pu
 * être affichée : l'autorisation a été refusée, ou **Android l'a révoquée tout seul** — il le fait
 * pour les applications peu utilisées, et Kokoro en est une par construction.
 *
 * 🔴 **Une phrase, pas une pastille** *(tranché par Xavier le 15/08/2026)*. Un point coloré sur la
 * roue dentée aurait dit *va voir* sans dire quoi : **c'est un sous-entendu, et le dispositif n'en
 * fait aucun.** D4 tient donc — *jamais de pastille dessus* — et le rouge reste hors de la palette.
 *
 * ⭐ **Ce n'est pas une relance** : il ne compte pas les jours, il ne revient pas, il n'insiste pas.
 * **Il constate un défaut, et il disparaît de lui-même quand le défaut est réparé.** C'est la seule
 * chose que l'écran central ait le droit de porter (§6.1) : *une carte, une phrase, un fait*.
 */
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

/**
 * La roue dentée de l'écran central — **l'écran de contrôle** (D4).
 *
 * ⚠️ **Exception assumée à « aucune icône seule »** : c'est le seul pictogramme universel du lot, et
 * le centre n'a pas de place pour un mot. 🔴 **Jamais de pastille dessus** — rien n'y compte, rien
 * n'y attend.
 */
@Composable
fun RoueDentee(onClic: () -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    val interactions = remember { MutableInteractionSource() }
    val nom = stringResource(R.string.monde_reglages)
    Box(
        modifier = modifier
            .semantics { contentDescription = nom }
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 14.dp, end = 22.dp)
            .clickable(interactionSource = interactions, indication = null, onClick = onClic)
            .matiere(
                palette = palette,
                couleur = palette.beurre,
                rayon = 999.dp,
                epaisseur = 5.dp,
                epaisseurReflet = 3.dp,
                epaisseurCreux = 0.dp,
            )
            .padding(bottom = 5.dp)
            .size(54.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(24.dp)) { rouage(palette.contour, 2.4.dp.toPx()) }
    }
}

/** Un moyeu et huit rayons — le dessin de la maquette, tracé au trait. */
private fun DrawScope.rouage(couleur: Color, trait: Float) {
    val centre = Offset(size.width / 2f, size.height / 2f)
    val moyen = size.minDimension / 2f
    drawCircle(couleur, radius = moyen * 0.27f, center = centre, style = Stroke(width = trait))
    repeat(8) { rang ->
        val angle = rang * Math.PI / 4.0
        val direction = Offset(cos(angle).toFloat(), sin(angle).toFloat())
        drawLine(
            color = couleur,
            start = centre + direction * (moyen * 0.55f),
            end = centre + direction * moyen,
            strokeWidth = trait,
            cap = StrokeCap.Round,
        )
    }
}
