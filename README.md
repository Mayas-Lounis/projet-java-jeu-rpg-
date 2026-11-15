# Learning Souls Game : mini-RPG en Java (console)

Travaux pratiques universitaires de programmation orientée objet en Java. Le projet construit pas à pas un petit jeu de combat en console inspiré des jeux « souls ».

## Ce que le projet met en œuvre

- **Héritage et classes abstraites** : `Character` (classe abstraite), `Hero`, `Monster`, `Lycanthrope`.
- **Équipement** : armes (`Sword`, `ShotGun`), armures, anneaux et talismans.
- **Consommables** : boissons, nourriture, kit de réparation.
- **Sac** à capacité limitée (`Bag`, `SmallBag`, `MediumBag`) avec transfert d'objets.
- **Exceptions personnalisées** (8) : arme absente ou cassée, endurance vide, sac absent ou plein, consommable absent ou vide, réparation sans arme.
- Un combat tour par tour entre le héros et un monstre (méthode `play()` de `LearningSoulsGame`).

## Lancer le projet

Prérequis : un JDK.

Depuis la racine du dépôt :

```bash
javac -Xprefer:source -encoding UTF-8 -d out -sourcepath . lsg/LearningSoulsGame.java
java -cp out lsg.LearningSoulsGame
```

Le point d'entrée actuel affiche le titre puis exécute une série de scénarios de test des exceptions. Un test du sac est disponible dans `lsg/bags/BagTest.java`.

## Remarque

Projet pédagogique : le sujet et la structure suivent les consignes d'un TP.
