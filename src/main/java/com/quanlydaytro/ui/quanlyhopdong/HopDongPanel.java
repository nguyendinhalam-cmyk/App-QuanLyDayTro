package com.quanlydaytro.ui.quanlyhopdong;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ HopDong. */
public class HopDongPanel extends JPanel {
 public HopDongPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình HopDong - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
