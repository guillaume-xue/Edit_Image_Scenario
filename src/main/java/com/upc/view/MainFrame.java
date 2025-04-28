package com.upc.view;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.upc.controller.MouseController;
import com.upc.controller.TransferController;

import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    private ViewPanel viewPanel;
    private TimeLinePanel timeLinePanel;
    private ImageEditPanel imageEditPanel;
    private AnimeViewPanel animeViewPanel;

    public MainFrame(ImageEditPanel imageEditPanel, TransferController transferController,
            MouseController mouseController, String path) {
        this.imageEditPanel = imageEditPanel;
        init();
        // Configurer le menu pour Mac
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        createMenuBar();
        viewPanel = new ViewPanel(path, transferController, mouseController);

        this.animeViewPanel = new AnimeViewPanel();
        timeLinePanel = new TimeLinePanel(transferController, mouseController, animeViewPanel);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.addTab("Draw", imageEditPanel);
        tabbedPane.addTab("Animation", animeViewPanel);

        splitPane.setLeftComponent(tabbedPane);
        splitPane.setRightComponent(viewPanel);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setTopComponent(splitPane);

        mainSplitPane.setBottomComponent(timeLinePanel);

        add(mainSplitPane);
    }

    private void init() {
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Image Editor");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

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

    public ViewPanel getViewPanel() {
        return viewPanel;
    }

    public TimeLinePanel getTimeLinePanel() {
        return timeLinePanel;
    }

}
