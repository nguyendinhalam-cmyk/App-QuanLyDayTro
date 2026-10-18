package com.quanlydaytro.ui.quanlyhoadon;

import com.quanlydaytro.model.ThanhToan;
import com.quanlydaytro.service.ThanhToanService;

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
    private final JTable table = new JTable(model);

    public ThanhToanPanel() {
        setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Làm mới công nợ"), pay = new JButton("Ghi nhận thanh toán"), history = new JButton("Lịch sử thanh toán");
        top.add(refresh);
        top.add(pay);
        top.add(history);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
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
            JTable t = new JTable(m);
            JOptionPane.showMessageDialog(this, new JScrollPane(t), "Lịch sử thanh toán", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage() == null ? "Có lỗi xảy ra" : ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
