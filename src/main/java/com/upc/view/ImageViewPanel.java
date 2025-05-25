package com.upc.view;

import javax.swing.*;

import java.awt.*;

/**
 * Panneau personnalisé pour afficher une image avec coins arrondis et contour.
 * Utilisé pour présenter les images dans une galerie ou une liste.
 */
public class ImageViewPanel extends JPanel {

  // Image à afficher dans le panneau
  private ImageIcon imageIcon;

  /**
   * Constructeur du panneau d'image.
   * @param imageIcon ImageIcon à afficher
   */
  public ImageViewPanel(ImageIcon imageIcon) {
    this.imageIcon = imageIcon;
    this.setPreferredSize(new Dimension(120, 80)); // Taille préférée du panneau
    this.setMinimumSize(new Dimension(120, 80));   // Taille minimale
    this.setMaximumSize(new Dimension(120, 80));   // Taille maximale
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Gestionnaire de layout vertical
    this.add(Box.createHorizontalStrut(10)); // Espace horizontal
    setOpaque(false); // Fond transparent
  }

  /**
   * Redéfinition de la méthode de dessin du composant.
   * Affiche l'image centrée dans un rectangle arrondi avec un contour.
   * @param g contexte graphique
   */
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

      // Calcul du ratio pour conserver les proportions de l'image
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

      // Centrer l'image dans le panneau
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

  /**
   * Retourne l'ImageIcon affichée.
   * @return ImageIcon courante
   */
  public ImageIcon getImageIcon() {
    return imageIcon;
  }

  /**
   * Modifie l'image affichée et redessine le panneau.
   * @param imageIcon nouvelle ImageIcon à afficher
   */
  public void setImageIcon(ImageIcon imageIcon) {
    this.imageIcon = imageIcon;
    repaint();
  }
}
