package com.upc.view;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame {

    public MainFrame(ImageEditPanel imageEditPanel, ViewPanel viewPanel, TimeLinePanel timeLinePanel,
            AnimeViewPanel animeViewPanel) {
        init();
        // Configurer le menu pour Mac
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        createMenuBar();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setEnabled(true);
        splitPane.setDividerSize(8);
        splitPane.setResizeWeight(0.5);

        // Définir une taille minimale très faible pour permettre le redimensionnement libre
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.addTab("Draw", imageEditPanel);
        tabbedPane.addTab("Animation", animeViewPanel);
        tabbedPane.setMinimumSize(new Dimension(10, 10));
        imageEditPanel.setMinimumSize(new Dimension(10, 10));
        animeViewPanel.setMinimumSize(new Dimension(10, 10));
        viewPanel.setMinimumSize(new Dimension(10, 10));

        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean isDragging = false;

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int tabIndex = tabbedPane.indexAtLocation(e.getX(), e.getY());
                if (tabIndex == 1 && e.getClickCount() == 2) { // Double-clic sur "Animation"
                    SwingUtilities.invokeLater(() -> { // Différer la suppression de l'onglet
                        tabbedPane.removeTabAt(tabIndex);
                        JFrame animationFrame = new JFrame("Animation");
                        animationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        animationFrame.add(animeViewPanel);
                        animationFrame.setSize(600, 400);
                        animationFrame.setLocationRelativeTo(null);
                        animationFrame.setVisible(true);

                        // Réattacher si la fenêtre Animation est fermée
                        animationFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                if (!isDragging) { // Ne réinsère que si l'utilisateur n'est pas en train de glisser
                                    tabbedPane.addTab("Animation", animeViewPanel);
                                }
                            }
                        });

                        // Réattacher si la fenêtre Animation est déplacée près de la fenêtre principale
                        animationFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
                            @Override
                            public void componentMoved(java.awt.event.ComponentEvent e) {
                                isDragging = true; // L'utilisateur est en train de glisser
                            }
                        });

                        animationFrame.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseReleased(java.awt.event.MouseEvent e) {
                                isDragging = false; // L'utilisateur a relâché le clic
                                java.awt.Point mainLoc = MainFrame.this.getLocationOnScreen();
                                java.awt.Point animLoc = animationFrame.getLocationOnScreen();
                                double distance = mainLoc.distance(animLoc);
                                if (distance < 100) { // Seuil de proximité en pixels
                                    animationFrame.dispose(); // Ferme la fenêtre Animation
                                    tabbedPane.addTab("Animation", animeViewPanel); // Réinsère l'onglet
                                }
                            }
                        });
                    });
                }
            }
        });

        splitPane.setLeftComponent(tabbedPane);
        splitPane.setRightComponent(viewPanel);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setEnabled(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(0.5);

        // Taille minimale faible pour le timeLinePanel
        timeLinePanel.setMinimumSize(new Dimension(10, 10));

        mainSplitPane.setTopComponent(splitPane);
        mainSplitPane.setBottomComponent(timeLinePanel);

        add(mainSplitPane);
    }

    private void init() {
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Image Editor");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        // Supprimer ou commenter la ligne suivante pour ne pas bloquer le redimensionnement :
        // this.setMinimumSize(new Dimension(800, 700));
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New Project");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        menuBar.add(editMenu);

        this.setJMenuBar(menuBar);
    }

    public JMenuItem getMenuItem(int menuIndex, int itemIndex) {
        JMenuBar menuBar = this.getJMenuBar();
        if (menuBar == null || menuIndex < 0 || menuIndex >= menuBar.getMenuCount()) {
            System.err.println("Invalid menu index: " + menuIndex);
            return null;
        }

        JMenu menu = menuBar.getMenu(menuIndex);
        if (menu == null || itemIndex < 0 || itemIndex >= menu.getItemCount()) {
            System.err.println("Invalid item index: " + itemIndex + " in menu: " + menuIndex);
            return null;
        }

        return menu.getItem(itemIndex);
    }

}
