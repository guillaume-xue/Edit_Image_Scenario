package com.upc.view;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    private ViewPanel viewPanel;
    private TimeLinePanel timeLinePanel;
    private ImageEditPanel imageEditPanel;

    public MainFrame() {
        viewPanel = new ViewPanel();
        timeLinePanel = new TimeLinePanel();
        imageEditPanel = new ImageEditPanel();
        this.setLayout(new BorderLayout());
        this.add(viewPanel, BorderLayout.EAST);
        this.add(timeLinePanel, BorderLayout.SOUTH);
        this.add(imageEditPanel, BorderLayout.WEST);
    }

}
