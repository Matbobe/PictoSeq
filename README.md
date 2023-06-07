# Documentation de l'Application PictoSeq
Cette application a été conçue pour aider à la Communication Alternative et Améliorée (CAA), offrant des stratégies alternatives à la parole pour les personnes en situation de handicap. Elle permet créer et modifier des séquentiels de pictogrammes.

## 1. Fonctionnalités
L'application offre les fonctionnalités suivantes :

- **Recherche de pictogrammes** : L'utilisateur peut effectuer une recherche de pictogrammes par mot-clé ou en parcourant des catégories prédéfinies.
- **Gestion des séquentiels** : L'utilisateur peut créer ou supprimer des séquentiels, ajouter des pictogrammes dans les séquentiels, retirer, éditer, réorganiser des pictogrammes dans les séquentiels.
- **Personnalisation des pictogrammes** : L'utilisateur peut choisir la position du texte sur le pictogramme, choisir l'affichage et le positionnement optionnel d'un numéro.
- **Personnalisation des séquentiels** : L'utilisateur peut choisir le type et la couleur de la bordure pour tout le séquentiel, nommer les séquentiels.
- **Sauvegarde et impression** : L'utilisateur peut sauvegarder, fermer, ouvrir et imprimer les séquentiels.

## 2. Utilisation de l'API Arasaac
L'application utilise l'API d'Arasaac pour obtenir les images des pictogrammes. Les requêtes HTTP sont effectuées à l'aide de la librairie JDK/JRE et les réponses JSON sont parsées avec la librairie Jackson.

## 3. Persistance des données
La persistance des données est réalisée par sérialisation/désérialisation dans un fichier. Cela permet de sauvegarder l'état actuel de l'application et de le restaurer lors de la prochaine ouverture.

## 4. Performance et robustesse
L'application est conçue pour être performante, avec un temps d'échange avec l'API inférieur à 1 seconde. Elle est également robuste et offre une persistance locale par fichier.

## 5. Développement et évaluation
L'application a été développée en utilisant Javafx. L'évaluation se fera sur le code de l'application et sur l'application elle-même.

## 6. TODO
| Fonctionnalité                                                      | Description                                                                                                                      | État          |
|---------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|---------------|
| Recherche de pictogrammes                                           | Implémenter la fonctionnalité permettant de rechercher des pictogrammes par mot-clé ou en parcourant des catégories prédéfinies. | Fait          |
| Création de séquences de pictogrammes                               | Développer la fonctionnalité pour créer de nouvelles séquences de pictogrammes.                                                  | Fait          |
| Suppression de séquences de pictogrammes                            | Ajouter la possibilité de supprimer des séquences de pictogrammes existantes.                                                    | Fait          |
| Ajout de pictogrammes aux séquences                                 | Permettre l'ajout de pictogrammes dans les séquences à partir des résultats de recherche.                                        | En cours      |
| Retrait de pictogrammes des séquences                               | Implémenter la fonctionnalité pour retirer des pictogrammes des séquences.                                                       | Pas commencer |
| Édition de pictogrammes dans les séquences                          | Ajouter la possibilité d'éditer (ajouter un texte) les pictogrammes dans les séquences.                                          | En cours      |
| Réorganisation des pictogrammes dans les séquences                  | Permettre la réorganisation des pictogrammes dans les séquences.                                                                 | Pas commencer |
| Choix de la position du texte sur le pictogramme                    | Ajouter une option pour choisir la position du texte sur le pictogramme.                                                         | En cours      |
| Choix de l'affichage et du positionnement optionnel d'un numéro     | Implémenter la fonctionnalité pour choisir l'affichage et le positionnement optionnel d'un numéro.                               | En cours      |
| Choix du type et de la couleur de la bordure pour toute la séquence | Ajouter une option pour choisir le type et la couleur de la bordure pour toute la séquence.                                      | En cours      |
| Nomination des séquences                                            | Permettre de nommer les séquences.                                                                                               | En cours      |
| Sauvegarde des séquences                                            | Implémenter la fonctionnalité pour sauvegarder les séquences.                                                                    | Fait          |
| Ouverture des séquences                                             | Ajouter la possibilité d'ouvrir les séquences.                                                                                   | Fait          |
| Impression d'une séquence                                           | Développer la fonctionnalité pour imprimer une séquence (génération d'un PDF).                                                   | Pas commencer |

## 7. Conclusion
Cette application offre une solution efficace pour la gestion de séquentiels de pictogrammes, aidant ainsi les personnes en situation de handicap à communiquer de manière plus efficace.