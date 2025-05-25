package com.upc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.upc.view.AnimeViewPanel;
import com.upc.view.MainFrame;
import com.upc.view.NewProjetFrame;
import com.upc.view.OptionFrame;
import com.upc.view.TimeLinePanel;
import com.upc.view.ViewPanel;

/**
 * Contrôleur principal de l'application graphique.
 * Gère l'initialisation des vues, la navigation entre projets, la sauvegarde et le chargement,
 * ainsi que la coordination entre les différents contrôleurs de l'application.
 */
public class GUIController {

  // Références aux différentes fenêtres de l'application
  private MainFrame mainFrame;
  private OptionFrame optionFrame;
  private NewProjetFrame newProjetFrame;

  // Contrôleur d'édition d'image
  private ImageEditorController imageEditor;

  // Autres contrôleurs nécessaires à l'application
  private TransferController transferController;
  private MouseController mouseController;
  private TimeLinePanelController timeLinePanelController;
  private ViewPanelController viewPanelController;
  private AnimaViewPanelController animaViewPanelController;

  // Fichiers et dossiers du projet courant
  private File currentFile;
  private File imageDir;
  private File propertiesFile;
  private File scenarioFile;

  /**
   * Constructeur du contrôleur principal.
   * Lance l'initialisation de l'écran d'options.
   */
  public GUIController() {
    initOption();
  }

  /**
   * Initialise la fenêtre d'options (nouveau projet ou ouvrir projet existant).
   */
  private void initOption() {
    // Créer un nouveau projet
    optionFrame = new OptionFrame();
    initOptionFrameListener();
    optionFrame.launch();
  }

  /**
   * Initialise la fenêtre de création de nouveau projet.
   */
  private void initNewProjet() {
    newProjetFrame = new NewProjetFrame();
    newProjetFrame.launch();
    initNewProjetFrameListener();
  }

