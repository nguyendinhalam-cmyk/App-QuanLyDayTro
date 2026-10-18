package com.quanlydaytro.ui.quanlyhoadon;

import com.quanlydaytro.model.ThanhToan;
import com.quanlydaytro.service.ThanhToanService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Màn hình ghi nhận thanh toán từng phần và theo dõi công nợ hóa đơn.
 */
public class ThanhToanPanel extends JPanel {
    private final ThanhToanService service = new ThanhToanService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã HĐ", "Mã hợp đồng", "Tháng", "Tổng tiền", "Đã thanh toán", "Còn nợ", "Trạng thái"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);

    public ThanhToanPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(DialogUtils.createHeaderPanel("💳 THANH TOÁN & CÔNG NỢ", null), BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(DialogUtils.createCardBorder(null, null));
        add(scroll, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actions.setOpaque(false);
        JButton refresh = DialogUtils.createStyledButton("Làm mới công nợ", DialogUtils.GRAY_BTN), pay = DialogUtils.createStyledButton("Ghi nhận thanh toán", DialogUtils.SUCCESS), history = DialogUtils.createStyledButton("Lịch sử thanh toán", DialogUtils.PRIMARY);
        actions.add(refresh);
        actions.add(pay);
        actions.add(history);
        add(actions, BorderLayout.SOUTH);
        refresh.addActionListener(e -> taiDuLieu());
        pay.addActionListener(e -> ghiNhan());
        history.addActionListener(e -> xemLichSu());
        taiDuLieu();
    }

    private void taiDuLieu() {
        try {
            model.setRowCount(0);
            for (Object[] row : service.danhSachCongNo()) model.addRow(row);
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void ghiNhan() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn hóa đơn cần thanh toán.");
            return;
        }
        int ma = (int) model.getValueAt(r, 0);
        BigDecimal conNo = (BigDecimal) model.getValueAt(r, 5);
        if (conNo.signum() <= 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn này đã thanh toán đủ.");
            return;
        }
        String soTien = JOptionPane.showInputDialog(this, "Còn nợ: " + conNo + "\nNhập số tiền thanh toán:");
        if (soTien == null) return;
        try {
            BigDecimal tien = new BigDecimal(soTien.trim());
            String ghiChu = JOptionPane.showInputDialog(this, "Ghi chú (có thể để trống):");
            ThanhToan tt = new ThanhToan();
            tt.setMaHoaDon(ma);
            tt.setNgayThanhToan(LocalDateTime.now());
            tt.setSoTienTra(tien);
            tt.setGhiChu(ghiChu);
            int id = service.ghiNhan(tt);
            JOptionPane.showMessageDialog(this, "Đã ghi nhận thanh toán #" + id);
            taiDuLieu();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void xemLichSu() {
        try {
            List<ThanhToan> list = service.layTatCa();
            String[] cols = {"Mã thanh toán", "Mã hóa đơn", "Ngày thanh toán", "Số tiền trả", "Ghi chú"};
            DefaultTableModel m = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (ThanhToan t : list)
                m.addRow(new Object[]{t.getMaThanhToan(), t.getMaHoaDon(), t.getNgayThanhToan() == null ? "" : t.getNgayThanhToan().format(f), t.getSoTienTra(), t.getGhiChu()});
            JTable t = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(m);
            JOptionPane.showMessageDialog(this, new JScrollPane(t), "Lịch sử thanh toán", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage() == null ? "Có lỗi xảy ra" : ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
