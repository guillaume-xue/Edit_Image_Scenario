package com.upc.view;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Image;
import java.io.File;

public class AnimeViewPanel extends JPanel {

  private JPanel animeViewPanel;
  private JButton startButton;
  private JButton breakButton;
  private JLabel timer;

  public AnimeViewPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.setBackground(Color.LIGHT_GRAY);
    JLabel titleLabel = new JLabel("Anime View");
    titleLabel.setFont(titleLabel.getFont().deriveFont(12f));
    topPanel.add(Box.createHorizontalStrut(10));
    topPanel.add(titleLabel);
    topPanel.add(Box.createHorizontalGlue());

    animeViewPanel = new JPanel();
    animeViewPanel.setLayout(new BoxLayout(animeViewPanel, BoxLayout.X_AXIS));
    animeViewPanel.setBackground(Color.WHITE);
    animeViewPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    animeViewPanel.setPreferredSize(new java.awt.Dimension(800, 600));
    animeViewPanel.setMaximumSize(new java.awt.Dimension(800, 600));

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
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
    breakButton.setVisible(false);

    timer = new JLabel("00:00:00");

    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(timer);
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(startButton);
    buttonPanel.add(breakButton);
    buttonPanel.add(Box.createHorizontalStrut(timer.getPreferredSize().width));
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(Box.createHorizontalStrut(10));

    add(topPanel);
    add(animeViewPanel);
    add(buttonPanel);
  }

  public void setTimer(String time) {
    timer.setText(time);
  }

  public JButton getStartButton() {
    return startButton;
  }

  public JButton getPauseButton() {
    return breakButton;
  }

  public JPanel getAnimeViewPanel() {
    return animeViewPanel;
  }
}
