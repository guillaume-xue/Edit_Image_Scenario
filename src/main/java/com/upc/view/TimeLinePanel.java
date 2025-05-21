package com.upc.view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class TimeLinePanel extends JPanel {
    private JPanel timeLineScene;

    public TimeLinePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(800, 200));
        setMinimumSize(new Dimension(400, 200));
        setBackground(Color.WHITE);

        JPanel timeLine = new JPanel();
        timeLine.setLayout(new BoxLayout(timeLine, BoxLayout.Y_AXIS));
        timeLine.setBackground(Color.WHITE);

        // Panel personnalisé pour afficher les chiffres tous les 1000 pixels
        JPanel numbersPanel = new TimePanel();
        numbersPanel.setBackground(Color.LIGHT_GRAY);

        this.timeLineScene = new JPanel();
        timeLineScene.setLayout(new BoxLayout(timeLineScene, BoxLayout.X_AXIS));
        timeLineScene.setMinimumSize(new Dimension(400, 100));
        timeLineScene.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        timeLineScene.setBackground(Color.YELLOW);

        timeLine.add(numbersPanel);
        timeLine.add(Box.createHorizontalStrut(40));
        timeLine.add(timeLineScene);
        timeLine.add(Box.createHorizontalStrut(40));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setMinimumSize(new Dimension(800, 200));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        scrollPane.setViewportView(timeLine);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getTimeLinePanel() {
        return timeLineScene;
    }
}
