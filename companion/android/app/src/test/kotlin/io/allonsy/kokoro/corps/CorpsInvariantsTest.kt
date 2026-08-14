package io.allonsy.kokoro.corps

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val FICHIER_SVG = File("../design/retenus/kokoro-corps-v2.svg")

private const val PRECISION = 1e-6f

/**
 * Le dessin fait foi.
 *
 * `design/retenus/kokoro-corps-v2.svg` est le corps de Kokoro tel que Xavier l'a dessiné, et
 * [Geometrie] n'en est qu'une transcription. Ces tests relisent le SVG et refusent la moindre
 * dérive : une forme, une transformation, une épaisseur ou une couleur qui ne correspond plus, et
 * l'application ne montre plus le personnage qui a été validé.
 */
class CorpsInvariantsTest {

    private val svg: String by lazy {
        assertTrue("SVG introuvable : ${FICHIER_SVG.absolutePath}", FICHIER_SVG.isFile)
        FICHIER_SVG.readText()
    }

    /** Le chemin de groupes qui mène à chaque pièce — la seconde déclaration que le test compare. */
    private val groupes = mapOf(
        "body-form" to listOf("kokoro", "body"),
        "body-line" to listOf("kokoro", "body"),
        "kanji-1" to listOf("kokoro", "body", "kanji"),
        "kanji-2" to listOf("kokoro", "body", "kanji"),
        "kanji-3" to listOf("kokoro", "body", "kanji"),
        "kanji-4" to listOf("kokoro", "body", "kanji"),
        "head-out" to listOf("kokoro", "head"),
        "head-in" to listOf("kokoro", "head"),
        "foot-right" to listOf("kokoro"),
        "foot-left" to listOf("kokoro"),
        "arm-right" to listOf("kokoro"),
        "arm-left" to listOf("kokoro"),
    )

    private val couleurs = mapOf(
        Remplissage.COQUE to "#faf7f0",
        Remplissage.PANNEAU to "#e5dfd4",
        Remplissage.ENCRE to "#383838",
        Remplissage.AUCUN to "none",
    )

    @Test
    fun `chaque piece porte la forme que le svg lui donne`() {
        PIECES.forEach { piece ->
            val bloc = balise(piece.nom)
            assertEquals(
                "Le tracé de ${piece.nom} a dérivé du dessin",
                chemin(piece.forme),
                attribut(bloc, "d"),
            )
        }
    }

    @Test
    fun `chaque piece est placee la ou le svg la pose`() {
        PIECES.forEach { piece ->
            val bloc = balise(piece.nom)
            comparer(piece.nom, transformation(attribut(bloc, "transform")), piece.transformation)
            comparer("groupe de ${piece.nom}", groupeDe(piece.nom), piece.groupe)
        }
    }

    @Test
    fun `chaque piece porte le trait et le remplissage du svg`() {
        PIECES.forEach { piece ->
            val bloc = balise(piece.nom)
            assertEquals(
                "Épaisseur de ${piece.nom}",
                attribut(bloc, "stroke-width")!!.toFloat(),
                piece.epaisseur,
                PRECISION,
            )
            assertEquals(
                "Remplissage de ${piece.nom}",
                attribut(bloc, "fill"),
                couleurs.getValue(piece.remplissage),
            )
        }
    }

    /**
     * `head-out` est la seule pièce étirée de façon non uniforme : son contour déclare 2 mais sort
     * à 2,61. C'est la règle SVG, et c'est ce qui donne à la tête son cerne plus épais.
     */
    @Test
    fun `le contour de la tete est epaissi par son etirement`() {
        assertEquals(1.304847f, TETE.placement.facteurTrait, 1e-5f)
        assertEquals(2.609694f, TETE.epaisseurRendue, 1e-5f)
        assertEquals(EPAISSEUR_CONTOUR, PANNEAU.epaisseurRendue, PRECISION)
    }

