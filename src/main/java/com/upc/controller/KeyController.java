package com.upc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController {

  public class TextFieldKeyListener extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent evt) {
      char c = evt.getKeyChar();
      if (!Character.isDigit(c) && c != '\b') { // Allow only digits and backspace
        evt.consume();
      }
    }
  }

}
