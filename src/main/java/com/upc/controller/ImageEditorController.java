package com.upc.controller;

import com.upc.model.ImageEditorModel;
import com.upc.view.ImageEditorView;
import com.upc.view.DrawingPanel;

import static javax.imageio.ImageIO.write;
import javax.swing.*;

import java.awt.*;
import java.io.File;

/**
 * Contrôleur pour l'édition d'image.
 * Gère la sélection des outils, la gestion des couleurs, l'ajout de nouveaux dessins,
 * la sauvegarde des dessins et l'interaction avec la vue et le modèle.
 */
public class ImageEditorController {
  // Modèle de l'éditeur d'image
  private ImageEditorModel model;
  // Vue de l'éditeur d'image
  private ImageEditorView view;
  // Contrôleur principal de l'application
  private GUIController guiController;
  // Contrôleur de transfert (drag & drop)
  private TransferController transferController;
  // Répertoire où sont stockées les images
  private File imageDir;
  // Compteur pour nommer les nouveaux dessins
  private int cpt = 0;
  // Popup pour le choix de l'épaisseur du trait
  private JPopupMenu thicknessPopup;

  /**
   * Constructeur du contrôleur d'édition d'image.
   * @param transferController contrôleur de transfert
   * @param imageDir dossier des images
   * @param guiController contrôleur principal
   */
  public ImageEditorController(TransferController transferController, File imageDir, GUIController guiController) {
    this.model = new ImageEditorModel();
    this.view = new ImageEditorView();
    this.imageDir = imageDir;
    this.transferController = transferController;
    this.guiController = guiController;
    initThicknessPopup();
    initController();
  }

  /**
   * Retourne le modèle de l'éditeur d'image.
   */
  public ImageEditorModel getImageEditorModel() {
    return model;
  }

  /**
   * Retourne la vue de l'éditeur d'image.
   */
  public ImageEditorView getImageEditPanel() {
    return view;
  }

  /**
   * Initialise les actions des boutons de la barre d'outils.
   */
  private void initController() {
    // Ajouter des actions aux boutons
    for (Component component : view.getToolBarComponents()) {
      if (component instanceof JButton) {
        JButton button = (JButton) component;
        button.addActionListener(e -> handleToolSelection(button));
      }
    }
  }

  /**
   * Gère la sélection d'un outil ou d'une action depuis la barre d'outils.
   * @param button bouton cliqué
   */
  private void handleToolSelection(JButton button) {
    String tool = button.getText();
    if (tool == null || tool.isEmpty()) {
      Icon icon = button.getIcon();
      if (icon instanceof ImageIcon) {
        tool = ((ImageIcon) icon).getDescription();
      } else if (icon instanceof ColorIcon) {
        tool = icon.toString();
      }
    }
    switch (tool) {
      case "Stylo":
        model.setSelectedTool(0);
        showThicknessPopup(button); // Afficher le popup pour l'épaisseur
        break;
      case "Gomme":
        model.setSelectedTool(1);
        showThicknessPopup(button);
        break;
      case "Cercle":
        model.setSelectedTool(2);
        showThicknessPopup(button);
        break;
      case "Carré":
        model.setSelectedTool(3);
        showThicknessPopup(button);
        break;
      case "Couleur":
        // Ouvre le sélecteur de couleur et met à jour l'icône et le modèle
        Color selectedColor = JColorChooser.showDialog(null, "Choisissez une couleur", null);
        button.setIcon(new ColorIcon(selectedColor, 20, "Couleur"));
        model.setSelectedColor(selectedColor);
        break;
      case "+":
        // Ajoute un nouvel onglet de dessin
        DrawingPanel newPanel = new DrawingPanel();
        DrawingController newController = new DrawingController(newPanel, model, transferController, imageDir);
        newPanel.addMouseListener(newController);
        newPanel.addMouseMotionListener(newController);
        view.addDrawingPanel("Dessin " + (++cpt), newPanel);
        break;
      case "Clear":
        // Efface le dessin courant ou ferme l'onglet s'il est vide
        JPanel selectedPanel = view.getSelectedDrawingPanel();
        if (selectedPanel instanceof DrawingPanel) {
          DrawingPanel drawingPanel = (DrawingPanel) selectedPanel;
          if (!drawingPanel.isEmpty()) {
            drawingPanel.clearAll();
          } else {
            int idx = view.getTabbedPane().getSelectedIndex();
            if (idx != -1) {
              view.getTabbedPane().removeTabAt(idx);
            }
          }
        }
        break;
    }
  }
  
