package io.allonsy.kokoro.monde

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import io.allonsy.kokoro.corps.Cote
import io.allonsy.kokoro.corps.Expression
import io.allonsy.kokoro.corps.OUVERTURE_BRAS_LEVES
import io.allonsy.kokoro.corps.OUVERTURE_HORIZONTALE
import io.allonsy.kokoro.corps.Posture
import io.allonsy.kokoro.corps.Vol
import io.allonsy.kokoro.corps.ombre
import io.allonsy.kokoro.corps.reglage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private val TAILLE = Size(72f, 60f)

private const val PRECISION = 1e-4f

class HabitantTest {

    private fun sejour(
        heure: Int = 9,
        checkinFait: Boolean = false,
        vides: Set<Ecran> = emptySet(),
    ) = Sejour(heure = heure, checkinFait = checkinFait, vides = vides)

    // E13, Xavier 16/08/2026 (après refus superviseur du 15/08) : écran de crise = veille fixe, indépendante de tout état.
    @Test
    fun `l'ecran de crise porte une veille, et rien de plus`() {
        val places = listOf(0, 12, 17, 18, 23).flatMap { heure ->
            listOf(true, false).map { fait ->
                place(Ecran.CRISE, sejour(heure = heure, checkinFait = fait))
            }
        }
        assertEquals(
            "L'écran de crise ne réagit à rien : ni à l'heure, ni au check-in",
            1,
            places.toSet().size,
        )

        val veille = places.first()
        assertNotNull(veille)
        assertEquals(Posture.Accoude, veille?.posture)
        assertEquals("Il s'accoude sur le bouton", Perchoir.CRISE, veille?.perchoir)
        assertEquals(Cadrage.EPAULES_AU_BORD, veille?.cadrage)
        assertTrue("Un visage, donc le panneau reste allumé", Posture.Accoude.reglage().panneauAllume)
        assertEquals("Accoudé, il ne vole pas", Vol.AUCUN, veille?.vol)
        assertNull("Il n'a donc pas d'ombre à poser sur l'interface", veille?.vol?.ombre())
        assertTrue("Le corps derrière le bouton, les bras devant", veille?.deuxPasses == true)
        assertNull("Il ne lit rien : il regarde", veille?.balayage)
    }

    // Un Zzz sur le bouton Mot-code serait le contresens le plus coûteux du dispositif.
    @Test
    fun `la crise ne l'endort jamais`() {
        val toutVide = place(Ecran.CRISE, sejour(vides = Ecran.entries.toSet()))
        assertEquals(Posture.Accoude, toutVide?.posture)
        assertEquals("La crise n'est pas une liste", place(Ecran.CRISE, sejour()), toutVide)
        assertTrue("La crise n'est jamais comptée vide", Ecran.CRISE !in ECRANS_VIDES)
    }

    @Test
    fun `il bascule vers le check in a dix huit heures`() {
        (0 until HEURE_DU_CHECKIN).forEach { heure ->
            val place = place(Ecran.THERAPIE, sejour(heure = heure))
            assertEquals("À ${heure}h il est pensif", Posture.Pensif, place?.posture)
            assertEquals("À ${heure}h il se tient devant Sans date", Perchoir.SANS_DATE, place?.perchoir)
        }
        (HEURE_DU_CHECKIN..23).forEach { heure ->
            val place = place(Ecran.THERAPIE, sejour(heure = heure))
            assertEquals("À ${heure}h il montre", Posture.Montre(Cote.GAUCHE), place?.posture)
            assertEquals("À ${heure}h il se tient devant Aujourd'hui", Perchoir.AUJOURDHUI, place?.perchoir)
        }
    }

