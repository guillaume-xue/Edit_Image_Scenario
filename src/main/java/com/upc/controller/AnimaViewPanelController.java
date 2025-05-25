package com.upc.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.upc.view.AnimeViewPanel;
import com.upc.view.DisplayAnimePanel;

/**
 * Contrôleur pour la gestion de l'affichage et de l'animation des images dans le panneau AnimeViewPanel.
 * Gère la lecture, la pause, la navigation, la boucle et la synchronisation de l'animation.
 */
public class AnimaViewPanelController {

  // Référence au panneau de vue de l'anime (interface utilisateur)
  private AnimeViewPanel animeViewPanel;
  // Contrôleur du panneau de la timeline (gestion des images et durées)
  private TimeLinePanelController timeLinePanelController;
  // Répertoire contenant les images à afficher
  private File imageDirectory;
  // Indique si l'animation est en pause
  private volatile boolean paused = false;
  // Indique si l'animation est terminée
  private volatile boolean ended = true;
  // Temps écoulé depuis le début de l'animation (en millisecondes)
  private volatile long elapsed = 0;
  // Indique si l'animation doit boucler
  private volatile boolean loop = false;
  // Index de l'image actuellement affichée
  private volatile int currentImageIndex = 0;
  // Liste des images avec leur durée d'affichage respective
  private ArrayList<Map.Entry<ImageIcon, Integer>> imageWithDuration = new ArrayList<>();

  /**
   * Constructeur du contrôleur.
   * @param animeViewPanel panneau de vue de l'anime
   * @param timeLinePanel contrôleur du panneau de la timeline
   * @param imageDirectory répertoire des images
   */
  AnimaViewPanelController(AnimeViewPanel animeViewPanel, TimeLinePanelController timeLinePanel, File imageDirectory) {
    this.animeViewPanel = animeViewPanel;
    this.timeLinePanelController = timeLinePanel;
    this.imageDirectory = imageDirectory;
  }

  /**
   * Initialise les écouteurs d'événements pour les boutons et la timeline.
   */
  public void initialize() {
    // Bouton démarrer : lance ou reprend l'animation
    animeViewPanel.getStartButton().addActionListener(e -> {
      animeViewPanel.getStartButton().setVisible(false);
      animeViewPanel.getPauseButton().setVisible(true);
      if (ended) {
        // Récupère la liste des images et durées depuis la timeline
        this.imageWithDuration = timeLinePanelController.getImageCopiesWithDurations();
        currentImageIndex = 0;
        animeViewPanel.getTimelineSlider().setMaximum(imageWithDuration.size() - 1);
        animeViewPanel.getTimelineSlider().setValue(0);
        animatePanel();
      } else {
        resumeAnimation();
      }
    });

    // Bouton pause : met l'animation en pause
    animeViewPanel.getPauseButton().addActionListener(e -> {
      animeViewPanel.getStartButton().setVisible(true);
      animeViewPanel.getPauseButton().setVisible(false);
      pauseAnimation();
    });

    // Bouton retour : recule d'une image dans l'animation
    animeViewPanel.getBackButton().addActionListener(e -> {
      if (currentImageIndex > 1) {
        animeViewPanel.getStartButton().setVisible(true);
        animeViewPanel.getPauseButton().setVisible(false);
        pauseAnimation();
        currentImageIndex -= 2;
        animeViewPanel.getTimelineSlider().setValue(currentImageIndex);
      }
    });

    // Bouton avancer : avance d'une image dans l'animation
    animeViewPanel.getAdvanceButton().addActionListener(e -> {
      if (currentImageIndex < imageWithDuration.size() - 1) {
        animeViewPanel.getStartButton().setVisible(true);
        animeViewPanel.getPauseButton().setVisible(false);
        pauseAnimation();
        currentImageIndex++;
        animeViewPanel.getTimelineSlider().setValue(currentImageIndex);
      }
    });

    // Bouton boucle : active le mode boucle
    animeViewPanel.getLoopButton().addActionListener(e -> {
      loop = true;
      animeViewPanel.getLoopButton().setVisible(false);
      animeViewPanel.getDirectButton().setVisible(true);
    });

    // Bouton direct : désactive le mode boucle
    animeViewPanel.getDirectButton().addActionListener(e -> {
      loop = false;
      animeViewPanel.getLoopButton().setVisible(true);
      animeViewPanel.getDirectButton().setVisible(false);
    });

    // Slider de la timeline : permet de naviguer dans l'animation
    animeViewPanel.getTimelineSlider().addChangeListener(e -> {
      currentImageIndex = animeViewPanel.getTimelineSlider().getValue();
      if (currentImageIndex < imageWithDuration.size()) {
        elapsed = calculateElapsedFromIndex(currentImageIndex);
        animeViewPanel.setTimer(formatMillis(elapsed));
        if (!paused) {
          pauseAnimation();
          resumeAnimation();
        }
      }
    });
  }

