package com.upc.model;

import java.util.ArrayList;
import javax.swing.ImageIcon;

public class ViewImage {

  private static ArrayList<ImageIcon> imageIcons;

  public ViewImage() {
    imageIcons = new ArrayList<>();
  }

  public void addImageIcon(ImageIcon imageIcon) {
    imageIcons.add(imageIcon);
  }

  public ArrayList<ImageIcon> getImageIcons() {
    return imageIcons;
  }
}
