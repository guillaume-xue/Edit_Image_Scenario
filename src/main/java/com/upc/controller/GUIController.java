package com.upc.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.upc.model.ViewImage;
import com.upc.view.MainFrame;
import com.upc.controller.MouseController.DragMouseAdapter;

public class GUIController {

  private MainFrame mainFrame;
  private ImageEditor imageEditor;
  private ViewImage viewImage;

  public GUIController() {
    // Créer et afficher le splash screen
    init();
  }

  private void init() {
    this.viewImage = new ViewImage();
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

        imageEditor = new ImageEditor();
        this.mainFrame = new MainFrame(imageEditor.getImageEditPanel(), imageDirectory);
        initMenuBarController(imageDirectory);
      } else {
        System.err.println("Image directory not specified in properties file.");
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void initMenuBarController(String imageDirectory) {
    JMenuItem openItem = mainFrame.getMenuItem(0, 0);
    if (openItem != null) {
      openItem.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser(); // Set default directory
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(mainFrame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          File destinationDir = new File(imageDirectory);
          File destinationFile = new File(destinationDir, selectedFile.getName());
          try {
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            ImageIcon originalIcon = new ImageIcon(selectedFile.getAbsolutePath());
            Image resizedImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            viewImage.addImageIcon(resizedIcon);
            mainFrame.getViewPanel().displayImages(viewImage.getImageIcons()); // Refresh the view panel
            new MouseController(mainFrame);
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      });
    }

    JMenuItem saveItem = mainFrame.getMenuItem(0, 1);
    if (saveItem != null) {
      saveItem.addActionListener(e -> MenuBarController.saveFile());
    }

    JMenuItem exitItem = mainFrame.getMenuItem(0, 2);
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
