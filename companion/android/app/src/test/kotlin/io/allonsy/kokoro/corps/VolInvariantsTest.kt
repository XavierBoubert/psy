package io.allonsy.kokoro.corps

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val FICHIER_VOL_DROITE = File("../../ressources/retenus/kokoro-corps-v2-right.svg")

/**
 * ⚠️ Plus lâche que celle du dessin de repos : là-bas on compare des chiffres transcrits, ici le
 * résultat d'une décomposition en `Float`. Sur des translations de l'ordre de 250, sept chiffres
 * significatifs laissent une dizaine de millièmes de reste — soit un cinq-millième de pixel.
 */
private const val PRECISION = 1e-4f

/**
 * Le dessin de vol fait foi, comme le dessin de repos.
 *
 * `retenus/kokoro-corps-v2-right.svg` est Kokoro en vol vers la droite. [POSE_VOL_DROITE_TRONC] et
 * les quatre [PoseMembre] n'en sont qu'une décomposition. Ce test recompose la pose par-dessus le
 * dessin de repos et vérifie qu'on retombe **exactement** sur la matrice de la variante, pièce par
 * pièce — 🔴 y compris la tête et le visage, dont l'oubli avait rendu le vol méconnaissable.
 */
class VolInvariantsTest {

    private val svg: String by lazy {
        assertTrue("SVG introuvable : ${FICHIER_VOL_DROITE.absolutePath}", FICHIER_VOL_DROITE.isFile)
        FICHIER_VOL_DROITE.readText()
    }

    /** La racine du dessin de variante — celle qui transporte tout le reste dans la vue. */
    private val racine: Transformation by lazy { transformation("kokoro") }

    @Test
    fun `les membres du vol sont ceux du dessin`() {
        comparer("bras gauche", place("arm-right"), pose(POSE_VOL_DROITE_BRAS_GAUCHE, BRAS_GAUCHE))
        comparer("bras droit", place("arm-left"), pose(POSE_VOL_DROITE_BRAS_DROIT, BRAS_DROIT))
        comparer("pied gauche", place("foot-right"), pose(POSE_VOL_DROITE_PIED_GAUCHE, PIED_GAUCHE))
        comparer("pied droit", place("foot-left"), pose(POSE_VOL_DROITE_PIED_DROIT, PIED_DROIT))
    }

    /** Le corps s'affine et s'allonge ; le 心, posé dessus, a en plus son glissement propre. */
    @Test
    fun `le corps du vol est celui du dessin`() {
        val vol = POSE_VOL_DROITE_TRONC
        comparer("torse", place("body-form"), TORSE.placement.sous(vol.torse))
        comparer("ligne du ventre", place("body-line"), LIGNE_VENTRE.placement.sous(vol.torse))
        comparer("心", place("kanji-1"), KANJI.first().placement.sous(vol.kanji))
    }

    /**
     * 🔴 **La tête tournée** — c'est elle qui manquait : coque et panneau se resserrent vers la
     * droite, les yeux se rapprochent en glissant du même côté, la bouche suit. **Sans ça, Kokoro
     * volait de face**, et le vol ne se lisait pas.
     */
    @Test
    fun `la tete et le visage du vol sont ceux du dessin`() {
        val vol = POSE_VOL_DROITE_TRONC
        comparer("coque", place("head-out"), TETE.placement.sous(vol.coque))
        comparer("panneau", place("head-in"), PANNEAU.placement.sous(vol.panneau))

        assertEquals("Œil gauche", 37f, vol.oeilGauche.decalage.x, PRECISION)
        assertEquals("Œil droit", 27f, vol.oeilDroit.decalage.x, PRECISION)
        assertTrue(
            "Les yeux se rapprochent : c'est le trois-quarts",
            vol.oeilGauche.decalage.x > vol.oeilDroit.decalage.x,
        )
        assertTrue("La coque se resserre", vol.coque.a < 1f && vol.panneau.a < vol.coque.a)
        assertTrue("Et rien ne tourne dans le tronc", listOf(vol.torse, vol.coque, vol.panneau).all { it.b == 0f })
    }

    /** 🔴 Au repos, la pose ne fait rien : c'est ce qui autorise à la composer partout sans garde. */
    @Test
    fun `la pose est l'identite au repos`() {
        listOf(
            POSE_VOL_DROITE_BRAS_GAUCHE,
            POSE_VOL_DROITE_BRAS_DROIT,
            POSE_VOL_DROITE_PIED_GAUCHE,
            POSE_VOL_DROITE_PIED_DROIT,
        ).forEach { comparer("membre à t = 0", Transformation(), it.echelle(0f).transformation) }

        val repos = POSE_VOL_DROITE_TRONC.echelle(0f)
        comparer("torse à t = 0", Transformation(), repos.torse)
        comparer("coque à t = 0", Transformation(), repos.coque)
        comparer("panneau à t = 0", Transformation(), repos.panneau)
        comparer("œil à t = 0", Transformation(), repos.oeilGauche)
    }

