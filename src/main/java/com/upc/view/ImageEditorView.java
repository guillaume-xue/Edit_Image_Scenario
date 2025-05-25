package com.upc.view;

import com.upc.controller.ImageEditorController.ColorIcon;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import static javax.swing.SwingUtilities.convertPoint;
import static javax.swing.SwingUtilities.isLeftMouseButton;

/**
 * Vue principale de l'éditeur d'image.
 * Gère la barre d'outils, les onglets de dessins et l'interface utilisateur associée.
 */
public class ImageEditorView extends JPanel {
  // Barre d'outils horizontale
  private JToolBar toolBar;
  // Onglets pour les différents panneaux de dessin
  private JTabbedPane tabbedPane;
  // Curseur pour l'épaisseur du trait (non initialisé ici)
  private JSlider thicknessSlider;

  /**
   * Constructeur de la vue éditeur d'image.
   * Initialise la disposition et les composants principaux.
   */
  public ImageEditorView() {
    super();
    setLayout(new BorderLayout());
    toolBar = new JToolBar();
    tabbedPane = new JTabbedPane();

    add(toolBar, BorderLayout.NORTH);
    add(tabbedPane, BorderLayout.CENTER);

    initView();
  }

  /**
   * Initialise la barre d'outils avec les boutons principaux de l'éditeur.
   */
  private void initView() {
    // Ajouter les boutons à la barre d'outils
    JButton add = new JButton("+");
    add.setPreferredSize(new Dimension(30, 30));
    add.setMinimumSize(new Dimension(30, 30));
    add.setMaximumSize(new Dimension(30, 30));
    JButton penButton = new JButton();
    penButton.setPreferredSize(new Dimension(30, 30));
    penButton.setMinimumSize(new Dimension(30, 30));
    penButton.setMaximumSize(new Dimension(30, 30));
    penButton.setIcon(resizeImageIcon("src/main/resources/Icon/stylo.png", "Stylo", 20, 20));
    JButton eraserButton = new JButton();
    eraserButton.setMinimumSize(new Dimension(30, 30));
    eraserButton.setMaximumSize(new Dimension(30, 30));
    eraserButton.setPreferredSize(new Dimension(30, 30));
    eraserButton.setIcon(resizeImageIcon("src/main/resources/Icon/gomme.png", "Gomme", 20, 20));
    JButton circleButton = new JButton();
    circleButton.setMinimumSize(new Dimension(30, 30));
    circleButton.setMaximumSize(new Dimension(30, 30));
    circleButton.setPreferredSize(new Dimension(30, 30));
    circleButton.setIcon(resizeImageIcon("src/main/resources/Icon/cercle.png", "Cercle", 20, 20));
    JButton squareButton = new JButton();
    squareButton.setMinimumSize(new Dimension(30, 30));
    squareButton.setMaximumSize(new Dimension(30, 30));
    squareButton.setPreferredSize(new Dimension(30, 30));
    squareButton.setIcon(resizeImageIcon("src/main/resources/Icon/carre.png", "Carré", 20, 20));
    JButton colorButton = new JButton();
    colorButton.setIcon(new ColorIcon(Color.BLACK, 20, "Couleur"));
    colorButton.setMinimumSize(new Dimension(30, 30));
    colorButton.setMaximumSize(new Dimension(30, 30));
    colorButton.setPreferredSize(new Dimension(30, 30));
    JButton clearButton = new JButton();
    clearButton.setMinimumSize(new Dimension(30, 30));
    clearButton.setMaximumSize(new Dimension(30, 30));
    clearButton.setPreferredSize(new Dimension(30, 30));
    clearButton.setIcon(resizeImageIcon("src/main/resources/Icon/effacer.png", "Clear", 20, 20));

    addToolBarButton(add);
    addToolBarButton(penButton);
    addToolBarButton(eraserButton);
    addToolBarButton(circleButton);
    addToolBarButton(squareButton);
    addToolBarButton(colorButton);
    addToolBarButton(clearButton);

  }

  /**
   * Redimensionne et retourne une ImageIcon à partir d'un chemin.
   * @param path chemin de l'image
   * @param description description de l'icône
   * @param width largeur cible
   * @param height hauteur cible
   * @return ImageIcon redimensionnée
   */
  public ImageIcon resizeImageIcon(String path, String description, int width, int height) {
    ImageIcon tempIcon = new ImageIcon(path);
    int maxW = width, maxH = height;
    int iw = tempIcon.getIconWidth(), ih = tempIcon.getIconHeight();
    double ratio = Math.min((double) maxW / iw, (double) maxH / ih);
    int w = (int) (iw * ratio), h = (int) (ih * ratio);
    Image scaled = tempIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
    tempIcon.getImage().flush(); // Libère la mémoire de l'image originale

    ImageIcon scaledIcon = new ImageIcon(scaled);
    scaledIcon.setDescription(description);
    return scaledIcon;
  }

