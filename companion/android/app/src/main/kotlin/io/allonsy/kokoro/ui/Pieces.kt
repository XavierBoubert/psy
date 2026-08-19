package io.allonsy.kokoro.ui

import android.os.SystemClock
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.corps.Expression
import io.allonsy.kokoro.corps.LARGEUR_BANDE_LOCUTEUR
import io.allonsy.kokoro.corps.Locuteur
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

// Tween linéaire uniquement, jamais spring : aucun rebond ni dépassement (hypersensibilités).
private const val ENFONCEMENT_MS = 90

// Sans ce plancher, clickable dans une surface qui défile retarde l'appui de 100 ms et masque une frappe brève.
private const val APPUI_MINIMUM_MS = ENFONCEMENT_MS.toLong()

private val CRAN = 18.dp
private val EPAISSEUR_RUBAN = 6.dp
private val PADDING_PANNEAU = PaddingValues(horizontal = 22.dp, vertical = 20.dp)

@Composable
fun PanneauExtrude(
    modifier: Modifier = Modifier,
    couleur: Teinte? = null,
    rayon: Dp = RAYON,
    epaisseur: Dp = EPAISSEUR,
    ombre: Boolean = true,
    contenuPadding: PaddingValues = PADDING_PANNEAU,
    arrangement: Arrangement.Vertical = Arrangement.Top,
    onClic: (() -> Unit)? = null,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    val interactions = remember { MutableInteractionSource() }
    val appuye by appuiTenu(interactions)
    val descente by animateDpAsState(
        targetValue = if (appuye && onClic != null) epaisseur else 0.dp,
        animationSpec = tween(durationMillis = ENFONCEMENT_MS, easing = LinearOutSlowInEasing),
        label = "enfoncement",
    )

    Column(
        modifier = modifier
            .then(
                if (onClic == null) Modifier
                else Modifier.clickable(
                    interactionSource = interactions,
                    indication = null,
                    onClick = onClic,
                ),
            )
            .matiere(
                palette = palette,
                couleur = couleur,
                rayon = rayon,
                epaisseur = epaisseur,
                enfoncement = descente,
                ombre = ombre,
            )
            .padding(bottom = epaisseur)
            .offset { IntOffset(x = 0, y = descente.roundToPx()) }
            .padding(contenuPadding),
        verticalArrangement = arrangement,
        content = contenu,
    )
}

@Composable
private fun appuiTenu(interactions: InteractionSource): State<Boolean> {
    val appuye = remember { mutableStateOf(false) }

    LaunchedEffect(interactions) {
        var depuis = 0L
        interactions.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    depuis = SystemClock.uptimeMillis()
                    appuye.value = true
                }

                is PressInteraction.Release -> {
                    delay(APPUI_MINIMUM_MS - (SystemClock.uptimeMillis() - depuis))
                    appuye.value = false
                }

                is PressInteraction.Cancel -> appuye.value = false
                else -> Unit
            }
        }
    }

    return appuye
}

@Composable
fun Carte(
    titre: String,
    modifier: Modifier = Modifier,
    duree: String? = null,
    // Une étape faite reste lisible et ouvrable : elle s'efface, elle ne se coche pas et ne se compte nulle part.
    faite: Boolean = false,
    picto: (@Composable () -> Unit)? = null,
    onClic: () -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    PanneauExtrude(modifier = modifier.fillMaxWidth(), onClic = onClic, ombre = !faite) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titre,
                    style = TypoKokoro.corps,
                    color = if (faite) palette.encreDouce else palette.encre,
                )
                if (duree != null) {
                    Text(
                        text = duree,
                        style = TypoKokoro.discret,
                        color = palette.encreDouce,
                        modifier = Modifier.padding(top = 5.dp),
                    )
                }
            }
            if (picto != null) {
                Box(modifier = Modifier.padding(start = 16.dp)) { picto() }
            }
        }
    }
}

// Le seul pictogramme du lot avec la roue dentée : il porte seul l'annonce de la sortie de l'app.
@Composable
fun PictoDehors(modifier: Modifier = Modifier, taille: Dp = 22.dp) {
    val couleur = LocalPaletteKokoro.current.encreDouce
    Canvas(modifier = modifier.size(taille)) { dehors(couleur, 2.4.dp.toPx()) }
}

