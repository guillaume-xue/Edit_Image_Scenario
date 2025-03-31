package com.upc.controller;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JMenuItem;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import com.upc.view.MainFrame;

import static java.awt.GraphicsDevice.WindowTranslucency.*;

public class Launcher {

  private MainFrame mainFrame;
  private ImageEditor imageEditor;

  public Launcher() {
    // Créer et afficher le splash screen
    JWindow splashScreen = creerSplashScreen();
    splashScreen.setVisible(true);

    // Programmer le lancement de la fenêtre principale après 3 secondes
    Timer timer = new Timer(1000, e -> {
      // Ajouter l'effet de fondu avant de fermer
      fadeOut(splashScreen);
      init();
      splashScreen.dispose();

    });

    timer.setRepeats(false);
    timer.start();

  }

  private static JWindow creerSplashScreen() {
    JWindow window = new JWindow();
    JRootPane rootPane = window.getRootPane();

    // Créer le contenu du splash screen
    JPanel contentPane = new JPanel(new BorderLayout());
    contentPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
    contentPane.setBackground(Color.WHITE);

    // Ajouter du contenu au splash screen
    JLabel label = new JLabel("Edit", SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 24));

    contentPane.add(label, BorderLayout.CENTER);

    // Configurer la fenêtre
    rootPane.setContentPane(contentPane);
    window.setSize(400, 300);
    window.setLocationRelativeTo(null);

    return window;
  }

  private static void fadeOut(JWindow window) {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();

    boolean isUniformTranslucencySupported = gd.isWindowTranslucencySupported(TRANSLUCENT);
    boolean isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
    boolean isShapedWindowSupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT);

    if (isPerPixelTranslucencySupported && isShapedWindowSupported && isUniformTranslucencySupported) {
      float opacity = 1.0f;
      while (opacity > 0.0f) {
        window.setOpacity(opacity);
        opacity -= 0.1f;
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void init() {
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
          if (!destinationDir.exists()) {
            destinationDir.mkdirs(); // Create the directory if it doesn't exist
          }
          File destinationFile = new File(destinationDir, selectedFile.getName());
          try {
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            mainFrame.getViewPanel().displayImages(imageDirectory); // Refresh the view panel
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