    // §4.4 : chaleureux n'a pas de contraire — rien ne doit signaler qu'une étape n'est pas faite.
    @Test
    fun `le check in non fait ne se voit nulle part`() {
        val fait = place(Ecran.THERAPIE, sejour(heure = 20, checkinFait = true))
        val pasFait = place(Ecran.THERAPIE, sejour(heure = 20, checkinFait = false))
        assertNotNull(fait)
        assertNotNull(pasFait)

        assertEquals("Seule l'expression sépare les deux états", pasFait, fait?.copy(expression = null))
        assertEquals(Expression.CHALEUREUX, fait?.expression)
        assertNull("Rien ne dit qu'une étape n'est pas faite", pasFait?.expression)
    }

    @Test
    fun `une liste vide l'endort sans le deplacer`() {
        listOf(Ecran.DOCUMENTATION, Ecran.BILAN).forEach { ecran ->
            val pleine = place(ecran, sejour())
            val vide = place(ecran, sejour(vides = setOf(ecran)))
            assertNotNull(pleine)

            assertEquals("La place ne change pas", pleine?.perchoir, vide?.perchoir)
            assertEquals("Le cadrage ne change pas", pleine?.cadrage, vide?.cadrage)
            assertEquals(Posture.Sommeil, vide?.posture)
            assertEquals("Le vol du sommeil est celui du sommeil", Vol.SOMMEIL, vide?.vol)
            assertNull("Il ne lit pas en dormant", vide?.balayage)
        }
    }

    @Test
    fun `la documentation et le bilan dorment aujourd'hui`() {
        assertEquals(setOf(Ecran.DOCUMENTATION, Ecran.BILAN), ECRANS_VIDES)
        ECRANS_VIDES.forEach { ecran ->
            assertEquals(
                "$ecran devrait dormir",
                Posture.Sommeil,
                place(ecran, Sejour(heure = 9, checkinFait = false))?.posture,
            )
        }
        assertTrue(
            "La thérapie n'est jamais vide : elle porte le check-in et sept démarches",
            Ecran.THERAPIE !in ECRANS_VIDES,
        )
    }

    // Seule exception au vol perpétuel : accoudé à la crise, sinon les bras glisseraient et l'ombre tomberait sur l'UI.
    @Test
    fun `il ne pose jamais les pieds, sauf accoude a la crise`() {
        Ecran.entries.forEach { ecran ->
            listOf(emptySet(), setOf(ecran)).forEach { vides ->
                val pose = place(ecran, sejour(vides = vides))
                when (pose?.posture) {
                    null -> Unit
                    Posture.Accoude -> assertEquals("Accoudé, il ne vole pas", Vol.AUCUN, pose.vol)
                    else -> assertTrue("$ecran — il s'est posé", pose.vol != Vol.AUCUN)
                }
            }
        }
        assertEquals(
            "Une seule place ne vole pas, et c'est la crise",
            listOf(Ecran.CRISE),
            Ecran.entries.filter { place(it, sejour())?.vol == Vol.AUCUN },
        )
    }

    @Test
    fun `il se pose dans la bande qu'on lui reserve`() {
        val bande = Rect(left = 40f, top = 200f, right = 1000f, bottom = 300f)

        val droite = pointDeLaPlace(bande, Cadrage.A_DROITE, TAILLE)!!
        assertEquals("Il touche le bord droit du contenu", bande.right, droite.x + TAILLE.width, PRECISION)
        assertEquals("Il est centré dans la hauteur de la bande", bande.center.y, droite.y + TAILLE.height / 2f, PRECISION)

        val centre = pointDeLaPlace(bande, Cadrage.AU_CENTRE, TAILLE)!!
        assertEquals(bande.center.x, centre.x + TAILLE.width / 2f, PRECISION)
        assertEquals(bande.center.y, centre.y + TAILLE.height / 2f, PRECISION)
    }

