package com.upc.view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Panneau principal de la timeline pour l'animation.
 * Permet d'afficher, de zoomer et de gérer dynamiquement les séquences/images sur la timeline.
 */
public class TimeLinePanel extends JPanel {
  // Panneau contenant les éléments de la timeline (scènes/images)
  private JPanel timeLineScene;
  // Panneau affichant les graduations temporelles
  private TimePanel numbersPanel;
  // Facteur de zoom courant
  private double zoomFactor = 1.0;
  // ScrollPane englobant la timeline
  private JScrollPane scrollPane;
  // Boutons de zoom avant/arrière
  private JButton zoomInButton;
  private JButton zoomOutButton;
  // Marge de fin pour permettre le scroll et l'ajout d'éléments
  private JPanel endMarginPanel = new JPanel();

  /**
   * Constructeur du panneau de timeline.
   * Initialise la structure, les boutons et les panneaux internes.
   */
  public TimeLinePanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(800, 200));
    setMinimumSize(new Dimension(400, 200));
    setBackground(Color.WHITE);

    // Panneau pour les boutons de zoom
    JPanel zoomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
    zoomInButton = new JButton("Zoom +");
    zoomOutButton = new JButton("Zoom -");
    zoomPanel.add(zoomInButton);
    zoomPanel.add(zoomOutButton);

    JPanel timeLine = new JPanel();
    timeLine.setLayout(new BoxLayout(timeLine, BoxLayout.Y_AXIS));
    timeLine.setBackground(Color.WHITE);

    // Panel personnalisé pour afficher les chiffres tous les 1000 pixels
    numbersPanel = new TimePanel();
    numbersPanel.setBackground(Color.LIGHT_GRAY);

    this.timeLineScene = new JPanel();
    timeLineScene.setLayout(new BoxLayout(timeLineScene, BoxLayout.X_AXIS));
    timeLineScene.setMinimumSize(new Dimension(400, 100));
    timeLineScene.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
    timeLineScene.setBackground(Color.WHITE);

    endMarginPanel.setBackground(Color.WHITE);
    endMarginPanel.setPreferredSize(new Dimension(5000, 100)); // Valeur initiale, sera ajustée

    timeLine.add(numbersPanel);
    timeLine.add(Box.createHorizontalStrut(40));
    timeLine.add(timeLineScene);
    timeLine.add(endMarginPanel); // Ajout de la marge à la fin
    timeLine.add(Box.createHorizontalStrut(40));

    scrollPane = new JScrollPane();
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setPreferredSize(new Dimension(0, 200));
    scrollPane.setMinimumSize(new Dimension(800, 200));
    scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
    scrollPane.setViewportView(timeLine);

    // Ajout du panneau de boutons en haut
    this.setLayout(new BorderLayout());
    this.add(zoomPanel, BorderLayout.NORTH);
    this.add(scrollPane, BorderLayout.CENTER);

    // Les boutons n'ont pas d'action tant que le contrôleur n'est pas défini
  }

  /** @return le bouton de zoom avant */
  public JButton getZoomInButton() {
    return zoomInButton;
  }

  /** @return le bouton de zoom arrière */
  public JButton getZoomOutButton() {
    return zoomOutButton;
  }

  /** @return le panneau contenant les éléments de la timeline */
  public JPanel getTimeLinePanel() {
    return timeLineScene;
  }

  /**
   * Met à jour dynamiquement la largeur de la marge de fin en fonction de la durée totale.
   * Permet d'assurer un espace de scroll et d'ajout d'éléments.
   */
  public void updateEndMarginPanel() {
    double zoom = zoomFactor;
    int totalDuration = 0;
    for (int i = 0; i < timeLineScene.getComponentCount(); i++) {
      java.awt.Component comp = timeLineScene.getComponent(i);
      if (comp instanceof ResizablePanel) {
        ResizablePanel rp = (ResizablePanel) comp;
        totalDuration += rp.getDuration();
      }
    }

    int marginWidth = (int) (1000 + totalDuration * zoom);
    // Si la timeline est plus courte que la largeur du panel, garder la marge à 5s
    endMarginPanel.setPreferredSize(new Dimension(marginWidth, 100));
    endMarginPanel.setMinimumSize(new Dimension(marginWidth, 100));
    endMarginPanel.setMaximumSize(new Dimension(marginWidth, 100));
    endMarginPanel.revalidate();
    endMarginPanel.repaint();
  }

  /**
   * Définit le facteur de zoom et redimensionne tous les éléments de la timeline.
   * @param zoomFactor nouveau facteur de zoom
   */
  public void setZoomFactor(double zoomFactor) {
    this.zoomFactor = zoomFactor;
    numbersPanel.setZoomFactor(zoomFactor);
    // Redimensionner tous les panels enfants (scènes/images)
    for (int i = 0; i < timeLineScene.getComponentCount(); i++) {
      Component comp = timeLineScene.getComponent(i);
      if (comp instanceof ResizablePanel) {
        ResizablePanel rp = (ResizablePanel) comp;
        rp.setZoomFactor(zoomFactor); // Ajouté
        rp.updateWidthFromDuration(zoomFactor);
      }
    }
    updateEndMarginPanel();
    timeLineScene.revalidate();
    timeLineScene.repaint();
    numbersPanel.revalidate();
    numbersPanel.repaint();
  }

  /** @return le facteur de zoom courant */
  public double getZoomFactor() {
    return zoomFactor;
  }

  /** @return le JScrollPane englobant la timeline */
  public JScrollPane getScrollPane() {
    return scrollPane;
  }

}
