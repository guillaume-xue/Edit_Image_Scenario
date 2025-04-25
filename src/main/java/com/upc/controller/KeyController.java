package com.upc.controller;

import java.awt.event.KeyAdapter;

public class KeyController {

  public class TextFieldKeyListener extends KeyAdapter {

    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
      char c = evt.getKeyChar();
      if (!Character.isDigit(c) && c != '\b') { // Allow only digits and backspace
        evt.consume();
      }
    }
  }

}