    @Test
    fun `il sort du champ avec la bande au lieu de se replacer`() {
        val taille = Size(72f, 60f)
        val dedans = Rect(left = 40f, top = 200f, right = 1000f, bottom = 300f)
        val remontee = dedans.translate(0f, -2_000f)

        val bas = pointDeLaPlace(dedans, Cadrage.AU_CENTRE, taille)!!
        val haut = pointDeLaPlace(remontee, Cadrage.AU_CENTRE, taille)!!
        assertEquals("Il monte exactement de ce que la bande monte", bas.y - 2_000f, haut.y, PRECISION)
        assertEquals("Il ne dérive pas latéralement en défilant", bas.x, haut.x, PRECISION)
        assertTrue("Il est bien sorti du champ", haut.y < 0f)

        assertNull("Sans bande posée, aucun point", pointDeLaPlace(null, Cadrage.AU_CENTRE, taille))
    }

    @Test
    fun `l'arc du transit monte et retombe exactement`() {
        val fleche = 26f
        assertEquals("Aucun saut au départ", 0f, arc(fleche, 0f), PRECISION)
        assertEquals("Aucun saut à l'arrivée", 0f, arc(fleche, 1f), PRECISION)
        assertEquals("Le sommet vaut la flèche", fleche, arc(fleche, 0.5f), PRECISION)

        val hauteurs = (0..100).map { arc(fleche, it / 100f) }
        assertEquals("Il ne descend jamais sous sa ligne", 0f, hauteurs.min(), PRECISION)
        assertEquals(
            "Un seul sommet : il monte puis il redescend",
            1,
            hauteurs.indices.count { rang ->
                rang > 0 && rang < hauteurs.lastIndex &&
                    hauteurs[rang] >= hauteurs[rang - 1] && hauteurs[rang] >= hauteurs[rang + 1]
            },
        )
    }

    @Test
    fun `il quitte le champ par le cote au lieu de s'effacer`() {
        val largeur = 1_080f
        assertEquals("Panneau fermé, il ne bouge pas", 0f, ecartDeSortie(largeur, 0f), PRECISION)

        val bande = Rect(left = 0f, top = 200f, right = largeur, bottom = 300f)
        listOf(Cadrage.A_DROITE, Cadrage.AU_CENTRE).forEach { cadrage ->
            val point = pointDeLaPlace(bande, cadrage, TAILLE)!!
            assertTrue(
                "$cadrage — il traîne encore dans le champ",
                point.x + ecartDeSortie(largeur, 1f) >= largeur,
            )
        }

        val avancements = (0..100).map { ecartDeSortie(largeur, it / 100f) }
        assertTrue(
            "La sortie ne revient jamais en arrière",
            avancements.zipWithNext().all { (avant, apres) -> apres >= avant },
        )
    }

    @Test
    fun `accoude, les epaules tombent sur l'arete du bouton`() {
        val bouton = Rect(left = 20f, top = 400f, right = 1060f, bottom = 700f)
        val point = pointDeLaPlace(bouton, Cadrage.EPAULES_AU_BORD, TAILLE)!!

        assertEquals(
            "Les épaules sont exactement sur le bord haut du bouton",
            bouton.top,
            point.y + TAILLE.height * HAUTEUR_EPAULES,
            PRECISION,
        )
        assertEquals("Il est centré sur le bouton", bouton.center.x, point.x + TAILLE.width / 2f, PRECISION)
        assertTrue("La tête dépasse au-dessus du bouton", point.y < bouton.top)
        assertTrue(
            "Les épaules sont dans la moitié haute du dessin : le corps passe derrière",
            HAUTEUR_EPAULES in 0.4f..0.7f,
        )
    }

