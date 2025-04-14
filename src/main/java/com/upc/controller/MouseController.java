package com.upc.controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.upc.view.MainFrame;

public class MouseController {

  private MainFrame mainFrame;

  public MouseController(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    init();
  }

  public void init() {
    // Add MouseController to all JLabels in the ViewPanel
    for (JLabel label : mainFrame.getViewPanel().getImageLabels()) {
      label.addMouseListener(new DragMouseAdapter());
    }
  }

  public static class DragMouseAdapter extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      JComponent component = (JComponent) e.getSource();
      TransferHandler handler = component.getTransferHandler();
      if (handler != null) {
        handler.exportAsDrag(component, e, TransferHandler.COPY); // Initiate drag with COPY action
      }
    }
  }
}
