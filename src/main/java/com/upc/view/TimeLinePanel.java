package com.upc.view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.upc.controller.KeyController;
import com.upc.controller.MouseController;
import com.upc.controller.TransferController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

public class TimeLinePanel extends JPanel {
    private TransferController transferController;
    private MouseController mouseController;
    private JPanel timeLinePanel;
    private AnimeViewPanel animeViewPanel; // Reference to AnimeViewPanel
    private DividerPanel currentDividerPanel = null;

    public TimeLinePanel(TransferController transferController, MouseController mouseController,
            AnimeViewPanel animeViewPanel) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        this.transferController = transferController;
        this.mouseController = mouseController;
        this.animeViewPanel = animeViewPanel; // Initialize AnimeViewPanel reference

        setTransferHandler(this.transferController.new TransferTimeLine(this));

        JButton startButton = new JButton("start");
        startButton.addActionListener(e -> startAnimation()); // Add ActionListener
        this.add(startButton, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        this.timeLinePanel = new JPanel();
        timeLinePanel.setLayout(new BoxLayout(timeLinePanel, BoxLayout.X_AXIS));
        scrollPane.setViewportView(timeLinePanel);

        this.add(Box.createHorizontalStrut(40));
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(Box.createHorizontalStrut(40));
    }

    private void startAnimation() {
        animeViewPanel.animatePanel(getImageCopiesWithDurations());
    }

    public void addImageLabel(ImageIcon imageIcon) {
        ResizablePanel resizablePanel = new ResizablePanel(imageIcon);
        ResizablePanel emptyPanel = new ResizablePanel();
        timeLinePanel.add(resizablePanel);
        if (currentDividerPanel != null) {
            timeLinePanel.remove(timeLinePanel.getComponentCount() - 2);
            currentDividerPanel = new DividerPanel(currentDividerPanel, resizablePanel, emptyPanel);
        } else {
            currentDividerPanel = new DividerPanel(null, resizablePanel, emptyPanel);
        }
        this.addMouseListener(mouseController.new TimeLinePanelMouseController(null, null));
        timeLinePanel.add(currentDividerPanel);
        timeLinePanel.add(emptyPanel);
        timeLinePanel.revalidate(); // Refresh layout
        timeLinePanel.repaint(); // Repaint the panel
    }

    public ArrayList<Map.Entry<ImageIcon, Integer>> getImageCopiesWithDurations() {
        ArrayList<Map.Entry<ImageIcon, Integer>> imageCopiesWithDurations = new ArrayList<>();
        DividerPanel tmp = currentDividerPanel;
        while (tmp != null) {
            ResizablePanel left = tmp.getLeft();
            if (left != null && left.getIcon() != null) {
                imageCopiesWithDurations.add(Map.entry(left.getIcon(), left.getDuration()));
            }
            tmp = tmp.getPrecDividerPanel();
        }
        // Reverse the list to maintain the correct order
        ArrayList<Map.Entry<ImageIcon, Integer>> reversedList = new ArrayList<>();
        for (int i = imageCopiesWithDurations.size() - 1; i >= 0; i--) {
            reversedList.add(imageCopiesWithDurations.get(i));
        }
        imageCopiesWithDurations = reversedList;
        return imageCopiesWithDurations;
    }
}
