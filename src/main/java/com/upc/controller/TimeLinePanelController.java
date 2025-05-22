package com.upc.controller;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;

import com.upc.view.DividerPanel;
import com.upc.view.ResizablePanel;
import com.upc.view.TimeLinePanel;

public class TimeLinePanelController {

  private TimeLinePanel timeLinePanel;
  private DividerPanel currentDividerPanel = null;
  private MouseController mouseController;

  public TimeLinePanelController(TimeLinePanel timeLinePanel, TransferController transferController,
      MouseController mouseController) {
    this.timeLinePanel = timeLinePanel;
    this.mouseController = mouseController;
    this.timeLinePanel.setTransferHandler(transferController.new TransferTimeLine(this));
  }

  public void initTimeLinePanel(File scenario, File imageDirectory) {
    if (scenario == null || !scenario.exists()) {
      return; // Scenario file does not exist
    }
    String scenarioContent;
    try {
      scenarioContent = new String(java.nio.file.Files.readAllBytes(scenario.toPath()));
    } catch (java.io.IOException e) {
      e.printStackTrace();
      return; // Exit if the file cannot be read
    }
    String[] lines = scenarioContent.split("\n");
    for (String line : lines) {
      String[] parts = line.split(",");
      if (parts.length == 2) {

        String imageName = parts[0].trim();
        File imageFile = new File(imageDirectory, imageName);
        int duration = Integer.parseInt(parts[1].trim());

        ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
        icon.setDescription(imageName);
        addImageLabel(icon, duration);
      }
    }
    timeLinePanel.getTimeLinePanel().revalidate();
    timeLinePanel.getTimeLinePanel().repaint();
  }

  public void addImageLabel(ImageIcon imageIcon, int duration) {
    ResizablePanel resizablePanel = new ResizablePanel(imageIcon, duration);
    int width = duration;
    int height = 100;
    resizablePanel.setPreferredSize(new Dimension(width, height));
    resizablePanel.setMinimumSize(new Dimension(width, height));
    resizablePanel.setMaximumSize(new Dimension(width, height));
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
  }

  public void removeImageLabel(ResizablePanel resizablePanel) {
    currentDividerPanel = removeImageLabelRecursive(currentDividerPanel, resizablePanel);
    if (currentDividerPanel == null) {
      timeLinePanel.getTimeLinePanel().removeAll();
    }
    timeLinePanel.getTimeLinePanel().revalidate();
    timeLinePanel.getTimeLinePanel().repaint();
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

}
