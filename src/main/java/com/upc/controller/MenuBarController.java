package com.upc.controller;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MenuBarController {

  public void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Open File");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      System.out.println("Selected file: " + selectedFile.getAbsolutePath());
      // Add logic to process the selected file if needed
    } else {
      System.out.println("File selection canceled.");
    }
  }
}
