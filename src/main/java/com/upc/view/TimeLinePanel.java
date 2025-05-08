package com.upc.view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class TimeLinePanel extends JPanel {
    private JPanel timeLinePanel;
    private DividerPanel currentDividerPanel = null;

    public TimeLinePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        this.timeLinePanel = new JPanel();
        timeLinePanel.setLayout(new BoxLayout(timeLinePanel, BoxLayout.X_AXIS));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(800, 100));
        scrollPane.setMinimumSize(new Dimension(800, 100));
        scrollPane.setViewportView(timeLinePanel);

        this.add(Box.createHorizontalStrut(40));
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(Box.createHorizontalStrut(40));
    }

    public void addImageLabel(ImageIcon imageIcon, int duration) {
        ResizablePanel resizablePanel = new ResizablePanel(imageIcon, duration);
        ResizablePanel emptyPanel = new ResizablePanel();
        timeLinePanel.add(resizablePanel);
        if (currentDividerPanel != null) {
            timeLinePanel.remove(timeLinePanel.getComponentCount() - 2);
            currentDividerPanel = new DividerPanel(currentDividerPanel, resizablePanel, emptyPanel);
        } else {
            currentDividerPanel = new DividerPanel(null, resizablePanel, emptyPanel);
        }
        timeLinePanel.add(currentDividerPanel);
        timeLinePanel.add(emptyPanel);
        timeLinePanel.revalidate(); // Refresh layout
        timeLinePanel.repaint(); // Repaint the panel
    }

    public DividerPanel getCurrentDividerPanel() {
        return currentDividerPanel;
    }
}