  /**
   * Initialise la fenêtre principale et tous les contrôleurs associés.
   * Affiche un dialogue de chargement pendant l'initialisation.
   */
  private void initMainFrame() {
    Properties properties = new Properties();

    // Créer le dialog de chargement
    JDialog loadingDialog = new JDialog();
    loadingDialog.setModal(true);
    loadingDialog.setTitle("Chargement");
    loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Empêche la fermeture
    loadingDialog.setAlwaysOnTop(true); // Toujours au premier plan
    loadingDialog.getContentPane().add(new JLabel("En cours de chargement...", SwingConstants.CENTER));
    loadingDialog.setSize(300, 100);
    loadingDialog.setLocationRelativeTo(null);
    loadingDialog.setFocusableWindowState(false);

    // Afficher le dialog dans un thread séparé pour ne pas bloquer l'EDT
    SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));

    try {
      properties.load(getClass().getResourceAsStream("/resources.properties"));
      this.transferController = new TransferController();
      this.mouseController = new MouseController();
      imageEditor = new ImageEditorController(transferController, imageDir, this);
      ViewPanel viewPanel = new ViewPanel();
      TimeLinePanel timeLinePanel = new TimeLinePanel();
      AnimeViewPanel animeViewPanel = new AnimeViewPanel();
      this.mainFrame = new MainFrame(imageEditor.getImageEditPanel(), viewPanel, timeLinePanel, animeViewPanel);
      this.viewPanelController = new ViewPanelController(viewPanel, mainFrame, transferController, mouseController,
          imageDir.getAbsolutePath(), loadingDialog);
      this.timeLinePanelController = new TimeLinePanelController(timeLinePanel, transferController,
          mouseController, loadingDialog);
      this.viewPanelController.initViewPanel(imageDir.getAbsolutePath());
      this.timeLinePanelController.initTimeLinePanel(scenarioFile, imageDir);
      this.animaViewPanelController = new AnimaViewPanelController(animeViewPanel, this.timeLinePanelController,
          imageDir);
      animaViewPanelController.initialize();
      initMenuBarController();
    } catch (IOException ex) {
      ex.printStackTrace();
      loadingDialog.dispose();
    }
  }

  /**
   * Initialise la structure du dossier projet (images, properties, scenario).
   * Crée les dossiers/fichiers s'ils n'existent pas.
   */
  private void initProjectFolder() {
    imageDir = new File(currentFile, "images");
    propertiesFile = new File(currentFile, "resources.properties");
    scenarioFile = new File(currentFile, "scenario.txt");
    if (!currentFile.exists()) {
      currentFile.mkdirs(); // Crée le dossier projet si inexistant
    }
    if (!imageDir.exists()) {
      imageDir.mkdirs(); // Crée le dossier images si inexistant
    }
    if (!propertiesFile.exists()) {
      try {
        propertiesFile.createNewFile(); // Crée le fichier properties si inexistant
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    if (!scenarioFile.exists()) {
      try {
        scenarioFile.createNewFile(); // Crée le fichier scenario si inexistant
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Ouvre un dossier projet existant ou en crée un nouveau selon le paramètre.
   * Vérifie la validité de la structure du dossier.
   * @param isNewProject true si ouverture dans le cadre d'un nouveau projet
   */
  private void openFolder(boolean isNewProject) {
    while (true) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnValue = fileChooser.showOpenDialog(optionFrame);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedDir = fileChooser.getSelectedFile();
        File imagesDir = new File(selectedDir, "images");
        File propertiesFile = new File(selectedDir, "resources.properties");
        File scenarioFile = new File(selectedDir, "scenario.txt");
        if (selectedDir.exists() && selectedDir.isDirectory()
            && imagesDir.exists() && imagesDir.isDirectory()
            && propertiesFile.exists() && propertiesFile.isFile()
            && scenarioFile.exists() && scenarioFile.isFile()) {
          optionFrame.dispose();
          if (isNewProject) {
            mainFrame.dispose();
          }
          currentFile = selectedDir;
          initProjectFolder(); // Initialise le dossier projet
          initMainFrame(); // Initialise la fenêtre principale
          break;
        } else {
          JOptionPane.showMessageDialog(optionFrame,
              "Le dossier sélectionné n'est pas un dossier de projet valide.\n" +
                  "Il doit contenir :\n- un dossier 'images'\n- un fichier 'resources.properties'\n- un fichier 'scenario.txt'",
              "Erreur de dossier projet", javax.swing.JOptionPane.ERROR_MESSAGE);
          // Laisse l'utilisateur choisir à nouveau
        }
      } else {
        // L'utilisateur a annulé, on sort de la boucle
        break;
      }
    }
  }

  /**
   * Initialise les écouteurs pour la fenêtre d'options (nouveau projet, ouvrir projet).
   */
  private void initOptionFrameListener() {
    optionFrame.getNewProjet().addActionListener(e -> {
      optionFrame.dispose();
      initNewProjet(); // Initialise la fenêtre de nouveau projet
    });

    optionFrame.getOpenProjet().addActionListener(e -> {
      openFolder(false);
    });
  }

  /**
   * Initialise les écouteurs pour la fenêtre de création de nouveau projet.
   */
  private void initNewProjetFrameListener() {
    newProjetFrame.getCreateButton().addActionListener(e -> {
      String name = newProjetFrame.getNameTextArea().getText();
      String location = newProjetFrame.getLocationTextArea().getText();
      if (name != null && !name.isEmpty() && location != null && !location.isEmpty()) {
        currentFile = new File(location, name);
        initProjectFolder(); // Initialise le dossier projet
        newProjetFrame.dispose();
        initMainFrame(); // Initialise la fenêtre principale
      } else {
        System.err.println("Name or location cannot be empty.");
      }
    });

    newProjetFrame.getCancelButton().addActionListener(e -> {
      newProjetFrame.dispose();
      initOption();
    });

    newProjetFrame.getBrowserButton().addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnValue = fileChooser.showOpenDialog(newProjetFrame);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedDirectory = fileChooser.getSelectedFile();
        if (selectedDirectory != null) {
          newProjetFrame.getLocationTextArea().setText(selectedDirectory.getAbsolutePath());
          String name = newProjetFrame.getNameTextArea().getText();
          String location = newProjetFrame.getLocationTextArea().getText();
          newProjetFrame.getDirectoryLabel().setText(location + "/" + name);
        }
      }
      newProjetFrame.repaint();
      newProjetFrame.revalidate();
    });

    newProjetFrame.getNameTextArea().addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent evt) {
        String name = newProjetFrame.getNameTextArea().getText();
        String location = newProjetFrame.getLocationTextArea().getText();
        newProjetFrame.getDirectoryLabel().setText(location + "/" + name);
        newProjetFrame.repaint();
        newProjetFrame.revalidate();
      }
    });
  }

  /**
   * Sauvegarde le fichier de scénario à partir de la timeline.
   * Écrase le fichier existant si besoin.
   */
  private void saveScenarioFile() {
    if (scenarioFile.exists()) {
      scenarioFile.delete(); // Supprime l'ancien fichier pour permettre l'écrasement
    }
    try (FileWriter writer = new FileWriter(scenarioFile)) {
      for (Map.Entry<ImageIcon, Integer> entry : timeLinePanelController.getImageCopiesWithDurations()) {
        ImageIcon image = entry.getKey();
        String imageName = image.getDescription();
        int duration = entry.getValue();

        writer.write(imageName + " ," + duration + System.lineSeparator());
      }
      System.out.println("Scenario file saved successfully.");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Initialise les actions de la barre de menu principale.
   */
  private void initMenuBarController() {
    JMenuItem newItem = mainFrame.getMenuItem(0, 0);
    if (newItem != null) {
      newItem.addActionListener(e -> {
        mainFrame.dispose();
        initNewProjet(); // Ouvre la fenêtre de nouveau projet
      });
    }

    JMenuItem openItem = mainFrame.getMenuItem(0, 1);
    if (openItem != null) {
      openItem.addActionListener(e -> {
        openFolder(true); // Ouvre un dossier projet
      });
    }

    JMenuItem saveSenarioItem = mainFrame.getMenuItem(0, 3);
    if (saveSenarioItem != null) {
      saveSenarioItem.addActionListener(e -> {
        saveScenarioFile();
      });
    }

    JMenuItem saveDrawItem = mainFrame.getMenuItem(0, 5);
    if (saveDrawItem != null) {
      saveDrawItem.addActionListener(e -> {
        imageEditor.valider();
      });
    }

    JMenuItem exitItem = mainFrame.getMenuItem(0, 7);
    if (exitItem != null) {
      exitItem.addActionListener(e -> {
        System.exit(0); // Quitte l'application
      });
    }

    JMenuItem undoItem = mainFrame.getMenuItem(1, 0);
    if (undoItem != null) {
      undoItem.addActionListener(e -> {
        // Ajouter la logique pour l'annulation
      });
    }

    JMenuItem redoItem = mainFrame.getMenuItem(1, 1);
    if (redoItem != null) {
      redoItem.addActionListener(e -> {
        // Ajouter la logique pour le rétablissement
      });
    }
  }

  /**
   * Met à jour le panneau de visualisation avec une nouvelle image.
   * @param imagePath chemin de l'image
   * @param imageName nom de l'image
   */
  public void updateViewPanel(String imagePath, String imageName) {
    if (viewPanelController != null) {
      viewPanelController.addOrUpdateImage(imagePath, imageName);
    }
  }

}
