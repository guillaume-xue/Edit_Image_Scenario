package com.upc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panneau redimensionnable utilisé dans la timeline pour représenter une séquence ou une image.
 * Permet d'ajuster dynamiquement sa largeur (donc sa durée) et d'afficher une icône répétée.
 */
public class ResizablePanel extends JPanel {

  // Label affichant la durée de la séquence
  private JLabel durationLabel;
  // Icône associée à la séquence
  private ImageIcon icon;
  // Facteur de zoom pour la conversion durée <-> largeur
  private double zoomFactor = 1.0;
  // Durée réelle de la séquence (en ms ou px selon la logique)
  private int duration = 0;
  // Référence au panneau parent de la timeline
  private TimeLinePanel parentTimeLinePanel;

  /**
   * Constructeur par défaut (sans icône ni durée).
   */
  public ResizablePanel() {
    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(100, 100));
    setMinimumSize(new Dimension(100, 100));
    durationLabel = new JLabel("");
    add(durationLabel);
  }

  /**
   * Constructeur avec icône et durée initiale.
   * @param icon icône à afficher
   * @param duration durée de la séquence
   */
  public ResizablePanel(ImageIcon icon, int duration) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(duration, 100));

    this.icon = icon;
    this.duration = duration; // stocke la vraie durée

    JPanel durationPanel = new JPanel();
    durationPanel.setLayout(new BoxLayout(durationPanel, BoxLayout.X_AXIS));
    durationPanel.setPreferredSize(new Dimension(100, 20));
    durationPanel.setMinimumSize(new Dimension(100, 20));
    durationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    durationLabel = new JLabel(duration + "");
    durationPanel.add(durationLabel);
    durationPanel.add(Box.createHorizontalStrut(10));

    add(durationPanel);
    add(Box.createHorizontalStrut(40));
  }

  /**
   * Redéfinition du rendu du composant.
   * Affiche l'icône répétée horizontalement selon la largeur du panneau.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (icon != null) {
      int iconWidth = 38;
      int iconHeight = 38;
      int y = 30;
      int count = 1 + (getWidth() / iconWidth);

      for (int i = 0; i < count; i++) {
        int x = i * iconWidth;
        g.drawImage(icon.getImage(), x, y, iconWidth, iconHeight, this);
      }
    }
  }

  /**
   * Définit la durée à partir d'une chaîne de caractères.
   * @param durationStr chaîne représentant la durée
   */
  public void setDuration(String durationStr) {
    try {
      int d = Integer.parseInt(durationStr);
      setDuration(d);
    } catch (NumberFormatException e) {
      // ignore
    }
  }

  /**
   * Définit le panneau parent de la timeline.
   * @param parent panneau parent
   */
  public void setParentTimeLinePanel(TimeLinePanel parent) {
    this.parentTimeLinePanel = parent;
  }

  /**
   * Définit la durée et met à jour la largeur du panneau.
   * @param duration nouvelle durée
   */
  public void setDuration(int duration) {
    this.duration = duration;
    this.durationLabel.setText(Integer.toString(duration));
    updateWidthFromDuration(zoomFactor);
    if (parentTimeLinePanel != null) {
      parentTimeLinePanel.updateEndMarginPanel(); // Met à jour la marge à chaque modification
    }
  }

  /**
   * Retourne la durée courante.
   * @return durée
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Retourne l'icône associée.
   * @return ImageIcon
   */
  public ImageIcon getIcon() {
    return icon;
  }

  /**
   * Définit le facteur de zoom.
   * @param zoomFactor nouveau facteur de zoom
   */
  public void setZoomFactor(double zoomFactor) {
    this.zoomFactor = zoomFactor;
  }

  /**
   * Retourne le facteur de zoom courant.
   * @return facteur de zoom
   */
  public double getZoomFactor() {
    return zoomFactor;
  }

  /**
   * Met à jour la durée en fonction de la largeur du panneau (utilisé lors du redimensionnement).
   */
  public void updateDurationFromWidth() {
    int width = getWidth();
    int newDuration = (int) (width / zoomFactor);
    this.duration = newDuration;
    this.durationLabel.setText(Integer.toString(newDuration));
  }

  /**
   * Met à jour la durée en fonction de la largeur et du facteur de zoom fourni.
   * @param zoomFactor facteur de zoom à utiliser
   */
  public void updateDurationFromWidth(double zoomFactor) {
    this.zoomFactor = zoomFactor;
    updateDurationFromWidth();
  }

  /**
   * Met à jour la largeur du panneau en fonction de la durée et du facteur de zoom.
   * @param zoomFactor facteur de zoom à utiliser
   */
  public void updateWidthFromDuration(double zoomFactor) {
    this.zoomFactor = zoomFactor;
    int width = (int) (duration * zoomFactor);
    int height = getPreferredSize().height;
    setPreferredSize(new Dimension(width, height));
    setMinimumSize(new Dimension(width, height));
    setMaximumSize(new Dimension(width, height));
    revalidate();
    repaint();
  }

  /**
   * Retourne le panneau parent de la timeline.
   * @return TimeLinePanel parent
   */
  public TimeLinePanel getParentTimeLinePanel() {
    return parentTimeLinePanel;
  }
}