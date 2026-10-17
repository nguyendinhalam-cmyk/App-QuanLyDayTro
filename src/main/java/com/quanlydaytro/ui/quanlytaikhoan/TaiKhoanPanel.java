package com.quanlydaytro.ui.quanlytaikhoan;

import com.quanlydaytro.service.TaiKhoanService;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Giao diện quản lý tài khoản: danh sách, thêm, sửa, xóa, tải lại.
 */
public class TaiKhoanPanel extends JPanel {
    private final TaiKhoanService service = new TaiKhoanService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã tài khoản", "Tên đăng nhập", "Mã NV", "Nhân viên", "Mã vai trò", "Vai trò"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public TaiKhoanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        JLabel title = new JLabel("QUẢN LÝ TÀI KHOẢN", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 19f));
        add(title, BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton them = new JButton("Thêm tài khoản"), sua = new JButton("Sửa tài khoản"), xoa = new JButton("Xóa tài khoản"), taiLai = new JButton("Làm mới");
        buttons.add(them);
        buttons.add(sua);
        buttons.add(xoa);
        buttons.add(taiLai);
        add(buttons, BorderLayout.SOUTH);
        them.addActionListener(e -> them());
        sua.addActionListener(e -> sua());
        xoa.addActionListener(e -> xoa());
        taiLai.addActionListener(e -> taiDuLieu());
        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            model.setRowCount(0);
            for (Object[] row : service.layDanhSachHienThi()) model.addRow(row);
        } catch (SQLException ex) {
            baoLoi("Không tải được danh sách tài khoản", ex);
        }
    }

    private void them() {
        hienThiForm(null);
    }

    private void sua() {
        int row = table.getSelectedRow();
        if (row < 0) {
            thongBao("Vui lòng chọn tài khoản cần sửa.");
            return;
        }
        int m = table.convertRowIndexToModel(row);
        hienThiForm(new Object[]{model.getValueAt(m, 0), model.getValueAt(m, 1), model.getValueAt(m, 2), model.getValueAt(m, 4)});
    }

    private void hienThiForm(Object[] duLieu) {
        boolean editing = duLieu != null;
        int ma = editing ? ((Number) duLieu[0]).intValue() : 0;
        JTextField ten = new JTextField(editing ? String.valueOf(duLieu[1]) : "", 18);
        JPasswordField pass = new JPasswordField(18);
        JComboBox<LuaChon> nhanVien = new JComboBox<>(), vaiTro = new JComboBox<>();
        try {
            for (Object[] nv : service.layNhanVienChuaCoTaiKhoan(ma))
                nhanVien.addItem(new LuaChon((Integer) nv[0], nv[1] + " (Mã NV: " + nv[0] + ")"));
            for (Object[] vt : service.layVaiTro()) vaiTro.addItem(new LuaChon((Integer) vt[0], String.valueOf(vt[1])));
        } catch (SQLException ex) {
            baoLoi("Không tải được danh mục nhân viên/vai trò", ex);
            return;
        }
        if (editing) {
            chonMa(nhanVien, ((Number) duLieu[2]).intValue());
            chonMa(vaiTro, ((Number) duLieu[3]).intValue());
        }
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 9));
        form.add(new JLabel("Tên đăng nhập:"));
        form.add(ten);
        form.add(new JLabel(editing ? "Mật khẩu mới (để trống nếu giữ nguyên):" : "Mật khẩu (ít nhất 8 ký tự):"));
        form.add(pass);
        form.add(new JLabel("Nhân viên:"));
        form.add(nhanVien);
        form.add(new JLabel("Vai trò:"));
        form.add(vaiTro);
        int result = JOptionPane.showConfirmDialog(this, form, editing ? "Sửa tài khoản" : "Thêm tài khoản", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        if (nhanVien.getSelectedItem() == null || vaiTro.getSelectedItem() == null) {
            thongBao("Cần có nhân viên chưa gán tài khoản và vai trò.");
            return;
        }
        char[] pw = pass.getPassword();
        LuaChon nv = (LuaChon) nhanVien.getSelectedItem(), vt = (LuaChon) vaiTro.getSelectedItem();
        try {
            if (editing) service.capNhat(ma, ten.getText(), pw, nv.ma, vt.ma);
            else service.them(ten.getText(), pw, nv.ma, vt.ma);
            thongBao(editing ? "Cập nhật tài khoản thành công." : "Thêm tài khoản thành công.");
            taiDuLieu();
        } catch (Exception ex) {
            baoLoi("Không thể lưu tài khoản", ex);
        } finally {
            Arrays.fill(pw, '\0');
        }
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row < 0) {
            thongBao("Vui lòng chọn tài khoản cần xóa.");
            return;
        }
        int m = table.convertRowIndexToModel(row);
        int ma = ((Number) model.getValueAt(m, 0)).intValue();
        String ten = String.valueOf(model.getValueAt(m, 1));
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài khoản '" + ten + "'?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        try {
            if (service.xoa(ma)) thongBao("Đã xóa tài khoản.");
            else thongBao("Không tìm thấy tài khoản cần xóa.");
            taiDuLieu();
        } catch (Exception ex) {
            baoLoi("Không thể xóa tài khoản", ex);
        }
    }

    private void chonMa(JComboBox<LuaChon> cb, int ma) {
        for (int i = 0; i < cb.getItemCount(); i++)
            if (cb.getItemAt(i).ma == ma) {
                cb.setSelectedIndex(i);
                return;
            }
    }

    private void thongBao(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    private void baoLoi(String s, Exception ex) {
        JOptionPane.showMessageDialog(this, s + ":\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private static class LuaChon {
        final int ma;
        final String ten;

        LuaChon(int ma, String ten) {
            this.ma = ma;
            this.ten = ten;
        }

        public String toString() {
            return ten;
        }
    }
}
