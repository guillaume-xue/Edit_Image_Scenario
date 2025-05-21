package com.upc.view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class TimeLinePanel extends JPanel {
    private JPanel timeLinePanel;

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

    public JPanel getTimeLinePanel() {
        return timeLinePanel;
    }
}
