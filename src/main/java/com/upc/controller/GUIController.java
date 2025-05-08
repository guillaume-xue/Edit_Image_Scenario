package com.upc.controller;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import com.upc.view.MainFrame;
import com.upc.view.NewProjetFrame;
import com.upc.view.OptionFrame;
import com.upc.view.ViewPanel;

public class GUIController {

  private MainFrame mainFrame;
  private OptionFrame optionFrame;
  private NewProjetFrame newProjetFrame;

  private ViewPanelController viewPanelController;

  private ImageEditor imageEditor;

  private TransferController transferController;
  private MouseController mouseController;
  private File currentFile;

  public GUIController() {
    initOption();
  }

  private void initOption() {
    // Créer un nouveau projet
    optionFrame = new OptionFrame();
    initOptionFrameListener();
    optionFrame.launch();
  }

  private void initNewProjet() {
    newProjetFrame = new NewProjetFrame();
    newProjetFrame.launch();
    initNewProjetFrameListener();
  }

  private void initMain() {
    Properties properties = new Properties();

    // Charger le fichier .properties
    try {
      properties.load(getClass().getResourceAsStream("/resources.properties"));
      String imageDirectory = properties.getProperty("image.directory");
      if (imageDirectory != null && !imageDirectory.isEmpty()) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
          directory.mkdirs(); // Create the directory if it doesn't exist
        }
        this.transferController = new TransferController();
        this.mouseController = new MouseController();
        imageEditor = new ImageEditor();
        ViewPanel viewPanel = new ViewPanel(imageDirectory);
        this.mainFrame = new MainFrame(imageEditor.getImageEditPanel(), transferController, mouseController, viewPanel);

        this.viewPanelController = new ViewPanelController(viewPanel, mainFrame, transferController, mouseController,
            imageDirectory);
        initMenuBarController(imageDirectory);
      } else {
        System.err.println("Image directory not specified in properties file.");
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void initOptionFrameListener() {
    optionFrame.getNewProjet().addActionListener(e -> {
      optionFrame.dispose();
      initNewProjet(); // Initialize the new project frame
    });

    optionFrame.getOpenProjet().addActionListener(e -> {
      optionFrame.dispose();
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnValue = fileChooser.showOpenDialog(optionFrame);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedDirectory = fileChooser.getSelectedFile();
        currentFile = selectedDirectory;
        initMain(); // Initialize the main frame
      }
    });
  }

  private void initNewProjetFrameListener() {
    newProjetFrame.getCreateButton().addActionListener(e -> {
      String name = newProjetFrame.getNameTextArea().getText();
      String location = newProjetFrame.getLocationTextArea().getText();
      String image = "images";
      String properties = "resources.properties";
      if (name != null && !name.isEmpty() && location != null && !location.isEmpty()) {
        currentFile = new File(location, name);
        File imageDir = new File(currentFile, image);
        File propertiesFile = new File(currentFile, properties);
        if (!currentFile.exists()) {
          currentFile.mkdirs(); // Create the directory if it doesn't exist
        }
        if (!imageDir.exists()) {
          imageDir.mkdirs(); // Create the image directory if it doesn't exist
        }
        if (!propertiesFile.exists()) {
          try {
            propertiesFile.createNewFile(); // Create the properties file if it doesn't exist
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
        newProjetFrame.dispose();
        initMain(); // Initialize the main frame
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
        newProjetFrame.getLocationTextArea().setText(selectedDirectory.getAbsolutePath());
      }
      newProjetFrame.getLocationTextArea().setText(fileChooser.getSelectedFile().getAbsolutePath());
      String name = newProjetFrame.getNameTextArea().getText();
      String location = newProjetFrame.getLocationTextArea().getText();
      newProjetFrame.getDirectoryLabel().setText(location + "/" + name);
      newProjetFrame.repaint();
      newProjetFrame.revalidate();
    });

    newProjetFrame.getNameTextArea().addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        String name = newProjetFrame.getNameTextArea().getText();
        String location = newProjetFrame.getLocationTextArea().getText();
        newProjetFrame.getDirectoryLabel().setText(location + "/" + name);
        newProjetFrame.repaint();
        newProjetFrame.revalidate();
      }
    });
  }

  private void initMenuBarController(String imageDirectory) {
    JMenuItem newItem = mainFrame.getMenuItem(0, 0);
    if (newItem != null) {
      newItem.addActionListener(e -> {
        mainFrame.dispose();
        initNewProjet(); // Initialize the new project frame
      });
    }

    JMenuItem openItem = mainFrame.getMenuItem(0, 1);
    if (openItem != null) {
      openItem.addActionListener(e -> {

      });
    }

    JMenuItem saveItem = mainFrame.getMenuItem(0, 2);
    if (saveItem != null) {
      saveItem.addActionListener(e -> MenuBarController.saveFile());
    }

    JMenuItem exitItem = mainFrame.getMenuItem(0, 3);
    if (exitItem != null) {
      exitItem.addActionListener(e -> System.exit(0));
    }

    JMenuItem undoItem = mainFrame.getMenuItem(1, 0);
    if (undoItem != null) {
      undoItem.addActionListener(e -> {
        // Add logic for undo operation
      });
    }

    JMenuItem redoItem = mainFrame.getMenuItem(1, 1);
    if (redoItem != null) {
      redoItem.addActionListener(e -> {
        // Add logic for redo operation
      });
    }
  }

}
