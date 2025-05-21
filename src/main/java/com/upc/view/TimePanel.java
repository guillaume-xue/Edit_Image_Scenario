package com.upc.view;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;

public class TimePanel extends JPanel {

  public TimePanel() {
    super();
    setMinimumSize(new Dimension(2000, 10));
    setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), 10));
    int width = getWidth();
    int step = 1000;
    int y = 10;
    // Ajoute un '|' tous les 100 pixels
    for (int x = 0; x < width; x += 100) {
      if (x % 1000 != 0) {
        g.drawString("|", x, y);
      }
    }
    // Affiche un chiffre tous les 1000 pixels
    for (int x = 0, n = 0; x < width; x += step, n++) {
      g.drawString(Integer.toString(n), x, y);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    // Largeur arbitraire, hauteur pour les chiffres
    return new Dimension(2000, 10);
  }
}
