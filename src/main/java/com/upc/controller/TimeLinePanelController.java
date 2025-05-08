package com.upc.controller;

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

  public TimeLinePanelController(TimeLinePanel timeLinePanel, TransferController transferController,
      MouseController mouseController) {
    this.timeLinePanel = timeLinePanel;
    this.timeLinePanel.setTransferHandler(transferController.new TransferTimeLine(timeLinePanel));
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
        ImageIcon resizedIcon = new ImageIcon(icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        resizedIcon.setDescription(imageName);
        timeLinePanel.addImageLabel(resizedIcon, duration);
      }
    }
  }

  public ArrayList<Map.Entry<ImageIcon, Integer>> getImageCopiesWithDurations() {
    ArrayList<Map.Entry<ImageIcon, Integer>> imageCopiesWithDurations = new ArrayList<>();
    DividerPanel tmp = timeLinePanel.getCurrentDividerPanel();
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
