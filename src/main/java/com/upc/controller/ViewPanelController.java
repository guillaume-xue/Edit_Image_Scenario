package com.upc.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.upc.view.ImageViewPanel;
import com.upc.view.ImportButton;
import com.upc.view.MainFrame;
import com.upc.view.ViewPanel;

public class ViewPanelController {

  private ViewPanel viewPanel;
  private MainFrame mainFrame;
  private TransferController transferController;
  private MouseController mouseController;
  private ImportButton importButton;
  private JDialog loadingDialog;

  public ViewPanelController(ViewPanel viewPanel, MainFrame mainFrame,
      TransferController transferController, MouseController mouseController, String imageDirectory,
      JDialog loadingDialog) {
    this.viewPanel = viewPanel;
    this.mainFrame = mainFrame;
    this.transferController = transferController;
    this.mouseController = mouseController;
    this.importButton = new ImportButton();
    this.loadingDialog = loadingDialog;
    initMouseListener();
    initActionListener(imageDirectory);
  }

  private void addImageViewPanel(String pathIcon, String descriptionIcon) {
    // Charge et redimensionne l'image AVANT de créer l'ImageIcon
    ImageIcon tempIcon = new ImageIcon(pathIcon);
    int maxW = 120, maxH = 80;
    int iw = tempIcon.getIconWidth(), ih = tempIcon.getIconHeight();
    double ratio = Math.min((double) maxW / iw, (double) maxH / ih);
    int w = (int) (iw * ratio), h = (int) (ih * ratio);
    Image scaled = tempIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
    tempIcon.getImage().flush(); // Libère la mémoire de l'image originale

    ImageIcon scaledIcon = new ImageIcon(scaled);
    scaledIcon.setDescription(descriptionIcon);

    ImageViewPanel viewImage = new ImageViewPanel(scaledIcon);
    viewImage.setTransferHandler(transferController.new TransferViewPanel());
    viewImage.addMouseListener(mouseController.new ViewPanelMouseController(viewImage, this));
    viewPanel.getMainPanel().add(viewImage);
    updateMainPanelSize();
  }

  private void updateMainPanelSize() {
    int count = viewPanel.getMainPanel().getComponentCount();
    int width = Math.max(1, viewPanel.getMainPanel().getWidth() / 130);
    int height = (count / width + 1) * 90;
    viewPanel.getMainPanel().setPreferredSize(new Dimension(400, height));
    viewPanel.getScrollPane().setPreferredSize(new Dimension(400, height));
    viewPanel.getMainPanel().revalidate();
    viewPanel.getScrollPane().revalidate();
    viewPanel.getScrollPane().repaint();
  }

  public void initViewPanel(String imageDirectory) {

    File directory = new File(imageDirectory);
    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles((dir, name) -> {
        String lowerName = name.toLowerCase();
        return lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".png") ||
            lowerName.endsWith(".gif") || lowerName.endsWith(".bmp");
      });

      if (files != null && files.length > 0) {
        // Utilise SwingWorker pour charger les images en arrière-plan
        new SwingWorker<Void, File>() {
          @Override
          protected Void doInBackground() {
            for (File file : files) {
              publish(file);
            }
            return null;
          }

          @Override
          protected void process(List<File> chunks) {
            for (File file : chunks) {
              addImageViewPanel(file.getAbsolutePath(), file.getName());
            }
            viewPanel.getMainPanel().revalidate();
            viewPanel.getMainPanel().repaint();
          }

          @Override
          protected void done() {
            emptyViewPanel();
            viewPanel.revalidate();
            viewPanel.repaint();
            updateMainPanelSize();
            if (loadingDialog != null && loadingDialog.isVisible()) {
              loadingDialog.dispose();
            }
          }
        }.execute();
      } else {
        emptyViewPanel();
        viewPanel.getMainPanel().revalidate();
        viewPanel.getMainPanel().repaint();
      }
    } else {
      emptyViewPanel();
      viewPanel.getMainPanel().revalidate();
      viewPanel.getMainPanel().repaint();
    }
  }

  public void emptyViewPanel() {
    if (viewPanel.getMainPanel().getComponentCount() == 0) {
      viewPanel.getMainPanel().setLayout(new BorderLayout());
      viewPanel.getMainPanel().add(importButton, BorderLayout.CENTER);
      viewPanel.getMainPanel().add(Box.createHorizontalStrut(10), BorderLayout.EAST);
      viewPanel.getMainPanel().add(Box.createHorizontalStrut(10), BorderLayout.WEST);
    }
  }

  public void initMouseListener() {
    importButton.addMouseListener(mouseController.new ButtonEffect(importButton));
    viewPanel.getMainPanel().addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateMainPanelSize();
      }
    });
  }

  public void initActionListener(String imageDirectory) {
    viewPanel.getImportButton().addActionListener(e -> {
      ActionImport(imageDirectory);
    });
    importButton.addActionListener(e -> {
      ActionImport(imageDirectory);
    });
  }

  public void removeImportButton() {
    if (viewPanel.getMainPanel().getComponent(0) instanceof ImportButton) {
      viewPanel.getMainPanel().removeAll();
      viewPanel.getMainPanel().setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    }
  }

  public void removeImageViewPanel(ImageViewPanel imageViewPanel) {
    viewPanel.getMainPanel().remove(imageViewPanel);
    emptyViewPanel();
    updateMainPanelSize();
    viewPanel.getMainPanel().revalidate();
    viewPanel.getMainPanel().repaint();
  }

  private void ActionImport(String imageDirectory) {
    JFileChooser fileChooser = new JFileChooser(); // Set default directory
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Image Files", "jpg", "jpeg", "png", "gif", "bmp");
    fileChooser.setFileFilter(filter);
    int returnValue = fileChooser.showOpenDialog(mainFrame);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      File destinationDir = new File(imageDirectory);
      File destinationFile = new File(destinationDir, selectedFile.getName());
      try {
        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        removeImportButton();
        addImageViewPanel(destinationFile.getAbsolutePath(), selectedFile.getName());
        updateMainPanelSize();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    viewPanel.getMainPanel().revalidate();
    viewPanel.getMainPanel().repaint();
  }

  public void addOrUpdateImage(String imagePath, String imageName) {
    boolean found = false;
    for (Component comp : viewPanel.getMainPanel().getComponents()) {
        if (comp instanceof com.upc.view.ImageViewPanel) {
            ImageViewPanel panel = (ImageViewPanel) comp;
            ImageIcon icon = panel.getImageIcon();
            if (icon != null && imageName.equals(icon.getDescription())) {
                // Actualiser l'image (remplacer l'icône)
                ImageIcon newIcon = new ImageIcon(imagePath);
                int maxW = 120, maxH = 80;
                int iw = newIcon.getIconWidth(), ih = newIcon.getIconHeight();
                double ratio = Math.min((double) maxW / iw, (double) maxH / ih);
                int w = (int) (iw * ratio), h = (int) (ih * ratio);
                Image scaled = newIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                newIcon.getImage().flush();
                ImageIcon scaledIcon = new ImageIcon(scaled);
                scaledIcon.setDescription(imageName);
                panel.setImageIcon(scaledIcon);
                panel.repaint();
                found = true;
                break;
            }
        }
    }
    if (!found) {
        // Si non trouvé, on ajoute
        addImageViewPanel(imagePath, imageName);
    }
    viewPanel.getMainPanel().revalidate();
    viewPanel.getMainPanel().repaint();
  }
}
