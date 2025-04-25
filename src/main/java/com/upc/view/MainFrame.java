package com.upc.view;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import com.upc.controller.MouseController;
import com.upc.controller.TransferController;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame {
    private ViewPanel viewPanel;
    private TimeLinePanel timeLinePanel;
    private ImageEditPanel imageEditPanel;

    public MainFrame(ImageEditPanel imageEditPanel, TransferController transferController,
            MouseController mouseController, String path) {
        this.imageEditPanel = imageEditPanel;
        init();
        // Configurer le menu pour Mac
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        createMenuBar();
        viewPanel = new ViewPanel(path, transferController, mouseController);
        timeLinePanel = new TimeLinePanel(transferController, mouseController);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setLeftComponent(imageEditPanel);
        splitPane.setRightComponent(viewPanel);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(400);
        mainSplitPane.setTopComponent(splitPane);

        JScrollPane scrollPane = new JScrollPane(timeLinePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(800, 120)); // Adjust scroll pane size if needed
        mainSplitPane.setBottomComponent(scrollPane);

        add(mainSplitPane);
    }

    private void init() {
        this.setSize(800, 600);
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
