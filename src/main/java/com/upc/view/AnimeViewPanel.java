package com.upc.view;

import javax.swing.*;
import static javax.imageio.ImageIO.read;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import java.io.File;
import static java.awt.Image.SCALE_SMOOTH;

/**
 * Panneau de visualisation pour l'animation.
 * Contient les boutons de contrôle, le timer, la timeline et la zone d'affichage de l'animation.
 */
public class AnimeViewPanel extends JPanel {

  // Panneau principal pour afficher l'animation
  private JPanel animeViewPanel;
  // Bouton pour démarrer l'animation
  private JButton startButton;
  // Bouton pour mettre en pause l'animation
  private JButton breakButton;
  // Bouton pour revenir en arrière dans l'animation
  private JButton backButton;
  // Bouton pour avancer dans l'animation
  private JButton advanceButton;
  // Bouton pour activer/désactiver la boucle
  private JButton loopButton;
  // Bouton pour lecture directe
  private JButton directButton;
  // Label pour afficher le temps écoulé
  private JLabel timer;
  // Curseur pour la timeline de l'animation
  private JSlider timelineSlider;

  /**
   * Constructeur du panneau d'animation.
   * Initialise l'ensemble des composants graphiques et leur disposition.
   */
  public AnimeViewPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Panneau supérieur avec le titre
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.setBackground(Color.LIGHT_GRAY);
    topPanel.setPreferredSize(new Dimension(100, 20));
    topPanel.setMinimumSize(new Dimension(100, 20));
    topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    JLabel titleLabel = new JLabel("Anime View");
    titleLabel.setFont(titleLabel.getFont().deriveFont(12f));
    topPanel.add(Box.createHorizontalStrut(10));
    topPanel.add(titleLabel);
    topPanel.add(Box.createHorizontalGlue());

    // Panneau d'affichage de l'animation
    animeViewPanel = new JPanel();
    animeViewPanel.setLayout(new BorderLayout());
    animeViewPanel.setBackground(Color.WHITE);

    // Panneau des boutons de contrôle
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    buttonPanel.setPreferredSize(new Dimension(100, 40));
    buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    buttonPanel.setMinimumSize(new Dimension(100, 40));

    // Chargement des icônes pour les boutons
    File startIcon = new File("src/main/resources/Icon/startIcon.png");
    File breakIcon = new File("src/main/resources/Icon/breakIcon.png");
    File backIcon = new File("src/main/resources/Icon/back.png");
    File advanceIcon = new File("src/main/resources/Icon/advance.png");
    File loopIcon = new File("src/main/resources/Icon/loop.png");
    File directIcon = new File("src/main/resources/Icon/direct.png");
    Image startImg = null;
    Image breakImg = null;
    Image backImg = null;
    Image advanceImg = null;
    Image loopImg = null;
    Image direct = null;
    try {
      startImg = read(startIcon).getScaledInstance(16, 16, SCALE_SMOOTH);
      breakImg = read(breakIcon).getScaledInstance(16, 16, SCALE_SMOOTH);
      backImg = read(backIcon).getScaledInstance(16, 16, SCALE_SMOOTH);
      advanceImg = read(advanceIcon).getScaledInstance(16, 16, SCALE_SMOOTH);
      loopImg = read(loopIcon).getScaledInstance(16, 16, SCALE_SMOOTH);
      direct = read(directIcon).getScaledInstance(16, 16, SCALE_SMOOTH);
    } catch (Exception e) {
      e.printStackTrace();
    }
    startButton = new JButton(new ImageIcon(startImg));
    breakButton = new JButton(new ImageIcon(breakImg));
    backButton = new JButton(new ImageIcon(backImg));
    advanceButton = new JButton(new ImageIcon(advanceImg));
    loopButton = new JButton(new ImageIcon(loopImg));
    directButton = new JButton(new ImageIcon(direct));
    directButton.setVisible(false);
    breakButton.setVisible(false);

    // Label du timer
    timer = new JLabel("00:00:00");

    // Ajout des composants au panneau des boutons
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(timer);
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(Box.createHorizontalStrut(loopButton.getPreferredSize().width));
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(backButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(startButton);
    buttonPanel.add(breakButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(advanceButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(loopButton);
    buttonPanel.add(directButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(Box.createHorizontalStrut(timer.getPreferredSize().width));
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(Box.createHorizontalStrut(10));

    // Initialisation du curseur de timeline
    timelineSlider = new JSlider(0, 1000, 0); // Plage par défaut, à ajuster dynamiquement
    timelineSlider.setPaintTicks(true);
    timelineSlider.setPaintLabels(true);

    // Ajout des différents panneaux à la vue principale
    add(topPanel);
    add(Box.createGlue());
    add(animeViewPanel);
    add(Box.createGlue());
    add(buttonPanel);
    add(timelineSlider, BorderLayout.SOUTH);
  }

  /**
   * Met à jour l'affichage du timer.
   * @param time temps à afficher (formaté)
   */
  public void setTimer(String time) {
    timer.setText(time);
  }

  /** @return le bouton de démarrage */
  public JButton getStartButton() {
    return startButton;
  }

  /** @return le bouton de pause */
  public JButton getPauseButton() {
    return breakButton;
  }

  /** @return le panneau d'affichage de l'animation */
  public JPanel getAnimeViewPanel() {
    return animeViewPanel;
  }

  /** @return le bouton pour reculer */
  public JButton getBackButton() {
    return backButton;
  }

  /** @return le bouton pour avancer */
  public JButton getAdvanceButton() {
    return advanceButton;
  }

  /** @return le bouton de boucle */
  public JButton getLoopButton() {
    return loopButton;
  }

  /** @return le bouton de lecture directe */
  public JButton getDirectButton() {
    return directButton;
  }

  /** @return le curseur de timeline */
  public JSlider getTimelineSlider() {
    return timelineSlider;
  }
}
