package com.upc.view;

import javax.swing.*;

import java.awt.*;

/**
 * Panneau de visualisation des images importées.
 * Contient un panneau principal pour l'affichage des images et un bouton d'importation.
 */
public class ViewPanel extends JPanel {

  // Panneau principal où sont affichées les images
  private JPanel mainPanel;
  // Bouton d'importation d'image
  private JButton importButton;
  // ScrollPane englobant le panneau principal
  JScrollPane scrollPane;

  /**
   * Constructeur du panneau de visualisation.
   * Initialise la structure, le bouton d'import et le panneau d'affichage.
   */
  public ViewPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setPreferredSize(new Dimension(400, 400));
    this.setMinimumSize(new Dimension(400, 400));
    this.setBackground(Color.WHITE);

    // Panneau principal pour les images
    this.mainPanel = new JPanel();
    mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    mainPanel.setMinimumSize(new Dimension(200, 200));
    mainPanel.setBackground(Color.WHITE);

    // ScrollPane pour permettre le scroll vertical sur les images
    scrollPane = new JScrollPane(mainPanel);
    scrollPane.setMinimumSize(new Dimension(200, 200));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    // Panneau pour le bouton d'importation
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setPreferredSize(new Dimension(1200, 40));
    buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    // Bouton d'importation d'image
    this.importButton = new JButton("Import");
    importButton.setPreferredSize(new Dimension(100, 30));
    importButton.setMaximumSize(new Dimension(100, 30));
    importButton.setMinimumSize(new Dimension(100, 30));

    buttonPanel.add(importButton);
    buttonPanel.add(Box.createHorizontalGlue());

    add(buttonPanel);
    add(scrollPane);
    add(Box.createVerticalStrut(20));
  }

  /** @return le bouton d'importation */
  public JButton getImportButton() {
    return importButton;
  }

  /** @return le panneau principal d'affichage des images */
  public JPanel getMainPanel() {
    return mainPanel;
  }

  /** @return le JScrollPane englobant le panneau principal */
  public JScrollPane getScrollPane() {
    return scrollPane;
  }

}
