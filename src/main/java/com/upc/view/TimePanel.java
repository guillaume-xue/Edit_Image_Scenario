package com.upc.view;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;

public class TimePanel extends JPanel {

  private double zoomFactor = 1.0; // 1.0 = 1000px/seconde

  public TimePanel() {
    super();
    setMinimumSize(new Dimension(2000, 10));
    setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
  }

  public void setZoomFactor(double zoomFactor) {
    this.zoomFactor = zoomFactor;
    revalidate();
    repaint();
  }

  public double getZoomFactor() {
    return zoomFactor;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), 10));
    int width = getWidth();
    int step = (int) (1000 * zoomFactor); // 1s = 1000px * zoom
    int y = 10;
    // Ajoute un '|' tous les 100 pixels (ajusté au zoom)
    int tickStep = (int) (100 * zoomFactor);
    for (int x = 0; x < width; x += tickStep) {
      if (x % step != 0) {
        g.drawString("|", x, y);
      }
    }
    // Affiche un chiffre tous les 1000 pixels (ajusté au zoom)
    for (int x = 0, n = 0; x < width; x += step, n++) {
      g.drawString(Integer.toString(n), x, y);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    // Largeur arbitraire, hauteur pour les chiffres
    int width = (int) (2000 * zoomFactor);
    return new Dimension(width, 10);
  }
}
