package com.upc.controller;

import javax.swing.*;

import com.upc.view.ImageViewPanel;

import java.awt.*;
import java.awt.datatransfer.*;

/**
 * Contrôleur pour la gestion du drag & drop (transfert d'images) entre les différents panneaux.
 * Permet le transfert d'images entre la timeline, le panneau de dessin et la vue des images.
 */
public class TransferController {

  /**
   * Handler pour le transfert d'images vers la timeline.
   */
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
        timeLinePanel.addImageLabel(imageIcon, 100); // Ajoute l'image à la timeline
        return true;
      } catch (UnsupportedFlavorException | java.io.IOException e) {
        e.printStackTrace();
      }
      return false;
    }
  }

  /**
   * Handler pour le transfert d'images vers le panneau de dessin.
   */
  public class TransferDrawing extends TransferHandler {

    private DrawingController drawingController;

    public TransferDrawing(DrawingController drawingController) {
      this.drawingController = drawingController;
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
        drawingController.draw(imageIcon);
        return true;
      } catch (UnsupportedFlavorException | java.io.IOException e) {
        e.printStackTrace();
      }
      return false;
    }
  }

  /**
   * Handler pour le transfert d'images depuis la vue des images (drag).
   */
  public class TransferViewPanel extends TransferHandler {
    @Override
    protected Transferable createTransferable(JComponent c) {
      ImageViewPanel panel = (ImageViewPanel) c;
      ImageIcon icon = panel.getImageIcon();
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
      return false;
    }
  }

  /**
   * Classe utilitaire pour rendre un ImageIcon transférable (drag & drop).
   */
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
