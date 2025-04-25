package com.upc.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class AnimeViewPanel extends JPanel {

  public AnimeViewPanel() {
    super();
    setLayout(null); // Use absolute positioning for animation
  }

  public void animatePanel(ArrayList<Map.Entry<ImageIcon, Integer>> imageWithDuration) {
    new Thread(() -> {
      for (Map.Entry<ImageIcon, Integer> entry : imageWithDuration) {
        ImageIcon image = entry.getKey();
        int duration = entry.getValue();

        JLabel label = new JLabel(image);
        label.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
        SwingUtilities.invokeLater(() -> {
          removeAll();
          add(label);
          repaint();
        });

        try {
          Thread.sleep(duration);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
      SwingUtilities.invokeLater(this::removeAll);
    }).start();
  }
}
