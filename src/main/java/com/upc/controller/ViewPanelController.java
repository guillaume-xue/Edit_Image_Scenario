package com.upc.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.upc.model.ViewImage;
import com.upc.view.MainFrame;
import com.upc.view.ViewPanel;

public class ViewPanelController {

  private ViewPanel viewPanel;
  private ViewImage viewImage;
  private MainFrame mainFrame;
  private TransferController transferController;
  private MouseController mouseController;

  public ViewPanelController(ViewPanel viewPanel, MainFrame mainFrame,
      TransferController transferController, MouseController mouseController, String imageDirectory) {
    this.viewPanel = viewPanel;
    this.mainFrame = mainFrame;
    this.transferController = transferController;
    this.mouseController = mouseController;
    this.viewImage = new ViewImage();
    initActionListener(imageDirectory);
  }

  public void initActionListener(String imageDirectory) {
    viewPanel.getAddButton().addActionListener(e -> {
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
          ImageIcon originalIcon = new ImageIcon(selectedFile.getAbsolutePath());
          Image resizedImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
          ImageIcon resizedIcon = new ImageIcon(resizedImage);
          resizedIcon.setDescription(selectedFile.getAbsolutePath());
          viewImage.addImageIcon(resizedIcon);
          displayImages(viewImage.getImageIcons()); // Refresh the view panel
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    });
  }

  public void displayImages(ArrayList<ImageIcon> imageIcons) {
    // Clear existing components
    viewPanel.getMainPanel().removeAll();
    if (imageIcons != null && !imageIcons.isEmpty()) {
      for (ImageIcon imageIcon : imageIcons) {
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setTransferHandler(transferController.new TransferViewPanel()); // Enable drag functionality
        imageLabel.addMouseListener(mouseController.new ViewPanelMouseController());
        viewPanel.getMainPanel().add(imageLabel);
      }
    } else {
      JLabel noImagesLabel = new JLabel("No images found in /resources/image");
      viewPanel.getMainPanel().add(noImagesLabel);
    }
    viewPanel.getMainPanel().revalidate(); // Refresh layout
    viewPanel.getMainPanel().repaint(); // Repaint the panel
  }
}
