package com.upc.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class TransferController extends TransferHandler {

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
