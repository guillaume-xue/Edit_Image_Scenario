package com.upc.view;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

public class AnimeViewPanel extends JPanel {

  private JPanel animeViewPanel;
  private JButton startButton;
  private JButton breakButton;
  private JButton backButton;
  private JButton advanceButton;
  private JButton loopButton;
  private JLabel timer;
  private JSlider timelineSlider;

  public AnimeViewPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.setBackground(Color.LIGHT_GRAY);
    topPanel.setPreferredSize(new Dimension(100, 20));
    topPanel.setMinimumSize(new Dimension(100, 20));
    topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    JLabel titleLabel = new JLabel("Anime View");
    titleLabel.setFont(titleLabel.getFont().deriveFont(12f));
    topPanel.add(Box.createHorizontalStrut(10));
    topPanel.add(titleLabel);
    topPanel.add(Box.createHorizontalGlue());

    animeViewPanel = new JPanel();
    animeViewPanel.setLayout(new BorderLayout());
    animeViewPanel.setBackground(Color.WHITE);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    buttonPanel.setPreferredSize(new Dimension(100, 40));
    buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    buttonPanel.setMinimumSize(new Dimension(100, 40));
    File startIcon = new File("src/main/resources/Icon/startIcon.png");
    File breakIcon = new File("src/main/resources/Icon/breakIcon.png");
    File backIcon = new File("src/main/resources/Icon/back.png");
    File advanceIcon = new File("src/main/resources/Icon/advance.png");
    File loopIcon = new File("src/main/resources/Icon/loop.png");
    Image startImg = null;
    Image breakImg = null;
    Image backImg = null;
    Image advanceImg = null;
    Image loopImg = null;
    try {
      startImg = ImageIO.read(startIcon).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
      breakImg = ImageIO.read(breakIcon).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
      backImg = ImageIO.read(backIcon).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
      advanceImg = ImageIO.read(advanceIcon).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
      loopImg = ImageIO.read(loopIcon).getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
    } catch (Exception e) {
      e.printStackTrace();
    }
    startButton = new JButton(new ImageIcon(startImg));
    breakButton = new JButton(new ImageIcon(breakImg));
    backButton = new JButton(new ImageIcon(backImg));
    advanceButton = new JButton(new ImageIcon(advanceImg));
    loopButton = new JButton(new ImageIcon(loopImg));
    breakButton.setVisible(false);

    timer = new JLabel("00:00:00");

    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(timer);
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(Box.createHorizontalStrut(loopButton.getPreferredSize().width));
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(backButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(startButton);
    buttonPanel.add(breakButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(advanceButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(loopButton);
    buttonPanel.add(Box.createHorizontalStrut(10));
    buttonPanel.add(Box.createHorizontalStrut(timer.getPreferredSize().width));
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(Box.createHorizontalStrut(10));

    // Initialize the timeline slider
    timelineSlider = new JSlider(0, 1000, 0); // Default range, will be updated dynamically
    timelineSlider.setPaintTicks(true);
    timelineSlider.setPaintLabels(true);

    add(topPanel);
    add(Box.createGlue());
    add(animeViewPanel);
    add(Box.createGlue());
    add(buttonPanel);
    add(timelineSlider, BorderLayout.SOUTH);
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

  public JButton getBackButton() {
    return backButton;
  }

  public JButton getAdvanceButton() {
    return advanceButton;
  }

  // Getter for the timeline slider
  public JSlider getTimelineSlider() {
    return timelineSlider;
  }
}
