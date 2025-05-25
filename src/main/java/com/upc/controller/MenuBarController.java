package com.upc.controller;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Contrôleur pour la gestion des actions de la barre de menu.
 * Permet d'ouvrir et de sauvegarder des fichiers via des boîtes de dialogue.
 */
public class MenuBarController {

  /**
   * Ouvre une boîte de dialogue pour sélectionner un fichier image à ouvrir.
   */
  public static void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Open File");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      System.out.println("Selected file: " + selectedFile.getAbsolutePath());
      // Ajouter ici la logique de traitement du fichier sélectionné si besoin
    } else {
      System.out.println("File selection canceled.");
    }
  }

  /**
   * Ouvre une boîte de dialogue pour sauvegarder un fichier texte.
   */
  public static void saveFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save File");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

    int result = fileChooser.showSaveDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
      File selectedFile = fileChooser.getSelectedFile();
      try (FileWriter writer = new FileWriter(selectedFile)) {
        writer.write("Example content to save."); // Remplacer par le contenu réel à sauvegarder
        System.out.println("File saved: " + selectedFile.getAbsolutePath());
      } catch (IOException e) {
        System.err.println("Error saving file: " + e.getMessage());
      }
    } else {
      System.out.println("File save canceled.");
    }
  }

}
