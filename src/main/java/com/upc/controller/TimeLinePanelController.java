package com.upc.controller;

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

  public void initTimeLinePanel() {

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
