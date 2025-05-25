package com.upc.view;

import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.event.MouseMotionAdapter;

public class DividerPanel extends JPanel {

  private ResizablePanel left;
  private ResizablePanel right;
  private DividerPanel precDividerPanel;
  private int startX;
  private int startLeftDuration, startRightDuration;
  private double zoomFactor;
  private boolean listenersAdded = false;

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

    // Ajout des listeners une seule fois
    if (!listenersAdded) {
      addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          startX = e.getXOnScreen();
          // On récupère la durée et le zoom courant
          startLeftDuration = left.getDuration();
          startRightDuration = right.getDuration();
          zoomFactor = left.getZoomFactor();
        }
      });

      addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
          int dx = e.getXOnScreen() - startX;
          // Calcul de la nouvelle durée en fonction du zoom
          int newLeftDuration = Math.max(1, (int)Math.round(startLeftDuration + (dx / zoomFactor)));
          int newRightDuration = startRightDuration > 0
            ? Math.max(1, (int)Math.round(startRightDuration - (dx / zoomFactor)))
            : 0;

          // Largeur minimale de 40px
          int minDuration = (int)Math.ceil(40.0 / zoomFactor);
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

          // Ajout : mettre à jour la marge de fin si possible
          if (left instanceof com.upc.view.ResizablePanel) {
            com.upc.view.TimeLinePanel parentPanel = left.getParentTimeLinePanel();
            if (parentPanel != null) {
              parentPanel.updateEndMarginPanel();
            }
          }
        }
      });
      listenersAdded = true;
    }
  }

  public ResizablePanel getLeft() {
    return left;
  }

  public ResizablePanel getRight() {
    return right;
  }

  public DividerPanel getPrecDividerPanel() {
    return precDividerPanel;
  }

  public void setPrecDividerPanel(DividerPanel precDividerPanel) {
    this.precDividerPanel = precDividerPanel;
  }

  public void setLeft(ResizablePanel left) {
    this.left = left;
  }

  public void setRight(ResizablePanel right) {
    this.right = right;
  }

}
