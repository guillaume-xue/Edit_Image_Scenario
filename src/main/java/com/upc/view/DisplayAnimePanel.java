package com.upc.view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panneau personnalisé pour afficher une image centrée et adaptée à la taille du panneau,
 * tout en conservant le ratio de l'image d'origine.
 */
public class DisplayAnimePanel extends JPanel {

  // Image à afficher dans le panneau
  private ImageIcon originalIcon;

  /**
   * Constructeur du panneau d'affichage d'animation.
   * @param originalIcon ImageIcon à afficher
   */
  public DisplayAnimePanel(ImageIcon originalIcon) {
    this.originalIcon = originalIcon;
    setOpaque(false); // Rendre le panneau transparent
  }

  /**
   * Redéfinition de la méthode de dessin du composant.
   * Centre et adapte l'image à la taille du panneau tout en conservant son ratio.
   * @param g contexte graphique utilisé pour le dessin
   */
  @Override
  protected void paintComponent(java.awt.Graphics g) {
    super.paintComponent(g);
    if (originalIcon != null) {
      int panelWidth = getWidth();
      int panelHeight = getHeight();
      int imgWidth = originalIcon.getIconWidth();
      int imgHeight = originalIcon.getIconHeight();

      // Calcul du ratio de l'image
      double imgRatio = (double) imgWidth / imgHeight;
      double panelRatio = (double) panelWidth / panelHeight;

      int drawWidth, drawHeight;
      if (panelRatio > imgRatio) {
        // Panel plus large que l'image : adapter à la hauteur
        drawHeight = panelHeight;
        drawWidth = (int) (imgRatio * drawHeight);
      } else {
        // Panel plus haut que l'image : adapter à la largeur
        drawWidth = panelWidth;
        drawHeight = (int) (drawWidth / imgRatio);
      }

      // Centrer l'image
      int x = (panelWidth - drawWidth) / 2;
      int y = (panelHeight - drawHeight) / 2;

      g.drawImage(originalIcon.getImage(), x, y, drawWidth, drawHeight, this);
    }
  }
}
