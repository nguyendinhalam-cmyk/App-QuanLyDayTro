package com.quanlydaytro.ui;
import javax.swing.*;
import java.awt.*;
public class TrangChinhFrame extends JFrame {
 public TrangChinhFrame() { setTitle("Hệ thống quản lý dãy trọ"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(1000,650); setLocationRelativeTo(null); add(new JLabel("Trang chính - chọn nghiệp vụ",SwingConstants.CENTER),BorderLayout.CENTER); }
}
