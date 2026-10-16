package com.quanlydaytro.ui.quanlychuyenphong;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ ChuyenPhong. */
public class ChuyenPhongPanel extends JPanel {
 public ChuyenPhongPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình ChuyenPhong - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
