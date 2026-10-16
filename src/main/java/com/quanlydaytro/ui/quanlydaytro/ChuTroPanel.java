package com.quanlydaytro.ui.quanlydaytro;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ ChuTro. */
public class ChuTroPanel extends JPanel {
 public ChuTroPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình ChuTro - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