private fun DrawScope.dehors(couleur: Color, trait: Float) {
    val cote = size.minDimension
    val trace = Stroke(width = trait, cap = StrokeCap.Round, join = StrokeJoin.Round)

    val cadre = Path().apply {
        moveTo(cote * 0.62f, cote * 0.58f)
        lineTo(cote * 0.62f, cote * 0.94f)
        lineTo(cote * 0.06f, cote * 0.94f)
        lineTo(cote * 0.06f, cote * 0.38f)
        lineTo(cote * 0.42f, cote * 0.38f)
    }
    drawPath(cadre, couleur, style = trace)

    val fleche = Path().apply {
        moveTo(cote * 0.40f, cote * 0.60f)
        lineTo(cote * 0.94f, cote * 0.06f)
        moveTo(cote * 0.58f, cote * 0.06f)
        lineTo(cote * 0.94f, cote * 0.06f)
        lineTo(cote * 0.94f, cote * 0.42f)
    }
    drawPath(fleche, couleur, style = trace)
}

@Composable
fun BoutonEpais(
    libelle: String,
    onClic: () -> Unit,
    modifier: Modifier = Modifier,
    couleur: Teinte? = null,
    actif: Boolean = true,
    hauteurMinimale: Dp = 66.dp,
    style: TextStyle = TypoKokoro.bouton,
) {
    val palette = LocalPaletteKokoro.current
    PanneauExtrude(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = hauteurMinimale + EPAISSEUR),
        couleur = couleur,
        contenuPadding = PaddingValues(horizontal = 22.dp, vertical = 18.dp),
        arrangement = Arrangement.Center,
        onClic = if (actif) onClic else null,
    ) {
        Text(
            text = libelle,
            style = when {
                !actif -> style.copy(color = palette.encreDouce)
                couleur == null -> style.copy(color = palette.encre)
                else -> grave(style, opacite = 0.20f)
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
fun Pancarte(texte: String, couleur: Teinte, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Box(
        modifier = modifier
            .matiere(
                palette = palette,
                couleur = couleur,
                rayon = 999.dp,
                epaisseur = 5.dp,
                ombre = false,
                epaisseurReflet = 3.dp,
                epaisseurCreux = 0.dp,
            )
            .padding(bottom = 5.dp)
            .padding(horizontal = 22.dp, vertical = 10.dp),
    ) {
        Text(text = texte, style = grave(TypoKokoro.pancarte, opacite = 0.20f))
    }
}

@Composable
fun Ruban(texte: String, couleur: Teinte, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Box(
        modifier = modifier
            .drawBehind {
                val ep = EPAISSEUR_RUBAN.toPx()
                val cran = CRAN.toPx()
                val corps = size.copy(height = size.height - ep)
                if (corps.height <= 0f) return@drawBehind
                val forme = cheminRuban(corps, cran)

                translate(top = ep) { drawPath(forme, palette.contour) }
                drawPath(
                    path = forme,
                    color = palette.contour,
                    style = Stroke(width = EPAISSEUR_RUBAN.toPx(), join = StrokeJoin.Round),
                )
                drawPath(
                    path = forme,
                    brush = Brush.verticalGradient(
                        colors = listOf(couleur.haut, couleur.bas),
                        startY = 0f,
                        endY = corps.height,
                    ),
                )
            }
            .padding(bottom = EPAISSEUR_RUBAN)
            .padding(horizontal = 40.dp, vertical = 13.dp),
    ) {
        Text(text = texte, style = grave(TypoKokoro.ruban))
    }
}

@Composable
fun BandeTitre(
    titre: String,
    couleur: Teinte,
    modifier: Modifier = Modifier,
    onFermer: (() -> Unit)? = null,
    onReglages: (() -> Unit)? = null,
) {
    val reserve = if (onFermer == null && onReglages == null) 0.dp else RESERVE_CROIX
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Ruban(
            texte = titre,
            couleur = couleur,
            modifier = Modifier.padding(horizontal = reserve),
        )
        val fin = Modifier.align(Alignment.CenterEnd)
        when {
            onFermer != null -> Croix(onFermer = onFermer, modifier = fin)
            onReglages != null -> RoueDentee(onClic = onReglages, modifier = fin)
            else -> Unit
        }
    }
}

@Composable
fun Croix(onFermer: () -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    val interactions = remember { MutableInteractionSource() }
    val nom = stringResource(R.string.action_fermer)
    Box(
        modifier = modifier
            .semantics { contentDescription = nom }
            .clickable(interactionSource = interactions, indication = null, onClick = onFermer)
            .matiere(
                palette = palette,
                rayon = 999.dp,
                epaisseur = 5.dp,
                epaisseurReflet = 3.dp,
                epaisseurCreux = 0.dp,
            )
            .padding(bottom = 5.dp)
            .size(TAILLE_CROIX),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(16.dp)) { croix(palette.encre, 3.dp.toPx()) }
    }
}

private fun DrawScope.croix(couleur: Color, trait: Float) {
    val marge = trait / 2f
    val bas = size.height - marge
    val droite = size.width - marge
    drawLine(couleur, Offset(marge, marge), Offset(droite, bas), trait, StrokeCap.Round)
    drawLine(couleur, Offset(droite, marge), Offset(marge, bas), trait, StrokeCap.Round)
}

private val TAILLE_CROIX = 44.dp
private val RESERVE_CROIX = TAILLE_CROIX + 4.dp

@Composable
fun RoueDentee(onClic: () -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    val interactions = remember { MutableInteractionSource() }
    val nom = stringResource(R.string.monde_reglages)
    Box(
        modifier = modifier
            .semantics { contentDescription = nom }
            .clickable(interactionSource = interactions, indication = null, onClick = onClic)
            .matiere(
                palette = palette,
                rayon = 999.dp,
                epaisseur = 5.dp,
                epaisseurReflet = 3.dp,
                epaisseurCreux = 0.dp,
            )
            .padding(bottom = 5.dp)
            .size(TAILLE_CROIX),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(22.dp)) { rouage(palette.encre, 2.4.dp.toPx()) }
    }
}

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

