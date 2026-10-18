package com.quanlydaytro.ui.quanlyhoadon;

import com.quanlydaytro.model.HoaDon;
import com.quanlydaytro.service.HoaDonService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

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
    private final JTextField txtTim = DialogUtils.createSearchField("Mã hóa đơn, hợp đồng, tháng, trạng thái...");
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã HĐ", "Mã hợp đồng", "Tháng/năm", "Tiền phòng", "Tiền điện", "Tiền nước", "Tổng tiền", "Hạn thanh toán", "Trạng thái"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);

    public HoaDonPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.setOpaque(false);
        north.add(DialogUtils.createHeaderPanel("📄 QUẢN LÝ HÓA ĐƠN TIỀN NHÀ", txtTim), BorderLayout.NORTH);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        DialogUtils.styleToolbar(top);
        JButton btnTim = DialogUtils.createStyledButton("Tìm kiếm", DialogUtils.PRIMARY), btnTai = DialogUtils.createStyledButton("Tải lại", DialogUtils.GRAY_BTN), btnLap = DialogUtils.createStyledButton("Lập hóa đơn tháng", DialogUtils.SUCCESS);
        top.add(btnTim);
        top.add(btnTai);
        north.add(top, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(DialogUtils.createCardBorder(null, null));
        add(scroll, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actions.setOpaque(false);
        actions.add(btnLap);
        add(actions, BorderLayout.SOUTH);
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
