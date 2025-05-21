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
  private int startX, startLeftWidth, startRightWidth;

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

    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        startX = e.getXOnScreen();
        startLeftWidth = left.getWidth();
        startRightWidth = right.getWidth();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        int dx = e.getXOnScreen() - startX;
        int newLeft = Math.max(50, startLeftWidth + dx);
        int newRight = Math.max(50, startRightWidth - dx);
        left.setPreferredSize(new Dimension(newLeft, left.getHeight()));
        left.setDuration(newLeft + "");
        right.setPreferredSize(new Dimension(newRight, right.getHeight()));
        if (right.getDuration() != 0) {
          right.setDuration(newRight + "");
        }
        left.revalidate();
        right.revalidate();
        DividerPanel.this.getParent().revalidate();
        DividerPanel.this.getParent().repaint();
        left.repaint();
        right.repaint();
      }
    });
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
