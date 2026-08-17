package io.allonsy.kokoro.corps

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.math.PI
import kotlin.math.abs
import kotlin.random.Random

private val FICHIER_SVG = File("../../ressources/retenus/kokoro-corps-v2.svg")

private const val PRECISION = 1e-6f

private const val TOUR = 360

private fun phase(degres: Int): Float = degres * 2f * PI.toFloat() / TOUR

private val POSTURES = listOf(
    Posture.Repos,
    Posture.Present,
    Posture.Montre(Cote.GAUCHE),
    Posture.Montre(Cote.DROITE),
    Posture.CoteACote,
    Posture.Retrait,
    Posture.Pensif,
    Posture.Lecture,
    Posture.Notes,
    Posture.Accoude,
    Posture.Sommeil,
)

class CorpsInvariantsTest {

    private val svg: String by lazy {
        assertTrue("SVG introuvable : ${FICHIER_SVG.absolutePath}", FICHIER_SVG.isFile)
        FICHIER_SVG.readText()
    }

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

    // head-out est étiré de façon non uniforme : son contour déclaré à 2 rend à 2,61 (règle SVG).
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

    @Test
    fun `le visage reste symetrique`() {
        assertEquals(OEIL_GAUCHE.y, OEIL_DROIT.y, PRECISION)
        assertTrue("L'œil gauche est à gauche", OEIL_GAUCHE.x < OEIL_DROIT.x)
        assertTrue("La bouche est sous les yeux", BOUCHE.y > OEIL_GAUCHE.y)
    }

    // Aucune forme de bouche tombante n'existe dans le jeu (§3) : ce n'est pas une règle appliquée, elle est absente.
    @Test
    fun `la bouche ne tombe jamais aux commissures`() {
        assertTrue("La bouche arc doit sourire", milieuPlusBas(BOUCHE_ARC))
        assertTrue("Le semi-sourire doit sourire aussi", milieuPlusBas(BOUCHE_SEMI))
        assertTrue("L'œil souriant se bombe vers le haut", !milieuPlusBas(OEIL_ARC_HAUT))
        assertTrue("L'œil au repos se creuse vers le bas", milieuPlusBas(OEIL_ARC_BAS))
        assertNull(
            "Aucun tracé de sourcil n'existe dans le jeu de pièces",
            TRACES.find { it.nom.contains("sourcil") },
        )
    }

    @Test
    fun `le semi sourire est exactement la moitie du sourire`() {
        assertEquals(3.5f, fleche(BOUCHE_ARC), 1e-5f)
        assertEquals(1.75f, fleche(BOUCHE_SEMI), 1e-5f)
        assertEquals(fleche(BOUCHE_ARC) / 2f, fleche(BOUCHE_SEMI), PRECISION)

        val demiSemi = demiLargeur(BOUCHE_SEMI)
        assertTrue("Le semi-sourire est plus large que le sourire", demiSemi > demiLargeur(BOUCHE_ARC))
        assertTrue("Il reste moins large que la bouche neutre", demiSemi < DEMI_BOUCHE)
    }

    @Test
    fun `chaque trace du visage a une silhouette morphable`() {
        TRACES.forEach { trace ->
            assertEquals("Silhouette de ${trace.nom}", POINTS_CONTOUR, trace.contour.points.size)
        }
    }

    @Test
    fun `la silhouette de l'oeil neutre est l'ellipse du dessin`() {
        val points = OEIL_OVALE.contour.points
        assertEquals(RAYON_OEIL_X, points.maxOf { it.x }, 2e-2f)
        assertEquals(-RAYON_OEIL_X, points.minOf { it.x }, 2e-2f)
        assertEquals(RAYON_OEIL_Y, points.maxOf { it.y }, PRECISION)
        assertEquals(-RAYON_OEIL_Y, points.minOf { it.y }, PRECISION)
    }

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

