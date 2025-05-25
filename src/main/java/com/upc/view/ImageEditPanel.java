package com.upc.view;

import javax.swing.*;
import java.awt.*;

public class ImageEditPanel extends JPanel {
    private JToolBar toolBar;
    private JTabbedPane tabbedPane;
    private JSlider thicknessSlider;

    public ImageEditPanel() {
        super();
        setLayout(new BorderLayout());
        toolBar = new JToolBar();
        tabbedPane = new JTabbedPane();

        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
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
