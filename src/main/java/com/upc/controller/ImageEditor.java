package com.upc.controller;

import com.upc.model.ImageEditorModel;
import com.upc.view.ImageEditPanel;
import com.upc.view.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageEditor {
    private ImageEditorModel model;
    private ImageEditPanel view;
    private GUIController guiController;
    private JPopupMenu thicknessPopup;
    private TransferController transferController;
    private File imageDir;
    private int cpt = 0;

    public ImageEditor(TransferController transferController, File imageDir, GUIController guiController) {
        this.model = new ImageEditorModel();
        this.view = new ImageEditPanel();
        this.imageDir = imageDir;
        this.transferController = transferController;
        this.guiController = guiController;
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
        JButton clearButton = new JButton();
        clearButton.setMinimumSize(new Dimension(30, 30));
        clearButton.setMaximumSize(new Dimension(30, 30));
        clearButton.setPreferredSize(new Dimension(30, 30));
        clearButton.setIcon(resizeImageIcon("src/main/resources/Icon/effacer.png", "Clear", 20, 20));
        JButton validateButton = new JButton();
        validateButton.setMinimumSize(new Dimension(30, 30));
        validateButton.setMaximumSize(new Dimension(30, 30));
        validateButton.setPreferredSize(new Dimension(30, 30));
        validateButton.setIcon(resizeImageIcon("src/main/resources/Icon/verifier.png", "Valider", 20, 20));

        view.addToolBarButton(add);
        view.addToolBarButton(penButton);
        view.addToolBarButton(eraserButton);
        view.addToolBarButton(circleButton);
        view.addToolBarButton(squareButton);
        view.addToolBarButton(colorButton);
        view.addToolBarButton(clearButton);
        view.addToolBarButton(validateButton);

        // Créer les panneaux de dessin
        DrawingPanel panel1 = new DrawingPanel();

        // Configurer les contrôleurs après la création des panneaux
        DrawingController controller1 = new DrawingController(panel1, model, transferController, imageDir);

        panel1.setController(controller1); // Associez le contrôleur au panneau
        view.addDrawingPanel("Dessin 1", panel1);
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
                DrawingController newController = new DrawingController(newPanel, model, transferController, imageDir);
                newPanel.setController(newController);
                view.addDrawingPanel("Dessin " + (++cpt), newPanel);
                break;
            case "Clear":
                JPanel selectedPanel = view.getSelectedDrawingPanel();
                if (selectedPanel instanceof com.upc.view.DrawingPanel) {
                    com.upc.view.DrawingPanel drawingPanel = (com.upc.view.DrawingPanel) selectedPanel;
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
            case "Valider":
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
                            javax.imageio.ImageIO.write(drawingPanel.getBufferedImage(), "png", outputFile);
                            showTemporaryMessage("Dessin sauvegardé sous : " + outputFile.getAbsolutePath());
                            // Actualiser la vue des images
                            guiController.updateViewPanel(outputFile.getAbsolutePath(), fileName);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Erreur lors de la sauvegarde : " + ex.getMessage());
                        }
                    }
                }
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

    // Affiche un message temporaire qui disparaît après 2 secondes
    private void showTemporaryMessage(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "Information");
        dialog.setModal(false); // Permet de ne pas bloquer l'UI
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        // Timer pour fermer la fenêtre après 2 secondes (2000 ms)
        new javax.swing.Timer(2000, e -> dialog.dispose()).start();
    }
}