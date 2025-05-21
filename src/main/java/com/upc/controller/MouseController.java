package com.upc.controller;

import javax.swing.*;

import com.upc.view.ResizablePanel;
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
      if (e.getClickCount() == 2) { // Double-click detected
        timeLinePanelController.removeImageLabel(resizablePanel);
      }
    }
  }

  public class ViewPanelMouseController extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      JLabel c = (JLabel) e.getSource();
      TransferHandler handler = c.getTransferHandler();
      handler.exportAsDrag(c, e, TransferHandler.COPY);
    }
  }

}
