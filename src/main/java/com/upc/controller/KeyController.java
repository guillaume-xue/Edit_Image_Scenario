package com.upc.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Contrôleur pour la gestion des événements clavier.
 * Permet de restreindre la saisie dans certains champs de texte.
 */
public class KeyController {

  /**
   * Listener pour les champs de texte numériques.
   * N'autorise que la saisie de chiffres et la touche retour arrière.
   */
  public class TextFieldKeyListener extends KeyAdapter {

    @Override
    public void keyTyped(KeyEvent evt) {
      char c = evt.getKeyChar();
      // Autorise uniquement les chiffres et la touche backspace
      if (!Character.isDigit(c) && c != '\b') {
        evt.consume();
      }
    }
  }

}
