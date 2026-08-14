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
)

/**
 * ⭐ Le `decalage` des couches du bas n'est pas un réglage esthétique : il **paie d'avance la
 * montée**. Une couche ancrée en bas qui remonte de plus que son décalage découvre le ciel sous
 * elle, et il faut alors prolonger sa dernière tranche de pixels — un étirement visible. En posant
 * chaque couche un peu plus bas que le bord, la montée reste dans ce qu'on a déjà donné.
 *
 * Le seuil se calcule : une couche remonte au plus `DEBATTEMENT_VERTICAL × profondeur` d'écran, soit
 * 0,052 pour la prairie et 0,078 pour le feuillage. Les deux décalages sont **au-dessus**, donc la
 * tranche prolongée ne sert jamais.
 *
 * ⭐ **La `largeur` est aussi l'échelle** : une tuile plus large agrandit ce qu'elle contient. La
 * prairie du fond est large (3,60) parce qu'elle doit se lire comme une plaine et pas comme une
 * bande au ras du bord ; le feuillage est étroit (1,50) parce qu'au premier plan, des feuilles trop
 * grandes mangent l'écran. C'est un compromis assumé : **plus une tuile est étroite, plus elle se
 * répète** — le feuillage croise donc son axe de symétrie plus souvent qu'avant, la prairie
 * beaucoup moins.
 */
val COUCHES: List<Couche> = listOf(
    Couche(R.drawable.decor_nuages_loin, profondeur = 0.14f, ancrage = Ancrage.HAUT, largeur = 1.40f, decalage = 0.01f),
    Couche(R.drawable.decor_nuages_pres, profondeur = 0.30f, ancrage = Ancrage.HAUT, largeur = 1.90f, decalage = 0.06f),
    Couche(R.drawable.decor_collines, profondeur = 0.52f, ancrage = Ancrage.BAS, largeur = 3.60f, decalage = 0.055f),
    Couche(R.drawable.decor_feuillage, profondeur = 0.78f, ancrage = Ancrage.BAS, largeur = 1.50f, decalage = 0.080f),
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
