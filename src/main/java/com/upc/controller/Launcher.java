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
import com.upc.view.MainFrame;

public class Launcher {
  public Launcher() {
    // Créer et afficher le splash screen
    JWindow splashScreen = creerSplashScreen();
    splashScreen.setVisible(true);

    // Programmer le lancement de la fenêtre principale après 3 secondes
    Timer timer = new Timer(1000, e -> {
      // Ajouter l'effet de fondu avant de fermer
      fadeOut(splashScreen);
      splashScreen.dispose();
      new MainFrame();
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
