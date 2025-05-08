package com.upc.view;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class AnimeViewPanel extends JPanel {

  private JPanel animeViewPanel;
  private JButton startButton;
  private JButton breakButton;
  private TimeLinePanel timeLinePanel;

  public AnimeViewPanel(TimeLinePanel timeLinePanel) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.timeLinePanel = timeLinePanel;

    animeViewPanel = new JPanel();
    animeViewPanel.setLayout(new BoxLayout(animeViewPanel, BoxLayout.X_AXIS));
    animeViewPanel.setBackground(Color.WHITE);
    animeViewPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    animeViewPanel.setPreferredSize(new java.awt.Dimension(800, 600));
    animeViewPanel.setMaximumSize(new java.awt.Dimension(800, 600));

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.BLUE);
    buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    File startIcon = new File("src/main/resources/Icon/startIcon.png");
    File breakIcon = new File("src/main/resources/Icon/breakIcon.png");
    Image startImg = null;
    Image breakImg = null;
    try {
      startImg = ImageIO.read(startIcon).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
      breakImg = ImageIO.read(breakIcon).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
    } catch (Exception e) {
      e.printStackTrace();
    }
    startButton = new JButton(new ImageIcon(startImg));
    breakButton = new JButton(new ImageIcon(breakImg));
    startButton.addActionListener(e -> animatePanel(this.timeLinePanel.getImageCopiesWithDurations()));
    buttonPanel.add(startButton);
    buttonPanel.add(breakButton);

    add(animeViewPanel);
    add(buttonPanel);
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
