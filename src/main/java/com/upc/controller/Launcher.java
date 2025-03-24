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

import com.upc.view.MainFrame;

import static java.awt.GraphicsDevice.WindowTranslucency.*;


public class Launcher {

  private MainFrame mainFrame;
  private ImageEditor imageEditor;

  public Launcher() {
    // Créer et afficher le splash screen
    JWindow splashScreen = creerSplashScreen();
    splashScreen.setVisible(true);

    imageEditor = new ImageEditor();

    // Programmer le lancement de la fenêtre principale après 3 secondes
    Timer timer = new Timer(1000, e -> {
      // Ajouter l'effet de fondu avant de fermer
      fadeOut(splashScreen);
      splashScreen.dispose();
      this.mainFrame = new MainFrame(imageEditor.getImageEditPanel());
      initMenuBarController();
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

    boolean isUniformTranslucencySupported =
        gd.isWindowTranslucencySupported(TRANSLUCENT);
    boolean isPerPixelTranslucencySupported =
        gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
    boolean isShapedWindowSupported =
        gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT);

    if(isPerPixelTranslucencySupported && isShapedWindowSupported && isUniformTranslucencySupported){
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

  private void initMenuBarController() {
    JMenuItem openItem = mainFrame.getMenuItem(0, 0);
    if (openItem != null) {
      openItem.addActionListener(e -> MenuBarController.openFile());
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
