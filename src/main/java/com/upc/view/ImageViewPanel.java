package com.upc.view;

import javax.swing.*;
import java.awt.*;

public class ImageViewPanel extends JPanel {

  private ImageIcon imageIcon;

  public ImageViewPanel(ImageIcon imageIcon) {
    this.imageIcon = imageIcon;
    this.setPreferredSize(new Dimension(120, 80)); // Set preferred size for the panel
    this.setMinimumSize(new Dimension(120, 80)); // Set minimum size for the panel
    this.setMaximumSize(new Dimension(120, 80)); // Set maximum size for the panel
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int arc = 16; // Rayon des coins arrondis
    int w = getWidth();
    int h = getHeight();

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Dessiner le fond arrondi
    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, w, h, arc, arc);

    // Appliquer le clip arrondi AVANT de dessiner l'image
    Shape oldClip = g2.getClip();
    g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, arc, arc));

    // Dessiner l'image à l'intérieur du clip arrondi
    if (imageIcon != null) {
      int imgWidth = imageIcon.getIconWidth();
      int imgHeight = imageIcon.getIconHeight();

      double imgRatio = (double) imgWidth / imgHeight;
      double panelRatio = (double) w / h;

      int drawWidth, drawHeight;
      if (panelRatio > imgRatio) {
        drawHeight = h;
        drawWidth = (int) (imgRatio * drawHeight);
      } else {
        drawWidth = w;
        drawHeight = (int) (drawWidth / imgRatio);
      }

      int x = (w - drawWidth) / 2;
      int y = (h - drawHeight) / 2;

      g2.drawImage(imageIcon.getImage(), x, y, drawWidth, drawHeight, this);
    }

    // Restaurer le clip d'origine
    g2.setClip(oldClip);

    // Dessiner le contour arrondi
    g2.setColor(Color.GRAY);
    g2.setStroke(new BasicStroke(2));
    g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

    g2.dispose();
  }

  public ImageIcon getImageIcon() {
    return imageIcon;
  }

  public void setImageIcon(ImageIcon imageIcon) {
    this.imageIcon = imageIcon;
    repaint();
  }
}
