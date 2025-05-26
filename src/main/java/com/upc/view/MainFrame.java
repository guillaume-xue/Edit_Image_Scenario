package com.upc.view;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Fenêtre principale de l'application.
 * Gère la disposition générale, les menus, les onglets et l'intégration des
 * différents panneaux.
 */
public class MainFrame extends JFrame {

  private JTabbedPane tabbedPane;
  private AnimeViewPanel animeViewPanel;

  /**
   * Constructeur de la fenêtre principale.
   * 
   * @param imageEditPanel panneau d'édition d'image
   * @param viewPanel      panneau de visualisation des images
   * @param timeLinePanel  panneau de timeline pour l'animation
   * @param animeViewPanel panneau de visualisation de l'animation
   */
  public MainFrame(ImageEditorView imageEditPanel, ViewPanel viewPanel, TimeLinePanel timeLinePanel,
      AnimeViewPanel animeViewPanel) {
    init();
    // Configurer le menu pour Mac
    System.setProperty("apple.laf.useScreenMenuBar", "true");

    createMenuBar();
    this.animeViewPanel = animeViewPanel;
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setDividerLocation(400);
    splitPane.setEnabled(true);
    splitPane.setDividerSize(8);
    splitPane.setResizeWeight(0.5);

    // Définir une taille minimale très faible pour permettre le redimensionnement
    // libre
    tabbedPane = new JTabbedPane();
    tabbedPane.setTabPlacement(JTabbedPane.LEFT);
    tabbedPane.addTab("Draw", imageEditPanel);
    tabbedPane.addTab("Animation", this.animeViewPanel);
    tabbedPane.setMinimumSize(new Dimension(400, 400));
    imageEditPanel.setMinimumSize(new Dimension(10, 10));
    this.animeViewPanel.setMinimumSize(new Dimension(10, 10));
    viewPanel.setMinimumSize(new Dimension(10, 10));

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
    this.setPreferredSize(new Dimension(1280, 720));
    this.setMinimumSize(new Dimension(800, 600));
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
   * 
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

  /**
   * Permet de récupérer le JTabbedPane principal.
   * 
   * @return le JTabbedPane principal
   */
  public JTabbedPane getTabbedPane() {
    return tabbedPane;
  }

  /**
   * Permet de récupérer le panneau de visualisation de l'animation.
   * 
   * @return le panneau de visualisation de l'animation
   */
  public AnimeViewPanel getAnimeViewPanel() {
    return animeViewPanel;
  }

}
