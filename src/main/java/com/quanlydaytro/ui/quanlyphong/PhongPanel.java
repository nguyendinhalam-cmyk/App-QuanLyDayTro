package com.quanlydaytro.ui.quanlyphong;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ Phong. */
public class PhongPanel extends JPanel {
 public PhongPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình Phong - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
