package com.upc.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ResizablePanel extends JPanel {

  private JLabel duration;
  private JLabel iconLabel;

  public ResizablePanel() {
    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(200, 100));
    setMinimumSize(new Dimension(150, 100));
    duration = new JLabel();
  }

  public ResizablePanel(ImageIcon icon) {
    super();
    setPreferredSize(new Dimension(200, 100));
    iconLabel = new JLabel(icon);
    duration = new JLabel(getPreferredSize().width + "");
    add(duration);
    add(iconLabel);
  }

  public void setDuration(String duration) {
    this.duration.setText(duration);
  }

  public int getDuration() {
    return Integer.parseInt(duration.getText());
  }

  public ImageIcon getIcon() {
    return (ImageIcon) iconLabel.getIcon();
  }

}
