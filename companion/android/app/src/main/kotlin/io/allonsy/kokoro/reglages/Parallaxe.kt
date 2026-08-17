package io.allonsy.kokoro.reglages

data class Parallaxe(
    val actif: Boolean,
    val inclinaison: Boolean,
)

val PARALLAXE_PAR_DEFAUT = Parallaxe(actif = true, inclinaison = true)
