package com.upc.controller;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.nio.file.Files.readAllBytes;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.JScrollBar;

import com.upc.view.DividerPanel;
import com.upc.view.ResizablePanel;
import com.upc.view.TimeLinePanel;

public class TimeLinePanelController {

  private TimeLinePanel timeLinePanel;
  private DividerPanel currentDividerPanel = null;
  private MouseController mouseController;
  private JDialog loadingDialog;

  public TimeLinePanelController(TimeLinePanel timeLinePanel, TransferController transferController,
      MouseController mouseController, JDialog loadingDialog) {
    this.timeLinePanel = timeLinePanel;
    this.mouseController = mouseController;
    this.loadingDialog = loadingDialog;
    this.timeLinePanel.setTransferHandler(transferController.new TransferTimeLine(this));
    this.timeLinePanel.getZoomInButton().addActionListener(e -> zoomIn());
    this.timeLinePanel.getZoomOutButton().addActionListener(e -> zoomOut());
  }

  public void initTimeLinePanel(File scenario, File imageDirectory) {
    if (scenario == null || !scenario.exists()) {
      return; // Scenario file does not exist
    }

    new SwingWorker<Void, Object[]>() {
      @Override
      protected Void doInBackground() {
        String scenarioContent;
        try {
          scenarioContent = new String(readAllBytes(scenario.toPath()));
        } catch (java.io.IOException e) {
          e.printStackTrace();
          return null; // Exit if the file cannot be read
        }
        String[] lines = scenarioContent.split("\n");
        for (String line : lines) {
          String[] parts = line.split(",");
          if (parts.length == 2) {
            String imageName = parts[0].trim();
            File imageFile = new File(imageDirectory, imageName);
            if (!imageFile.exists()) {
              // Publier l'erreur pour affichage dans l'EDT
              publish(new Object[] { "error", imageFile.getAbsolutePath() });
              continue;
            }
            int duration = Integer.parseInt(parts[1].trim());

            // Charge et redimensionne l'image
            ImageIcon tempIcon = new ImageIcon(imageFile.getAbsolutePath());
            int maxW = 120, maxH = 80;
            int iw = tempIcon.getIconWidth(), ih = tempIcon.getIconHeight();
            double ratio = Math.min((double) maxW / iw, (double) maxH / ih);
            int w = (int) (iw * ratio), h = (int) (ih * ratio);
            Image scaled = tempIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);
            scaledIcon.setDescription(imageName);

            // Publier l'image et la durée pour ajout dans l'EDT
            publish(new Object[] { "image", scaledIcon, duration });
          }
        }
        return null;
      }

      @Override
      protected void process(java.util.List<Object[]> chunks) {
        for (Object[] obj : chunks) {
          if ("error".equals(obj[0])) {
            String path = (String) obj[1];
            System.err.println("Image file not found: " + path);
          } else if ("image".equals(obj[0])) {
            ImageIcon scaledIcon = (ImageIcon) obj[1];
            int duration = (int) obj[2];
            addImageLabel(scaledIcon, duration);
          }
        }
        timeLinePanel.getTimeLinePanel().revalidate();
        timeLinePanel.getTimeLinePanel().repaint();
      }

      @Override
      protected void done() {
        if (loadingDialog != null && loadingDialog.isVisible()) {
          loadingDialog.dispose();
        }
      }
    }.execute();
  }

  public void zoomIn() {
    double current = timeLinePanel.getZoomFactor();
    if (current < 5.0) { // Limite max de zoom
      timeLinePanel.setZoomFactor(current * 1.25);
      syncScroll();
    }
  }

  public void zoomOut() {
    double current = timeLinePanel.getZoomFactor();
    if (current > 0.2) { // Limite min de zoom
      timeLinePanel.setZoomFactor(current / 1.25);
      syncScroll();
    }
  }

  private void syncScroll() {
    JScrollBar hBar = timeLinePanel.getScrollPane().getHorizontalScrollBar();
    int value = hBar.getValue();
    hBar.setValue(value); // Force la synchro de la position
  }

  public void addImageLabel(ImageIcon imageIcon, int duration) {
    ResizablePanel resizablePanel = new ResizablePanel(imageIcon, duration);
    double zoom = timeLinePanel.getZoomFactor();
    resizablePanel.updateWidthFromDuration(zoom);
    resizablePanel.setParentTimeLinePanel(timeLinePanel); // Ajouté
    resizablePanel.addMouseListener(mouseController.new TimeLinePanelMouseController(resizablePanel, this));
    ResizablePanel emptyPanel = new ResizablePanel();
    timeLinePanel.getTimeLinePanel().add(resizablePanel);
    if (currentDividerPanel != null) {
      timeLinePanel.getTimeLinePanel().remove(timeLinePanel.getTimeLinePanel().getComponentCount() - 2);
      currentDividerPanel = new DividerPanel(currentDividerPanel, resizablePanel, emptyPanel);
    } else {
      currentDividerPanel = new DividerPanel(null, resizablePanel, emptyPanel);
    }

    timeLinePanel.getTimeLinePanel().add(currentDividerPanel);
    timeLinePanel.getTimeLinePanel().add(emptyPanel);
    timeLinePanel.getTimeLinePanel().revalidate(); // Refresh layout
    timeLinePanel.getTimeLinePanel().repaint(); // Repaint the panel
    timeLinePanel.updateEndMarginPanel();
  }

  public void removeImageLabel(ResizablePanel resizablePanel) {
    currentDividerPanel = removeImageLabelRecursive(currentDividerPanel, resizablePanel);
    if (currentDividerPanel == null) {
      timeLinePanel.getTimeLinePanel().removeAll();
    }
    timeLinePanel.getTimeLinePanel().revalidate();
    timeLinePanel.getTimeLinePanel().repaint();
    timeLinePanel.updateEndMarginPanel();
  }

  private DividerPanel removeImageLabelRecursive(DividerPanel divider, ResizablePanel resizablePanel) {
    if (divider == null) {
      return null;
    }
    if (divider.getLeft() == resizablePanel) {
      // Suppression dans l'affichage
      timeLinePanel.getTimeLinePanel().remove(divider);
      timeLinePanel.getTimeLinePanel().remove(divider.getLeft());
      // Mise à jour de la chaîne
      return divider.getPrecDividerPanel();
    } else {
      divider.setPrecDividerPanel(removeImageLabelRecursive(divider.getPrecDividerPanel(), resizablePanel));
      return divider;
    }
  }

  public ArrayList<Map.Entry<ImageIcon, Integer>> getImageCopiesWithDurations() {
    ArrayList<Map.Entry<ImageIcon, Integer>> imageCopiesWithDurations = new ArrayList<>();
    DividerPanel tmp = currentDividerPanel;
    while (tmp != null) {
      ResizablePanel left = tmp.getLeft();
      if (left != null && left.getIcon() != null) {
        imageCopiesWithDurations.add(Map.entry(left.getIcon(), left.getDuration()));
      }
      tmp = tmp.getPrecDividerPanel();
    }
    // Reverse the list to maintain the correct order
    ArrayList<Map.Entry<ImageIcon, Integer>> reversedList = new ArrayList<>();
    for (int i = imageCopiesWithDurations.size() - 1; i >= 0; i--) {
      reversedList.add(imageCopiesWithDurations.get(i));
    }
    imageCopiesWithDurations = reversedList;
    return imageCopiesWithDurations;
  }

  public TimeLinePanel getTimeLinePanel() {
    return timeLinePanel;
  }

  // Déplacer la scène à une position absolue (0 = début, -1 = fin)
  public void moveResizablePanelTo(ResizablePanel panel, int index) {
    JPanel scene = timeLinePanel.getTimeLinePanel();
    List<ResizablePanel> panels = new ArrayList<>();
    for (int i = 0; i < scene.getComponentCount(); i++) {
      if (scene.getComponent(i) instanceof ResizablePanel) {
        ResizablePanel rp = (ResizablePanel) scene.getComponent(i);
        if (rp.getIcon() != null) {
          panels.add(rp);
        }
      }
    }
    panels.remove(panel);
    if (index < 0 || index > panels.size()) {
      panels.add(panel); // fin
    } else {
      panels.add(index, panel);
    }

    // Reconstruire la timeline sans empty panels
    scene.removeAll();
    DividerPanel prev = null;
    double zoom = timeLinePanel.getZoomFactor();
    for (int i = 0; i < panels.size(); i++) {
      ResizablePanel rp = panels.get(i);
      rp.setZoomFactor(zoom);
      rp.updateWidthFromDuration(zoom);
      scene.add(rp);
      DividerPanel divider = new DividerPanel(prev, rp, null);
      scene.add(divider);
      prev = divider;
    }
    currentDividerPanel = prev;
    scene.revalidate();
    scene.repaint();
    timeLinePanel.updateEndMarginPanel();
    timeLinePanel.revalidate();
    timeLinePanel.repaint();
  }

  // Déplacer la scène d'un pas relatif (+1 = avancer, -1 = reculer)
  public void moveResizablePanelRelative(ResizablePanel panel, int delta) {
    JPanel scene = timeLinePanel.getTimeLinePanel();
    List<ResizablePanel> panels = new ArrayList<>();
    for (int i = 0; i < scene.getComponentCount(); i++) {
      if (scene.getComponent(i) instanceof ResizablePanel) {
        ResizablePanel rp = (ResizablePanel) scene.getComponent(i);
        if (rp.getIcon() != null) {
          panels.add(rp);
        }
      }
    }
    int idx = panels.indexOf(panel);
    if (idx == -1)
      return;
    int newIdx = idx + delta;
    if (newIdx < 0)
      newIdx = 0;
    if (newIdx >= panels.size())
      newIdx = panels.size() - 1;
    if (newIdx == idx)
      return;
    panels.remove(idx);
    panels.add(newIdx, panel);

    // Reconstruire la timeline sans empty panels
    scene.removeAll();
    DividerPanel prev = null;
    double zoom = timeLinePanel.getZoomFactor();
    for (int i = 0; i < panels.size(); i++) {
      ResizablePanel rp = panels.get(i);
      rp.setZoomFactor(zoom);
      rp.updateWidthFromDuration(zoom);
      scene.add(rp);
      DividerPanel divider = new DividerPanel(prev, rp, null);
      scene.add(divider);
      prev = divider;
    }
    currentDividerPanel = prev;
    scene.revalidate();
    scene.repaint();
    timeLinePanel.updateEndMarginPanel();
    timeLinePanel.revalidate();
    timeLinePanel.repaint();
  }
}
