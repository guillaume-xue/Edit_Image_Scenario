package com.upc.model;

import javax.swing.ImageIcon;

/**
 * Classe utilitaire pour le chargement d'images sous forme d'ImageIcon.
 */
public class ChargeImage {

  /**
   * Charge une image à partir d'un chemin donné et lui associe une description.
   *
   * @param pathIcon        chemin du fichier image
   * @param descriptionIcon description à associer à l'ImageIcon
   * @return                ImageIcon correspondant à l'image chargée
   */
  public static ImageIcon chargeImage(String pathIcon, String descriptionIcon) {
    ImageIcon icon = new ImageIcon(pathIcon);
    icon.setDescription(descriptionIcon);
    return icon;
  }

}