  /**
   * Valide le dessin courant et le sauvegarde dans le répertoire d'images.
   * Le nom du fichier est basé sur le nom de l'onglet actif.
   */
  public void valider() {
    JPanel selectedPanelValider = view.getSelectedDrawingPanel();
    if (selectedPanelValider instanceof DrawingPanel) {
      DrawingPanel drawingPanel = (DrawingPanel) selectedPanelValider;
      // Récupérer le nom de l'onglet
      int idx = view.getTabbedPane().getSelectedIndex();
      if (idx != -1) {
        String tabName = view.getTabbedPane().getTitleAt(idx);
        // Nettoyer le nom pour éviter les caractères interdits dans un nom de fichier
        String fileName = tabName.replaceAll("[^a-zA-Z0-9-_\\.]", "_") + ".png";
        File outputFile = new File(imageDir, fileName);
        try {
          write(drawingPanel.getBufferedImage(), "png", outputFile);
          showTemporaryMessage("Dessin sauvegardé sous : " + outputFile.getAbsolutePath());
          // Actualiser la vue des images
          guiController.updateViewPanel(outputFile.getAbsolutePath(), fileName);
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(null, "Erreur lors de la sauvegarde : " + ex.getMessage());
        }
      }
    }
  }

  /**
   * Affiche le popup pour choisir l'épaisseur du trait à côté du bouton sélectionné.
   * @param button bouton source
   */
  private void showThicknessPopup(JButton button) {
    // Affiche le popup juste sous le bouton
    thicknessPopup.show(button, 0, button.getHeight());
  }

  /**
   * Initialise le popup d'épaisseur du trait avec un slider.
   */
  private void initThicknessPopup() {
    thicknessPopup = new JPopupMenu();
    JSlider thicknessSlider = new JSlider(0, 100, model.getStrokeWidth() + 1); // Min: 1, Max: 100
    thicknessSlider.setPaintTicks(true);
    thicknessSlider.setPaintLabels(true);
    thicknessSlider.setMajorTickSpacing(20);
    thicknessSlider.setMinorTickSpacing(10);

    thicknessSlider.addChangeListener(e -> {
      int thickness = thicknessSlider.getValue();
      model.setStrokeWidth(thickness);
    });

    thicknessPopup.add(thicknessSlider);
  }

  /**
   * Classe utilitaire pour dessiner une icône de couleur dans la barre d'outils.
   */
  public static class ColorIcon implements Icon {
    private final int size;
    private Color color;
    private String description;

    public ColorIcon(Color color, int size, String description) {
      this.color = color;
      this.size = size;
      this.description = description;
    }

    public void setColor(Color color) {
      this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      g.setColor(color);
      g.fillOval(x, y, size, size); // Dessine un cercle rempli de la couleur
      g.setColor(Color.BLACK);
      g.drawOval(x, y, size - 1, size - 1); // Ajoute une bordure noire
    }

    @Override
    public int getIconWidth() {
      return size;
    }

    @Override
    public int getIconHeight() {
      return size;
    }

    @Override
    public String toString() {
      return description; // Retourne la description de l'icône
    }
  }

  /**
   * Affiche un message temporaire qui disparaît après 2 secondes.
   * @param message message à afficher
   */
  private void showTemporaryMessage(String message) {
    JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
    JDialog dialog = pane.createDialog(null, "Information");
    dialog.setModal(false); // Permet de ne pas bloquer l'UI
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setVisible(true);

    // Timer pour fermer la fenêtre après 2 secondes (2000 ms)
    new Timer(2000, e -> dialog.dispose()).start();
  }
}