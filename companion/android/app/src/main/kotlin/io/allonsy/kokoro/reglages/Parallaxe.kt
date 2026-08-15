package io.allonsy.kokoro.reglages

/**
 * Ce qui a le droit de déplacer le décor.
 *
 * ⭐ **Deux interrupteurs et pas un**, parce que ce sont deux gênes différentes : [actif] coupe
 * **tout** mouvement du décor — il devient une image fixe derrière des écrans qui glissent — tandis
 * que [inclinaison] ne coupe que la part venue de la main qui tient le téléphone, et laisse le
 * décor suivre le doigt. **Un seul réglage aurait obligé à perdre l'un pour se débarrasser de
 * l'autre.**
 *
 * 🔴 **[inclinaison] ne veut rien dire quand [actif] est faux** : rien ne bouge, donc rien ne suit
 * l'inclinaison. L'écran de contrôle **retire la ligne** dans ce cas au lieu d'afficher un
 * interrupteur inerte.
 */
data class Parallaxe(
    val actif: Boolean,
    val inclinaison: Boolean,
)

/**
 * ⭐ **L'inclinaison est active d'origine** *(15/08/2026, demande de Xavier)*. Ce n'est pas un
 * changement d'apparence non annoncé : c'est exactement ce qui a été demandé, et il se coupe d'un
 * geste à l'écran de contrôle.
 */
val PARALLAXE_PAR_DEFAUT = Parallaxe(actif = true, inclinaison = true)
