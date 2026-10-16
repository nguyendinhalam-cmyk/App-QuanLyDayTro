package com.quanlydaytro.ui.quanlydiennuoc;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ BangGia. */
public class BangGiaPanel extends JPanel {
 public BangGiaPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình BangGia - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
