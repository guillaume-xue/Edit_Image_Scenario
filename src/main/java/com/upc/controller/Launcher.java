package com.upc.controller;

import javax.swing.JFrame;

import com.upc.view.MainFrame;

public class Launcher {
  public Launcher() {
    JFrame frame = new MainFrame();
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
