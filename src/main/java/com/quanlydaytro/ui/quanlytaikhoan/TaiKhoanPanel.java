package com.quanlydaytro.ui.quanlytaikhoan;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ TaiKhoan. */
public class TaiKhoanPanel extends JPanel {
 public TaiKhoanPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình TaiKhoan - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
