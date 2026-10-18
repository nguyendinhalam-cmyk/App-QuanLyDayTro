package com.quanlydaytro.ui.quanlytraphong;

import com.quanlydaytro.service.TraPhongService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Giao diện danh sách hợp đồng hiệu lực và quyết toán trả phòng.
 */
public class TraPhongPanel extends JPanel {
    private final TraPhongService service = new TraPhongService();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã HĐ", "Mã phòng", "Phòng", "Mã người thuê", "Người thuê", "Ngày bắt đầu", "Ngày kết thúc", "Tiền cọc", "Tổng hóa đơn", "Đã thanh toán", "Còn nợ"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);
    private final NumberFormat money = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public TraPhongPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(DialogUtils.createHeaderPanel("🔑 QUẢN LÝ TRẢ PHÒNG & QUYẾT TOÁN CỌC", null), BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(DialogUtils.createCardBorder(null, null));
        add(scroll, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        buttons.setOpaque(false);
        JButton refresh = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN), settle = DialogUtils.createStyledButton("Quyết toán trả phòng", DialogUtils.SUCCESS);
        refresh.addActionListener(e -> taiDuLieu());
        settle.addActionListener(e -> quyetToan());
        buttons.add(refresh);
        buttons.add(settle);
        add(buttons, BorderLayout.SOUTH);
        taiDuLieu();
    }

    private void taiDuLieu() {
        model.setRowCount(0);
        try {
            List<Object[]> rows = service.danhSachHopDongHieuLuc();
            for (Object[] r : rows)
                model.addRow(new Object[]{r[0], r[1], r[2], r[3], r[4], r[5], r[6], format(r[7]), format(r[8]), format(r[9]), format(r[10])});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tải được danh sách hợp đồng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String format(Object value) {
        if (value instanceof BigDecimal) return money.format(value);
        return value == null ? "" : value.toString();
    }

    private void quyetToan() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hợp đồng cần trả phòng.");
            return;
        }
        int maHopDong = ((Number) table.getValueAt(row, 0)).intValue();
        try {
            Object[] hd = service.thongTinHopDong(maHopDong);
            BigDecimal coc = (BigDecimal) hd[5];
            JTextField khauTruField = new JTextField("0", 12);
            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(4, 4, 4, 4);
            g.anchor = GridBagConstraints.WEST;
            g.fill = GridBagConstraints.HORIZONTAL;
            g.gridx = 0;
            g.gridy = 0;
            form.add(new JLabel("Hợp đồng:"), g);
            g.gridx = 1;
            form.add(new JLabel(maHopDong + " - " + hd[2] + " / " + hd[4]), g);
            g.gridx = 0;
            g.gridy++;
            form.add(new JLabel("Tiền cọc:"), g);
            g.gridx = 1;
            form.add(new JLabel(format(coc) + " đồng"), g);
            g.gridx = 0;
            g.gridy++;
            form.add(new JLabel("Khấu trừ tiền cọc:"), g);
            g.gridx = 1;
            form.add(khauTruField, g);
            int choice = JOptionPane.showConfirmDialog(this, form, "Quyết toán trả phòng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice != JOptionPane.OK_OPTION) return;
            BigDecimal khauTru;
            try {
                khauTru = new BigDecimal(khauTruField.getText().trim().replace(" ", "").replace(",", "."));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Tiền khấu trừ phải là số hợp lệ.");
            }
            BigDecimal hoan = service.tinhTienHoanCoc(coc, khauTru);
            int confirm = JOptionPane.showConfirmDialog(this, "Tiền cọc: " + format(coc) + " đồng\nKhấu trừ: " + format(khauTru) + " đồng\nTiền cọc còn hoàn: " + format(hoan) + " đồng\n\nHệ thống sẽ kết thúc hợp đồng và chuyển phòng sang trạng thái Trống. Tiếp tục?", "Xác nhận quyết toán", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            service.quyetToan(maHopDong, khauTru, "");
            JOptionPane.showMessageDialog(this, "Đã quyết toán trả phòng. Tiền cọc dự kiến hoàn: " + format(hoan) + " đồng. Lịch sử hóa đơn và thanh toán được giữ nguyên.");
            taiDuLieu();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể quyết toán: " + ex.getMessage(), "Không thể trả phòng", JOptionPane.ERROR_MESSAGE);
        }
    }
}
