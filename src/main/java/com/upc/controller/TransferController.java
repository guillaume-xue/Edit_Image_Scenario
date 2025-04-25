package com.upc.controller;

import javax.swing.*;

import com.upc.view.TimeLinePanel;

import java.awt.*;
import java.awt.datatransfer.*;

public class TransferController {

  public class TransferTimeLine extends TransferHandler {

    private TimeLinePanel timeLinePanel;

    public TransferTimeLine(TimeLinePanel timeLinePanel) {
      this.timeLinePanel = timeLinePanel;
    }

    @Override
    public boolean canImport(TransferSupport support) {
      return support.isDataFlavorSupported(DataFlavor.imageFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
      if (!canImport(support)) {
        return false;
      }
      try {
        ImageIcon imageIcon = new ImageIcon(
            (Image) support.getTransferable().getTransferData(DataFlavor.imageFlavor));
        timeLinePanel.addImageLabel(imageIcon); // Add the vertical box to the timeLinePanel
        return true;
      } catch (UnsupportedFlavorException | java.io.IOException e) {
        e.printStackTrace();
      }
      return false;
    }
  }

  // Classe pour gérer le transfert d'images
  public class TransferViewPanel extends TransferHandler {
    @Override
    protected Transferable createTransferable(JComponent c) {
      JLabel label = (JLabel) c;
      return new ImageIconTransferable(label.getIcon());
    }

    @Override
    public int getSourceActions(JComponent c) {
      return COPY;
    }

    @Override
    public boolean canImport(TransferSupport support) {
      return support.isDataFlavorSupported(DataFlavor.imageFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
      if (!canImport(support)) {
        return false;
      }

      try {
        Image image = (Image) support.getTransferable().getTransferData(DataFlavor.imageFlavor);
        JLabel label = (JLabel) support.getComponent();
        label.setIcon(new ImageIcon(image));
        return true;
      } catch (Exception e) {
        e.printStackTrace();
      }
      return false;
    }
  }

  // Classe pour rendre ImageIcon transférable
  class ImageIconTransferable implements Transferable {
    private final Image image;

    public ImageIconTransferable(Icon icon) {
      this.image = ((ImageIcon) icon).getImage();
    }

    public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { DataFlavor.imageFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(DataFlavor.imageFlavor);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (!isDataFlavorSupported(flavor))
        throw new UnsupportedFlavorException(flavor);
      return image;
    }
  }
}
