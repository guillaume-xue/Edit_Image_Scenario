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

  private AnimeViewPanel animeViewPanel;
  private TimeLinePanelController timeLinePanelController;
  private File imageDirectory;
  private volatile boolean paused = false;
  private volatile boolean ended = true;
  private volatile long elapsed = 0;
  private volatile boolean loop = false;
  private volatile int currentImageIndex = 0;
  private ArrayList<Map.Entry<ImageIcon, Integer>> imageWithDuration = new ArrayList<>();

  AnimaViewPanelController(AnimeViewPanel animeViewPanel, TimeLinePanelController timeLinePanel, File imageDirectory) {
    this.animeViewPanel = animeViewPanel;
    this.timeLinePanelController = timeLinePanel;
    this.imageDirectory = imageDirectory;
  }

  public void initialize() {
    animeViewPanel.getStartButton().addActionListener(e -> {
      animeViewPanel.getStartButton().setVisible(false);
      animeViewPanel.getPauseButton().setVisible(true);
      if (ended) {
        this.imageWithDuration = timeLinePanelController.getImageCopiesWithDurations();
        currentImageIndex = 0;
        animeViewPanel.getTimelineSlider().setMaximum(imageWithDuration.size() - 1);
        animeViewPanel.getTimelineSlider().setValue(0);
        animatePanel();
      } else {
        resumeAnimation();
      }
    });
    animeViewPanel.getPauseButton().addActionListener(e -> {
      animeViewPanel.getStartButton().setVisible(true);
      animeViewPanel.getPauseButton().setVisible(false);
      pauseAnimation();
    });
    animeViewPanel.getBackButton().addActionListener(e -> {
      if (currentImageIndex > 1) {
        animeViewPanel.getStartButton().setVisible(true);
        animeViewPanel.getPauseButton().setVisible(false);
        pauseAnimation();
        currentImageIndex -= 2;
        animeViewPanel.getTimelineSlider().setValue(currentImageIndex);
      }
    });
    animeViewPanel.getAdvanceButton().addActionListener(e -> {
      if (currentImageIndex < imageWithDuration.size() - 1) {
        animeViewPanel.getStartButton().setVisible(true);
        animeViewPanel.getPauseButton().setVisible(false);
        pauseAnimation();
        currentImageIndex++;
        animeViewPanel.getTimelineSlider().setValue(currentImageIndex);
      }
    });
    animeViewPanel.getLoopButton().addActionListener(e -> {
      loop = true;
      animeViewPanel.getLoopButton().setVisible(false);
      animeViewPanel.getDirectButton().setVisible(true);
    });
    animeViewPanel.getDirectButton().addActionListener(e -> {
      loop = false;
      animeViewPanel.getLoopButton().setVisible(true);
      animeViewPanel.getDirectButton().setVisible(false);
    });
    animeViewPanel.getTimelineSlider().addChangeListener(e -> {
      currentImageIndex = animeViewPanel.getTimelineSlider().getValue();
      if (currentImageIndex < imageWithDuration.size()) {
        elapsed = calculateElapsedFromIndex(currentImageIndex);
        animeViewPanel.setTimer(formatMillis(elapsed));
        if (!paused) {
          pauseAnimation();
          resumeAnimation();
        }
      }
    });

  }

  private void pauseAnimation() {
    paused = true;
  }

  private void resumeAnimation() {
    paused = false;
    synchronized (this) {
      notifyAll();
    }
  }

  private void updateImagePanel(Map.Entry<ImageIcon, Integer> entry) {
    new Thread(() -> {
      ImageIcon image = entry.getKey();
      File imageFile = new File(imageDirectory, image.getDescription());
      ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());

      JPanel imagePanel = new DisplayAnimePanel(originalIcon);
      imagePanel.setPreferredSize(animeViewPanel.getAnimeViewPanel().getSize());

      SwingUtilities.invokeLater(() -> {
        animeViewPanel.getAnimeViewPanel().removeAll();
        animeViewPanel.getAnimeViewPanel().add(imagePanel);
        animeViewPanel.getAnimeViewPanel().revalidate();
        animeViewPanel.getAnimeViewPanel().repaint();
      });
    }).start();
  }

  private void animatePanel() {
    ended = false;

    Thread animationThread = new Thread(() -> {
      while (currentImageIndex < imageWithDuration.size()) {
        Map.Entry<ImageIcon, Integer> entry = imageWithDuration.get(currentImageIndex);
        if (paused) {
          synchronized (this) {
            try {
              wait();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          }
        }

        updateImagePanel(entry);

        elapsed += entry.getValue();
        animeViewPanel.setTimer(formatMillis(elapsed));
        animeViewPanel.getTimelineSlider().setValue(currentImageIndex);

        try {
          Thread.sleep(entry.getValue());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }

        currentImageIndex++;
        if (loop && currentImageIndex >= imageWithDuration.size()) {
          currentImageIndex = 0;
          elapsed = 0;
          animeViewPanel.getTimelineSlider().setValue(0);
        }
      }
      ended = true;
      animeViewPanel.getStartButton().setVisible(true);
      animeViewPanel.getPauseButton().setVisible(false);
    });
    animationThread.start();
  }

  private String formatMillis(long millis) {
    long seconds = millis / 1000;
    long minutes = seconds / 60;
    long hours = minutes / 60;
    seconds = seconds % 60;
    minutes = minutes % 60;
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  private long calculateElapsedFromIndex(int index) {
    long totalElapsed = 0;
    for (int i = 0; i < index; i++) {
      totalElapsed += imageWithDuration.get(i).getValue();
    }
    return totalElapsed;
  }

}
