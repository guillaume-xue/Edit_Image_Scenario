package com.upc.view;

import javax.swing.*;
import java.awt.*;

import com.upc.controller.ImageEditor;
import com.upc.model.ImageEditorModel;

public class ImageEditPanel extends JPanel {
    JPanel panel;
    JToolBar toolBar;
    JTabbedPane tabbedPane;
    ImageEditor imageEditPanel;
    ImageEditorModel imageEditorModel;

    public JPanel getPanel(){
        return panel;
    }

    public ImageEditPanel(ImageEditor imageEditPanel, ImageEditorModel imageEditorModel) {
        panel = new JPanel(new BorderLayout()); // Utiliser BorderLayout pour organiser les composants
        toolBar = new JToolBar();
        tabbedPane = new JTabbedPane();
        this.imageEditPanel = imageEditPanel;
        this.imageEditorModel = imageEditorModel;

        topImageEditor(); // Ajouter la barre d'outils
        tabbedPane(); // Ajouter les onglets avec des zones de dessin

        // Ajouter les composants au panneau principal
        panel.add(toolBar, BorderLayout.NORTH); // Barre d'outils en haut
        panel.add(tabbedPane, BorderLayout.CENTER); // Onglets au centre
    }

    private void topImageEditor(){
        // Bouton Stylo
        JButton penButton = new JButton("Stylo");
        imageEditPanel.imageEditorTopButton(penButton);
        // Bouton Gomme
        JButton eraserButton = new JButton("Gomme");
        imageEditPanel.imageEditorTopButton(eraserButton);
        // Bouton Cercle
        JButton circleButton = new JButton("Cercle");
        imageEditPanel.imageEditorTopButton(circleButton);
        // Bouton Carré
        JButton squareButton = new JButton("Carré");
        imageEditPanel.imageEditorTopButton(squareButton);
        // Bouton Couleur
        JButton colorButton = new JButton("Couleur");
        imageEditPanel.imageEditorTopButton(colorButton);
        
        // Ajouter les boutons à la barre d'outils
        toolBar.add(penButton);
        toolBar.add(eraserButton);
        toolBar.add(circleButton);
        toolBar.add(squareButton);
        toolBar.add(colorButton);

        // Ajouter la barre d'outils au panneau principal
        panel.add(toolBar);
    }

    private JPanel createDrawingPanel() {
        // Créer une zone de dessin
        JPanel drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE); // Fond blanc pour la zone de dessin
        drawingPanel.setPreferredSize(new Dimension(400, 400)); // Taille préférée
        return drawingPanel;
    }

    private void tabbedPane(){
        tabbedPane = new JTabbedPane();
        JPanel drawingPanel = createDrawingPanel();
        tabbedPane.addTab("Dessin 1", drawingPanel);
        panel.add(tabbedPane);
    }

    public void drawAt(int x, int y){
        Graphics g = tabbedPane.getSelectedComponent().getGraphics();
        if (g != null) {
            g.setColor(imageEditorModel.getSelectedColor());
            switch (imageEditorModel.getSelectedTool()) {
                case 0:
                    g.drawLine(x, y, x, y);
                    break;
                case 1:
                    g.setColor(Color.WHITE);
                    g.fillOval(x, y, 5, 5);
                    break;
                case 2:
                    g.drawOval(x, y, 50, 50);
                    break;
                case 3:
                    g.drawRect(x, y, 50, 50);
                    break;
                default:
                    break;
            }
        }
        tabbedPane.getSelectedComponent().repaint();
    }
}
