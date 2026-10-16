package com.quanlydaytro.ui.quanlyhoadon;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ HoaDon. */
public class HoaDonPanel extends JPanel {
 public HoaDonPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình HoaDon - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
