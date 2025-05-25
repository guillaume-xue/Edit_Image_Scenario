package com.upc.view;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

/**
 * Fenêtre principale de l'application.
 * Gère la disposition générale, les menus, les onglets et l'intégration des différents panneaux.
 */
public class MainFrame extends JFrame {

  /**
   * Constructeur de la fenêtre principale.
   * @param imageEditPanel panneau d'édition d'image
   * @param viewPanel panneau de visualisation des images
   * @param timeLinePanel panneau de timeline pour l'animation
   * @param animeViewPanel panneau de visualisation de l'animation
   */
  public MainFrame(ImageEditorView imageEditPanel, ViewPanel viewPanel, TimeLinePanel timeLinePanel,
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

    // Gestion du détachement de l'onglet Animation par double-clic
    tabbedPane.addMouseListener(new MouseAdapter() {
      private boolean isDragging = false;

      @Override
      public void mousePressed(MouseEvent e) {
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
            animationFrame.addWindowListener(new WindowAdapter() {
              @Override
              public void windowClosed(WindowEvent e) {
                if (!isDragging) { // Ne réinsère que si l'utilisateur n'est pas en train de glisser
                  tabbedPane.addTab("Animation", animeViewPanel);
                }
              }
            });

            // Réattacher si la fenêtre Animation est déplacée près de la fenêtre principale
            animationFrame.addComponentListener(new ComponentAdapter() {
              @Override
              public void componentMoved(ComponentEvent e) {
                isDragging = true; // L'utilisateur est en train de glisser
              }
            });

            animationFrame.addMouseListener(new MouseAdapter() {
              @Override
              public void mouseReleased(MouseEvent e) {
                isDragging = false; // L'utilisateur a relâché le clic
                Point mainLoc = MainFrame.this.getLocationOnScreen();
                Point animLoc = animationFrame.getLocationOnScreen();
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

  /**
   * Initialise la fenêtre principale (taille, titre, fermeture, etc.).
   */
  private void init() {
    this.setSize(1280, 720);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Image Editor");
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    this.setLayout(new BorderLayout());
  }

  /**
   * Crée la barre de menus principale avec les menus Fichier et Edition.
   */
  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem newItem = new JMenuItem("New Project");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveSenarioItem = new JMenuItem("Save Senario");
    JMenuItem saveDrawItem = new JMenuItem("Save Draw");
    JMenuItem exitItem = new JMenuItem("Exit");

    fileMenu.add(newItem);
    fileMenu.add(openItem);
    fileMenu.addSeparator();
    fileMenu.add(saveSenarioItem);
    fileMenu.addSeparator();
    fileMenu.add(saveDrawItem);
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

  /**
   * Permet de récupérer un JMenuItem à partir de son index de menu et d'item.
   * @param menuIndex index du menu dans la barre de menus
   * @param itemIndex index de l'item dans le menu
   * @return JMenuItem correspondant ou null si non trouvé
   */
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
