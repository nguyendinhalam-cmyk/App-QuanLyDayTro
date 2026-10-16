package com.quanlydaytro.ui.quanlytaikhoan;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ NhanVien. */
public class NhanVienPanel extends JPanel {
 public NhanVienPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình NhanVien - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
