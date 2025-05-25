package com.upc.view;

import javax.swing.*;
import java.awt.*;
import com.upc.controller.ImageEditorController.ColorIcon;;

public class ImageEditorView extends JPanel {
    private JToolBar toolBar;
    private JTabbedPane tabbedPane;
    private JSlider thicknessSlider;

    public ImageEditorView() {
        super();
        setLayout(new BorderLayout());
        toolBar = new JToolBar();
        tabbedPane = new JTabbedPane();

        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        initView();
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

        addToolBarButton(add);
        addToolBarButton(penButton);
        addToolBarButton(eraserButton);
        addToolBarButton(circleButton);
        addToolBarButton(squareButton);
        addToolBarButton(colorButton);
        addToolBarButton(clearButton);
        addToolBarButton(validateButton);

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

    public void addToolBarButton(JButton button) {
        toolBar.add(button);
    }

    public void addDrawingPanel(String title, JPanel panel) {
        tabbedPane.addTab(title, panel);
        int index = tabbedPane.indexOfComponent(panel);
        tabbedPane.setTabComponentAt(index, new ClosableTabComponent(tabbedPane, index));
    }

    public JPanel getSelectedDrawingPanel() {
        return (JPanel) tabbedPane.getSelectedComponent();
    }

    public Component[] getToolBarComponents() {
        return toolBar.getComponents();
    }

    public JSlider getThicknessSlider() {
        return thicknessSlider;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public class ClosableTabComponent extends JPanel {
        private JButton closeButton;
        private JLabel titleLabel;

        private javax.swing.Timer singleClickTimer;
        private final int DOUBLE_CLICK_DELAY = 100; // ms

        public ClosableTabComponent(JTabbedPane tabbedPane, int index) {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setOpaque(false);

            titleLabel = new JLabel(tabbedPane.getTitleAt(index));
            add(titleLabel);

            // Edition directe du nom d'onglet
            titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                        int tabIndex = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
                        if (singleClickTimer != null && singleClickTimer.isRunning()) {
                            singleClickTimer.stop();
                        }
                        if (tabIndex != -1) {
                            remove(titleLabel);
                            JTextField textField = new JTextField(titleLabel.getText());
                            textField.setColumns(10);
                            add(textField, 0);
                            revalidate();
                            repaint();
                            textField.requestFocusInWindow();

                            // Validation par Entrée ou perte de focus
                            textField.addActionListener(ev -> finishEdit(tabbedPane, tabIndex, textField));
                            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                                @Override
                                public void focusLost(java.awt.event.FocusEvent ev) {
                                    finishEdit(tabbedPane, tabIndex, textField);
                                }
                            });
                        }
                    } else if (e.getClickCount() == 1) {
                        // Simple clic : attendre pour voir si c’est un double-clic
                        if (singleClickTimer != null && singleClickTimer.isRunning()) {
                            singleClickTimer.stop();
                        }
                        // Conversion des coordonnées du label vers le tabbedPane
                        Point tabbedPanePoint = SwingUtilities.convertPoint(titleLabel, e.getPoint(), tabbedPane);
                        int tabIndex = tabbedPane.indexAtLocation(tabbedPanePoint.x, tabbedPanePoint.y);
                        singleClickTimer = new javax.swing.Timer(DOUBLE_CLICK_DELAY, evt -> {
                            if (tabIndex != -1 && tabbedPane.getSelectedIndex() != tabIndex) {
                                tabbedPane.setSelectedIndex(tabIndex);
                            }
                        });
                        singleClickTimer.setRepeats(false);
                        singleClickTimer.start();
                    }
                }
            });
        }

        private void finishEdit(JTabbedPane tabbedPane, int tabIndex, JTextField textField) {
            String newTitle = textField.getText().trim();
            if (!newTitle.isEmpty()) {
                titleLabel.setText(newTitle);
                tabbedPane.setTitleAt(tabIndex, newTitle);
            }
            remove(textField);
            add(titleLabel, 0);
            revalidate();
            repaint();
        }

        public JButton geButtontCloseButton() {
            return closeButton;
        }
    }
}
