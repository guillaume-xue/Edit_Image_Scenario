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
  private volatile boolean paused = false;
  private volatile boolean ended = true;
  private Thread animationThread;

  AnimaViewPanelController(AnimeViewPanel animeViewPanel, TimeLinePanelController timeLinePanel, File imageDirectory) {
    this.animeViewPanel = animeViewPanel;
    this.timeLinePanelController = timeLinePanel;
    this.imageDirectory = imageDirectory;
    initialize();
  }

  private void initialize() {
    animeViewPanel.getStartButton().addActionListener(e -> {
      animeViewPanel.getStartButton().setVisible(false);
      animeViewPanel.getPauseButton().setVisible(true);
      if (ended) {
        animatePanel(this.timeLinePanelController.getImageCopiesWithDurations());
      } else {
        resumeAnimation();
      }
    });
    animeViewPanel.getPauseButton().addActionListener(e -> {
      animeViewPanel.getStartButton().setVisible(true);
      animeViewPanel.getPauseButton().setVisible(false);
      pauseAnimation();
    });
  }

  public void pauseAnimation() {
    paused = true;
  }

  public void resumeAnimation() {
    paused = false;
    synchronized (this) {
      notifyAll();
    }
  }

  public void animatePanel(ArrayList<Map.Entry<ImageIcon, Integer>> imageWithDuration) {
    SwingUtilities.invokeLater(() -> ended = false);

    // ...existing code...
    animationThread = new Thread(() -> {
      long startTime = System.currentTimeMillis();
      long elapsed = 0;
      long pauseAccum = 0; // total time spent paused

      for (Map.Entry<ImageIcon, Integer> entry : imageWithDuration) {
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
        while (System.currentTimeMillis() - frameStart - framePauseAccum < duration) {
          // Pause logic inside timer loop
          synchronized (this) {
            if (paused) {
              long pauseStart = System.currentTimeMillis();
              while (paused) {
                try {
                  wait();
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  return;
                }
              }
              long pauseEnd = System.currentTimeMillis();
              pauseAccum += pauseEnd - pauseStart;
              framePauseAccum += pauseEnd - pauseStart;
            }
          }
          elapsed = System.currentTimeMillis() - startTime - pauseAccum;
          String timerText = formatMillis(elapsed);
          SwingUtilities.invokeLater(() -> animeViewPanel.setTimer(timerText));
          try {
            Thread.sleep(100); // update timer every 100ms
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
          }
        }
        if (Thread.currentThread().isInterrupted())
          break;
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

}
