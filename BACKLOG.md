**Fichier pour Xavier, Claude n'en tient pas compte**

# Dette

```
Maintenant que nous savons ce que nous faisons avec Kokoro, refactorons les cartes pour le rendre génériques et multi-steps. C'est à dire qu'une carte ouvre son panneau qui peut contenir des steps avec des questions, un chrono, de l'info, etc. tout ce qui existe déja comme feature.

Les fiches sont donc des cartes comme les autres, mais au lieu d'avoir un type "panneau" (ou comme tu veux qui va bien) elle a un type "pdf" (ou comme tu veux qui va bien) qui affiche l'icone de lien externe et permet de charger le fichier

Les écrans hardcodés avec le type "ecran" ne devraient donc plus exister : ce sont des cartes multi-steps.
Même le bouton de réglage devient un panneau multi-steps.

Il est possible en revanche d'utiliser une sorte d'id (textuel) entre Kokoro et Claude Psy pour faire le lien entre les cartes pour savoir comment interpréter les résultats.
```

- Refresh du contenu dans Kokoro toutes les X secondes

- Changer l'anim de kokoro dans les bilans
- Supprimer le test K1

- Est-ce possible que le bouton de la notification n'envoie pas au même endroit si le tel est en veille ?

# Kokoro Générique
