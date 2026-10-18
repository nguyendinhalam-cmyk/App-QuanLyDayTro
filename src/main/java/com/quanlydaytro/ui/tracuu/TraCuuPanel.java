package com.quanlydaytro.ui.tracuu;

import com.quanlydaytro.service.TraCuuService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Màn hình nghiệp vụ 13: tra cứu phòng, người thuê, hợp đồng, hóa đơn và công nợ.
 */
public class TraCuuPanel extends JPanel {
    private final JComboBox<String> cbLoai = new JComboBox<>(new String[]{"Tất cả", "Phòng", "Người thuê", "Hợp đồng", "Hóa đơn", "Công nợ"});
    private final JTextField txtTuKhoa = new JTextField(28);
    private final JLabel lblKetQua = new JLabel("Nhập từ khóa hoặc để trống để xem danh sách.");
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Mã", "Thông tin", "Chi tiết", "Trạng thái", "Số tiền", "Công nợ"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final TraCuuService service = new TraCuuService();
    private final NumberFormat tienFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public TraCuuPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("TRA CỨU THÔNG TIN", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(title, BorderLayout.NORTH);
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.add(new JLabel("Loại thông tin:"));
        filters.add(cbLoai);
        filters.add(new JLabel("Từ khóa:"));
        filters.add(txtTuKhoa);
        JButton btnTim = new JButton("Tra cứu");
        JButton btnLamMoi = new JButton("Làm mới");
        filters.add(btnTim);
        filters.add(btnLamMoi);
        top.add(filters, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(lblKetQua, BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);
        btnTim.addActionListener(e -> thucHienTraCuu());
        txtTuKhoa.addActionListener(e -> thucHienTraCuu());
        btnLamMoi.addActionListener(e -> {
            txtTuKhoa.setText("");
            cbLoai.setSelectedIndex(0);
            thucHienTraCuu();
        });
        thucHienTraCuu();
    }

    private void thucHienTraCuu() {
        try {
            List<Object[]> rows = service.traCuu((String) cbLoai.getSelectedItem(), txtTuKhoa.getText());
            model.setRowCount(0);
            for (Object[] row : rows) {
                for (int i : new int[]{4, 5}) if (row[i] instanceof BigDecimal) row[i] = tienFormat.format(row[i]);
                model.addRow(row);
            }
            lblKetQua.setText("Tìm thấy " + rows.size() + " kết quả.");
        } catch (Exception ex) {
            lblKetQua.setText("Không thể tra cứu dữ liệu.");
            JOptionPane.showMessageDialog(this, "Tra cứu thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