    @Test
    fun `le visage neutre est celui du svg`() {
        val oeilGauche = balise("eye-right")
        assertEquals(RAYON_OEIL_X, attribut(oeilGauche, "rx")!!.toFloat(), PRECISION)
        assertEquals(RAYON_OEIL_Y, attribut(oeilGauche, "ry")!!.toFloat(), PRECISION)
        assertEquals(OEIL_OVALE.forme, Forme.Ellipse(RAYON_OEIL_X, RAYON_OEIL_Y))

        val placementOeil = transformation(attribut(oeilGauche, "transform"))
        comparer("œil gauche", placementOeil.sous(groupeDe("eye-right")).origine, OEIL_GAUCHE)

        val bouche = balise("mouth")
        val echelle = -transformation(attribut(bouche, "transform")).a
        assertEquals(DEMI_BOUCHE, attribut(bouche, "x2")!!.toFloat() * echelle, 1e-5f)
        assertEquals(EPAISSEUR_VISAGE, attribut(bouche, "stroke-width")!!.toFloat() * echelle, 1e-5f)
        comparer("bouche", transformation(attribut(bouche, "transform")).sous(groupeDe("mouth")).origine, BOUCHE)
    }

    /** Les yeux sont symétriques dans le dessin, donc ils le restent dans le rig. */
    @Test
    fun `le visage reste symetrique`() {
        assertEquals(OEIL_GAUCHE.y, OEIL_DROIT.y, PRECISION)
        assertTrue("L'œil gauche est à gauche", OEIL_GAUCHE.x < OEIL_DROIT.x)
        assertTrue("La bouche est sous les yeux", BOUCHE.y > OEIL_GAUCHE.y)
    }

    /**
     * 🔴 §3 : les commissures de la bouche ne tombent jamais, et ce n'est pas une règle de
     * discipline — la forme n'existe pas. Le seul arc de bouche du jeu est donc un sourire : son
     * milieu est **plus bas** que ses extrémités. L'œil souriant, lui, fait l'inverse.
     */
    @Test
    fun `la bouche ne tombe jamais aux commissures`() {
        assertTrue("La bouche arc doit sourire", milieuPlusBas(BOUCHE_ARC))
        assertTrue("L'œil souriant se bombe vers le haut", !milieuPlusBas(OEIL_ARC_HAUT))
        assertTrue("L'œil au repos se creuse vers le bas", milieuPlusBas(OEIL_ARC_BAS))
        assertNull(
            "Aucun tracé de sourcil n'existe dans le jeu de pièces",
            TRACES.find { it.nom.contains("sourcil") },
        )
    }

    /**
     * Le morphing n'est possible que parce que toutes les silhouettes se découpent pareil : même
     * nombre de points, même ordre, même rôle à chaque indice.
     */
    @Test
    fun `chaque trace du visage a une silhouette morphable`() {
        TRACES.forEach { trace ->
            assertEquals("Silhouette de ${trace.nom}", POINTS_CONTOUR, trace.contour.points.size)
        }
    }

    /**
     * La silhouette est une approche polygonale du tracé — mais elle approche **ce** tracé-là :
     * celle de l'œil neutre tient dans l'ellipse du SVG, à la corde près.
     */
    @Test
    fun `la silhouette de l'oeil neutre est l'ellipse du dessin`() {
        val points = OEIL_OVALE.contour.points
        assertEquals(RAYON_OEIL_X, points.maxOf { it.x }, 2e-2f)
        assertEquals(-RAYON_OEIL_X, points.minOf { it.x }, 2e-2f)
        assertEquals(RAYON_OEIL_Y, points.maxOf { it.y }, PRECISION)
        assertEquals(-RAYON_OEIL_Y, points.minOf { it.y }, PRECISION)
    }

    /** Aucune apparition instantanée, aucun *cut* (§5) : la déformation part et arrive sur pièce. */
    @Test
    fun `le morphing part d'une forme et arrive exactement sur l'autre`() {
        TRACES.forEach { depuis ->
            TRACES.forEach { vers ->
                val trajet = "${depuis.nom} → ${vers.nom}"
                comparer("$trajet au départ", depuis.contour, depuis.contour.vers(vers.contour, 0f))
                comparer("$trajet à l'arrivée", vers.contour, depuis.contour.vers(vers.contour, 1f))
            }
        }
    }

