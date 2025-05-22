package com.upc.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.upc.view.AnimeViewPanel;
import com.upc.view.DisplayAnimePanel;

public class AnimaViewPanelController {

  AnimeViewPanel animeViewPanel;
  TimeLinePanelController timeLinePanelController;
  File imageDirectory;

  AnimaViewPanelController(AnimeViewPanel animeViewPanel, TimeLinePanelController timeLinePanel, File imageDirectory) {
    this.animeViewPanel = animeViewPanel;
    this.timeLinePanelController = timeLinePanel;
    this.imageDirectory = imageDirectory;
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
        File imageFile = new File(imageDirectory, image.getDescription());
        ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());

        int duration = entry.getValue();

        JPanel imagePanel = new DisplayAnimePanel(originalIcon);
        imagePanel.setPreferredSize(animeViewPanel.getAnimeViewPanel().getSize());

        SwingUtilities.invokeLater(() -> {
          animeViewPanel.getAnimeViewPanel().removeAll();
          animeViewPanel.getAnimeViewPanel().add(imagePanel);
          animeViewPanel.getAnimeViewPanel().revalidate();
          animeViewPanel.getAnimeViewPanel().repaint();
        });

        try {
          Thread.sleep(duration);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
      SwingUtilities.invokeLater(animeViewPanel.getAnimeViewPanel()::removeAll);
    }).start();
  }

}
