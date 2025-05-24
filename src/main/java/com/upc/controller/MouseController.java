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

    public TimeLinePanelMouseController(ResizablePanel resizablePanel,
        TimeLinePanelController timeLinePanelController) {
      this.resizablePanel = resizablePanel;
      this.timeLinePanelController = timeLinePanelController;
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
        menu.show(resizablePanel, e.getX(), e.getY());
      }
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
