# `aidant/scripts/` — vide aujourd'hui

**L'aide-au-patient n'exécute rien, et ne doit rien exécuter.** Elle tient un téléphone et suit un déroulé affiché par Kokoro. **C'est tout son périmètre.**

---

## Ce qui aurait vocation à venir ici

Un outil qui produit ce qu'elle lit — par exemple la vérification, hors publication, qu'une séquence `seance-duo` en cours d'écriture rappelle bien le **signal d'arrêt**, expose les **critères d'arrêt** en un tap et exige l'**entraînement** avant la première fois.

> ⭐ **Mais ces trois vérifications sont déjà câblées dans `npm run publish`**, et les dédoubler créerait un second endroit à tenir à jour — **C3 et C7 en même temps**. Un script ici devrait donc apporter autre chose que ces trois-là.

> 🔴 **Aucun script de ce répertoire n'aura jamais le droit de lui donner une information sur Xavier.** C'est le contrôle **C10** : elle lit des consignes, pas un dossier.