    /**
     * 🔴 §3, **pendant la déformation aussi.** Une bouche qui se déforme vers une autre passe par
     * des formes que personne n'a dessinées : chacune doit encore sourire ou être droite. Une forme
     * intermédiaire étant une combinaison convexe des deux silhouettes, la propriété se transporte —
     * le test le vérifie quand même, sur les seize couples et sur toute la durée.
     */
    @Test
    fun `aucune deformation de bouche ne fait tomber les commissures`() {
        val bouches = listOf(BOUCHE_TRAIT, BOUCHE_BARRE, BOUCHE_ARC, BOUCHE_COURTE)
        bouches.forEach { depuis ->
            bouches.forEach { vers ->
                listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { avancement ->
                    val forme = depuis.contour.vers(vers.contour, avancement)
                    listOf(forme.bordHaut, forme.bordBas).forEach { bord ->
                        assertTrue(
                            "${depuis.nom} → ${vers.nom} à $avancement : le milieu remonte",
                            bord[bord.size / 2].y >= maxOf(bord.first().y, bord.last().y) - 1e-3f,
                        )
                    }
                }
            }
        }
    }

    /**
     * 🔴 §6, garde-fou 1 : le bras ne dépasse jamais la ligne des épaules. Le dessin pose déjà le
     * bras à 19,5° de la verticale — la borne se calcule depuis là, elle ne se choisit pas.
     */
    @Test
    fun `la designation ne leve jamais le bras au dessus de l'epaule`() {
        assertEquals(90f, INCLINAISON_REPOS + OUVERTURE_HORIZONTALE, 1e-3f)
        assertTrue(
            "Le vol n'a pas le droit de lever le bras plus haut que la désignation",
            OUVERTURE_VOL <= OUVERTURE_HORIZONTALE,
        )
    }

    @Test
    fun `la designation regarde du cote qu'elle montre`() {
        val gauche = RigKokoro.pose(Posture.Montre(Cote.GAUCHE))
        val droite = RigKokoro.pose(Posture.Montre(Cote.DROITE))
        assertEquals(OUVERTURE_HORIZONTALE, gauche.rotationBrasGauche, 0f)
        assertTrue("Le regard suit le bras gauche", gauche.regard < 0f)
        assertEquals(-OUVERTURE_HORIZONTALE, droite.rotationBrasDroit, 0f)
        assertTrue("Le regard suit le bras droit", droite.regard > 0f)
    }

    /** Le repos, c'est le dessin : un rig par défaut ne déplace pas une seule pièce. */
    @Test
    fun `le rig au repos ne bouge rien`() {
        val repos = RigKokoro.pose(Posture.Repos)
        assertEquals(0f, repos.rotationBrasGauche, 0f)
        assertEquals(0f, repos.rotationBrasDroit, 0f)
        assertEquals(0f, repos.rotationPiedGauche, 0f)
        assertEquals(0f, repos.rotationPiedDroit, 0f)
        assertEquals(0f, repos.regard, 0f)
        assertEquals(1f, repos.echelle, 0f)
        assertEquals(1f, repos.etirementCorps, 0f)
    }

    /** Les deux épaules se répondent autour de l'axe : le calcul d'un pivot vaut pour l'autre. */
    @Test
    fun `les epaules sont symetriques autour de l'axe`() {
        assertEquals(AXE, (EPAULE_GAUCHE.x + EPAULE_DROITE.x) / 2f, 1e-3f)
        assertEquals(EPAULE_GAUCHE.y, EPAULE_DROITE.y, 1e-3f)
        assertTrue("L'épaule est au-dessus du ventre", EPAULE_GAUCHE.y < CENTRE_VENTRE.y)
    }

    /**
     * Le centre du ventre n'est pas choisi : c'est le point fixe de la rotation que `foot-right`
     * porte dans le SVG. Xavier a fait pivoter le pied autour du ventre, la matrice l'a gardé.
     */
    @Test
    fun `le centre du ventre est le pivot ecrit dans le dessin`() {
        val ventre = Ancre(CENTRE_VENTRE.x - RACINE.e, CENTRE_VENTRE.y - RACINE.f)
        val image = PIED_GAUCHE.transformation.applique(ventre)
        assertEquals("Le pied tourne autour d'un autre point", ventre.x, image.x, 1e-3f)
        assertEquals("Le pied tourne autour d'un autre point", ventre.y, image.y, 1e-3f)
        assertEquals("Le ventre est sur l'axe", AXE, CENTRE_VENTRE.x, 0.2f)
    }