@Composable
fun CadreVide(texte: String, modifier: Modifier = Modifier, ornements: Boolean = true) {
    val palette = LocalPaletteKokoro.current
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 10.dp)
                .matiere(palette = palette, creuse = true)
                .padding(horizontal = 26.dp, vertical = 40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = texte,
                style = TypoKokoro.vide,
                color = palette.encreDouce,
                textAlign = TextAlign.Center,
            )
        }
        if (ornements) {
            Etincelle(Modifier.align(Alignment.TopStart).offset(x = 22.dp, y = 2.dp))
            Etincelle(Modifier.align(Alignment.BottomEnd).offset(x = (-30).dp, y = (-2).dp), taille = 12.dp)
            Coeur(Modifier.align(Alignment.TopEnd).offset(x = (-24).dp))
        }
    }
}

@Composable
fun PileDeBoutons(
    modifier: Modifier = Modifier,
    ecart: Dp = 18.dp,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(ecart), content = contenu)
}

private val MARGE_PANNEAU = 20.dp
private val BULLE_RAYON = 24.dp
private val BULLE_QUEUE_LARGEUR = 34.dp
private val BULLE_QUEUE_HAUTEUR = 20.dp
private val BULLE_QUEUE_MORSURE = 6.dp

private const val BULLE_QUEUE_POINTE = 0.42f

// Le locuteur est centré dans sa bande : la pointe tombe donc sur l'axe du personnage sans dépendre de sa taille.
private val BULLE_QUEUE_DECALAGE =
    LARGEUR_BANDE_LOCUTEUR / 2f - MARGE_PANNEAU - BULLE_QUEUE_LARGEUR * BULLE_QUEUE_POINTE

private const val OPACITE_SCRIM = 0.28f

