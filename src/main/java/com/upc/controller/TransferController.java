package com.upc.controller;

import javax.swing.*;

import java.awt.*;
import java.awt.datatransfer.*;

public class TransferController {

  public class TransferTimeLine extends TransferHandler {

    private TimeLinePanelController timeLinePanel;

    public TransferTimeLine(TimeLinePanelController timeLinePanel) {
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
        String description = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        imageIcon.setDescription(description);
        timeLinePanel.addImageLabel(imageIcon, 100); // Add the vertical box to the timeLinePanel
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
      ImageIcon icon = (ImageIcon) label.getIcon();
      return new ImageIconTransferable(icon);
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
        String description = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        ImageIcon imageIcon = new ImageIcon(image);
        imageIcon.setDescription(description);
        JLabel label = (JLabel) support.getComponent();
        label.setIcon(imageIcon);
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
    private final String description;

    public ImageIconTransferable(Icon icon) {
      this.image = ((ImageIcon) icon).getImage();
      this.description = ((ImageIcon) icon).getDescription();
    }

    public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { DataFlavor.imageFlavor, DataFlavor.stringFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
      return flavor.equals(DataFlavor.imageFlavor) || flavor.equals(DataFlavor.stringFlavor);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
      if (flavor.equals(DataFlavor.imageFlavor)) {
        return image;
      } else if (flavor.equals(DataFlavor.stringFlavor)) {
        return description;
      } else {
        throw new UnsupportedFlavorException(flavor);
      }
    }
  }
}
