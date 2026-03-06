# Image Editor - Éditeur d'Images

Un éditeur d'images graphique complet développé en Java avec Swing, offrant des outils avancés pour l'édition, le dessin et l'animation d'images.

## Description

Ce projet est une application de bureau permettant de :
- **Importer et charger des images** dans l'éditeur
- **Dessiner librement** sur le canevas avec différents outils
- **Créer des animations** via une timeline
- **Zoomer et naviguer** dans les images
- **Redimensionner des images** avec preview
- **Gérer les projets** (créer, ouvrir, enregistrer)

## Architecture

Le projet suit une architecture **MVC (Model-View-Controller)** :

```
src/main/java/com/upc/
├── App.java                          # Point d'entrée de l'application
├── controller/                       # Contrôleurs (logique métier)
│   ├── Launcher.java                 # Démarrage de l'application
│   ├── ImageEditorController.java    # Contrôle principal de l'éditeur
│   ├── DrawingController.java        # Gestion du dessin
│   ├── GUIController.java            # Coordination de l'interface
│   ├── MouseController.java          # Gestion des événements souris
│   ├── KeyController.java            # Gestion des événements clavier
│   ├── MenuBarController.java        # Gestion de la barre de menu
│   ├── TimeLinePanelController.java  # Gestion de la timeline
│   ├── ViewPanelController.java      # Gestion du panneau de vue
│   ├── AnimaViewPanelController.java # Gestion de la vue animation
│   └── TransferController.java       # Transfert de données
├── model/                            # Modèles de données
│   ├── ImageEditorModel.java         # Modèle principal de l'éditeur
│   └── ChargeImage.java              # Gestion du chargement d'images
└── view/                             # Composants d'interface
    ├── MainFrame.java                # Fenêtre principale
    ├── ImageEditorView.java          # Vue principale de l'éditeur
    ├── DrawingPanel.java             # Canevas de dessin
    ├── ImageViewPanel.java           # Panneau de visualisation
    ├── TimeLinePanel.java            # Panneau timeline/animation
    ├── AnimeViewPanel.java           # Vue des animations
    ├── ViewPanel.java                # Panneau de vue
    ├── NewProjetFrame.java           # Créer un nouveau projet
    ├── OptionFrame.java              # Options et paramètres
    └── ...                           # Autres composants UI
```

## Prérequis

- **Java Development Kit (JDK)** 21 ou supérieur
- **Apache Maven** 3.6 ou supérieur

## Installation et Compilation

1. Cloner ou télécharger le projet
2. Naviguer vers le répertoire du projet
3. Compiler le projet :

```bash
mvn clean install
```

## Exécution

Lancer l'application :

```bash
mvn exec:java
```

Ou directement depuis le JAR compilé :

```bash
java -jar target/my-maven-project-1.0-SNAPSHOT.jar
```

## Tests

Pour exécuter les tests unitaires :

```bash
mvn test
```

## Configuration

Les paramètres de configuration de l'application se trouvent dans :
- `src/main/resources/application.properties` - Propriétés de l'application
- `src/main/resources/resources.properties` - Ressources localisées

## Fonctionnalités Principales

- ✅ Importation d'images (formats courants)
- ✅ Outils de dessin personnalisés
- ✅ Système de zoom et navigation
- ✅ Timeline pour les animations
- ✅ Redimensionnement d'images
- ✅ Gestion de projets (nouveau, ouvrir, enregistrer)
- ✅ Raccourcis clavier
- ✅ Interface intuitive avec panneaux configurables

## Dépendances

- **JUnit Jupiter 5.9.3** - Framework de test

## Licence

À définir - Voir le fichier LICENSE pour plus de détails.
