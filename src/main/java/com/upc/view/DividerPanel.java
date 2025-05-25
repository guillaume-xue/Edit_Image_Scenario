package com.upc.view;

import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.event.MouseMotionAdapter;

/**
 * Panneau séparateur permettant de redimensionner dynamiquement deux panneaux adjacents
 * (gauche et droite) dans une timeline ou une interface graphique similaire.
 */
public class DividerPanel extends JPanel {

  // Panneau à gauche du séparateur
  private ResizablePanel left;
  // Panneau à droite du séparateur
  private ResizablePanel right;
  // Référence au séparateur précédent (pour chaînage)
  private DividerPanel precDividerPanel;
  // Position X initiale lors du drag
  private int startX;
  // Durée initiale des panneaux gauche et droite lors du drag
  private int startLeftDuration, startRightDuration;
  // Facteur de zoom courant (pour conversion pixel <-> durée)
  private double zoomFactor;
  // Indique si les listeners ont déjà été ajoutés (évite doublons)
  private boolean listenersAdded = false;

  /**
   * Constructeur du DividerPanel.
   * @param prec séparateur précédent (peut être null)
   * @param left panneau gauche à redimensionner
   * @param right panneau droit à redimensionner
   */
  public DividerPanel(DividerPanel prec, ResizablePanel left, ResizablePanel right) {
    if (prec != null) {
      prec.setRight(left);
    }
    this.left = left;
    this.right = right;
    this.precDividerPanel = prec;
    setPreferredSize(new Dimension(5, 100));
    setMinimumSize(new Dimension(5, 100));
    setMaximumSize(new Dimension(5, 100));
    setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
    setBackground(Color.DARK_GRAY);

    // Ajout des listeners une seule fois pour gérer le redimensionnement
    if (!listenersAdded) {
      addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          startX = e.getXOnScreen();
          // On récupère la durée et le zoom courant
          startLeftDuration = left.getDuration();
          startRightDuration = right.getDuration();
          zoomFactor = left.getZoomFactor();
        }
      });

      addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
          int dx = e.getXOnScreen() - startX;
          // Calcul de la nouvelle durée en fonction du zoom
          int newLeftDuration = Math.max(1, (int) Math.round(startLeftDuration + (dx / zoomFactor)));
          int newRightDuration = startRightDuration > 0
              ? Math.max(1, (int) Math.round(startRightDuration - (dx / zoomFactor)))
              : 0;

          // Largeur minimale de 1px
          int minDuration = (int) Math.ceil(1.0 / zoomFactor);
          newLeftDuration = Math.max(newLeftDuration, minDuration);
          if (newRightDuration > 0) {
            newRightDuration = Math.max(newRightDuration, minDuration);
          }

          left.setDuration(newLeftDuration);
          if (right.getDuration() != 0) {
            right.setDuration(newRightDuration);
          }

          left.revalidate();
          right.revalidate();
          DividerPanel.this.getParent().revalidate();
          DividerPanel.this.getParent().repaint();
          left.repaint();
          right.repaint();

          // Mise à jour de la marge de fin si le panneau gauche le permet
          if (left instanceof ResizablePanel) {
            TimeLinePanel parentPanel = left.getParentTimeLinePanel();
            if (parentPanel != null) {
              parentPanel.updateEndMarginPanel();
            }
          }
        }
      });
      listenersAdded = true;
    }
  }

  /** @return le panneau gauche */
  public ResizablePanel getLeft() {
    return left;
  }

  /** @return le panneau droit */
  public ResizablePanel getRight() {
    return right;
  }

  /** @return le séparateur précédent */
  public DividerPanel getPrecDividerPanel() {
    return precDividerPanel;
  }

  /** Définit le séparateur précédent */
  public void setPrecDividerPanel(DividerPanel precDividerPanel) {
    this.precDividerPanel = precDividerPanel;
  }

  /** Définit le panneau gauche */
  public void setLeft(ResizablePanel left) {
    this.left = left;
  }

  /** Définit le panneau droit */
  public void setRight(ResizablePanel right) {
    this.right = right;
  }

}
