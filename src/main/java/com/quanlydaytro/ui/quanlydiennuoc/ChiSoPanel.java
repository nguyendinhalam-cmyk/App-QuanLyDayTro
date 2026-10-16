package com.quanlydaytro.ui.quanlydiennuoc;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ ChiSo. */
public class ChiSoPanel extends JPanel {
 public ChiSoPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình ChiSo - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
