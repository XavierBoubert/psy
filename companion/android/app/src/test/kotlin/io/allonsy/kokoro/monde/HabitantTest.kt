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

/** Une taille d'habitant quelconque : la géométrie ne dépend pas de la sienne en particulier. */
private val TAILLE = Size(72f, 60f)

private const val PRECISION = 1e-4f

/**
 * L'habitant — `PRESENCE.md` §2 et étapes **E9 → E11**.
 *
 * ⭐ **Tout ce qui décide ici est une fonction pure** : la place, le point où il se pose, l'arc du
 * transit. C'est ce qui rend le comportement vérifiable **sans écran** — et donc ce qui permet
 * d'affirmer qu'aucun écart n'existe entre un check-in fait et un check-in non fait, au lieu de
 * l'espérer.
 */
class HabitantTest {

    private fun sejour(
        heure: Int = 9,
        checkinFait: Boolean = false,
        vides: Set<Ecran> = emptySet(),
    ) = Sejour(heure = heure, checkinFait = checkinFait, vides = vides)

    /**
     * 🔴 **La dérogation de l'écran de crise, et ses quatre bornes** — `CORPS.md` §10, **E13
     * arbitrée par Xavier le 16/08/2026** après le refus de la supervision du 15/08.
     *
     * ⭐ **Ce test a changé de nature, il n'a pas disparu.** Il verrouillait l'**absence** de
     * personnage tant que la supervision n'avait pas eu lieu ; il verrouille maintenant **ce que la
     * dérogation admet, et rien de plus**. Ce qui la borne se vérifie ici : le panneau est allumé
     * *(c'est un visage qu'on veut, pas une présence muette)*, il ne vole pas *(il est accoudé, et
     * une ombre tomberait sur l'interface)*, et **rien de tout ça ne dépend de l'heure ni du
     * check-in** — l'écran de crise ne réagit à aucun état du dossier.
     */
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

    /**
     * 🔴 **Il ne s'endort jamais sur l'écran de crise.** Il n'y a pas de liste à cet écran, et
     * **veiller est précisément ce qu'il y fait** : des Zzz par-dessus le bouton *Mot code* seraient
     * le contresens le plus coûteux du dispositif.
     */
    @Test
    fun `la crise ne l'endort jamais`() {
        val toutVide = place(Ecran.CRISE, sejour(vides = Ecran.entries.toSet()))
        assertEquals(Posture.Accoude, toutVide?.posture)
        assertEquals("La crise n'est pas une liste", place(Ecran.CRISE, sejour()), toutVide)
        assertTrue("La crise n'est jamais comptée vide", Ecran.CRISE !in ECRANS_VIDES)
    }

    /**
     * ⭐ La bascule de 18 h (§2) — **avant, il est pensif devant la liste ; après, il montre le
     * check-in.** Le seuil est net : rien ne se déclenche à 17 h 59.
     */
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

    /**
     * 🔴 **Le garde-fou du §4.4, en toutes lettres.** `chaleureux` réagit à un fait accompli et n'a
     * pas de contraire : quand le check-in n'est pas fait, **la posture, la place, le geste et le
     * balayage sont identiques au caractère près**, et seule l'expression reste celle de tous les
     * jours. Comparer les deux places champ à champ est la seule façon d'affirmer qu'aucun autre
     * écart n'est observable — un reproche se glisse dans un détail, pas dans une déclaration.
     */
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

    /**
     * ⭐ Les deux écrans qui n'ont rien (§2) : **il dort, et sa place ne bouge pas.** Un personnage
     * qui se déplacerait parce qu'une liste est vide dirait quelque chose de la liste.
     */
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

    /**
     * ⭐ **C'est l'état d'aujourd'hui** : la bibliothèque et le bilan sont vides tant que **K5** n'a
     * pas branché la lecture du dossier, donc les deux montrent le sommeil — et leur texte reste.
     */
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

    /**
     * ⭐ **Ce qui n'est pas endormi lévite** — et donc porte une ombre. Rien dans le monde ne se pose
     * au sol (§1.3).
     *
     * 🔴 **Une seule exception, et elle est nommée : l'écran de crise.** Il n'y est pas *posé*, il y
     * est **accoudé** — ses bras reposent sur le bouton. Le faire léviter ferait glisser ses bras le
     * long de l'arête, et son ombre tomberait sur l'interface. **L'exception est écrite ici pour
     * qu'une seconde ne puisse pas se glisser à côté sans se voir.**
     */
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

    /**
     * ⭐ **Le cadre est la bande, pas la pancarte** : posé à droite, il touche le bord droit du
     * contenu ; posé au centre, il est centré dessus. Dans les deux cas il est **centré en
     * hauteur**, donc la bande n'a besoin que d'être assez haute pour lui.
     */
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

