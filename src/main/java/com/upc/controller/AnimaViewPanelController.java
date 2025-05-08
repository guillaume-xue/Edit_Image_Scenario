package com.upc.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.upc.view.AnimeViewPanel;

public class AnimaViewPanelController {

  AnimeViewPanel animeViewPanel;
  TimeLinePanelController timeLinePanelController;

  AnimaViewPanelController(AnimeViewPanel animeViewPanel, TimeLinePanelController timeLinePanel) {
    this.animeViewPanel = animeViewPanel;
    this.timeLinePanelController = timeLinePanel;
    initialize();
  }

  private void initialize() {
    animeViewPanel.getStartButton()
        .addActionListener(e -> animatePanel(this.timeLinePanelController.getImageCopiesWithDurations()));
  }

  public void animatePanel(ArrayList<Map.Entry<ImageIcon, Integer>> imageWithDuration) {
    new Thread(() -> {
      for (Map.Entry<ImageIcon, Integer> entry : imageWithDuration) {
        ImageIcon image = entry.getKey();
        ImageIcon originalIcon = new ImageIcon(image.getDescription());

        int duration = entry.getValue();

        JLabel label = new JLabel(originalIcon);
        label.setBounds(0, 0, originalIcon.getIconWidth(), originalIcon.getIconHeight());
        SwingUtilities.invokeLater(() -> {
          animeViewPanel.removeAll();
          animeViewPanel.add(label);
          animeViewPanel.repaint();
        });

        try {
          Thread.sleep(duration);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
      SwingUtilities.invokeLater(animeViewPanel::removeAll);
    }).start();
  }

}
