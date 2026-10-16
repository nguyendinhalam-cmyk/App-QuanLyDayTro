package com.quanlydaytro.ui;
import javax.swing.*;
import java.awt.*;
public class DangNhapFrame extends JFrame {
 public DangNhapFrame() {
  setTitle("Đăng nhập - Quản lý dãy trọ"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(420,230); setLocationRelativeTo(null);
  JTextField ten = new JTextField(18); JPasswordField matKhau = new JPasswordField(18);
  JPanel form = new JPanel(new GridLayout(0,2,8,8)); form.setBorder(BorderFactory.createEmptyBorder(20,20,12,20));
  form.add(new JLabel("Tên đăng nhập:")); form.add(ten); form.add(new JLabel("Mật khẩu:")); form.add(matKhau);
  JButton nut = new JButton("Đăng nhập"); nut.addActionListener(e -> JOptionPane.showMessageDialog(this,"Giao diện mẫu. Cần kết nối DangNhapService để xác thực."));
  JPanel bottom = new JPanel(); bottom.add(nut); add(form,BorderLayout.CENTER); add(bottom,BorderLayout.SOUTH);
 }
}
