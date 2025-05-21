package com.upc.view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DisplayAnimePanel extends JPanel {

  private ImageIcon originalIcon;

  public DisplayAnimePanel(ImageIcon originalIcon) {
    this.originalIcon = originalIcon;
    setOpaque(false); // Rendre le panneau transparent
  }

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
