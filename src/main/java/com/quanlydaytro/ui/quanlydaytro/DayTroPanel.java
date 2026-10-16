package com.quanlydaytro.ui.quanlydaytro;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ DayTro. */
public class DayTroPanel extends JPanel {
 public DayTroPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình DayTro - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