// Un élément qui défile s'arrête au trait haut de la bulle et à l'arête haute de la queue : il ne chevauche jamais le décor.
private val DEDANS_BULLE = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val trait = with(density) { CONTOUR.toPx() }
        val queue = with(density) { (EPAISSEUR + BULLE_QUEUE_MORSURE).toPx() }
        val rayon = with(density) { BULLE_RAYON.toPx() } - trait
        val boite = Rect(trait, trait, size.width - trait, size.height - queue)
        return Outline.Rounded(RoundRect(boite, CornerRadius(rayon.coerceAtLeast(0f))))
    }
}

// Faux hors du monde : aucune scène ne porte le panneau — ni queue de bulle, ni Kokoro dessous, et il descend jusqu'en bas.
val LocalPanneauPorte = staticCompositionLocalOf { true }

// Le panneau de toute ouverture de contexte (démarche, réglages, check-in, tension, phrase) : une seule forme,
// une seule expression, jamais une page plein écran à bandeau — Xavier, 17/08/2026.
@Composable
fun PanneauDialogue(
    titre: String,
    onFermer: () -> Unit,
    modifier: Modifier = Modifier,
    ecart: Dp = 18.dp,
    // Un panneau qui enchaîne des pages repart du haut à chaque changement : sans ça, la nouvelle page s'ouvre au milieu.
    remonteSur: Any? = null,
    contenu: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalPaletteKokoro.current
    val porte = LocalPanneauPorte.current
    val blocageScrim = remember { MutableInteractionSource() }
    val defilement = rememberScrollState()

    LaunchedEffect(remonteSur) {
        if (remonteSur != null) defilement.scrollTo(0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            // Consomme l'appui, et rien d'autre : consommer aussi les mouvements annulerait la croix et le défilement
            // du panneau lui-même, qui abandonnent leur geste dès qu'une consommation leur revient en passe Final.
            .clickable(interactionSource = blocageScrim, indication = null, onClick = {})
            .drawBehind { drawRect(palette.encre.copy(alpha = OPACITE_SCRIM)) }
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MARGE_PANNEAU)
                .padding(top = 14.dp),
        ) {
            Croix(onFermer = onFermer, modifier = Modifier.align(Alignment.CenterEnd))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = MARGE_PANNEAU)
                .then(
                    if (porte) {
                        Modifier
                    } else {
                        Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(bottom = MARGE_PANNEAU)
                    },
                ),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .matiere(palette = palette, rayon = BULLE_RAYON)
                    .clip(DEDANS_BULLE)
                    .verticalScroll(defilement)
                    .padding(horizontal = MARGE_PANNEAU)
                    .padding(top = 20.dp, bottom = 24.dp + EPAISSEUR),
                verticalArrangement = Arrangement.spacedBy(ecart),
            ) {
                Text(
                    text = titre,
                    style = TypoKokoro.titre,
                    color = palette.encre,
                )
                contenu()
            }

            // Remontée dans la bulle du relief plus la morsure : la couture disparaît sous le recouvrement.
            if (porte) {
                QueueBulle(
                    modifier = Modifier.offset(
                        x = BULLE_QUEUE_DECALAGE,
                        y = -(EPAISSEUR + BULLE_QUEUE_MORSURE),
                    ),
                )
            }
        }

        if (porte) Locuteur(expression = Expression.PARLE, modifier = Modifier.align(Alignment.Start))
    }
}

// Triangle porté par le même relief que la bulle ; son bord haut, caché sous elle, ne porte donc pas de trait.
@Composable
private fun QueueBulle(modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Canvas(modifier.size(width = BULLE_QUEUE_LARGEUR, height = BULLE_QUEUE_HAUTEUR + EPAISSEUR)) {
        val relief = EPAISSEUR.toPx()
        val pointe = Offset(size.width * BULLE_QUEUE_POINTE, size.height - relief)
        val chemin = Path().apply {
            moveTo(0f, 0f)
            lineTo(pointe.x, pointe.y)
            lineTo(size.width, 0f)
            close()
        }
        translate(top = relief) { drawPath(chemin, palette.contour) }
        drawPath(path = chemin, color = palette.panneauBas)
        val trait = CONTOUR.toPx()
        drawLine(palette.contour, Offset(0f, 0f), pointe, trait, StrokeCap.Round)
        drawLine(palette.contour, Offset(size.width, 0f), pointe, trait, StrokeCap.Round)
    }
}