  /**
   * Met l'animation en pause.
   */
  private void pauseAnimation() {
    paused = true;
  }

  /**
   * Reprend l'animation si elle était en pause.
   */
  private void resumeAnimation() {
    paused = false;
    synchronized (this) {
      notifyAll();
    }
  }

  /**
   * Met à jour le panneau d'affichage avec l'image courante.
   * @param entry entrée contenant l'image et sa durée
   */
  private void updateImagePanel(Map.Entry<ImageIcon, Integer> entry) {
    new Thread(() -> {
      ImageIcon image = entry.getKey();
      File imageFile = new File(imageDirectory, image.getDescription());
      ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());

      JPanel imagePanel = new DisplayAnimePanel(originalIcon);
      imagePanel.setPreferredSize(animeViewPanel.getAnimeViewPanel().getSize());

      SwingUtilities.invokeLater(() -> {
        animeViewPanel.getAnimeViewPanel().removeAll();
        animeViewPanel.getAnimeViewPanel().add(imagePanel);
        animeViewPanel.getAnimeViewPanel().revalidate();
        animeViewPanel.getAnimeViewPanel().repaint();
      });
    }).start();
  }

  /**
   * Lance l'animation des images dans un thread séparé.
   */
  private void animatePanel() {
    ended = false;
    Thread animationThread = new Thread(() -> {
      while (currentImageIndex < imageWithDuration.size()) {
        Map.Entry<ImageIcon, Integer> entry = imageWithDuration.get(currentImageIndex);
        if (paused) {
          synchronized (this) {
            try {
              wait();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          }
        }
        updateImagePanel(entry);
        elapsed += entry.getValue();
        animeViewPanel.setTimer(formatMillis(elapsed));
        animeViewPanel.getTimelineSlider().setValue(currentImageIndex);
        try {
          Thread.sleep(entry.getValue());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
        currentImageIndex++;
        // Si le mode boucle est activé, recommence l'animation depuis le début
        if (loop && currentImageIndex >= imageWithDuration.size()) {
          currentImageIndex = 0;
          elapsed = 0;
          animeViewPanel.getTimelineSlider().setValue(0);
        }
      }
      ended = true;
      animeViewPanel.getStartButton().setVisible(true);
      animeViewPanel.getPauseButton().setVisible(false);
    });
    animationThread.start();
  }

  /**
   * Formate un temps en millisecondes en chaîne hh:mm:ss.
   * @param millis temps en millisecondes
   * @return chaîne formatée
   */
  private String formatMillis(long millis) {
    long seconds = millis / 1000;
    long minutes = seconds / 60;
    long hours = minutes / 60;
    seconds = seconds % 60;
    minutes = minutes % 60;
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  /**
   * Calcule le temps écoulé jusqu'à un index donné dans la liste des images.
   * @param index index de l'image
   * @return temps écoulé en millisecondes
   */
  private long calculateElapsedFromIndex(int index) {
    long totalElapsed = 0;
    for (int i = 0; i < index; i++) {
      totalElapsed += imageWithDuration.get(i).getValue();
    }
    return totalElapsed;
  }
}
