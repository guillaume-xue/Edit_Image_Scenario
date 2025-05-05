package com.upc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NewProjetFrame extends JFrame {

  private JButton browserButton;
  private JButton createButton;
  private JButton cancelButton;
  private JTextArea name;
  private JTextArea location;
  private JLabel directoryLabel;

  public NewProjetFrame() {
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
  }

  public JPanel emptyPanel() {
    JPanel emptyPanel = new JPanel();
    emptyPanel.setBackground(new Color(255, 255, 255));
    return emptyPanel;
  }

  public void customizeTextArea(JTextArea... textArea) {
    for (JTextArea text : textArea) {
      text.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
      text.setLineWrap(true);
      text.setWrapStyleWord(true);
      text.setBackground(new Color(255, 255, 255));
      text.setPreferredSize(new Dimension(200, 20));
      text.setMinimumSize(new Dimension(200, 20));
      text.setMaximumSize(new Dimension(200, 20));
    }
  }

  public void customizeJLabel(JLabel... labels) {
    for (JLabel label : labels) {
      label.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
      label.setBackground(new Color(255, 255, 255));
      label.setPreferredSize(new Dimension(200, 20));
      label.setMinimumSize(new Dimension(200, 20));
      label.setMaximumSize(new Dimension(200, 20));
    }
  }

  public JPanel folderChooserPanel() {

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(new Color(255, 255, 255));
    mainPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

    JPanel folderChooserPanel = new JPanel();
    folderChooserPanel.setLayout(new BoxLayout(folderChooserPanel, BoxLayout.Y_AXIS));
    folderChooserPanel.setBackground(new Color(255, 255, 255));

    JPanel namePanel = new JPanel();
    namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
    namePanel.setBackground(new Color(255, 255, 255));
    namePanel.setPreferredSize(new Dimension(getWidth() - 64, 20));
    namePanel.setMinimumSize(new Dimension(getWidth() - 64, 20));
    namePanel.setMaximumSize(new Dimension(getWidth() - 64, 20));
    JLabel nameLabel = new JLabel("Name:");
    name = new JTextArea();
    name.setText("Project");
    name.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(name);
    namePanel.add(Box.createHorizontalStrut(20));
    namePanel.add(nameLabel);
    namePanel.add(Box.createHorizontalStrut(20));
    namePanel.add(scrollPane);
    namePanel.add(Box.createHorizontalStrut(20));

    JPanel locationPanel = new JPanel();
    locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.X_AXIS));
    locationPanel.setBackground(new Color(255, 255, 255));
    locationPanel.setPreferredSize(new Dimension(getWidth() - 64, 20));
    locationPanel.setMinimumSize(new Dimension(getWidth() - 64, 20));
    locationPanel.setMaximumSize(new Dimension(getWidth() - 64, 20));
    JLabel locationLabel = new JLabel("Location:");
    location = new JTextArea();
    location.setText(System.getProperty("user.home"));
    JScrollPane scrollPane2 = new JScrollPane();
    scrollPane2.setViewportView(location);

    browserButton = new JButton("Browse");
    browserButton.setPreferredSize(new Dimension(80, 20));
    browserButton.setMinimumSize(new Dimension(80, 20));
    browserButton.setMaximumSize(new Dimension(80, 20));
    browserButton.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
    browserButton.setBackground(new Color(255, 255, 255));

    locationPanel.add(Box.createHorizontalStrut(20));
    locationPanel.add(locationLabel);
    locationPanel.add(Box.createHorizontalStrut(20));
    locationPanel.add(scrollPane2);
    locationPanel.add(Box.createHorizontalStrut(20));
    locationPanel.add(browserButton);
    locationPanel.add(Box.createHorizontalStrut(20));

    JPanel directoryPanel = new JPanel();
    directoryPanel.setBackground(new Color(255, 255, 255));
    directoryPanel.setPreferredSize(new Dimension(getWidth() - 64, 20));
    directoryPanel.setMinimumSize(new Dimension(getWidth() - 64, 20));
    directoryPanel.setMaximumSize(new Dimension(getWidth() - 64, 20));
    directoryPanel.setLayout(new BoxLayout(directoryPanel, BoxLayout.X_AXIS));
    directoryLabel = new JLabel(System.getProperty("user.home") + "/Project");
    directoryLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
    directoryLabel.setBackground(new Color(255, 255, 255));
    JLabel emptyLabel = new JLabel();
    directoryPanel.add(Box.createHorizontalStrut(20));
    directoryPanel.add(emptyLabel);
    directoryPanel.add(Box.createHorizontalStrut(22));
    directoryPanel.add(directoryLabel);

    customizeJLabel(nameLabel, locationLabel, emptyLabel);
    customizeTextArea(name, location);

    folderChooserPanel.add(namePanel);
    folderChooserPanel.add(Box.createVerticalStrut(20));
    folderChooserPanel.add(locationPanel);
    folderChooserPanel.add(directoryPanel);
    folderChooserPanel.add(Box.createVerticalStrut(20));

    JPanel buttunPanel = new JPanel();
    buttunPanel.setLayout(new BoxLayout(buttunPanel, BoxLayout.X_AXIS));
    buttunPanel.setBackground(new Color(255, 255, 255));
    buttunPanel.setPreferredSize(new Dimension(getWidth() - 64, 80));
    buttunPanel.setMinimumSize(new Dimension(getWidth() - 64, 80));
    buttunPanel.setMaximumSize(new Dimension(getWidth() - 64, 80));
    createButton = new JButton("Create");
    cancelButton = new JButton("Cancel");
    buttunPanel.add(Box.createHorizontalGlue());
    buttunPanel.add(createButton);
    buttunPanel.add(Box.createHorizontalStrut(20));
    buttunPanel.add(cancelButton);

    mainPanel.add(Box.createVerticalStrut(20));
    mainPanel.add(folderChooserPanel);
    mainPanel.add(Box.createVerticalStrut(320));
    mainPanel.add(buttunPanel);

    return mainPanel;
  }

  public void launch() {
    JPanel folderChooserPanel = folderChooserPanel();
    add(folderChooserPanel);
    setVisible(true);
  }

  public JButton getBrowserButton() {
    return browserButton;
  }

  public JButton getCreateButton() {
    return createButton;
  }

  public JButton getCancelButton() {
    return cancelButton;
  }

  public JTextArea getNameTextArea() {
    return name;
  }

  public JTextArea getLocationTextArea() {
    return location;
  }

  public JLabel getDirectoryLabel() {
    return directoryLabel;
  }
}
