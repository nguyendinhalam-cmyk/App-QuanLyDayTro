package com.quanlydaytro.ui.quanlychuyenphong;

import com.quanlydaytro.service.ChuyenPhongService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Giao diện nghiệp vụ chuyển phòng.
 */
public class ChuyenPhongPanel extends JPanel {
    private final ChuyenPhongService service = new ChuyenPhongService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã HĐ", "Mã phòng", "Phòng hiện tại", "Mã người thuê", "Người thuê", "Ngày bắt đầu", "Ngày kết thúc", "Giá thuê", "Tiền cọc"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);
    private final JComboBox<PhongItem> cboPhongMoi = new JComboBox<>();
    private final JTextField txtNgay = new JTextField(LocalDate.now().toString(), 10);
    private final JTextField txtGia = new JTextField(12);
    private final JTextField txtCoc = new JTextField(12);
    private final JLabel lblNguoiThue = new JLabel("Chọn hợp đồng đang hiệu lực");
    private List<Object[]> hopDong = List.of();

    public ChuyenPhongPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(DialogUtils.createHeaderPanel("⇄ QUẢN LÝ CHUYỂN PHÒNG", null), BorderLayout.CENTER);
        JButton btnTaiLai = DialogUtils.createStyledButton("Tải lại", DialogUtils.GRAY_BTN);
        btnTaiLai.addActionListener(e -> taiDuLieu());
        header.add(btnTaiLai, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(24);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) capNhatThongTinHopDong();
        });
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(DialogUtils.createCardBorder(null, null));
        add(scroll, BorderLayout.CENTER);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(DialogUtils.createCardBorder("Thông tin chuyển phòng", DialogUtils.PRIMARY), BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.anchor = GridBagConstraints.WEST;
        them(form, g, 0, 0, new JLabel("Người thuê:"));
        them(form, g, 1, 0, lblNguoiThue);
        them(form, g, 0, 1, new JLabel("Phòng mới:"));
        them(form, g, 1, 1, cboPhongMoi);
        them(form, g, 2, 1, new JLabel("Ngày chuyển (yyyy-MM-dd):"));
        them(form, g, 3, 1, txtNgay);
        them(form, g, 0, 2, new JLabel("Giá thuê mới:"));
        them(form, g, 1, 2, txtGia);
        them(form, g, 2, 2, new JLabel("Tiền cọc mới:"));
        them(form, g, 3, 2, txtCoc);
        JButton btnChuyen = DialogUtils.createStyledButton("Xác nhận chuyển phòng", DialogUtils.SUCCESS);
        btnChuyen.addActionListener(e -> thucHienChuyenPhong());
        JButton btnPhong = DialogUtils.createStyledButton("Tải phòng trống", DialogUtils.PRIMARY);
        btnPhong.addActionListener(e -> taiPhongTrong());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actions.setOpaque(false);
        actions.add(btnPhong);
        actions.add(btnChuyen);
        g.gridx = 0;
        g.gridy = 3;
        g.gridwidth = 4;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        form.add(actions, g);
        add(form, BorderLayout.SOUTH);
        cboPhongMoi.addActionListener(e -> {
            PhongItem p = (PhongItem) cboPhongMoi.getSelectedItem();
            if (p != null) txtGia.setText(p.gia.toPlainString());
        });
        taiDuLieu();
    }

    private void them(JPanel p, GridBagConstraints g, int x, int y, Component c) {
        g.gridx = x;
        g.gridy = y;
        g.gridwidth = 1;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        p.add(c, g);
    }

    private void taiDuLieu() {
        try {
            int selected = table.getSelectedRow();
            int selectedId = selected >= 0 ? ((Number) model.getValueAt(table.convertRowIndexToModel(selected), 0)).intValue() : -1;
            hopDong = service.danhSachHopDongHieuLuc();
            model.setRowCount(0);
            int rowToSelect = -1;
            for (Object[] h : hopDong) {
                model.addRow(new Object[]{h[0], h[1], h[2], h[3], h[4], h[5], h[6], h[7], h[8]});
                if (((Number) h[0]).intValue() == selectedId) rowToSelect = model.getRowCount() - 1;
            }
            if (rowToSelect >= 0) table.setRowSelectionInterval(rowToSelect, rowToSelect);
            else if (model.getRowCount() > 0) table.setRowSelectionInterval(0, 0);
            else {
                lblNguoiThue.setText("Không có hợp đồng hiệu lực");
                cboPhongMoi.removeAllItems();
            }
            capNhatThongTinHopDong();
        } catch (SQLException ex) {
            loi("Không thể tải danh sách hợp đồng: " + ex.getMessage());
        }
    }

    private void capNhatThongTinHopDong() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        r = table.convertRowIndexToModel(r);
        int id = ((Number) model.getValueAt(r, 0)).intValue();
        for (Object[] h : hopDong)
            if (((Number) h[0]).intValue() == id) {
                lblNguoiThue.setText(h[4] + " (HĐ " + h[0] + ", phòng " + h[2] + ")");
                txtCoc.setText(String.valueOf(h[8]));
                taiPhongTrong();
                break;
            }
    }

    private void taiPhongTrong() {
        int r = table.getSelectedRow();
        if (r < 0) {
            cboPhongMoi.removeAllItems();
            return;
        }
        r = table.convertRowIndexToModel(r);
        int maPhong = ((Number) model.getValueAt(r, 1)).intValue();
        try {
            Object old = cboPhongMoi.getSelectedItem();
            int oldId = old instanceof PhongItem ? ((PhongItem) old).id : -1;
            cboPhongMoi.removeAllItems();
            for (Object[] p : service.danhSachPhongTrong(maPhong)) {
                PhongItem item = new PhongItem(((Number) p[0]).intValue(), String.valueOf(p[1]), (BigDecimal) p[3], ((Number) p[2]).intValue());
                cboPhongMoi.addItem(item);
                if (item.id == oldId) cboPhongMoi.setSelectedItem(item);
            }
            if (cboPhongMoi.getItemCount() == 0) txtGia.setText("");
        } catch (SQLException ex) {
            loi("Không thể tải phòng trống: " + ex.getMessage());
        }
    }

    private void thucHienChuyenPhong() {
        int r = table.getSelectedRow();
        if (r < 0) {
            loi("Vui lòng chọn hợp đồng cần chuyển.");
            return;
        }
        Object selected = cboPhongMoi.getSelectedItem();
        if (!(selected instanceof PhongItem p)) {
            loi("Không có phòng trống phù hợp.");
            return;
        }
        try {
            int mr = table.convertRowIndexToModel(r);
            int maHD = ((Number) model.getValueAt(mr, 0)).intValue();
            LocalDate ngay = LocalDate.parse(txtNgay.getText().trim());
            BigDecimal gia = new BigDecimal(txtGia.getText().trim());
            BigDecimal coc = new BigDecimal(txtCoc.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Chuyển người thuê sang phòng " + p.ten + "? Hợp đồng cũ sẽ kết thúc và hợp đồng mới được tạo.", "Xác nhận chuyển phòng", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            service.chuyenPhong(maHD, p.id, ngay, gia, coc);
            JOptionPane.showMessageDialog(this, "Chuyển phòng thành công.");
            taiDuLieu();
        } catch (java.time.format.DateTimeParseException ex) {
            loi("Ngày chuyển không hợp lệ. Nhập theo yyyy-MM-dd, ví dụ 2026-10-20.");
        } catch (NumberFormatException ex) {
            loi("Giá thuê và tiền cọc phải là số hợp lệ.");
        } catch (IllegalArgumentException | SQLException ex) {
            loi(ex.getMessage());
        }
    }

    private void loi(String s) {
        JOptionPane.showMessageDialog(this, s, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    private static class PhongItem {
        final int id, maDay;
        final String ten;
        final BigDecimal gia;

        PhongItem(int id, String ten, BigDecimal gia, int maDay) {
            this.id = id;
            this.ten = ten;
            this.gia = gia;
            this.maDay = maDay;
        }

        @Override
        public String toString() {
            return "Phòng " + ten + " (mã " + id + ", dãy " + maDay + ") - " + gia;
        }
    }
}