@Composable
fun ChampTexte(
    valeur: String,
    onValeur: (String) -> Unit,
    modifier: Modifier = Modifier,
    repere: String = "",
    clavier: KeyboardOptions = KeyboardOptions.Default,
    uneSeuleLigne: Boolean = true,
    alignement: TextAlign = TextAlign.Start,
) {
    val palette = LocalPaletteKokoro.current
    val style = TypoKokoro.corps.copy(color = palette.encre, textAlign = alignement)
    Box(
        modifier = modifier
            .matiere(palette = palette, creuse = true, rayon = 16.dp)
            .padding(horizontal = 18.dp, vertical = 15.dp),
    ) {
        if (valeur.isEmpty()) {
            Text(
                text = repere,
                style = style.copy(color = palette.encreDouce),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        BasicTextField(
            value = valeur,
            onValueChange = onValeur,
            singleLine = uneSeuleLigne,
            textStyle = style,
            cursorBrush = SolidColor(palette.encre),
            keyboardOptions = clavier,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun Interrupteur(actif: Boolean, onChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    val interactions = remember { MutableInteractionSource() }
    // Tween linéaire uniquement, jamais spring : aucun rebond ni dépassement (hypersensibilités).
    val glisse by animateDpAsState(
        targetValue = if (actif) COURSE_INTERRUPTEUR else 0.dp,
        animationSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
        label = "interrupteur",
    )

    Box(
        modifier = modifier
            .clickable(interactionSource = interactions, indication = null) { onChange(!actif) }
            .size(width = PISTE_INTERRUPTEUR, height = BOUTON_INTERRUPTEUR + 13.dp)
            .matiere(
                palette = palette,
                couleur = if (actif) palette.menthe else null,
                rayon = 999.dp,
                epaisseur = 0.dp,
                ombre = false,
                creuse = true,
                epaisseurReflet = 3.dp,
                epaisseurCreux = 3.dp,
            )
            .padding(5.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = glisse)
                .size(width = BOUTON_INTERRUPTEUR, height = BOUTON_INTERRUPTEUR + 3.dp)
                .matiere(
                    palette = palette,
                    rayon = 999.dp,
                    epaisseur = 3.dp,
                    epaisseurReflet = 3.dp,
                    epaisseurCreux = 0.dp,
                ),
        )
    }
}

private val BOUTON_INTERRUPTEUR = 30.dp
private val PISTE_INTERRUPTEUR = 68.dp
private val COURSE_INTERRUPTEUR = PISTE_INTERRUPTEUR - BOUTON_INTERRUPTEUR - 10.dp

// Aucun son, vibration ni clignotement (hypersensibilités) ; rien à toucher pour le faire partir, un geste de moins en crise.
@Composable
fun Accuse(texte: String?, onFini: () -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    val dernier = remember { mutableStateOf("") }
    if (texte != null) dernier.value = texte

    LaunchedEffect(texte) {
        if (texte == null) return@LaunchedEffect
        delay(TENUE_ACCUSE_MS)
        onFini()
    }

    AnimatedVisibility(
        visible = texte != null,
        enter = fadeIn(animationSpec = tween(PARUTION_ACCUSE_MS)),
        exit = fadeOut(animationSpec = tween(PARUTION_ACCUSE_MS)),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
        ) {
            PanneauExtrude(
                modifier = Modifier.fillMaxWidth(),
                couleur = palette.menthe,
                contenuPadding = PaddingValues(horizontal = 22.dp, vertical = 16.dp),
            ) {
                Text(
                    text = dernier.value,
                    style = grave(TypoKokoro.bouton, opacite = 0.20f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private const val PARUTION_ACCUSE_MS = 220
private const val TENUE_ACCUSE_MS = 4_000L

@Composable
fun Separateur(modifier: Modifier = Modifier) {
    val palette = LocalPaletteKokoro.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .height(2.dp)
            .drawBehind { drawRect(palette.contour.copy(alpha = 0.16f)) },
    )
}
