package com.upc.view;

import javax.swing.Box;
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Map;

public class TimeLinePanel extends JPanel {
    private TransferController transferController;
    private MouseController mouseController;
    private JPanel timeLinePanel;
    private AnimeViewPanel animeViewPanel; // Reference to AnimeViewPanel

    public TimeLinePanel(TransferController transferController, MouseController mouseController,
            AnimeViewPanel animeViewPanel) {
        super();
        setLayout(new BorderLayout());
        this.transferController = transferController;
        this.mouseController = mouseController;
        this.animeViewPanel = animeViewPanel; // Initialize AnimeViewPanel reference
        setTransferHandler(this.transferController.new TransferTimeLine(this));
        JButton startButton = new JButton("start");
        startButton.addActionListener(e -> startAnimation()); // Add ActionListener
        this.add(startButton, BorderLayout.NORTH);
        this.timeLinePanel = new JPanel();
        this.timeLinePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(timeLinePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(800, 120));
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void startAnimation() {
        animeViewPanel.animatePanel(getImageCopiesWithDurations());
    }

    public void addImageLabel(ImageIcon imageIcon) {
        // Create a vertical box to hold the image and text field
        Box verticalBox = Box.createVerticalBox();

        JLabel label = new JLabel(imageIcon);
        // Make the textField accept only integers

        JTextField textField = new JTextField("");
        textField.addKeyListener(new KeyController().new TextFieldKeyListener());
        textField.setMaximumSize(new Dimension(100, 30)); // Set maximum size for JTextField

        // Add mouse listener for double-click removal
        verticalBox.addMouseListener(mouseController.new TimeLinePanelMouseController(verticalBox, this));

        verticalBox.add(label);
        verticalBox.add(textField);
        timeLinePanel.add(verticalBox);
        timeLinePanel.revalidate(); // Refresh layout
        timeLinePanel.repaint(); // Repaint the panel
    }

    public ArrayList<Map.Entry<ImageIcon, Integer>> getImageCopiesWithDurations() {
        ArrayList<Map.Entry<ImageIcon, Integer>> imageCopiesWithDurations = new ArrayList<>();
        for (Component component : timeLinePanel.getComponents()) {
            if (component instanceof Box) {
                Box verticalBox = (Box) component;
                if (verticalBox.getComponent(0) instanceof JLabel
                        && verticalBox.getComponent(1) instanceof JTextField) {
                    JLabel label = (JLabel) verticalBox.getComponent(0);
                    JTextField textField = (JTextField) verticalBox.getComponent(1);
                    ImageIcon imageIcon = (ImageIcon) label.getIcon();
                    int duration = textField.getText().isEmpty() ? 0
                            : Integer.parseInt(textField.getText().trim());
                    imageCopiesWithDurations.add(Map.entry(imageIcon, duration));
                }
            }
        }
        return imageCopiesWithDurations;
    }
}
