package com.quanlydaytro.ui;

import com.quanlydaytro.ui.quanlytaikhoan.TaiKhoanPanel;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import javax.swing.*;
import java.awt.*;

/**
 * Khung chính và menu được giới hạn theo vai trò đang đăng nhập.
 */
public class TrangChinhFrame extends JFrame {
    private final JPanel noiDung = new JPanel(new BorderLayout());

    public TrangChinhFrame() {
        setTitle("Hệ thống quản lý dãy trọ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        String ten = QuanLyPhienDangNhap.getHoTen() == null ? QuanLyPhienDangNhap.getTenDangNhap() : QuanLyPhienDangNhap.getHoTen();
        JLabel banner = new JLabel("  Xin chào " + ten + "  |  Vai trò: " + QuanLyPhienDangNhap.getTenVaiTro());
        banner.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
        banner.setFont(banner.getFont().deriveFont(Font.BOLD, 14f));
        add(banner, BorderLayout.NORTH);
        JMenuBar bar = new JMenuBar();
        JMenu heThong = new JMenu("Hệ thống");
        if (QuanLyPhienDangNhap.coVaiTro("Admin")) {
            JMenuItem taiKhoan = new JMenuItem("Quản lý tài khoản");
            taiKhoan.addActionListener(e -> hienThiTaiKhoan());
            heThong.add(taiKhoan);
        }
        JMenuItem dangXuat = new JMenuItem("Đăng xuất");
        dangXuat.addActionListener(e -> {
            QuanLyPhienDangNhap.ketThuc();
            dispose();
            new DangNhapFrame().setVisible(true);
        });
        heThong.addSeparator();
        heThong.add(dangXuat);
        bar.add(heThong);
        setJMenuBar(bar);
        noiDung.add(new JLabel("HỆ THỐNG QUẢN LÝ DÃY TRỌ", SwingConstants.CENTER), BorderLayout.CENTER);
        add(noiDung, BorderLayout.CENTER);
        if (QuanLyPhienDangNhap.coVaiTro("Admin")) hienThiTaiKhoan();
    }

    private void hienThiTaiKhoan() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin")) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này.");
            return;
        }
        noiDung.removeAll();
        noiDung.add(new TaiKhoanPanel(), BorderLayout.CENTER);
        noiDung.revalidate();
        noiDung.repaint();
    }
}
