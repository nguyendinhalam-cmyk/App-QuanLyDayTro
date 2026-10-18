package com.quanlydaytro.ui.quanlydaytro;

import com.quanlydaytro.model.ChuTro;
import com.quanlydaytro.service.ChuTroService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/**
 * Giao diện CRUD chủ trọ.
 */
public class ChuTroPanel extends JPanel {
    private final ChuTroService service = new ChuTroService();
    private final JTextField txtHoTen = new JTextField(22), txtSDT = new JTextField(16), txtTim = new JTextField(18);
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã chủ trọ", "Họ tên", "Số điện thoại"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private int maDangChon = 0;

    public ChuTroPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("QUẢN LÝ CHỦ TRỌ");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 5, 4, 5);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0;
        g.gridy = 0;
        form.add(new JLabel("Họ tên:"), g);
        g.gridx = 1;
        form.add(txtHoTen, g);
        g.gridx = 2;
        form.add(new JLabel("Số điện thoại:"), g);
        g.gridx = 3;
        form.add(txtSDT, g);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton them = new JButton("Thêm"), sua = new JButton("Cập nhật"), xoa = new JButton("Xóa"), moi = new JButton("Làm mới");
        buttons.add(them);
        buttons.add(sua);
        buttons.add(xoa);
        buttons.add(moi);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Tìm theo mã, họ tên, SĐT:"));
        search.add(txtTim);
        JButton tim = new JButton("Tìm kiếm"), tatCa = new JButton("Tất cả");
        search.add(tim);
        search.add(tatCa);
        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.NORTH);
        north.add(buttons, BorderLayout.CENTER);
        north.add(search, BorderLayout.SOUTH);
        add(north, BorderLayout.CENTER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        setPreferredSize(new Dimension(900, 600));
        // Đặt bảng vào vùng chính, phần nhập liệu phía trên.
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.add(north, BorderLayout.NORTH);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
        them.addActionListener(e -> luu(false));
        sua.addActionListener(e -> luu(true));
        xoa.addActionListener(e -> xoa());
        moi.addActionListener(e -> lamMoi());
        tim.addActionListener(e -> napDanhSach(txtTim.getText()));
        tatCa.addActionListener(e -> {
            txtTim.setText("");
            napDanhSach("");
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.convertRowIndexToModel(table.getSelectedRow());
                maDangChon = (int) model.getValueAt(r, 0);
                txtHoTen.setText(String.valueOf(model.getValueAt(r, 1)));
                txtSDT.setText(String.valueOf(model.getValueAt(r, 2)));
            }
        });
        napDanhSach("");
    }

    private void napDanhSach(String key) {
        try {
            model.setRowCount(0);
            for (ChuTro c : (key == null || key.isBlank() ? service.layTatCa() : service.timKiem(key)))
                model.addRow(new Object[]{c.getMaChuTro(), c.getHoTen(), c.getSoDienThoai()});
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void luu(boolean sua) {
        try {
            ChuTro c = new ChuTro();
            c.setHoTen(txtHoTen.getText());
            c.setSoDienThoai(txtSDT.getText());
            if (sua) {
                if (maDangChon <= 0) throw new IllegalArgumentException("Hãy chọn chủ trọ cần cập nhật.");
                service.capNhat(maDangChon, c);
            } else service.them(c);
            JOptionPane.showMessageDialog(this, sua ? "Đã cập nhật chủ trọ." : "Đã thêm chủ trọ.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void xoa() {
        if (maDangChon <= 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn chủ trọ cần xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa chủ trọ đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        try {
            service.xoa(maDangChon);
            JOptionPane.showMessageDialog(this, "Đã xóa chủ trọ.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void lamMoi() {
        maDangChon = 0;
        table.clearSelection();
        txtHoTen.setText("");
        txtSDT.setText("");
        txtTim.setText("");
        napDanhSach("");
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage() == null ? "Có lỗi xảy ra." : ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
}
