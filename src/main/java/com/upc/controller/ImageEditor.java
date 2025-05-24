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
    private int cpt = 0;

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

    public ImageIcon resizeImageIcon(String path, String description, int width, int height) {
        ImageIcon tempIcon = new ImageIcon(path);
        int maxW = width, maxH = height;
        int iw = tempIcon.getIconWidth(), ih = tempIcon.getIconHeight();
        double ratio = Math.min((double) maxW / iw, (double) maxH / ih);
        int w = (int) (iw * ratio), h = (int) (ih * ratio);
        Image scaled = tempIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        tempIcon.getImage().flush(); // Libère la mémoire de l'image originale

        ImageIcon scaledIcon = new ImageIcon(scaled);
        scaledIcon.setDescription(description);
        return scaledIcon;
    }

    private void initView() {
        // Ajouter les boutons à la barre d'outils
        JButton add = new JButton("+");
        add.setPreferredSize(new Dimension(30, 30));
        add.setMinimumSize(new Dimension(30, 30));
        add.setMaximumSize(new Dimension(30, 30));
        JButton penButton = new JButton();
        penButton.setPreferredSize(new Dimension(30, 30));
        penButton.setMinimumSize(new Dimension(30, 30));
        penButton.setMaximumSize(new Dimension(30, 30));
        penButton.setIcon(resizeImageIcon("src/main/resources/Icon/stylo.png", "Stylo", 20, 20));
        JButton eraserButton = new JButton();
        eraserButton.setMinimumSize(new Dimension(30, 30));
        eraserButton.setMaximumSize(new Dimension(30, 30));
        eraserButton.setPreferredSize(new Dimension(30, 30));
        eraserButton.setIcon(resizeImageIcon("src/main/resources/Icon/gomme.png", "Gomme", 20, 20));
        JButton circleButton = new JButton();
        circleButton.setMinimumSize(new Dimension(30, 30));
        circleButton.setMaximumSize(new Dimension(30, 30));
        circleButton.setPreferredSize(new Dimension(30, 30));
        circleButton.setIcon(resizeImageIcon("src/main/resources/Icon/cercle.png", "Cercle", 20, 20));
        JButton squareButton = new JButton();
        squareButton.setMinimumSize(new Dimension(30, 30));
        squareButton.setMaximumSize(new Dimension(30, 30));
        squareButton.setPreferredSize(new Dimension(30, 30));
        squareButton.setIcon(resizeImageIcon("src/main/resources/Icon/carre.png", "Carré", 20, 20));
        JButton colorButton = new JButton();
        colorButton.setIcon(new ColorIcon(Color.BLACK, 20, "Couleur"));
        colorButton.setMinimumSize(new Dimension(30, 30));
        colorButton.setMaximumSize(new Dimension(30, 30));
        colorButton.setPreferredSize(new Dimension(30, 30));

        view.addToolBarButton(add);
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
        cpt += 2;
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
        JSlider thicknessSlider = new JSlider(0, 100, model.getStrokeWidth() + 1); // Min: 1, Max: 20
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
                button.setIcon(new ColorIcon(selectedColor, 20, "Couleur"));
                model.setSelectedColor(selectedColor);
                break;
            case "+":
                DrawingPanel newPanel = new DrawingPanel();
                DrawingController newController = new DrawingController(newPanel, model);
                newPanel.setController(newController);
                view.addDrawingPanel("Dessin " + (++cpt), newPanel);
                break;
        }
    }

    private void showThicknessPopup(JButton button) {
        // Obtenir la position du bouton
        Point location = button.getLocationOnScreen();
        thicknessPopup.show(button, 0, button.getHeight());
    }

    // Classe pour dessiner une icône de couleur
    class ColorIcon implements Icon {
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
}