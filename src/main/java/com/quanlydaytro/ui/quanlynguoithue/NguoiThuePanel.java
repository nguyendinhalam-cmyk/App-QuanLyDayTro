package com.quanlydaytro.ui.quanlynguoithue;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ NguoiThue. */
public class NguoiThuePanel extends JPanel {
 public NguoiThuePanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình NguoiThue - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
