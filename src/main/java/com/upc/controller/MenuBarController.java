package com.upc.controller;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MenuBarController {

  public static void openFile() {
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

  public static void saveFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save File");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

    int result = fileChooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      try (FileWriter writer = new FileWriter(selectedFile)) {
        writer.write("Example content to save."); // Replace with actual content
        System.out.println("File saved: " + selectedFile.getAbsolutePath());
      } catch (IOException e) {
        System.err.println("Error saving file: " + e.getMessage());
      }
    } else {
      System.out.println("File save canceled.");
    }
  }

}
