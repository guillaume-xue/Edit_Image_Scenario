package com.upc.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.upc.view.ImageViewPanel;
import com.upc.view.MainFrame;
import com.upc.view.ViewPanel;

public class ViewPanelController {

  private ViewPanel viewPanel;
  private MainFrame mainFrame;
  private TransferController transferController;
  private MouseController mouseController;

  public ViewPanelController(ViewPanel viewPanel, MainFrame mainFrame,
      TransferController transferController, MouseController mouseController, String imageDirectory) {
    this.viewPanel = viewPanel;
    this.mainFrame = mainFrame;
    this.transferController = transferController;
    this.mouseController = mouseController;
    initActionListener(imageDirectory);
    initViewPanel(imageDirectory);
  }

  public void initViewPanel(String imageDirectory) {
    File directory = new File(imageDirectory);
    viewPanel.getMainPanel().add(Box.createHorizontalStrut(10));
    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles((dir, name) -> {
        String lowerName = name.toLowerCase();
        return lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".png") ||
            lowerName.endsWith(".gif") || lowerName.endsWith(".bmp");
      });
      if (files != null && files.length > 0) {
        for (File file : files) {
          ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
          originalIcon.setDescription(file.getName());
          ImageViewPanel viewImage = new ImageViewPanel(originalIcon);
          viewImage.setTransferHandler(transferController.new TransferViewPanel()); // Enable drag functionality
          viewImage.addMouseListener(mouseController.new ViewPanelMouseController());
          viewPanel.getMainPanel().add(viewImage);
          viewPanel.getMainPanel().add(Box.createHorizontalStrut(10)); // Add space between images
        }
      }
    }
    viewPanel.getMainPanel().revalidate();
    viewPanel.getMainPanel().repaint();
  }

  public void initActionListener(String imageDirectory) {
    viewPanel.getImportButton().addActionListener(e -> {
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
          ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
          icon.setDescription(destinationFile.getName());
          ImageViewPanel viewImage = new ImageViewPanel(icon);
          viewImage.setTransferHandler(transferController.new TransferViewPanel()); // Enable drag functionality
          viewImage.addMouseListener(mouseController.new ViewPanelMouseController());
          viewPanel.getMainPanel().add(viewImage);
          viewPanel.getMainPanel().add(Box.createHorizontalStrut(10)); // Add space between images
          viewPanel.getMainPanel().revalidate();
          viewPanel.getMainPanel().repaint();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });
  }
}