    /**
     * ⭐ **Une échelle interpolée reste une échelle autour du même point** — c'est ce qui autorise
     * [Transformation.versIdentite] à lerper les termes au lieu de décomposer.
     */
    @Test
    fun `l'interpolation garde le pivot de l'echelle`() {
        val pleine = POSE_VOL_DROITE_TRONC.panneau
        val pivot = pleine.e / (1f - pleine.a)
        listOf(0.2f, 0.5f, 0.9f).forEach { t ->
            val partielle = pleine.versIdentite(t)
            assertEquals("Pivot à t = $t", pivot, partielle.e / (1f - partielle.a), 1e-2f)
        }
    }

    /**
     * 🔴 Le vol vers la gauche est le miroir de celui vers la droite, et rien d'autre — il n'a pas de
     * troisième dessin. Les côtés s'échangent, les angles changent de sens, les pivots passent l'axe.
     */
    @Test
    fun `le vol vers la gauche est le miroir de celui vers la droite`() {
        listOf(
            POSE_VOL_DROITE_BRAS_DROIT to POSE_VOL_GAUCHE_BRAS_GAUCHE,
            POSE_VOL_DROITE_BRAS_GAUCHE to POSE_VOL_GAUCHE_BRAS_DROIT,
            POSE_VOL_DROITE_PIED_DROIT to POSE_VOL_GAUCHE_PIED_GAUCHE,
            POSE_VOL_DROITE_PIED_GAUCHE to POSE_VOL_GAUCHE_PIED_DROIT,
        ).forEach { (droite, gauche) ->
            assertEquals("L'angle change de sens", -droite.angle, gauche.angle, PRECISION)
            assertEquals("Le pivot passe l'axe", 2f * AXE - droite.pivot.x, gauche.pivot.x, PRECISION)
            assertEquals("Sans changer de hauteur", droite.pivot.y, gauche.pivot.y, PRECISION)
        }

        val gauche = POSE_VOL_GAUCHE_TRONC
        assertTrue("Le visage part de l'autre côté", gauche.oeilGauche.decalage.x < 0f)
        assertTrue(
            "Et ce sont les yeux qui s'échangent",
            gauche.oeilDroit.decalage.x < gauche.oeilGauche.decalage.x,
        )
        assertEquals(
            "Le corps s'affine pareil",
            POSE_VOL_DROITE_TRONC.torse.a,
            gauche.torse.a,
            PRECISION,
        )
        assertEquals(
            "Le pivot de la coque passe l'axe",
            2f * AXE - POSE_VOL_DROITE_TRONC.coque.let { it.e / (1f - it.a) },
            gauche.coque.let { it.e / (1f - it.a) },
            1e-2f,
        )
    }

    /**
     * ⭐ Le pivot du corps et ceux des pieds sortent du même geste de la main de Xavier, et tombent
     * au même endroit à un demi-unité près. Ils ont été décomposés séparément : les voir se recouper
     * vaut vérification, comme les deux épaules autour de [AXE].
     */
    @Test
    fun `les pivots du vol se recoupent`() {
        val corps = POSE_VOL_DROITE_TRONC.torse
        val pivotX = corps.e / (1f - corps.a)
        val pivotY = corps.f / (1f - corps.d)
        listOf(POSE_VOL_DROITE_PIED_GAUCHE.pivot, POSE_VOL_DROITE_PIED_DROIT.pivot).forEach { pied ->
            assertEquals("En x", pivotX, pied.x, 0.5f)
            assertEquals("En y", pivotY, pied.y, 0.6f)
        }
    }

    // ————————————————————————————————————————————————————————————————————————————————————————
    // Lecture du SVG de variante
    // ————————————————————————————————————————————————————————————————————————————————————————

    /** Le chemin de groupes qui mène à chaque pièce, racine exclue — elle est ajoutée par [place]. */
    private val groupes = mapOf(
        "body-form" to listOf("body"),
        "body-line" to listOf("body"),
        "kanji-1" to listOf("kanji", "body"),
        "head-out" to listOf("head"),
        "head-in" to listOf("head"),
    )

    /** La pièce du dessin de repos, portée par sa pose de vol — ce que l'application dessine. */
    private fun pose(pose: PoseMembre, piece: Piece): Transformation =
        piece.placement.sous(pose.transformation)

    /** La pièce de la variante, posée dans la vue. */
    private fun place(nom: String): Transformation =
        groupes.getOrDefault(nom, emptyList())
            .fold(transformation(nom)) { piece, groupe -> piece.sous(transformation(groupe)) }
            .sous(racine)

    private fun transformation(nom: String): Transformation {
        val debut = svg.indexOf("id=\"kokoro-corps-v2-u-$nom\"")
        assertTrue("Élément absent du dessin de vol : $nom", debut >= 0)
        val balise = svg.substring(debut, svg.indexOf('>', debut))
        val valeur = Regex("(?<![\\w-])transform=\"([^\"]*)\"").find(balise)?.groupValues?.get(1)
        return when {
            valeur == null -> Transformation()
            valeur.startsWith("translate(") -> nombres(valeur).let { translation(it[0], it[1]) }
            valeur.startsWith("matrix(") ->
                nombres(valeur).let { Transformation(it[0], it[1], it[2], it[3], it[4], it[5]) }

            else -> error("Transformation non reconnue : $valeur")
        }
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
}
