package com.quanlydaytro.ui.quanlyhoadon;

import com.quanlydaytro.model.HoaDon;
import com.quanlydaytro.service.HoaDonService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Màn hình lập và tra cứu hóa đơn tháng.
 */
public class HoaDonPanel extends JPanel {
    private final HoaDonService service = new HoaDonService();
    private final JTextField txtTim = new JTextField(18);
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã HĐ", "Mã hợp đồng", "Tháng/năm", "Tiền phòng", "Tiền điện", "Tiền nước", "Tổng tiền", "Hạn thanh toán", "Trạng thái"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public HoaDonPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel("QUẢN LÝ HÓA ĐƠN", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Tìm mã hóa đơn / hợp đồng / tháng / trạng thái:"));
        top.add(txtTim);
        JButton btnTim = new JButton("Tìm kiếm"), btnTai = new JButton("Tải lại"), btnLap = new JButton("Lập hóa đơn tháng");
        top.add(btnTim);
        top.add(btnTai);
        top.add(btnLap);
        JPanel north = new JPanel(new BorderLayout());
        north.add(title, BorderLayout.NORTH);
        north.add(top, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);
        btnTim.addActionListener(e -> taiDuLieu(true));
        btnTai.addActionListener(e -> taiDuLieu(false));
        txtTim.addActionListener(e -> taiDuLieu(true));
        btnLap.addActionListener(e -> hienFormLap());
        taiDuLieu(false);
    }

    private void taiDuLieu(boolean tim) {
        try {
            List<HoaDon> ds = tim ? service.timKiem(txtTim.getText()) : service.layTatCa();
            model.setRowCount(0);
            for (HoaDon h : ds)
                model.addRow(new Object[]{h.getMaHoaDon(), h.getMaHopDong(), h.getThangNam(), fmt(h.getTienPhong()), fmt(h.getTienDien()), fmt(h.getTienNuoc()), fmt(h.getTongTien()), h.getHanThanhToan(), h.getTrangThai()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tải được hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String fmt(BigDecimal n) {
        return n == null ? "0" : n.stripTrailingZeros().toPlainString();
    }

    private void hienFormLap() {
        JTextField maHopDong = new JTextField(), thangNam = new JTextField(java.time.YearMonth.now().format(DateTimeFormatter.ofPattern("MM/yyyy"))), han = new JTextField(LocalDate.now().plusDays(5).toString());
        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Mã hợp đồng đang hiệu lực:"));
        p.add(maHopDong);
        p.add(new JLabel("Tháng/năm (MM/yyyy):"));
        p.add(thangNam);
        p.add(new JLabel("Hạn thanh toán (yyyy-MM-dd):"));
        p.add(han);
        p.add(new JLabel("Cách tính:"));
        p.add(new JLabel("Tiền phòng + chỉ số điện/nước × đơn giá áp dụng"));
        int ok = JOptionPane.showConfirmDialog(this, p, "Lập hóa đơn tháng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;
        try {
            int ma = Integer.parseInt(maHopDong.getText().trim());
            LocalDate ngay = LocalDate.parse(han.getText().trim());
            int id = service.lapHoaDon(ma, thangNam.getText().trim(), ngay);
            JOptionPane.showMessageDialog(this, "Đã lập hóa đơn mã " + id + ".");
            taiDuLieu(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mã hợp đồng phải là số nguyên.", "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không lập được hóa đơn: " + ex.getMessage(), "Lỗi nghiệp vụ", JOptionPane.ERROR_MESSAGE);
        }
    }
}
