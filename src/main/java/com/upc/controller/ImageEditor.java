package com.upc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.upc.view.ImageEditPanel;
import com.upc.model.ImageEditorModel;

public class ImageEditor {

    ImageEditorModel imageEditorModel;
    ImageEditPanel imageEditPanel;

    public ImageEditPanel getImageEditPanel() {
        return imageEditPanel;
    }

    public ImageEditor() {
        imageEditorModel = new ImageEditorModel();
        imageEditPanel = new ImageEditPanel(this, imageEditorModel);
    }
    
    public void imageEditorTopButton(JButton button){
        switch (button.getText()) {
            case "Stylo":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        imageEditorModel.setSelectedTool(0);
                    }
                });
                break;
            case "Gomme":
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        imageEditorModel.setSelectedTool(1);
                    }
                });
                break;
            case "Cercle":
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    imageEditorModel.setSelectedTool(2);
                }
            });
                break;
            case "Carré":
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    imageEditorModel.setSelectedTool(3);
                }
            });
                break;
            case "Couleur":
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    java.awt.Color selectedColor = javax.swing.JColorChooser.showDialog(null, "Choisissez une couleur", null);
                    imageEditorModel.setSelectedColor(selectedColor);
                }
            });
                break;
            default:
                break;
        }
    }

    public void imageEditorMainPanel(JPanel panel){
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            // @Override
            // public void mousePressed(java.awt.event.MouseEvent e) {
            //     int x = e.getX();
            //     int y = e.getY();
            //     imageEditPanel.drawAt(x, y);
            // }
            
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                imageEditPanel.drawAt(x, y);
            }
        });
    }


}
