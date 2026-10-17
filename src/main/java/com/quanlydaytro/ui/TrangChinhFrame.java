package com.quanlydaytro.ui;

import com.quanlydaytro.ui.quanlytaikhoan.NhanVienPanel;
import com.quanlydaytro.ui.quanlydaytro.ChuTroPanel;
import com.quanlydaytro.ui.quanlydaytro.DayTroPanel;
import com.quanlydaytro.ui.quanlyphong.PhongPanel;
import com.quanlydaytro.ui.quanlytaikhoan.TaiKhoanPanel;
import com.quanlydaytro.util.QuanLyPhienDangNhap;
import javax.swing.*;
import java.awt.*;

/** Khung chính; các chức năng quản trị được kiểm tra quyền trước khi mở. */
public class TrangChinhFrame extends JFrame {
    private final JPanel noiDung = new JPanel(new BorderLayout());

    public TrangChinhFrame() {
        setTitle("Hệ thống quản lý dãy trọ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String ten = QuanLyPhienDangNhap.getHoTen() == null
                ? QuanLyPhienDangNhap.getTenDangNhap() : QuanLyPhienDangNhap.getHoTen();
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

            JMenuItem nhanVien = new JMenuItem("Quản lý nhân viên");
            nhanVien.addActionListener(e -> hienThiNhanVien());
            heThong.add(nhanVien);
        }
        if (QuanLyPhienDangNhap.coVaiTro("Admin", "Manager")) {
            JMenuItem chuTro = new JMenuItem("Quản lý chủ trọ");
            chuTro.addActionListener(e -> hienThiChuTro());
            heThong.add(chuTro);
            JMenuItem dayTro = new JMenuItem("Quản lý dãy trọ");
            dayTro.addActionListener(e -> hienThiDayTro());
            heThong.add(dayTro);
            JMenuItem phong = new JMenuItem("Quản lý phòng");
            phong.addActionListener(e -> hienThiPhong());
            heThong.add(phong);
        }
        JMenuItem trangChu = new JMenuItem("Trang chủ");
        trangChu.addActionListener(e -> hienThiTrangChu());
        heThong.addSeparator();
        heThong.add(trangChu);
        JMenuItem dangXuat = new JMenuItem("Đăng xuất");
        dangXuat.addActionListener(e -> {
            QuanLyPhienDangNhap.ketThuc();
            dispose();
            new DangNhapFrame().setVisible(true);
        });
        heThong.add(dangXuat);
        bar.add(heThong);
        setJMenuBar(bar);

        add(noiDung, BorderLayout.CENTER);
        hienThiTrangChu();
    }

    private void hienThiTrangChu() {
        noiDung.removeAll();
        JLabel welcome = new JLabel("HỆ THỐNG QUẢN LÝ DÃY TRỌ", SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 22f));
        noiDung.add(welcome, BorderLayout.CENTER);
        noiDung.revalidate();
        noiDung.repaint();
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

    private void hienThiChuTro() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin", "Manager")) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này.");
            return;
        }
        noiDung.removeAll();
        noiDung.add(new ChuTroPanel(), BorderLayout.CENTER);
        noiDung.revalidate();
        noiDung.repaint();
    }

    private void hienThiDayTro() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin", "Manager")) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này.");
            return;
        }
        noiDung.removeAll();
        noiDung.add(new DayTroPanel(), BorderLayout.CENTER);
        noiDung.revalidate();
        noiDung.repaint();
    }

    private void hienThiPhong() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin", "Manager")) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này.");
            return;
        }
        noiDung.removeAll();
        noiDung.add(new PhongPanel(), BorderLayout.CENTER);
        noiDung.revalidate();
        noiDung.repaint();
    }

    private void hienThiNhanVien() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin")) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này.");
            return;
        }
        noiDung.removeAll();
        noiDung.add(new NhanVienPanel(), BorderLayout.CENTER);
        noiDung.revalidate();
        noiDung.repaint();
    }
}
