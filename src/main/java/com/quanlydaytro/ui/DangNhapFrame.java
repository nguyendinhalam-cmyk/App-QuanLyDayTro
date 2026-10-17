package com.quanlydaytro.ui;

import com.quanlydaytro.dto.KetQuaDangNhap;
import com.quanlydaytro.service.DangNhapService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Màn hình đăng nhập, xác thực qua Service.
 */
public class DangNhapFrame extends JFrame {
    private final JTextField txtTenDangNhap = new JTextField(20);
    private final JPasswordField txtMatKhau = new JPasswordField(20);
    private final JButton btnDangNhap = new JButton("Đăng nhập");
    private final DangNhapService service = new DangNhapService();

    public DangNhapFrame() {
        setTitle("Đăng nhập - Hệ thống quản lý dãy trọ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(460, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(22, 30, 22, 30));
        JLabel title = new JLabel("QUẢN LÝ DÃY TRỌ", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 21f));
        JLabel sub = new JLabel("Đăng nhập vào hệ thống", SwingConstants.CENTER);
        sub.setForeground(new Color(90, 90, 90));
        JPanel header = new JPanel(new GridLayout(2, 1, 2, 2));
        header.add(title);
        header.add(sub);
        root.add(header, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 12));
        form.add(new JLabel("Tên đăng nhập:"));
        form.add(txtTenDangNhap);
        form.add(new JLabel("Mật khẩu:"));
        form.add(txtMatKhau);
        root.add(form, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThoat = new JButton("Thoát");
        bottom.add(btnThoat);
        bottom.add(btnDangNhap);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        txtMatKhau.addActionListener(e -> xuLyDangNhap());
        btnThoat.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnDangNhap);
    }

    private void xuLyDangNhap() {
        btnDangNhap.setEnabled(false);
        char[] password = txtMatKhau.getPassword();
        try {
            KetQuaDangNhap kq = service.dangNhap(txtTenDangNhap.getText(), password);
            if (!kq.isThanhCong()) {
                JOptionPane.showMessageDialog(this, kq.getThongBao(), "Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
                txtMatKhau.setText("");
                txtMatKhau.requestFocus();
                return;
            }
            JOptionPane.showMessageDialog(this, "Xin chào " + com.quanlydaytro.util.QuanLyPhienDangNhap.getHoTen() + " (" + kq.getTenVaiTro() + ")", "Đăng nhập thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new TrangChinhFrame().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể đăng nhập. Hãy kiểm tra kết nối SQL Server và dữ liệu tài khoản.\nChi tiết: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        } finally {
            java.util.Arrays.fill(password, '\0');
            btnDangNhap.setEnabled(true);
        }
    }
}
