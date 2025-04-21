package com.upc.controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController extends MouseAdapter {

  @Override
  public void mousePressed(MouseEvent e) {
    JLabel c = (JLabel) e.getSource();
    TransferHandler handler = c.getTransferHandler();
    handler.exportAsDrag(c, e, TransferHandler.COPY);
  }
}
