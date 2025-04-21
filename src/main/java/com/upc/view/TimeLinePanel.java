package com.upc.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.TransferHandler;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JTextField;
import java.awt.Dimension;

public class TimeLinePanel extends JPanel {

    public TimeLinePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // Use BoxLayout for horizontal alignment

        setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.imageFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                try {
                    ImageIcon imageIcon = new ImageIcon(
                            (Image) support.getTransferable().getTransferData(DataFlavor.imageFlavor));

                    // Create a vertical box to hold the JLabel and JTextArea
                    Box verticalBox = Box.createVerticalBox();

                    JLabel label = new JLabel(imageIcon);
                    JTextField textField = new JTextField("test");
                    textField.setMaximumSize(new Dimension(100, 30)); // Set maximum size for JTextField

                    verticalBox.add(label);
                    verticalBox.add(textField);

                    add(verticalBox);
                    revalidate(); // Refresh layout
                    repaint(); // Repaint the panel
                    return true;
                } catch (UnsupportedFlavorException | java.io.IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
