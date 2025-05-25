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
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import static java.awt.GraphicsDevice.WindowTranslucency.*;

/**
 * Classe de lancement de l'application.
 * Affiche un splash screen au démarrage puis lance le contrôleur principal.
 */
public class Launcher {

  /**
   * Constructeur : affiche le splash screen puis lance l'application principale.
   */
  public Launcher() {
    // Créer et afficher le splash screen
    JWindow splashScreen = creerSplashScreen();
    splashScreen.setVisible(true);

    // Programmer le lancement de la fenêtre principale après 1 seconde
    Timer timer = new Timer(1000, e -> {
      // Ajouter l'effet de fondu avant de fermer
      fadeOut(splashScreen);
      new GUIController();
      splashScreen.dispose();
    });

    timer.setRepeats(false);
    timer.start();
  }

  /**
   * Crée et configure la fenêtre du splash screen.
   * @return la fenêtre splash screen
   */
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

  /**
   * Applique un effet de fondu lors de la fermeture du splash screen.
   * @param window la fenêtre à faire disparaître en fondu
   */
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
}
