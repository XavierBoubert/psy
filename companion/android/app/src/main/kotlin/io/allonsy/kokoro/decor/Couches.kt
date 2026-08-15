package io.allonsy.kokoro.decor

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import io.allonsy.kokoro.R

/**
 * Le décor — quatre couches peintes, empilées du loin vers le près.
 *
 * Chacune est un calque partiel : elle occupe sa bande et laisse le reste transparent. Le ciel n'est
 * dans aucune d'elles — il est peint en dégradé sous la pile, ce qui le rend gratuit à décliner en
 * clair et en sombre.
 *
 * ⭐ **La profondeur est la seule chose qui fasse le parallaxe** : une couche se déplace de
 * `profondeur × un écran` quand la caméra traverse un écran. Le contenu, lui, est à 1 — il se
 * déplace exactement d'un écran, donc il colle au doigt.
 */
enum class Ancrage { HAUT, BAS }

data class Couche(
    @DrawableRes val image: Int,
    val profondeur: Float,
    val ancrage: Ancrage,
    /** Largeur de la tuile, en multiples de la largeur de l'écran. */
    val largeur: Float,
    /** Écart au bord d'ancrage, en fraction de la hauteur de l'écran. */
    val decalage: Float,
    /**
     * Fraction de la tuile **peinte en vide** à gauche comme à droite. `0` pour un dessin qui va
     * bord à bord.
     *
     * ⭐ **C'est elle qui décide de la façon de répéter** *(15/08/2026)*, et il n'y a pas de second
     * réglage : **une tuile à marges se répète simplement**, en avançant de sa partie peinte, si
     * bien que les deux marges se recouvrent et que le dessin reprend exactement où il s'arrête.
     * **Une tuile bord à bord n'a pas ce luxe** — elle ne peut être répétée qu'en miroir.
     */
    val marge: Float = 0f,
) {
    val enMiroir: Boolean get() = marge <= 0f

    /** De combien on avance d'une tuile à la suivante, en multiples de la largeur de l'écran. */
    val pas: Float get() = largeur * (1f - 2f * marge)
}

/**
 * 🔴 **Le `decalage` d'une couche ancrée en bas ne descend jamais sous zéro** : elle sortirait alors
 * de la dalle par le haut de son propre bord, et découvrirait le ciel sous elle. En la posant un peu
 * plus bas que le bord, son pied reste hors champ quoi qu'il arrive.
 *
 * ⭐ **La `largeur` est aussi l'échelle** : une tuile plus large agrandit ce qu'elle contient. La
 * prairie du fond est large (3,60) parce qu'elle doit se lire comme une plaine et pas comme une
 * bande au ras du bord ; le feuillage reste modeste parce qu'au premier plan, des feuilles trop
 * grandes mangent l'écran.
 *
 * 🔄 **Deux couches ont perdu leur miroir le 15/08/2026** *(relevé par Xavier : « la jointure des
 * nuages et des feuilles n'est pas très jolie »)*. **Le miroir ne faisait pas un raccord, il faisait
 * un papillon** : le dessin allait bord à bord, donc un nuage coupé par le bord retrouvait sa propre
 * image retournée et formait une masse symétrique — sans discontinuité, mais parfaitement
 * reconnaissable. ⭐ **La sortie n'était pas d'élargir, c'était de vider les bords** : redessinées
 * avec une marge latérale, les deux couches se répètent maintenant **sans miroir**, en avançant de
 * leur partie peinte. Le dessin reprend là où il s'arrête, et il n'y a plus d'axe du tout.
 *
 * ⏳ **Les nuages lointains gardent le leur** : quatre planches ont été essayées, aucune n'a rendu
 * un fond d'un seul ton *(le modèle peint les marges dans un magenta plus clair, que le détourage ne
 * coupe pas)*. **C'est la couche la plus pâle et la plus lente** — son axe est le moins visible du
 * lot. 🔴 **La prairie garde le sien pour une raison qui ne changera pas** : une couche de sol doit
 * aller bord à bord, sinon elle découvre le ciel sous elle.
 */
val COUCHES: List<Couche> = listOf(
    Couche(R.drawable.decor_nuages_loin, profondeur = 0.14f, ancrage = Ancrage.HAUT, largeur = 1.40f, decalage = 0.01f),
    Couche(R.drawable.decor_nuages_pres, profondeur = 0.30f, ancrage = Ancrage.HAUT, largeur = 2.40f, decalage = 0.06f, marge = 0.16f),
    Couche(R.drawable.decor_collines, profondeur = 0.52f, ancrage = Ancrage.BAS, largeur = 3.60f, decalage = 0.055f),
    Couche(R.drawable.decor_feuillage, profondeur = 0.78f, ancrage = Ancrage.BAS, largeur = 1.90f, decalage = 0.080f, marge = 0.16f),
)

/**
 * Le ciel et, s'il y en a une, la teinte appliquée aux couches.
 *
 * ⭐ **Une déclinaison ne demande pas un second jeu de dessins** : c'est le même, multiplié par
 * [teinte]. Deux jeux d'images dériveraient l'un de l'autre à la première retouche.
 */
data class PaletteDecor(
    val ciel: List<Color>,
    val teinte: Color?,
)

val DECOR_JOUR = PaletteDecor(
    ciel = listOf(Color(0xFF1FA9CE), Color(0xFF7FD4E6), Color(0xFFC8ECF2)),
    teinte = null,
)

/**
 * ⭐ **Branchée sur une plage horaire, et sur rien d'autre** *(14/08/2026)*.
 *
 * 🔴 Le décor **ne suit toujours pas le thème sombre du système** : il changerait alors d'apparence
 * selon un réglage qui n'a rien à voir avec la thérapie, et sans que Xavier l'ait décidé. Une plage
 * horaire est l'inverse — elle est **fixée d'avance, visible dans les réglages, et désactivable**.
 * L'invariant interdit le changement *non annoncé*, pas le changement *prévu* (`companion/README.md` §5).
 *
 * ⭐ **Et il ne bascule jamais sous les yeux** : la palette est décidée à l'arrivée dans le monde,
 * puis tenue tant qu'il est ouvert. Voir un décor virer tout seul à 21 h serait exactement le
 * mouvement à interpréter que le dispositif ne provoque jamais.
 */
val DECOR_NUIT = PaletteDecor(
    ciel = listOf(Color(0xFF08202E), Color(0xFF103648), Color(0xFF1A4A63)),
    teinte = Color(0xFF4C7691),
)
