package io.allonsy.kokoro.monde

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.corps.CorpsKokoro
import io.allonsy.kokoro.corps.HAUTEUR_VUE
import io.allonsy.kokoro.corps.LARGEUR_VUE
import io.allonsy.kokoro.corps.PALETTE_CLAIRE
import io.allonsy.kokoro.corps.Posture
import io.allonsy.kokoro.corps.rigAnime
import io.allonsy.kokoro.crise.PortesDeCrise
import io.allonsy.kokoro.ui.BandeTitre
import io.allonsy.kokoro.ui.BoutonEpais
import io.allonsy.kokoro.ui.CadreVide
import io.allonsy.kokoro.ui.Carte
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.Pancarte
import io.allonsy.kokoro.ui.PanneauExtrude
import io.allonsy.kokoro.ui.Teinte
import io.allonsy.kokoro.ui.TypoKokoro

/**
 * Les quatre écrans du monde (`companion/INTERFACE.md` §3) — **une rubrique par écran**.
 *
 * ```
 *  ← … ┌──────────┐ ┌───────────────┐ ┌────────┐ ┌───────┐ ┌──────────┐ … →
 *      │ Thérapie │ │ Documentation │ │ Bilan  │ │ Crise │ │ Thérapie │
 *      └──────────┘ └───────────────┘ └────────┘ └───────┘ └──────────┘
 * ```
 *
 * ⭐ **Un contenu ne change jamais de place** : la rubrique est écrite dans le programme et ne
 * bouge pas, alors que `quand` bouge tous les jours. **L'interface n'arbitre rien.**
 *
 * ⭐ **L'ordre est celui des choses, pas celui des besoins** *(15/08/2026)* — la thérapie d'abord
 * parce que c'est là qu'on arrive, puis ce qu'on lit, puis ce qu'on passe, puis la crise. **La crise
 * est la dernière, donc elle est aussi la voisine de gauche de l'entrée** : un seul geste, dans le
 * sens qu'on veut, et jamais une traversée à faire quand le temps manque.
 *
 * ✅ **Le point dur P1 est levé.** Le glissement vertical n'appartient plus au monde : **n'importe
 * quel écran peut porter une liste qui défile**, et aucun contenu n'est plus logé ailleurs que là où
 * il a du sens.
 */

/** L'écart qui laisse passer le monde entre les cartes. Ce n'est pas de la respiration graphique. */
private val ECART_CARTES = 20.dp

/**
 * Kokoro est haut d'environ un quart de dalle. **Une hauteur en dp, et non une fraction d'écran** :
 * il est posé dans une liste qui défile, où la hauteur disponible est infinie par construction.
 */
private val HAUTEUR_KOKORO = 184.dp

@Composable
fun EcranDeBord(
    titre: String,
    couleur: Teinte,
    defilant: Boolean,
    modifier: Modifier = Modifier,
    onReglages: (() -> Unit)? = null,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        BandeTitre(titre = titre, couleur = couleur, onReglages = onReglages)

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
                verticalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterVertically),
                content = contenu,
            )
        }
    }
}

/**
 * **THÉRAPIE — l'écran d'entrée.** Les prochaines actions de la thérapie en cours, groupées par
 * `quand`, et **Kokoro en tête de liste**.
 *
 * ⭐ **C'est ici qu'on arrive** *(15/08/2026)*, donc c'est ici que vivent les trois choses qui
 * n'appartiennent à aucune rubrique : Kokoro, la roue dentée de l'écran de contrôle (**D4**), et
 * l'avis de porte fermée quand il y a lieu.
 *
 * ⏳ **La place de Kokoro est provisoire** : il occupe le haut de la liste et s'en va avec elle quand
 * on défile. **Comment il habite vraiment l'écran se décide à part** — ça touche au corps, pas au
 * rangement.
 *
 * ⭐ **L'écran est long avant d'être riche** : sept démarches administratives sans date. C'est
 * l'état réel du chantier n° 1, et l'interface ne le maquille pas.
 */
