package com.upc.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Bouton personnalisé pour l'importation d'images.
 * Affiche une icône et un texte centrés verticalement, avec coins arrondis.
 */
public class ImportButton extends JButton {

  /**
   * Constructeur du bouton d'importation.
   * Charge l'icône d'importation et configure la disposition verticale.
   */
  public ImportButton() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    File folderIcon = new File("src/main/resources/Icon/import.png");
    if (folderIcon.exists()) {
      ImageIcon icon = new ImageIcon(folderIcon.getAbsolutePath());
      Image scaledImage = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
      JLabel label = new JLabel(new ImageIcon(scaledImage));
      label.setAlignmentX(CENTER_ALIGNMENT);

      JLabel textLabel = new JLabel("Import");
      textLabel.setAlignmentX(CENTER_ALIGNMENT);

      add(Box.createVerticalGlue());
      add(label);
      add(Box.createVerticalStrut(5));
      add(textLabel);
      add(Box.createVerticalGlue());
    } else {
      System.err.println("Icon file not found: " + folderIcon.getAbsolutePath());
    }
    setOpaque(false);
  }

  /**
   * Redéfinition du rendu du composant pour dessiner un fond arrondi.
   * @param g contexte graphique
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int arc = 24; // Rayon des coins arrondis
    int w = getWidth();
    int h = getHeight();
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, w, h, arc, arc);
    g2.dispose();
  }
}
