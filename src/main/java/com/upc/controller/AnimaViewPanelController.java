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
  private volatile long pauseAccum = 0;
  private volatile int currentImageIndex = 0;
  private Thread animationThread;
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
    animeViewPanel.getTimelineSlider().addChangeListener(e -> {
      currentImageIndex = animeViewPanel.getTimelineSlider().getValue();
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

  private void animatePanel() {
    SwingUtilities.invokeLater(() -> ended = false);

    animationThread = new Thread(() -> {
      elapsed = 0;
      pauseAccum = 0;

      while (currentImageIndex < imageWithDuration.size() && !Thread.currentThread().isInterrupted()) {
        Map.Entry<ImageIcon, Integer> entry = imageWithDuration.get(currentImageIndex);
        animeViewPanel.getTimelineSlider().setValue(currentImageIndex - 1);
        // Pause logic
        synchronized (this) {
          while (paused) {
            long pauseStart = System.currentTimeMillis();
            try {
              wait();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return;
            }
            pauseAccum += System.currentTimeMillis() - pauseStart;
          }
        }

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

        // Timer update loop
        long frameStart = System.currentTimeMillis();
        long framePauseAccum = 0;
        while (System.currentTimeMillis() - frameStart - framePauseAccum < duration / 1000) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
          }
        }
        elapsed = calculateElapsedFromIndex(currentImageIndex)
            + (System.currentTimeMillis() - frameStart - framePauseAccum);
        String timerText = formatMillis(elapsed);
        SwingUtilities.invokeLater(() -> animeViewPanel.setTimer(timerText));
        if (Thread.currentThread().isInterrupted())
          break;
        currentImageIndex++;
      }
      SwingUtilities.invokeLater(() -> {
        animeViewPanel.getAnimeViewPanel().removeAll();
        animeViewPanel.getStartButton().setVisible(true);
        animeViewPanel.getPauseButton().setVisible(false);
        ended = true;
      });
    });

    animationThread.start();
    SwingUtilities.invokeLater(() -> {
      animeViewPanel.getAnimeViewPanel().revalidate();
      animeViewPanel.getAnimeViewPanel().repaint();
    });
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