@Composable
fun ContenuTherapie(
    accesPerdu: Boolean,
    onReglages: () -> Unit,
    onOuvrir: (Etape) -> Unit,
) {
    EcranDeBord(
        titre = stringResource(R.string.monde_therapie_titre),
        couleur = LocalPaletteKokoro.current.menthe,
        defilant = true,
        onReglages = onReglages,
    ) {
        if (accesPerdu) {
            AvisAcces(onReglages = onReglages, modifier = Modifier.padding(top = 18.dp))
        }
        KokoroPose()
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
 * 🔴 **Kokoro garde les couleurs du SVG, jour et nuit** ([PALETTE_CLAIRE]) : il n'est pas posé sur le
 * fond de l'application, il est posé dans le décor. Le repeindre avec le ciel reviendrait à lui
 * donner une deuxième apparence à décoder — le décor change d'heure, lui non.
 */
@Composable
private fun KokoroPose(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(top = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        CorpsKokoro(
            rig = rigAnime(Posture.Repos),
            modifier = Modifier.height(HAUTEUR_KOKORO).aspectRatio(LARGEUR_VUE / HAUTEUR_VUE),
            palette = PALETTE_CLAIRE,
        )
    }
}

/**
 * **DOCUMENTATION.** La bibliothèque, une fiche par ligne.
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
 * **BILAN.** Les questionnaires à passer et les comptes rendus écrits en séance.
 *
 * 🔴 **Aucun chiffre, aucune courbe, aucun score** : la cotation n'est pas dans Kokoro
 * (`companion/PROGRAMME.md` §3), et un score mal lu est pire qu'un score absent.
 *
 * ⭐ **Il défile comme la documentation** *(15/08/2026)* — il ne le pouvait pas tant qu'il était en
 * haut du monde. **Les deux écrans qui n'ont rien se ressemblent maintenant à la lettre** : un état
 * vide posé en haut se lit comme une liste sans rien dedans, ce qu'il est, et non comme un message
 * adressé.
 */
@Composable
fun ContenuBilan() {
    EcranDeBord(
        titre = stringResource(R.string.monde_bilan_titre),
        couleur = LocalPaletteKokoro.current.beurre,
        defilant = true,
    ) {
        CadreVide(texte = stringResource(R.string.monde_bilan_vide))
    }
}

/**
 * **CRISE.** Trois grands boutons, **jamais de défilement** — 🔴 **c'est une exigence en soi**, et
 * elle survit à la levée de **P1** : en crise, une liste qui bouge sous le doigt est une chose de
 * plus à maîtriser.
 *
 * 🔴 **L'écart assumé : c'est l'écran le moins décoré du monde** (§4.5). Aucun ornement, aucune
 * étincelle, aucun cœur, une seule couleur, texte à 21 sp et boutons à 88 dp. **En crise, la
 * mignonnerie est du bruit** — il se distingue en étant plus grand et plus vide, **pas plus vif**.
 *
 * ⭐ **Cet écran est la première porte, pas la seule.** La notification y mène aussi, et depuis le
 * 15/08/2026 **elle mène ici même** : les boutons sont ceux de [PortesDeCrise], partagés à la
 * lettre avec l'écran de crise ouvert hors du monde (§6.2).
 *
 * ⭐ **Il est le voisin de gauche de l'entrée** : un seul geste depuis l'ouverture de l'app, dans le
 * sens qu'on veut.
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
 * **L'avis de porte fermée**, en tête de l'écran d'entrée. Il ne paraît que quand la notification
 * d'accès n'a pas pu être affichée : l'autorisation a été refusée, ou **Android l'a révoquée tout
 * seul** — il le fait pour les applications peu utilisées, et Kokoro en est une par construction.
 *
 * 🔴 **Une phrase, pas une pastille** *(tranché par Xavier le 15/08/2026)*. Un point coloré sur la
 * roue dentée aurait dit *va voir* sans dire quoi : **c'est un sous-entendu, et le dispositif n'en
 * fait aucun.** D4 tient donc — *jamais de pastille dessus* — et le rouge reste hors de la palette.
 *
 * ⭐ **Ce n'est pas une relance** : il ne compte pas les jours, il ne revient pas, il n'insiste pas.
 * **Il constate un défaut, et il disparaît de lui-même quand le défaut est réparé.**
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
