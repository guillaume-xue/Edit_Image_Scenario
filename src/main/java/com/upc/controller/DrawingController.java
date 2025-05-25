package com.upc.controller;

import com.upc.model.ImageEditorModel;
import com.upc.view.DrawingPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.ImageIcon;

/**
 * Contrôleur pour la gestion des interactions de dessin sur le DrawingPanel.
 * Gère les outils de dessin, la souris et l'intégration avec le modèle.
 */
public class DrawingController implements MouseListener, MouseMotionListener {
  // Coordonnées précédentes de la souris (pour le dessin continu)
  private int ox, oy;
  // Vue associée au dessin
  private DrawingPanel view;
  // Modèle de l'éditeur d'image
  private ImageEditorModel model;
  // Répertoire des images
  private File imageDir;

  /**
   * Constructeur du contrôleur de dessin.
   * @param view panneau de dessin
   * @param model modèle de l'éditeur d'image
   * @param transferController contrôleur de transfert (drag & drop)
   * @param imageDir répertoire des images
   */
  public DrawingController(DrawingPanel view, ImageEditorModel model, TransferController transferController,
      File imageDir) {
    this.view = view;
    this.model = model;
    this.imageDir = imageDir;
    // Initialisation du canevas de dessin
    this.view.initializeCanvas();
    // Configuration du gestionnaire de transfert pour le drag & drop
    this.view.setTransferHandler(transferController.new TransferDrawing(this));
  }

  /**
   * Dessine une image sur le panneau à partir d'un ImageIcon.
   * @param image l'image à dessiner
   */
  public void draw(ImageIcon image) {
    File imageFile = new File(imageDir, image.getDescription());
    ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
    view.drawImageIcon(originalIcon);
  }

  /**
   * Gestion de l'appui de la souris : mémorise la position initiale.
   */
  @Override
  public void mousePressed(MouseEvent e) {
    ox = e.getX();
    oy = e.getY();
  }

  /**
   * Gestion du déplacement de la souris avec bouton appuyé (dessin ou prévisualisation).
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    int selectedTool = model.getSelectedTool();

    switch (selectedTool) {
      case 0: // Stylo
        // Dessine une ligne continue avec la couleur et l'épaisseur sélectionnées
        view.drawLine(ox, oy, x, y, model.getSelectedColor(), model.getStrokeWidth());
        ox = x;
        oy = y;
        break;
      case 1: // Gomme
        // Dessine avec la couleur de fond pour effacer
        view.drawLine(ox, oy, x, y, view.getBackground(), model.getStrokeWidth());
        ox = x;
        oy = y;
        break;
      case 2: // Cercle
        // Prévisualisation d'un cercle pendant le drag
        int width = Math.abs(x - ox);
        int height = Math.abs(y - oy);
        int startX = Math.min(ox, x);
        int startY = Math.min(oy, y);
        view.setPreviewShape("Cercle", startX, startY, width, height, model.getStrokeWidth(),
            model.getSelectedColor());
        break;
      case 3: // Carré
        // Prévisualisation d'un carré pendant le drag
        int side = Math.max(Math.abs(x - ox), Math.abs(y - oy));
        int squareX = Math.min(ox, x);
        int squareY = Math.min(oy, y);
        view.setPreviewShape("Carré", squareX, squareY, side, side, model.getStrokeWidth(),
            model.getSelectedColor());
        break;
      default:
        // Les outils Cercle et Carré seront gérés dans mouseReleased
        break;
    }
  }

  /**
   * Gestion du relâchement de la souris : dessine la forme finale (cercle ou carré).
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    int selectedTool = model.getSelectedTool();

    switch (selectedTool) {
      case 2: // Cercle
        int width = Math.abs(x - ox);
        int height = Math.abs(y - oy);
        int startX = Math.min(ox, x);
        int startY = Math.min(oy, y);
        view.drawOval(startX, startY, width, height, model.getSelectedColor(), model.getStrokeWidth());
        break;
      case 3: // Carré
        int side = Math.max(Math.abs(x - ox), Math.abs(y - oy));
        int squareX = Math.min(ox, x);
        int squareY = Math.min(oy, y);
        view.drawRect(squareX, squareY, side, side, model.getSelectedColor(), model.getStrokeWidth());
        break;
    }

    // Efface la prévisualisation de la forme
    view.clearPreview();
  }

  /**
   * Gestion du déplacement de la souris sans bouton appuyé (non utilisé ici).
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    // No specific action needed on move
  }

  /**
   * Gestion du clic de souris (non utilisé ici).
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    // No specific action needed on click
  }

  /**
   * Gestion de l'entrée de la souris dans le composant (non utilisé ici).
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    // No specific action needed on enter
  }

  /**
   * Gestion de la sortie de la souris du composant (non utilisé ici).
   */
  @Override
  public void mouseExited(MouseEvent e) {
    // No specific action needed on exit
  }
}