    @Test
    fun `aucune deformation de bouche ne fait tomber les commissures`() {
        val bouches = listOf(BOUCHE_TRAIT, BOUCHE_BARRE, BOUCHE_ARC, BOUCHE_SEMI, BOUCHE_COURTE)
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

    @Test
    fun `la designation ne leve jamais le bras au dessus de l'epaule`() {
        assertEquals(90f, INCLINAISON_REPOS + OUVERTURE_HORIZONTALE, 1e-3f)
    }

    @Test
    fun `aucune posture ne sort de la course des bras`() {
        POSTURES.forEach { posture ->
            val reglage = posture.reglage()
            val geste = if (reglage.ecriture == null) 0f else ECRITURE_AMPLITUDE
            val membres = mapOf("gauche" to reglage.ouvertureBrasGauche, "droit" to reglage.ouvertureBrasDroit)
            membres.forEach membre@{ (cote, ouverture) ->
                if (posture == Posture.Lecture && cote == "gauche") return@membre
                assertTrue(
                    "$posture ($cote) — le bras passe au-dessus de l'épaule : ${ouverture + geste}",
                    ouverture + geste <= OUVERTURE_HORIZONTALE,
                )
                assertTrue(
                    "$posture ($cote) — le bras croise le corps : $ouverture",
                    ouverture >= OUVERTURE_MINIMALE,
                )
            }
        }
        assertEquals(-INCLINAISON_REPOS, OUVERTURE_MINIMALE, 0f)
    }

    // Exception nommée du garde-fou bras : lecture porte la main au menton, geste vers soi, pas un salut (Xavier, 16/08/2026).
    @Test
    fun `la lecture porte la main droite de Kokoro contre son menton`() {
        val lecture = Posture.Lecture.reglage()
        assertEquals(OUVERTURE_MAIN_AU_MENTON, lecture.ouvertureBrasGauche, 0f)
        assertEquals(OUVERTURE_AVANCEE, lecture.ouvertureBrasDroit, 0f)

        val main = rotationAutour(OUVERTURE_MAIN_AU_MENTON, EPAULE_GAUCHE).applique(BOUT_DU_BRAS)
        assertTrue("La main monte sur le visage : ${main.y}", main.y > BOUCHE.y)
        assertTrue("La main descend sous le menton : ${main.y}", main.y < BAS_DE_LA_TETE)
        assertTrue("La main s'écarte du visage : ${main.x}", abs(main.x - AXE) < 10f)
    }

    @Test
    fun `les postures immobiles disent ce que le document leur fait dire`() {
        assertEquals(Expression.SEREIN, Posture.Pensif.reglage().expression)
        assertEquals(OUVERTURE_REPOS, Posture.Pensif.reglage().ouvertureBrasGauche, 0f)
        assertTrue("Le pensif regarde la liste", Posture.Pensif.reglage().abaissement > 0f)

        assertTrue("La lecture avance les bras", Posture.Lecture.reglage().ouvertureBrasGauche < 0f)
        assertTrue("La lecture baisse les yeux", Posture.Lecture.reglage().abaissement > 0f)

        val accoude = Posture.Accoude.reglage()
        assertTrue("Accoudé, il montre un visage : le panneau reste allumé", accoude.panneauAllume)
        assertEquals("Un visage bienveillant, pas une bouche neutre", Expression.SEREIN, accoude.expression)

        val sommeil = Posture.Sommeil.reglage()
        assertEquals("Le sommeil réutilise veille", Expression.VEILLE, sommeil.expression)
        assertTrue("Des yeux fermés se voient : le panneau reste allumé", sommeil.panneauAllume)
        assertTrue(
            "Le geste de désignation reste neutre : c'est la pose empruntée qui bouge les bras",
            sommeil.ouvertureBrasGauche == OUVERTURE_REPOS,
        )
        assertTrue("Le sommeil demande sa pose empruntée", sommeil.sommeil)
    }

    @Test
    fun `le geste d'ecriture est intermittent et borne`() {
        val notes = Posture.Notes.reglage()
        assertEquals(Cote.GAUCHE, notes.ecriture)
        assertTrue("Les yeux sont baissés vers le bras", notes.abaissement > 0f)
        assertTrue("Le regard va du côté du bras qui écrit", notes.regard < 0f)
        assertEquals(
            "Seule la posture notes écrit",
            listOf<Posture>(Posture.Notes),
            POSTURES.filter { it.reglage().ecriture != null },
        )

        assertTrue(
            "L'arrêt doit durer plus longtemps que le geste",
            ECRITURE_ARRET_MIN_MILLIS > ECRITURE_GESTE_MILLIS,
        )
        assertEquals(8, ECRITURE_ALLERS_RETOURS)

        val alea = Random(20260815)
        var precedent = 0L
        repeat(500) {
            val arret = attenteEcriture(precedent, alea)
            assertTrue(
                "Arrêt hors bornes : $arret",
                arret >= ECRITURE_ARRET_MIN_MILLIS && arret < ECRITURE_ARRET_MAX_MILLIS,
            )
            assertTrue("Deux arrêts égaux de suite font un rythme", arret != precedent)
            precedent = arret
        }
    }

    // Dérogation Xavier 16/08/2026 (CORPS.md §2/§9) : seule accoude incline la tête, à 6° max, pivot = ligne des épaules.
    @Test
    fun `seule la posture accoude penche la tete, et elle reste bornee`() {
        assertEquals(
            "Une seule posture incline la tête",
            listOf<Posture>(Posture.Accoude),
            POSTURES.filter { it.reglage().inclinaisonTete != 0f },
        )
        POSTURES.forEach { posture ->
            val angle = posture.reglage().inclinaisonTete
            assertTrue(
                "$posture — la tête penche trop : $angle",
                kotlin.math.abs(angle) <= INCLINAISON_TETE_MAX,
            )
        }
        assertTrue("Elle penche vers la gauche de l'écran", INCLINAISON_TETE < 0f)

        assertEquals("Le pivot de la tête est sur l'axe", AXE, PIVOT_TETE.x, PRECISION)
        assertEquals(
            "Le pivot de la tête est la ligne des épaules",
            EPAULE_GAUCHE.y,
            PIVOT_TETE.y,
            PRECISION,
        )
        assertEquals("Et les deux épaules y sont", EPAULE_DROITE.y, PIVOT_TETE.y, 1e-3f)
        assertEquals(
            "Le rig au repos ne penche pas la tête",
            0f,
            RigKokoro.pose(Posture.Repos).inclinaisonTete,
            0f,
        )
    }

    // Bras levés = état de passage uniquement (Xavier, 16/08/2026) ; au repos, toujours l'horizontale (garde-fou §6).
    @Test
    fun `les bras leves de la crise ne sont qu'un passage`() {
        assertTrue(
            "Les bras doivent partir au-dessus de l'horizontale",
            OUVERTURE_BRAS_LEVES > OUVERTURE_HORIZONTALE,
        )
        assertEquals(
            "La pose tenue reste l'horizontale, pas la levée",
            OUVERTURE_HORIZONTALE,
            Posture.Accoude.reglage().ouvertureBrasGauche,
            0f,
        )
    }

    @Test
    fun `accoude pose les deux bras sur la ligne des epaules`() {
        val accoude = Posture.Accoude.reglage()
        assertEquals(OUVERTURE_HORIZONTALE, accoude.ouvertureBrasGauche, 0f)
        assertEquals("Les deux bras, symétriquement", accoude.ouvertureBrasGauche, accoude.ouvertureBrasDroit, 0f)
        assertNull("Rien ne bouge : il veille, il n'écrit pas", accoude.ecriture)
        assertEquals("Il regarde devant lui, il ne suit personne", 0f, accoude.regard, 0f)
        assertEquals("Il ne baisse pas les yeux non plus", 0f, accoude.abaissement, 0f)
        assertEquals("Il n'est pas réduit", 1f, accoude.echelle, 0f)
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

    @Test
    fun `le repos porte le semi sourire`() {
        assertEquals(Expression.SEREIN, Posture.Repos.reglage().expression)
        assertEquals(BOUCHE_SEMI, Expression.SEREIN.bouche)
        assertTrue(
            "Une posture retombe encore sur neutre",
            POSTURES.none { it.reglage().expression == Expression.NEUTRE },
        )
    }

    @Test
    fun `le regard ne se decale que vers ce que les bras font`() {
        POSTURES.forEach { posture ->
            val reglage = posture.reglage()
            assertEquals(
                "Le regard de $posture",
                posture is Posture.Montre || reglage.ecriture != null,
                reglage.regard != 0f,
            )
        }
        assertEquals(REGARD_DESIGNATION, Posture.Montre(Cote.DROITE).reglage().regard, 0f)
    }

    // Bornes anti-papillonnement/anti-événement ; deux intervalles égaux créeraient un rythme perceptible.
    @Test
    fun `deux attentes de clignement ne sont jamais egales`() {
        assertEquals(2_800L, CLIGNEMENT_ATTENTE_MIN_MILLIS)
        assertEquals(6_500L, CLIGNEMENT_ATTENTE_MAX_MILLIS)

        val alea = Random(20260815)
        var precedente = 0L
        repeat(500) {
            val attente = attenteClignement(precedente, alea)
            assertTrue(
                "Attente hors bornes : $attente",
                attente >= CLIGNEMENT_ATTENTE_MIN_MILLIS && attente < CLIGNEMENT_ATTENTE_MAX_MILLIS,
            )
            assertTrue("Deux attentes égales de suite font un rythme", attente != precedente)
            precedente = attente
        }
    }

    @Test
    fun `le clignement ne touche pas la bouche`() {
        val clignant = Visage.de(Expression.SEREIN)
            .copy(oeil = Morphing(OEIL_OVALE, OEIL_TRAIT, 0.5f))
        assertTrue("Les yeux se déforment", !clignant.oeil.stable)
        assertTrue("La bouche reste posée", clignant.bouche.stable)
        assertEquals(BOUCHE_SEMI, clignant.bouche.vers)
    }

    @Test
    fun `le balayage lit lentement et revient vite`() {
        val balayage = Balayage()
        assertEquals(3_000, balayage.ligneMillis)
        assertTrue("Le retour doit rester bref", balayage.retourMillis <= 200)
        assertTrue("Le retour ne dépasse jamais la ligne", balayage.retourMillis < balayage.ligneMillis)
        assertTrue("Aucun mouvement continu : le regard s'arrête", balayage.pauseMillis > 0)
        assertTrue(
            "Le balayage ne décale pas les yeux plus loin que la désignation",
            balayage.amplitude > 0f && balayage.amplitude <= REGARD_DESIGNATION,
        )
    }

    @Test
    fun `le rig au repos ne bouge rien`() {
        val repos = RigKokoro.pose(Posture.Repos)
        assertEquals(0f, repos.rotationBrasGauche, 0f)
        assertEquals(0f, repos.rotationBrasDroit, 0f)
        assertEquals(0f, repos.rotationPiedGauche, 0f)
        assertEquals(0f, repos.rotationPiedDroit, 0f)
        assertEquals(0f, repos.regard, 0f)
        assertEquals(0f, repos.abaissement, 0f)
        assertEquals(1f, repos.echelle, 0f)
        assertEquals(1f, repos.etirementCorps, 0f)
    }

    @Test
    fun `les epaules sont symetriques autour de l'axe`() {
        assertEquals(AXE, (EPAULE_GAUCHE.x + EPAULE_DROITE.x) / 2f, 1e-3f)
        assertEquals(EPAULE_GAUCHE.y, EPAULE_DROITE.y, 1e-3f)
        assertTrue("L'épaule est au-dessus du ventre", EPAULE_GAUCHE.y < CENTRE_VENTRE.y)
    }

    @Test
    fun `le centre du ventre est le pivot ecrit dans le dessin`() {
        val ventre = Ancre(CENTRE_VENTRE.x - RACINE.e, CENTRE_VENTRE.y - RACINE.f)
        val image = PIED_GAUCHE.transformation.applique(ventre)
        assertEquals("Le pied tourne autour d'un autre point", ventre.x, image.x, 1e-3f)
        assertEquals("Le pied tourne autour d'un autre point", ventre.y, image.y, 1e-3f)
        assertEquals("Le ventre est sur l'axe", AXE, CENTRE_VENTRE.x, 0.2f)
    }

    // Xavier 16/08/2026 : la respiration ne rétracte plus en largeur, uniquement vers le haut.
    @Test
    fun `la respiration reste dans l'amplitude annoncee`() {
        val expiration = RigKokoro(visage = Visage.de(Expression.NEUTRE), respiration = 0f)
        val inspiration = RigKokoro(visage = Visage.de(Expression.NEUTRE), respiration = 1f)
        assertEquals(1f, expiration.etirementCorps, 0f)
        assertEquals(1f + AMPLITUDE_HAUTEUR, inspiration.etirementCorps, 1e-6f)
        assertEquals(0f, expiration.decalageRespirationHaut, 0f)
        assertTrue(
            "La tête et les bras montent quand le ventre grossit",
            inspiration.decalageRespirationHaut < 0f,
        )
    }

    // Vol et respiration doivent rester en quadrature : deux rythmes désynchronisés créeraient un battement perceptible.
    @Test
    fun `la levitation bat sur l'horloge de la respiration`() {
        assertEquals(0.09f * HAUTEUR_PERSONNAGE, LEVITATION_AMPLITUDE, PRECISION)
        assertEquals(PI.toFloat() / 2f, LEVITATION_DEPHASAGE, PRECISION)

        val hauteurs = (0..TOUR).map { levitation(phase(it)) }
        assertEquals("Le cycle se referme", levitation(0f), levitation(phase(TOUR)), 1e-5f)
        assertEquals("Le bas du cycle est la pose dessinée", 0f, hauteurs.max(), 1e-4f)
        assertEquals("Le haut du cycle monte de 3 %", -LEVITATION_AMPLITUDE, hauteurs.min(), 1e-4f)

        val sommetDuSouffle = PI.toFloat() / 2f
        assertEquals("Le souffle culmine ici", 1f, souffle(sommetDuSouffle), 1e-5f)
        assertEquals(
            "Le vol y est à mi-course, pas au sommet — c'est le quart de période",
            -LEVITATION_AMPLITUDE / 2f,
            levitation(sommetDuSouffle),
            1e-4f,
        )
    }

    // Ralentir par division de phase (pas une 2e horloge) : sinon saccade toutes les 9 s, interdite par les hypersensibilités.
    @Test
    fun `le sommeil ralentit le vol de moitie sans seconde horloge`() {
        assertEquals("Un tour d'horloge vaut deux respirations", 2 * RESPIRATION_MILLIS, HORLOGE_MILLIS)
        assertEquals(4f * PI.toFloat(), TOUR_HORLOGE, PRECISION)

        val lentes = (0..2 * TOUR).map { levitationLente(phase(it)) }
        assertEquals(
            "Le vol du sommeil se referme sur le tour d'horloge",
            levitationLente(0f),
            levitationLente(TOUR_HORLOGE),
            1e-5f,
        )
        assertEquals("Il monte deux fois moins haut", -LEVITATION_AMPLITUDE / 2f, lentes.min(), 1e-4f)
        assertEquals("Le bas du cycle reste la pose dessinée", 0f, lentes.max(), 1e-4f)

        val demiTour = TOUR_HORLOGE / 2f
        assertEquals(
            "Le vol éveillé est revenu chez lui après une respiration",
            levitation(0f),
            levitation(demiTour),
            1e-5f,
        )
        assertTrue(
            "Le sommeil, lui, n'en est qu'à la moitié de son cycle — c'est ça, la demi-vitesse",
            levitationLente(demiTour) - levitationLente(0f) > LEVITATION_AMPLITUDE / 4f,
        )

        assertEquals(
            "Le sommeil vole",
            Offset(0f, levitationLente(1f)),
            Vol.SOMMEIL.deplacement(1f, avance = 1f).decalage,
        )
        assertEquals("Le sommeil ne bascule pas", 0f, Vol.SOMMEIL.deplacement(1f, 1f).inclinaison, 0f)
        assertEquals("Le sommeil porte son ombre", Ombre(), Vol.SOMMEIL.ombre())
    }

    // La v1 avait une dérive latérale sur 3 périodes qui battaient entre elles ; la lévitation doit rester purement verticale.
    @Test
    fun `la levitation ne fait que monter et descendre`() {
        (0..TOUR).forEach {
            val deplacement = Vol.LEVITATION.deplacement(phase(it), avance = 0f)
            assertEquals("Aucune dérive latérale", 0f, deplacement.decalage.x, 0f)
            assertEquals("Aucune bascule", 0f, deplacement.inclinaison, 0f)
            assertEquals("La hauteur est celle de l'horloge", levitation(phase(it)), deplacement.decalage.y, 0f)
        }
        assertEquals("Ce qui ne vole pas ne bouge pas", Offset.Zero, Vol.AUCUN.deplacement(1f, 1f).decalage)
        assertEquals(0f, Vol.AUCUN.deplacement(1f, 1f).inclinaison, 0f)
    }

    // Xavier 16/08/2026 : l'ombre ne touche plus les pieds ; son opacité ne dépend que de la hauteur de vol.
    @Test
    fun `l'ombre est posee un peu sous le sol et ne dit rien d'autre que la hauteur`() {
        val ombre = Ombre()
        assertEquals("L'ombre est posée sous le sol du dessin", BAS_PIEDS + DECALAGE_SOL_OMBRE, ombre.sol, 0f)
        assertTrue("Elle ne touche plus les pieds", ombre.sol > BAS_PIEDS)
        assertEquals(
            "L'empreinte au sol est celle des épaules",
            (EPAULE_DROITE.x - EPAULE_GAUCHE.x) / 2f,
            ombre.demiLargeur,
            PRECISION,
        )
        assertTrue("L'ombre est très aplatie", ombre.aplatissement < 0.25f)
        assertTrue(
            "L'ombre déborde de la vue",
            ombre.sol + ombre.demiLargeur * ombre.aplatissement <= HAUTEUR_VUE,
        )
        assertTrue(
            "Posé, il est le plus sombre ; loin, le plus transparent",
            ombre.opaciteA(0f) > ombre.opaciteA(-LEVITATION_AMPLITUDE),
        )
        assertTrue(
            "L'opacité reste discrète aux deux bouts",
            ombre.opaciteA(0f) < 0.3f && ombre.opaciteA(-LEVITATION_AMPLITUDE) > 0f,
        )

        assertNull("Ce qui ne vole pas n'a pas d'ombre", Vol.AUCUN.ombre())
        assertEquals("Le vol porte son ombre", Ombre(), Vol.LEVITATION.ombre())
        assertEquals("La traversée aussi", Ombre(), Vol.TRAVERSEE.ombre())
    }

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

    private fun milieuPlusBas(trace: Trace): Boolean = fleche(trace) > 0f

    private fun fleche(trace: Trace): Float {
        val arc = arc(trace)
        return 0.25f * arc.y1 + 0.5f * arc.cy + 0.25f * arc.y2 - arc.y1
    }

    private fun demiLargeur(trace: Trace): Float = arc(trace).x2

    private fun arc(trace: Trace): Forme.Arc {
        val arc = checkNotNull(trace.forme as? Forme.Arc) { "Tracé sans arc : ${trace.nom}" }
        assertEquals("L'arc doit être symétrique", arc.y1, arc.y2, PRECISION)
        assertEquals("L'arc doit être symétrique", -arc.x1, arc.x2, PRECISION)
        return arc
    }
}
