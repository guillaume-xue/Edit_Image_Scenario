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
}