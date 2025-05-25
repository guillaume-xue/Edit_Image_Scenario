package com.upc.controller;

import javax.swing.*;

import com.upc.view.ImageViewPanel;
import com.upc.view.ResizablePanel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController {

  public class TimeLinePanelMouseController extends MouseAdapter {

    ResizablePanel resizablePanel;
    TimeLinePanelController timeLinePanelController;
    private int dragStartScreenX = -1;
    private int originalDuration = -1;
    private boolean resizing = false;

    public TimeLinePanelMouseController(ResizablePanel resizablePanel,
        TimeLinePanelController timeLinePanelController) {
      this.resizablePanel = resizablePanel;
      this.timeLinePanelController = timeLinePanelController;
      resizablePanel.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON3) { // Right mouse button
        JPopupMenu menu = new JPopupMenu();
        JMenuItem del = new JMenuItem("Delete");
        del.addActionListener(event -> {
          timeLinePanelController.removeImageLabel(resizablePanel);
        });
        menu.add(del);

        // Ajout du bouton "Temps"
        JMenuItem setTime = new JMenuItem("Temps");
        setTime.addActionListener(event -> {
          // Ouvre un petit JDialog pour saisir la durée en ms
          String input = JOptionPane.showInputDialog(resizablePanel, "Entrer la durée (ms) :", resizablePanel.getDuration());
          if (input != null) {
            try {
              int newDuration = Integer.parseInt(input.trim());
              if (newDuration > 0) {
                resizablePanel.setDuration(newDuration);
              } else {
                JOptionPane.showMessageDialog(resizablePanel, "La durée doit être positive.", "Erreur", JOptionPane.ERROR_MESSAGE);
              }
            } catch (NumberFormatException ex) {
              JOptionPane.showMessageDialog(resizablePanel, "Entrée invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
          }
        });
        menu.add(setTime);

        // Ajout des actions de déplacement
        JMenuItem moveStart = new JMenuItem("Début");
        moveStart.addActionListener(event -> {
          timeLinePanelController.moveResizablePanelTo(resizablePanel, 0);
        });
        menu.add(moveStart);

        JMenuItem moveEnd = new JMenuItem("Fin");
        moveEnd.addActionListener(event -> {
          timeLinePanelController.moveResizablePanelTo(resizablePanel, -1); // -1 pour la fin
        });
        menu.add(moveEnd);

        JMenuItem moveForward = new JMenuItem("Avancer");
        moveForward.addActionListener(event -> {
          timeLinePanelController.moveResizablePanelRelative(resizablePanel, 1);
        });
        menu.add(moveForward);

        JMenuItem moveBackward = new JMenuItem("Reculer");
        moveBackward.addActionListener(event -> {
          timeLinePanelController.moveResizablePanelRelative(resizablePanel, -1);
        });
        menu.add(moveBackward);

        menu.show(resizablePanel, e.getX(), e.getY());
      }
    }

    @Override
    public void mousePressed(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        int x = e.getX();
        int width = resizablePanel.getWidth();
        // Toujours synchroniser le zoom avec le TimeLinePanel
        double currentZoom = timeLinePanelController.getTimeLinePanel().getZoomFactor();
        resizablePanel.setZoomFactor(currentZoom);
        if (x >= width - 10 && x <= width) {
          resizing = true;
          dragStartScreenX = e.getLocationOnScreen().x;
          originalDuration = resizablePanel.getDuration();
        } else {
          resizing = false;
        }
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (resizing && dragStartScreenX >= 0 && originalDuration >= 0) {
        int currentScreenX = e.getLocationOnScreen().x;
        int delta = currentScreenX - dragStartScreenX;
        double zoom = resizablePanel.getZoomFactor();
        // Calcul en double pour éviter les erreurs d'arrondi
        double newDurationD = originalDuration + (delta / zoom);
        int newDuration = Math.max(1, (int) Math.round(newDurationD));
        // Largeur minimale de 40px
        int minDuration = (int) Math.ceil(40.0 / zoom);
        newDuration = Math.max(newDuration, minDuration);
        resizablePanel.setDuration(newDuration);
        java.awt.Container parent = resizablePanel.getParent();
        if (parent != null) {
          parent.revalidate();
          parent.repaint();
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      dragStartScreenX = -1;
      originalDuration = -1;
      resizing = false;
    }

    // Nécessaire pour MouseMotionListener, mais non utilisé ici
    @Override
    public void mouseMoved(MouseEvent e) {
    }
  }

  public class ButtonEffect extends MouseAdapter {

    JButton button;
    private Color normalColor = new Color(230, 230, 230);
    private Color hoverColor = new Color(200, 200, 200);

    ButtonEffect(JButton button) {
      this.button = button;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      button.setBackground(hoverColor);
      button.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
      button.setBackground(normalColor);
      button.repaint();
    }
  }

  public class ViewPanelMouseController extends MouseAdapter {

    ImageViewPanel panel;
    ViewPanelController viewPanelController;

    public ViewPanelMouseController(ImageViewPanel panel,
        ViewPanelController viewPanelController) {
      this.panel = panel;
      this.viewPanelController = viewPanelController;
    }

    @Override
    public void mousePressed(MouseEvent e) {
      if (e.getButton() == MouseEvent.BUTTON1) { // Left mouse button
        TransferHandler handler = panel.getTransferHandler();
        handler.exportAsDrag(panel, e, TransferHandler.COPY);
      } else if (e.getButton() == MouseEvent.BUTTON3) { // Right mouse button
        JPopupMenu menu = new JPopupMenu();
        JMenuItem del = new JMenuItem("Delete");
        del.addActionListener(event -> {
          viewPanelController.removeImageViewPanel(panel);
        });
        menu.add(del);
        menu.show(panel, e.getX(), e.getY());
      }
    }
  }

}
