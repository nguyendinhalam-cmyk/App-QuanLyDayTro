package com.quanlydaytro.ui.thongke;

import com.quanlydaytro.service.BaoCaoService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Màn hình thống kê doanh thu, công nợ, phòng và tiêu thụ điện nước.
 */
public class BaoCaoPanel extends JPanel {
    private final BaoCaoService service = new BaoCaoService();
    private final JTextField tuThang = new JTextField(7), denThang = new JTextField(7);
    private final JComboBox<String> loai = new JComboBox<>(new String[]{"Doanh thu", "Công nợ", "Tình trạng phòng", "Tiêu thụ điện nước"});
    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable bang = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);
    private final JLabel trangThai = new JLabel("Chọn loại báo cáo và nhấn Xem báo cáo.");

    public BaoCaoPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        LocalDate now = LocalDate.now();
        tuThang.setText(now.withDayOfMonth(1).minusMonths(2).format(DateTimeFormatter.ofPattern("MM/yyyy")));
        denThang.setText(now.format(DateTimeFormatter.ofPattern("MM/yyyy")));
        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.setOpaque(false);
        north.add(DialogUtils.createHeaderPanel("📈 THỐNG KÊ & BÁO CÁO", null), BorderLayout.NORTH);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        DialogUtils.styleToolbar(top);
        top.add(new JLabel("Loại báo cáo:"));
        top.add(loai);
        top.add(new JLabel("Từ tháng (MM/yyyy):"));
        top.add(tuThang);
        top.add(new JLabel("Đến tháng (MM/yyyy):"));
        top.add(denThang);
        JButton xem = DialogUtils.createStyledButton("Xem báo cáo", DialogUtils.PRIMARY), xuat = DialogUtils.createStyledButton("Xuất CSV", DialogUtils.SUCCESS);
        top.add(xem);
        top.add(xuat);
        north.add(top, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        bang.setAutoCreateRowSorter(true);
        bang.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(bang);
        scroll.setBorder(DialogUtils.createCardBorder(null, null));
        add(scroll, BorderLayout.CENTER);
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(false);
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 4, 0, 4));
        statusBar.add(trangThai, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);
        xem.addActionListener(e -> taiBaoCao());
        xuat.addActionListener(e -> xuatCSV());
        loai.addActionListener(e -> {
            boolean ky = loai.getSelectedIndex() != 2;
            tuThang.setEnabled(ky);
            denThang.setEnabled(ky);
        });
        taiBaoCao();
    }

    private void taiBaoCao() {
        try {
            int i = loai.getSelectedIndex();
            List<Object[]> rows;
            String[] cols;
            switch (i) {
                case 0 -> {
                    rows = service.doanhThu(tuThang.getText().trim(), denThang.getText().trim());
                    cols = new String[]{"Tháng", "Đã thu (VNĐ)", "Tổng hóa đơn (VNĐ)", "Còn nợ (VNĐ)"};
                }
                case 1 -> {
                    rows = service.congNo(tuThang.getText().trim(), denThang.getText().trim());
                    cols = new String[]{"Mã hóa đơn", "Tháng", "Phòng", "Người thuê", "Tổng tiền", "Đã trả", "Còn nợ", "Trạng thái"};
                }
                case 2 -> {
                    rows = service.tinhTrangPhong();
                    cols = new String[]{"Dãy trọ", "Phòng", "Trạng thái", "Số người ở", "Giá thuê (VNĐ)"};
                }
                default -> {
                    rows = service.tieuThu(tuThang.getText().trim(), denThang.getText().trim());
                    cols = new String[]{"Dãy trọ", "Phòng", "Tháng", "Điện tiêu thụ (kWh)", "Nước tiêu thụ (m³)"};
                }
            }
            model.setRowCount(0);
            model.setColumnIdentifiers(cols);
            for (Object[] row : rows) model.addRow(row);
            trangThai.setText("Có " + rows.size() + " dòng dữ liệu.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Kỳ báo cáo không hợp lệ", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải báo cáo. Hãy kiểm tra kết nối SQL Server.\nChi tiết: " + ex.getMessage(), "Lỗi báo cáo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuatCSV() {
        if (model.getColumnCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu để xuất.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("bao-cao.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(chooser.getSelectedFile()), java.nio.charset.StandardCharsets.UTF_8))) {
            out.write('\ufeff');
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) out.print(',');
                out.print(csv(model.getColumnName(c)));
            }
            out.println();
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    if (c > 0) out.print(',');
                    out.print(csv(String.valueOf(model.getValueAt(r, c))));
                }
                out.println();
            }
            JOptionPane.showMessageDialog(this, "Đã xuất báo cáo CSV thành công.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể xuất CSV: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String csv(String s) {
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
