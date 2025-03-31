package com.upc.controller;

import com.upc.model.ImageEditorModel;
import com.upc.view.ImageEditPanel;
import com.upc.view.DrawingPanel;

import javax.swing.*;
import java.awt.*;

public class ImageEditor {
    private ImageEditorModel model;
    private ImageEditPanel view;
    private JPopupMenu thicknessPopup;


    public ImageEditor() {
        this.model = new ImageEditorModel();
        this.view = new ImageEditPanel();
        initView();
        initController();
    }

    public ImageEditorModel getImageEditorModel() {
        return model;
    }

    public ImageEditPanel getImageEditPanel() {
        return view;
    }

    private void initView() {
        // Ajouter les boutons à la barre d'outils
        JButton penButton = new JButton("Stylo");
        JButton eraserButton = new JButton("Gomme");
        JButton circleButton = new JButton("Cercle");
        JButton squareButton = new JButton("Carré");
        JButton colorButton = new JButton("Couleur");

        view.addToolBarButton(penButton);
        view.addToolBarButton(eraserButton);
        view.addToolBarButton(circleButton);
        view.addToolBarButton(squareButton);
        view.addToolBarButton(colorButton);

        // Créer les panneaux de dessin
        DrawingPanel panel1 = new DrawingPanel();
        DrawingPanel panel2 = new DrawingPanel();

        // Configurer les contrôleurs après la création des panneaux
        DrawingController controller1 = new DrawingController(panel1, model);
        DrawingController controller2 = new DrawingController(panel2, model);

        panel1.setController(controller1); // Associez le contrôleur au panneau
        panel2.setController(controller2);
        view.addDrawingPanel("Dessin 1", panel1);
        view.addDrawingPanel("Dessin 2", panel2);

        initThicknessPopup();

    }

    private void initController() {
        // Ajouter des actions aux boutons
        for (Component component : view.getToolBarComponents()) { // Utiliser une méthode dédiée
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.addActionListener(e -> handleToolSelection(button));
            }
        }
    }

    private void initThicknessPopup() {
        thicknessPopup = new JPopupMenu();
        JSlider thicknessSlider = new JSlider(0, 100, model.getStrokeWidth()+1); // Min: 1, Max: 20
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

    private void handleToolSelection(JButton button) {
        String tool = button.getText();

        switch (tool) {
            case "Stylo":
                model.setSelectedTool(0);
                showThicknessPopup(button); // Afficher le popup
                break;
            case "Gomme":
                model.setSelectedTool(1);
                showThicknessPopup(button); // Afficher le popup
                break;
            case "Cercle":
                model.setSelectedTool(2);
                showThicknessPopup(button); // Afficher le popup
                break;
            case "Carré":
                model.setSelectedTool(3);
                showThicknessPopup(button); // Afficher le popup
                break;
            case "Couleur":
                Color selectedColor = JColorChooser.showDialog(null, "Choisissez une couleur", null);
                model.setSelectedColor(selectedColor);
                break;
        }
    }
    private void showThicknessPopup(JButton button) {
        // Obtenir la position du bouton
        Point location = button.getLocationOnScreen();
        thicknessPopup.show(button, 0, button.getHeight());
    }
}