    /**
     * ⭐ **Une bande sortie du champ garde des coordonnées qui comptent.** C'est ce qui laisse
     * l'habitant sortir avec sa liste au lieu de se coincer contre le bord de la dalle : la place
     * suit le cadre, aussi loin qu'il aille.
     */
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

    /**
     * 🔴 **L'arc du transit est nul aux deux bouts et n'a qu'un sommet** : il monte, il redescend,
     * et il ne change jamais de direction net (§3). Un arc qui ne retomberait pas exactement à zéro
     * poserait l'habitant plus haut à chaque traversée.
     */
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

    /**
     * 🔴 **E12 — il quitte le champ, il ne s'efface pas** (§1.1 et §4.2). La sortie est latérale et
     * vaut une largeur de dalle : **d'où qu'il parte, il est dehors à l'arrivée**, et il n'a pas
     * bougé d'un pixel au départ.
     */
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

    /**
     * ⭐ **Accoudé : la ligne des épaules tombe sur l'arête du bouton, au pixel.** C'est ce qui pose
     * les bras — tenus à l'horizontale par la posture — **sur** le bord, et met tout le reste du
     * corps **derrière**. 🔴 **Rien n'est réglé à l'œil** : la hauteur des épaules est une ancre du
     * dessin, et le cadre est le bouton lui-même.
     */
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

    /**
     * 🔴 **L'enveloppe du vol tient, elle ne fait pas que passer** — c'est ce qui a fait échouer les
     * deux premières tentatives *(16/08/2026 : « les animations de vol n'apparaissent pas du tout »)*.
     *
     * ⭐ **Le pic d'un demi-sinus ne dure rien**, et pendant ce rien Kokoro traverse l'écran à plus de
     * 2 000 px/s — quand il n'est pas encore rentré dans le champ. **Ce test dit la seule chose qui
     * compte : la pose est pleine pendant au moins la moitié du transit**, donc elle est vue.
     */
    @Test
    fun `l'enveloppe du vol tient pendant la traversee`() {
        assertEquals("Rien au départ", 0f, enveloppeDuVol(0f), PRECISION)
        assertEquals("Rien à l'arrivée", 0f, enveloppeDuVol(1f), PRECISION)

        val pleine = (0..100).count { enveloppeDuVol(it / 100f) > 0.99f }
        assertTrue("La pose est pleine sur au moins la moitié du transit ($pleine %)", pleine >= 50)

        assertTrue("Elle est déjà franche quand il rentre dans le champ", enveloppeDuVol(0.3f) > 0.9f)
        assertTrue("Et elle s'est relâchée en se posant", enveloppeDuVol(0.95f) < 0.15f)
    }

    /**
     * 🔴 **Elle ne saute nulle part, et elle ne fait pas de marche** — ni au démarrage, ni à la
     * bascule du plateau, ni à l'atterrissage (`CORPS.md` §5 : aucune animation brusque).
     */
    @Test
    fun `l'enveloppe du vol est continue`() {
        val pas = (0..1000).map { enveloppeDuVol(it / 1000f) }
        pas.zipWithNext().forEach { (avant, apres) ->
            assertTrue("Saut de ${apres - avant} dans l'enveloppe", kotlin.math.abs(apres - avant) < 0.02f)
        }
        assertTrue("Elle ne sort jamais de ses bornes", pas.all { it in 0f..1f })
    }

    /**
     * ⭐ **Les bras de la crise descendent sur la seconde moitié de la montée, et finissent avec
     * elle** *(demande de Xavier, 16/08/2026)*. 🔴 Le geste ne remonte jamais et ne démarre pas d'un
     * coup — sa dérivée s'annule aux deux bouts.
     */
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

    /**
     * 🔴 **Les bras levés sont à la verticale, et c'est de la géométrie, pas un goût** : le bouton
     * cache tout ce qui passe sous son arête et les bras pivotent aux épaules, qui n'y arrivent qu'à
     * la fin de la montée. Plus bas que la verticale, l'affaissement se joue **derrière le bouton**.
     */
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

    /** La géométrie est une translation pure : aucun décalage caché ne se glisse dedans. */
    @Test
    fun `le point ne depend que du cadre`() {
        val cadre = Rect(Offset(10f, 20f), Size(400f, 80f))
        assertEquals(
            Offset(cadre.right - TAILLE.width, cadre.center.y - TAILLE.height / 2f),
            pointDeLaPlace(cadre, Cadrage.A_DROITE, TAILLE),
        )
    }
}
