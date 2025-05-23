package com.upc.model;

import javax.swing.ImageIcon;

public class ChargeImage {

  public static ImageIcon chargeImage(String pathIcon, String descriptionIcon) {
    ImageIcon icon = new ImageIcon(pathIcon);
    icon.setDescription(descriptionIcon);
    return icon;
  }

}
