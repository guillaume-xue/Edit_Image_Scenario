package com.upc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        public ClosableTabComponent(JTabbedPane tabbedPane, int index) {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setOpaque(false);
    
            // Ajouter le titre de l'onglet
            JLabel titleLabel = new JLabel(tabbedPane.getTitleAt(index));
            add(titleLabel);
    
            // Ajouter un bouton de fermeture
            closeButton = new JButton(" X");
            closeButton.setMargin(new Insets(0, 0, 0, 0));
            closeButton.setBorder(BorderFactory.createEmptyBorder());
            closeButton.setFocusPainted(false);
            closeButton.setContentAreaFilled(false);

            // Définir une taille fixe pour le bouton
            closeButton.setVisible(false); // Masquer le bouton par défaut

                 // Ajouter un écouteur pour fermer l'onglet
            closeButton.addActionListener(e -> {
                int tabIndex = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
                if (tabIndex != -1) {
                    tabbedPane.remove(tabIndex);
                }
            });
        
            add(closeButton);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    closeButton.setVisible(true); // Afficher le bouton lorsque la souris entre dans le composant
                }
    
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    // Vérifier si la souris n'est pas sur le composant ou le bouton avant de masquer
                    if (!closeButton.getBounds().contains(e.getPoint())) {
                        closeButton.setVisible(false);
                    }
                }
            });
    
            // Ajouter un écouteur de souris pour le bouton
            closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    closeButton.setVisible(true); // Garder le bouton visible lorsque la souris est dessus
                }
    
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    // Vérifier si la souris n'est pas sur le composant principal avant de masquer
                    Point point = SwingUtilities.convertPoint(closeButton, e.getPoint(), ClosableTabComponent.this);
                    if (!ClosableTabComponent.this.getBounds().contains(point)) {
                        closeButton.setVisible(false);
                    }
                }
            });

        }

        public JButton geButtontCloseButton() {
            return closeButton;
        }
    }
}
