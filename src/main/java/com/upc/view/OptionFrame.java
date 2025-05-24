package com.upc.view;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class OptionFrame extends JFrame {

  private JButton newProjet;
  private JButton openProjet;

  public OptionFrame() {
    super();
    setResizable(false);
    init();
  }

  public void init() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Welcome to Image Editor");
    setSize(800, 600);
    setLocationRelativeTo(null);
    setVisible(true);

    JPanel newProjetPanel = createNewProjet();
    add(newProjetPanel);
  }

  private void setButtonDimensions(JButton... buttons) {
    int width = 178;
    int height = 48;
    for (JButton button : buttons) {
      button.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
      button.setHorizontalAlignment(SwingConstants.LEFT);
      button.setPreferredSize(new java.awt.Dimension(width, height));
      button.setMaximumSize(new java.awt.Dimension(width, height));
      button.setMinimumSize(new java.awt.Dimension(width, height));
    }
  }

  public JPanel createNewProjet() {
    JPanel newProjetPanel = new JPanel();
    newProjetPanel.setLayout(new BoxLayout(newProjetPanel, BoxLayout.Y_AXIS));
    newProjetPanel.setBackground(new Color(255, 255, 255));
    newProjetPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

    JLabel label = new JLabel("Welcome to Image Editor");
    label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
    newProjet = new JButton("  new project");
    openProjet = new JButton("  open project");
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    newProjet.setAlignmentX(JButton.CENTER_ALIGNMENT);
    openProjet.setAlignmentX(JButton.CENTER_ALIGNMENT);
    setButtonDimensions(newProjet, openProjet);
    try {
      File folderIcon = new File("src/main/resources/Icon/folderIcon.png");
      File plusIcon = new File("src/main/resources/Icon/plusIcon.png");
      Image folderImg = ImageIO.read(folderIcon).getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
      Image plusImg = ImageIO.read(plusIcon).getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
      ImageIcon folderImage = new ImageIcon(folderImg);
      ImageIcon plusImage = new ImageIcon(plusImg);
      newProjet.setIcon(plusImage);
      openProjet.setIcon(folderImage);

    } catch (IOException e) {
      e.printStackTrace();
    }

    newProjetPanel.add(Box.createVerticalStrut(128));
    newProjetPanel.add(label);
    newProjetPanel.add(Box.createVerticalStrut(64));
    newProjetPanel.add(newProjet);
    newProjetPanel.add(Box.createVerticalStrut(20));
    newProjetPanel.add(openProjet);
    return newProjetPanel;
  }

  public void launch() {
    setVisible(true);
  }

  public JButton getNewProjet() {
    return newProjet;
  }

  public JButton getOpenProjet() {
    return openProjet;
  }

}