    @Test
    fun `la respiration reste dans l'amplitude annoncee`() {
        val expiration = RigKokoro(visage = Visage.de(Expression.NEUTRE), respiration = 0f)
        val inspiration = RigKokoro(visage = Visage.de(Expression.NEUTRE), respiration = 1f)
        assertEquals(1f, expiration.etirementCorps, 0f)
        assertEquals(1f + AMPLITUDE_HAUTEUR, inspiration.etirementCorps, 1e-6f)
        assertEquals(1f - AMPLITUDE_LARGEUR, inspiration.retractionCorps, 1e-6f)
    }

    // ————————————————————————————————————————————————————————————————————————————————————————
    // Lecture du SVG
    // ————————————————————————————————————————————————————————————————————————————————————————

    private fun balise(id: String): String {
        val debut = svg.indexOf("id=\"$id\"")
        assertTrue("Élément absent du SVG : $id", debut >= 0)
        return svg.substring(debut, svg.indexOf('>', debut))
    }

    private fun attribut(bloc: String, nom: String): String? =
        Regex("(?<![\\w-])$nom=\"([^\"]*)\"").find(bloc)?.groupValues?.get(1)

    private fun groupeDe(nom: String): Transformation =
        (groupes[nom] ?: listOf("kokoro", chapeau(nom)))
            .map { transformation(attribut(balise(it), "transform")) }
            .reduce { parent, enfant -> enfant.sous(parent) }

    private fun chapeau(nom: String) = if (nom == "mouth" || nom.startsWith("eye")) "head" else "body"

    private fun transformation(valeur: String?): Transformation = when {
        valeur == null -> Transformation()
        valeur.startsWith("translate(") -> nombres(valeur).let { translation(it[0], it[1]) }
        valeur.startsWith("matrix(") ->
            nombres(valeur).let { Transformation(it[0], it[1], it[2], it[3], it[4], it[5]) }

        else -> error("Transformation non reconnue : $valeur")
    }

    private fun nombres(valeur: String) = valeur
        .substringAfter('(')
        .substringBefore(')')
        .split(' ', ',')
        .filter { it.isNotBlank() }
        .map { it.toFloat() }

    private fun comparer(quoi: String, attendue: Transformation, obtenue: Transformation) {
        listOf(
            "a" to (attendue.a to obtenue.a), "b" to (attendue.b to obtenue.b),
            "c" to (attendue.c to obtenue.c), "d" to (attendue.d to obtenue.d),
            "e" to (attendue.e to obtenue.e), "f" to (attendue.f to obtenue.f),
        ).forEach { (terme, valeurs) ->
            assertEquals("$quoi — terme $terme", valeurs.first, valeurs.second, PRECISION)
        }
    }

    private fun comparer(quoi: String, attendue: Ancre, obtenue: Ancre) {
        assertEquals("$quoi — x", attendue.x, obtenue.x, PRECISION)
        assertEquals("$quoi — y", attendue.y, obtenue.y, PRECISION)
    }

    private fun comparer(quoi: String, attendu: Contour, obtenu: Contour) {
        attendu.points.forEachIndexed { indice, point ->
            assertEquals("$quoi — point $indice en x", point.x, obtenu.points[indice].x, PRECISION)
            assertEquals("$quoi — point $indice en y", point.y, obtenu.points[indice].y, PRECISION)
        }
    }

    private fun chemin(forme: Forme): String =
        checkNotNull(forme as? Forme.Chemin) { "Forme sans tracé : $forme" }.donnees

    /** Le milieu d'un arc quadratique est-il plus bas que ses extrémités ? *(y croît vers le bas.)* */
    private fun milieuPlusBas(trace: Trace): Boolean {
        val arc = checkNotNull(trace.forme as? Forme.Arc) { "Tracé sans arc : ${trace.nom}" }
        assertEquals("L'arc doit être symétrique", arc.y1, arc.y2, PRECISION)
        return 0.25f * arc.y1 + 0.5f * arc.cy + 0.25f * arc.y2 > arc.y1
    }
}
