package com.upc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.sql.Time;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResizablePanel extends JPanel {

  private JLabel durationLabel;
  private ImageIcon icon;
  private double zoomFactor = 1.0;
  private int duration = 0; // durée réelle en ms (ou px selon votre logique)
  private TimeLinePanel parentTimeLinePanel; // Ajouté

  public ResizablePanel() {
    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(100, 100));
    setMinimumSize(new Dimension(100, 100));
    durationLabel = new JLabel("");
    add(durationLabel);
  }

  public ResizablePanel(ImageIcon icon, int duration) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(duration, 100));

    this.icon = icon;
    this.duration = duration; // stocke la vraie durée

    JPanel durationPanel = new JPanel();
    durationPanel.setLayout(new BoxLayout(durationPanel, BoxLayout.X_AXIS));
    durationPanel.setPreferredSize(new Dimension(100, 20));
    durationPanel.setMinimumSize(new Dimension(100, 20));
    durationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    durationLabel = new JLabel(duration + "");
    durationPanel.add(durationLabel);
    durationPanel.add(Box.createHorizontalStrut(10));

    add(durationPanel);
    add(Box.createHorizontalStrut(40));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (icon != null) {
      int iconWidth = 38;
      int iconHeight = 38;
      int y = 30;
      int count = 1 + (getWidth() / iconWidth);

      for (int i = 0; i < count; i++) {
        int x = i * iconWidth;
        g.drawImage(icon.getImage(), x, y, iconWidth, iconHeight, this);
      }
    }
  }

  public void setDuration(String durationStr) {
    try {
      int d = Integer.parseInt(durationStr);
      setDuration(d);
    } catch (NumberFormatException e) {
      // ignore
    }
  }

  public void setParentTimeLinePanel(TimeLinePanel parent) {
    this.parentTimeLinePanel = parent;
  }

  public void setDuration(int duration) {
    this.duration = duration;
    this.durationLabel.setText(Integer.toString(duration));
    updateWidthFromDuration(zoomFactor);
    if (parentTimeLinePanel != null) {
      parentTimeLinePanel.updateEndMarginPanel(); // Met à jour la marge à chaque modification
    }
  }

  public int getDuration() {
    return duration;
  }

  public ImageIcon getIcon() {
    return icon;
  }

  public void setZoomFactor(double zoomFactor) {
    this.zoomFactor = zoomFactor;
  }

  public double getZoomFactor() {
    return zoomFactor;
  }

  // Appelée lors du drag/redimensionnement
  public void updateDurationFromWidth() {
    int width = getWidth();
    int newDuration = (int) (width / zoomFactor);
    this.duration = newDuration;
    this.durationLabel.setText(Integer.toString(newDuration));
  }

  public void updateDurationFromWidth(double zoomFactor) {
    this.zoomFactor = zoomFactor;
    updateDurationFromWidth();
  }

  // Appelée lors du zoom ou de la création
  public void updateWidthFromDuration(double zoomFactor) {
    this.zoomFactor = zoomFactor;
    int width = (int) (duration * zoomFactor);
    int height = getPreferredSize().height;
    setPreferredSize(new Dimension(width, height));
    setMinimumSize(new Dimension(width, height));
    setMaximumSize(new Dimension(width, height));
    revalidate();
    repaint();
  }

  public TimeLinePanel getParentTimeLinePanel() {
    return parentTimeLinePanel;
  }
}