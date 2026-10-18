package com.quanlydaytro.ui.quanlydaytro;

import com.quanlydaytro.model.ChuTro;
import com.quanlydaytro.service.ChuTroService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/**
 * Giao diện CRUD chủ trọ.
 */
public class ChuTroPanel extends JPanel {
    private final ChuTroService service = new ChuTroService();
    private final JTextField txtHoTen = DialogUtils.createFormField(), txtSDT = DialogUtils.createFormField();
    private final JTextField txtTim = DialogUtils.createSearchField("Tìm theo mã, họ tên, số điện thoại...");
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã chủ trọ", "Họ tên", "Số điện thoại"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);
    private int maDangChon = 0;

    public ChuTroPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(DialogUtils.createHeaderPanel("QUẢN LÝ CHỦ TRỌ", txtTim), BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(DialogUtils.createCardBorder(null, null));
        tableScroll.getViewport().setBackground(Color.WHITE);
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);
        content.add(tableScroll, BorderLayout.CENTER);
        content.add(createDetailsCard(), BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        actions.setOpaque(false);
        JButton them = DialogUtils.createStyledButton("Thêm mới", DialogUtils.SUCCESS);
        JButton sua = DialogUtils.createStyledButton("Cập nhật", DialogUtils.WARNING);
        JButton xoa = DialogUtils.createStyledButton("Xóa", DialogUtils.DANGER);
        JButton moi = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN);
        actions.add(them); actions.add(sua); actions.add(xoa); actions.add(moi);
        add(actions, BorderLayout.SOUTH);
        them.addActionListener(e -> luu(false));
        sua.addActionListener(e -> luu(true));
        xoa.addActionListener(e -> xoa());
        moi.addActionListener(e -> lamMoi());
        txtTim.addActionListener(e -> napDanhSach(txtTim.getText()));
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

    private JPanel createDetailsCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(DialogUtils.createCardBorder("Thông tin chủ trọ", DialogUtils.PRIMARY), BorderFactory.createEmptyBorder(8, 16, 12, 16)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 12); g.fill = GridBagConstraints.HORIZONTAL;
        addField(card, g, 0, "Họ và tên:", txtHoTen);
        addField(card, g, 2, "Số điện thoại:", txtSDT);
        txtHoTen.setPreferredSize(new Dimension(260, 36));
        txtSDT.setPreferredSize(new Dimension(220, 36));
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints base, int x, String label, Component field) {
        GridBagConstraints g = (GridBagConstraints) base.clone();
        g.gridx = x; g.gridy = 0; g.weightx = 0;
        panel.add(DialogUtils.createFormLabel(label), g);
        g.gridx = x + 1; g.weightx = 1;
        panel.add(field, g);
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
