package com.quanlydaytro.ui.quanlydiennuoc;

import com.quanlydaytro.model.*;
import com.quanlydaytro.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Màn hình quản lý chỉ số điện, nước theo kỳ.
 */
public class ChiSoPanel extends JPanel {
    private final ChiSoDienService dien = new ChiSoDienService();
    private final ChiSoNuocService nuoc = new ChiSoNuocService();
    private final DefaultTableModel dmD = new DefaultTableModel(new Object[]{"Mã", "Mã phòng", "Kỳ (MM/yyyy)", "Chỉ số cũ", "Chỉ số mới", "Tiêu thụ", "Ngày ghi"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final DefaultTableModel dmN = new DefaultTableModel(new Object[]{"Mã", "Mã phòng", "Kỳ (MM/yyyy)", "Chỉ số cũ", "Chỉ số mới", "Tiêu thụ", "Ngày ghi"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tbD = new JTable(dmD), tbN = new JTable(dmN);
    private final JTextField timD = new JTextField(15), timN = new JTextField(15);

    public ChiSoPanel() {
        setLayout(new BorderLayout(8, 8));
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Chỉ số điện", taoTab(true));
        tabs.addTab("Chỉ số nước", taoTab(false));
        add(tabs, BorderLayout.CENTER);
        napDien("");
        napNuoc("");
    }

    private JPanel taoTab(boolean isDien) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JTable table = isDien ? tbD : tbN;
        JTextField search = isDien ? timD : timN;
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Tìm mã chỉ số / mã phòng / kỳ:"));
        top.add(search);
        JButton tim = new JButton("Tìm kiếm"), all = new JButton("Làm mới"), add = new JButton("Thêm"), edit = new JButton("Sửa"), del = new JButton("Xóa");
        top.add(tim);
        top.add(all);
        top.add(add);
        top.add(edit);
        top.add(del);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        tim.addActionListener(e -> {
            if (isDien) napDien(search.getText());
            else napNuoc(search.getText());
        });
        all.addActionListener(e -> {
            search.setText("");
            if (isDien) napDien("");
            else napNuoc("");
        });
        add.addActionListener(e -> form(isDien, false, table));
        edit.addActionListener(e -> form(isDien, true, table));
        del.addActionListener(e -> xoa(isDien, table));
        return p;
    }

    private void napDien(String k) {
        try {
            List<ChiSoDien> ds = k == null || k.trim().isEmpty() ? dien.layTatCa() : dien.timKiem(k);
            dmD.setRowCount(0);
            for (ChiSoDien e : ds)
                dmD.addRow(new Object[]{e.getMaChiSoDien(), e.getMaPhong(), e.getThangNam(), e.getChiSoCu(), e.getChiSoMoi(), e.getChiSoMoi() - e.getChiSoCu(), e.getNgayGhi()});
        } catch (Exception e) {
            baoLoi(e);
        }
    }

    private void napNuoc(String k) {
        try {
            List<ChiSoNuoc> ds = k == null || k.trim().isEmpty() ? nuoc.layTatCa() : nuoc.timKiem(k);
            dmN.setRowCount(0);
            for (ChiSoNuoc e : ds)
                dmN.addRow(new Object[]{e.getMaChiSoNuoc(), e.getMaPhong(), e.getThangNam(), e.getChiSoCu(), e.getChiSoMoi(), e.getChiSoMoi() - e.getChiSoCu(), e.getNgayGhi()});
        } catch (Exception e) {
            baoLoi(e);
        }
    }

    private void form(boolean d, boolean edit, JTable table) {
        int row = table.getSelectedRow();
        if (edit && row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi cần sửa.");
            return;
        }
        Object[] old = null;
        if (edit) {
            int r = table.convertRowIndexToModel(row);
            DefaultTableModel m = d ? dmD : dmN;
            old = new Object[]{m.getValueAt(r, 0), m.getValueAt(r, 1), m.getValueAt(r, 2), m.getValueAt(r, 3), m.getValueAt(r, 4), m.getValueAt(r, 6)};
        }
        JTextField phong = new JTextField(edit ? String.valueOf(old[1]) : ""), ky = new JTextField(edit ? String.valueOf(old[2]) : java.time.YearMonth.now().format(java.time.format.DateTimeFormatter.ofPattern("MM/yyyy"))), cu = new JTextField(edit ? String.valueOf(old[3]) : "0"), moi = new JTextField(edit ? String.valueOf(old[4]) : ""), ngay = new JTextField(edit ? String.valueOf(old[5]) : LocalDate.now().toString());
        JPanel f = new JPanel(new GridLayout(0, 2, 6, 6));
        f.add(new JLabel("Mã phòng"));
        f.add(phong);
        f.add(new JLabel("Kỳ ghi (MM/yyyy)"));
        f.add(ky);
        f.add(new JLabel("Chỉ số cũ"));
        f.add(cu);
        f.add(new JLabel("Chỉ số mới"));
        f.add(moi);
        f.add(new JLabel("Ngày ghi (yyyy-MM-dd)"));
        f.add(ngay);
        if (JOptionPane.showConfirmDialog(this, f, edit ? "Sửa chỉ số" : "Thêm chỉ số", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION)
            return;
        try {
            int mp = Integer.parseInt(phong.getText().trim()), c = Integer.parseInt(cu.getText().trim()), m = Integer.parseInt(moi.getText().trim());
            String th = ky.getText().trim();
            LocalDate date = LocalDate.parse(ngay.getText().trim());
            if (d) {
                ChiSoDien x = new ChiSoDien(0, mp, th, c, m, date);
                if (edit) dien.capNhat(Integer.parseInt(old[0].toString()), x);
                else dien.them(x);
            } else {
                ChiSoNuoc x = new ChiSoNuoc(0, mp, th, c, m, date);
                if (edit) nuoc.capNhat(Integer.parseInt(old[0].toString()), x);
                else nuoc.them(x);
            }
            JOptionPane.showMessageDialog(this, "Lưu chỉ số thành công.");
            if (d) napDien("");
            else napNuoc("");
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void xoa(boolean d, JTable t) {
        int r = t.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi cần xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa bản ghi chỉ số này?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        try {
            int id = Integer.parseInt(t.getValueAt(r, 0).toString());
            if (d) dien.xoa(id);
            else nuoc.xoa(id);
            if (d) napDien("");
            else napNuoc("");
        } catch (Exception e) {
            baoLoi(e);
        }
    }

    private void baoLoi(Exception e) {
        String m = e.getMessage() == null ? "Đã xảy ra lỗi." : e.getMessage();
        JOptionPane.showMessageDialog(this, m, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