  /**
   * Ajoute un bouton à la barre d'outils.
   * @param button bouton à ajouter
   */
  public void addToolBarButton(JButton button) {
    toolBar.add(button);
  }

  /**
   * Ajoute un panneau de dessin sous forme d'onglet.
   * @param title titre de l'onglet
   * @param panel panneau de dessin à ajouter
   */
  public void addDrawingPanel(String title, JPanel panel) {
    tabbedPane.addTab(title, panel);
    int index = tabbedPane.indexOfComponent(panel);
    tabbedPane.setTabComponentAt(index, new ClosableTabComponent(tabbedPane, index));
  }

  /**
   * Retourne le panneau de dessin actuellement sélectionné.
   * @return JPanel sélectionné
   */
  public JPanel getSelectedDrawingPanel() {
    return (JPanel) tabbedPane.getSelectedComponent();
  }

  /**
   * Retourne les composants de la barre d'outils.
   * @return tableau de composants
   */
  public Component[] getToolBarComponents() {
    return toolBar.getComponents();
  }

  /**
   * Retourne le curseur d'épaisseur.
   * @return JSlider d'épaisseur
   */
  public JSlider getThicknessSlider() {
    return thicknessSlider;
  }

  /**
   * Retourne le composant JTabbedPane.
   * @return JTabbedPane
   */
  public JTabbedPane getTabbedPane() {
    return tabbedPane;
  }

  /**
   * Composant personnalisé pour les onglets avec possibilité de modification du titre par double-clic.
   */
  public class ClosableTabComponent extends JPanel {
    // Bouton de fermeture (non utilisé ici)
    private JButton closeButton;
    // Label affichant le titre de l'onglet
    private JLabel titleLabel;

    // Timer pour différencier simple et double clic
    private Timer singleClickTimer;
    private final int DOUBLE_CLICK_DELAY = 100; // ms

    /**
     * Constructeur du composant d'onglet personnalisable.
     * @param tabbedPane le JTabbedPane parent
     * @param index index de l'onglet
     */
    public ClosableTabComponent(JTabbedPane tabbedPane, int index) {
      super(new FlowLayout(FlowLayout.LEFT, 0, 0));
      setOpaque(false);

      titleLabel = new JLabel(tabbedPane.getTitleAt(index));
      add(titleLabel);

      // Edition directe du nom d'onglet par double-clic
      titleLabel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2 && isLeftMouseButton(e)) {
            int tabIndex = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
            if (singleClickTimer != null && singleClickTimer.isRunning()) {
              singleClickTimer.stop();
            }
            if (tabIndex != -1) {
              remove(titleLabel);
              JTextField textField = new JTextField(titleLabel.getText());
              textField.setColumns(10);
              add(textField, 0);
              revalidate();
              repaint();
              textField.requestFocusInWindow();

              // Validation par Entrée ou perte de focus
              textField.addActionListener(ev -> finishEdit(tabbedPane, tabIndex, textField));
              textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent ev) {
                  finishEdit(tabbedPane, tabIndex, textField);
                }
              });
            }
          } else if (e.getClickCount() == 1) {
            // Simple clic : attendre pour voir si c’est un double-clic
            if (singleClickTimer != null && singleClickTimer.isRunning()) {
              singleClickTimer.stop();
            }
            // Conversion des coordonnées du label vers le tabbedPane
            Point tabbedPanePoint = convertPoint(titleLabel, e.getPoint(), tabbedPane);
            int tabIndex = tabbedPane.indexAtLocation(tabbedPanePoint.x, tabbedPanePoint.y);
            singleClickTimer = new Timer(DOUBLE_CLICK_DELAY, evt -> {
              if (tabIndex != -1 && tabbedPane.getSelectedIndex() != tabIndex) {
                tabbedPane.setSelectedIndex(tabIndex);
              }
            });
            singleClickTimer.setRepeats(false);
            singleClickTimer.start();
          }
        }
      });
    }

    /**
     * Finalise l'édition du titre d'onglet (validation ou perte de focus).
     * @param tabbedPane le JTabbedPane parent
     * @param tabIndex index de l'onglet
     * @param textField champ de saisie temporaire
     */
    private void finishEdit(JTabbedPane tabbedPane, int tabIndex, JTextField textField) {
      String newTitle = textField.getText().trim();
      if (!newTitle.isEmpty()) {
        titleLabel.setText(newTitle);
        tabbedPane.setTitleAt(tabIndex, newTitle);
      }
      remove(textField);
      add(titleLabel, 0);
      revalidate();
      repaint();
    }

    /**
     * Retourne le bouton de fermeture (non utilisé ici).
     * @return JButton de fermeture
     */
    public JButton geButtontCloseButton() {
      return closeButton;
    }
  }
}
