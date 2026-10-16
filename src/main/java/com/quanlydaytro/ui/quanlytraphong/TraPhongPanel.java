package com.quanlydaytro.ui.quanlytraphong;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ TraPhong. */
public class TraPhongPanel extends JPanel {
 public TraPhongPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình TraPhong - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
