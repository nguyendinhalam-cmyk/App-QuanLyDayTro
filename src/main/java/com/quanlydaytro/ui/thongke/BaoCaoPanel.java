package com.quanlydaytro.ui.thongke;
import javax.swing.*;
import java.awt.*;
/** Giao diện khung cho nghiệp vụ BaoCao. */
public class BaoCaoPanel extends JPanel {
 public BaoCaoPanel() {
  setLayout(new BorderLayout());
  add(new JLabel("Màn hình BaoCao - đang chờ triển khai", SwingConstants.CENTER), BorderLayout.CENTER);
 }
}
