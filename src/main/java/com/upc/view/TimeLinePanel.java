package com.upc.view;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.upc.controller.MouseController;
import com.upc.controller.TransferController;

import java.awt.Dimension;
import java.awt.FlowLayout;

public class TimeLinePanel extends JPanel {
    private TransferController transferController;
    private MouseController mouseController;

    public TimeLinePanel(TransferController transferController, MouseController mouseController) {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT)); // Use BoxLayout for horizontal alignment
        this.transferController = transferController;
        this.mouseController = mouseController;
        setTransferHandler(this.transferController.new TransferTimeLine(this));
    }

    public void addImageLabel(ImageIcon imageIcon) {
        // Create a vertical box to hold the image and text field
        Box verticalBox = Box.createVerticalBox();

        JLabel label = new JLabel(imageIcon);
        JTextField textField = new JTextField("test");
        textField.setMaximumSize(new Dimension(100, 30)); // Set maximum size for JTextField

        // Add mouse listener for double-click removal
        verticalBox.addMouseListener(mouseController.new TimeLinePanelMouseController(verticalBox, this));

        verticalBox.add(label);
        verticalBox.add(textField);
        add(verticalBox);
        revalidate(); // Refresh layout
        repaint(); // Repaint the panel

    }
}
