package com.upc.controller;

import javax.swing.*;

import com.upc.view.TimeLinePanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController {

  public class TimeLinePanelMouseController extends MouseAdapter {

    private Box verticalBox;
    private TimeLinePanel timeLinePanel;

    public TimeLinePanelMouseController(Box verticalBox, TimeLinePanel timeLinePanel) {
      this.verticalBox = verticalBox;
      this.timeLinePanel = timeLinePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2) { // Double-click detected
        timeLinePanel.remove(verticalBox);
        timeLinePanel.revalidate(); // Refresh layout
        timeLinePanel.repaint(); // Repaint the panel
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