    // Deux tentatives avaient échoué (16/08/2026 : le vol n'apparaissait pas) faute d'enveloppe pleine assez longtemps.
    // Xavier, 18/08/2026 : accoudé, il ne glisse pas sur le côté en ouvrant un panneau — il repart derrière le bouton.
    @Test
    fun `il quitte la crise en rejouant son entree a l'envers`() {
        assertEquals("Posé, il ne s'enfonce pas", 0f, enfouissementDeLaCrise(entree = 1f, sortie = 0f), PRECISION)
        assertEquals("Caché avant d'entrer", 1f, enfouissementDeLaCrise(entree = 0f, sortie = 0f), PRECISION)
        assertEquals("Caché une fois sorti", 1f, enfouissementDeLaCrise(entree = 1f, sortie = 1f), PRECISION)

        val entrant = (0..100).map { enfouissementDeLaCrise(entree = it / 100f, sortie = 0f) }
        val sortant = (0..100).map { enfouissementDeLaCrise(entree = 1f, sortie = it / 100f) }
        entrant.zip(sortant.reversed()).forEach { (entre, sort) ->
            assertEquals("La sortie est l'entrée à l'envers", entre, sort, PRECISION)
            assertEquals("Les bras aussi", secondeMoitie(1f - entre), secondeMoitie(1f - sort), PRECISION)
        }
        assertTrue("Les bras sont dressés au moment où il disparaît", secondeMoitie(1f - sortant.last()) < PRECISION)
    }

    @Test
    fun `l'enveloppe du vol tient pendant la traversee`() {
        assertEquals("Rien au départ", 0f, enveloppeDuVol(0f), PRECISION)
        assertEquals("Rien à l'arrivée", 0f, enveloppeDuVol(1f), PRECISION)

        val pleine = (0..100).count { enveloppeDuVol(it / 100f) > 0.99f }
        assertTrue("La pose est pleine sur au moins la moitié du transit ($pleine %)", pleine >= 50)

        assertTrue("Elle est déjà franche quand il rentre dans le champ", enveloppeDuVol(0.3f) > 0.9f)
        assertTrue("Et elle s'est relâchée en se posant", enveloppeDuVol(0.95f) < 0.15f)
    }

    @Test
    fun `l'enveloppe du vol est continue`() {
        val pas = (0..1000).map { enveloppeDuVol(it / 1000f) }
        pas.zipWithNext().forEach { (avant, apres) ->
            assertTrue("Saut de ${apres - avant} dans l'enveloppe", kotlin.math.abs(apres - avant) < 0.02f)
        }
        assertTrue("Elle ne sort jamais de ses bornes", pas.all { it in 0f..1f })
    }

    // Xavier, 16/08/2026 : les bras de la crise descendent sur la seconde moitié de la montée, jamais d'un coup.
    @Test
    fun `les bras de la crise descendent sur la seconde moitie de la montee`() {
        assertEquals("Levés à mi-montée encore", 0f, secondeMoitie(0.5f), PRECISION)
        assertEquals("Levés au départ", 0f, secondeMoitie(0f), PRECISION)
        assertEquals("Posés à l'arrivée du corps", 1f, secondeMoitie(1f), PRECISION)
        assertTrue("À mi-descente, à mi-chemin", secondeMoitie(0.75f) in 0.4f..0.6f)

        val course = (0..200).map { secondeMoitie(it / 200f) }
        course.zipWithNext().forEach { (avant, apres) ->
            assertTrue("Les bras remontent : $avant -> $apres", apres >= avant - PRECISION)
            assertTrue("Départ sec dans la descente", apres - avant < 0.02f)
        }
    }

    @Test
    fun `les bras leves de la crise sont a la verticale`() {
        assertEquals(
            "Un quart de tour au-dessus de l'horizontale",
            90f,
            OUVERTURE_BRAS_LEVES - OUVERTURE_HORIZONTALE,
            PRECISION,
        )
        assertEquals(
            "Et la pose tenue reste l'horizontale",
            OUVERTURE_HORIZONTALE,
            Posture.Accoude.reglage().ouvertureBrasGauche,
            PRECISION,
        )
    }

    @Test
    fun `le point ne depend que du cadre`() {
        val cadre = Rect(Offset(10f, 20f), Size(400f, 80f))
        assertEquals(
            Offset(cadre.right - TAILLE.width, cadre.center.y - TAILLE.height / 2f),
            pointDeLaPlace(cadre, Cadrage.A_DROITE, TAILLE),
        )
    }
}
