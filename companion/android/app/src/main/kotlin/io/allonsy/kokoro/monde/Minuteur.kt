package io.allonsy.kokoro.monde

import java.util.Locale

fun resteSecondes(total: Int, ecouleesMillis: Long): Int =
    (total - ecouleesMillis.coerceAtLeast(0L) / 1000L).coerceAtLeast(0L).toInt()

fun libelleDuReste(secondes: Int): String =
    String.format(Locale.ROOT, "%d:%02d", secondes / 60, secondes % 60)
