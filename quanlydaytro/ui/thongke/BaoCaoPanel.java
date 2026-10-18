package com.quanlydaytro.ui.thongke;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BaoCaoPanel extends JPanel {
    private JTabbedPane tabbedPane;

    public BaoCaoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = DialogUtils.createHeaderPanel("📈 BÁO CÁO THỐNG KÊ DOANH THU & HOẠT ĐỘNG", null);
        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(DialogUtils.HEADER_FONT);
        tabbedPane.addTab("💰 Doanh Thu & Tài Chính", createTabDoanhThu());
        tabbedPane.addTab("📋 Tổng Hợp Công Nợ", createTabCongNo());
        tabbedPane.addTab("🚪 Tình Trạng Lấp Đầy Phòng", createTabPhong());
        tabbedPane.addTab("⚡ Tiêu Thụ Điện Nước", createTabDienNuoc());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTabDoanhThu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DialogUtils.BG_CONTENT);

        // Cards summary
        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
        cards.setBackground(DialogUtils.BG_CONTENT);
        cards.add(DialogUtils.createSummaryCard("Tổng doanh thu (Năm 2024)", "1,250,000,000 đ", DialogUtils.SUCCESS));
        cards.add(DialogUtils.createSummaryCard("Tiền phòng thu được", "950,000,000 đ", DialogUtils.PRIMARY));
        cards.add(DialogUtils.createSummaryCard("Tiền điện & nước", "230,000,000 đ", DialogUtils.WARNING));
        cards.add(DialogUtils.createSummaryCard("Phí dịch vụ khác", "70,000,000 đ", DialogUtils.PURPLE));
        panel.add(cards, BorderLayout.NORTH);

        // Table monthly
        String[] cols = {"Tháng", "Tiền Phòng (VNĐ)", "Tiền Điện (VNĐ)", "Tiền Nước (VNĐ)", "Phí Dịch Vụ (VNĐ)", "Tổng Doanh Thu (VNĐ)", "Tăng Trưởng"};
        Object[][] data = {
            {"Tháng 10/2024", "125,000,000", "28,500,000", "12,400,000", "7,200,000", "173,100,000", "+ 5.2%"},
            {"Tháng 09/2024", "120,000,000", "27,100,000", "11,800,000", "7,000,000", "165,900,000", "+ 2.1%"},
            {"Tháng 08/2024", "118,000,000", "26,500,000", "11,500,000", "6,800,000", "162,800,000", "+ 1.5%"},
            {"Tháng 07/2024", "115,000,000", "25,000,000", "11,000,000", "6,500,000", "157,500,000", "+ 3.0%"}
        };
        JTable table = DialogUtils.createStyledTable(new DefaultTableModel(data, cols));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        bottom.setBackground(DialogUtils.BG_CONTENT);
        JButton btnExportExcel = DialogUtils.createStyledButton("📊 Xuất Báo Cáo Excel", DialogUtils.SUCCESS);
        JButton btnPrint = DialogUtils.createStyledButton("🖨️ In Báo Cáo", DialogUtils.PRIMARY);
        btnExportExcel.addActionListener(e -> DialogUtils.showSuccess(this, "Đã xuất báo cáo doanh thu ra file Excel!"));
        btnPrint.addActionListener(e -> DialogUtils.showInfo(this, "Đang kết nối máy in xuất tài liệu thống kê..."));
        bottom.add(btnExportExcel);
        bottom.add(btnPrint);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTabCongNo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DialogUtils.BG_CONTENT);

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));
        cards.setBackground(DialogUtils.BG_CONTENT);
        cards.add(DialogUtils.createSummaryCard("Tổng tiền nợ đọng", "18,500,000 đ", DialogUtils.DANGER));
        cards.add(DialogUtils.createSummaryCard("Số phòng nợ tiền", "3 phòng", DialogUtils.WARNING));
        cards.add(DialogUtils.createSummaryCard("Khoản nợ lâu nhất", "45 ngày", DialogUtils.DANGER));
        panel.add(cards, BorderLayout.NORTH);

        String[] cols = {"Phòng", "Người Đại Diện", "Số ĐT", "Kỳ Nợ", "Số Tiền Nợ (VNĐ)", "Hạn Thanh Toán", "Số Ngày Quá Hạn", "Mức Độ"};
        Object[][] data = {
            {"Phòng 102", "Trần Thị Mai Phương", "0982345678", "10/2024", "2,606,500", "10/10/2024", "2 ngày", "Nhẹ"},
            {"Phòng 201", "Hoàng Thùy Linh", "0914567890", "10/2024", "5,410,000", "10/10/2024", "2 ngày", "Nhẹ"},
            {"Phòng 204 (Đã trả)", "Bùi Tiến Đạt", "0934112233", "07/2024", "10,483,500", "10/08/2024", "45 ngày", "Nghiêm trọng"}
        };
        JTable table = DialogUtils.createStyledTable(new DefaultTableModel(data, cols));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTabPhong() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DialogUtils.BG_CONTENT);

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
        cards.setBackground(DialogUtils.BG_CONTENT);
        cards.add(DialogUtils.createSummaryCard("Tổng số phòng", "50 phòng", DialogUtils.PRIMARY));
        cards.add(DialogUtils.createSummaryCard("Đang cho thuê", "42 phòng (84%)", DialogUtils.SUCCESS));
        cards.add(DialogUtils.createSummaryCard("Phòng trống sẵn sàng", "6 phòng (12%)", DialogUtils.WARNING));
        cards.add(DialogUtils.createSummaryCard("Đang bảo trì/sửa", "2 phòng (4%)", DialogUtils.DANGER));
        panel.add(cards, BorderLayout.NORTH);

        String[] cols = {"Dãy Trọ", "Tổng Phòng", "Đang Thuê", "Phòng Trống", "Bảo Trì", "Tỷ Lệ Lấp Đầy"};
        Object[][] data = {
            {"Dãy A - Khu Đông", "20", "18", "2", "0", "90 %"},
            {"Dãy B - Khu Tây", "20", "16", "2", "2", "80 %"},
            {"Dãy C - VIP", "10", "8", "2", "0", "80 %"}
        };
        JTable table = DialogUtils.createStyledTable(new DefaultTableModel(data, cols));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTabDienNuoc() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DialogUtils.BG_CONTENT);

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
        cards.setBackground(DialogUtils.BG_CONTENT);
        cards.add(DialogUtils.createSummaryCard("Tổng điện tiêu thụ", "8,450 kWh", DialogUtils.PRIMARY));
        cards.add(DialogUtils.createSummaryCard("Tổng nước tiêu thụ", "540 m³", DialogUtils.SUCCESS));
        cards.add(DialogUtils.createSummaryCard("Tiêu thụ điện TB/phòng", "185 kWh/tháng", DialogUtils.WARNING));
        cards.add(DialogUtils.createSummaryCard("Tiêu thụ nước TB/phòng", "12 m³/tháng", DialogUtils.PURPLE));
        panel.add(cards, BorderLayout.NORTH);

        String[] cols = {"Kỳ Tháng", "Tổng Điện (kWh)", "Tổng Tiền Điện", "Tổng Nước (m³)", "Tổng Tiền Nước", "Tháng Trước"};
        Object[][] data = {
            {"Tháng 10/2024", "8,450", "28,500,000", "540", "12,400,000", "+ 4.1%"},
            {"Tháng 09/2024", "8,120", "27,100,000", "520", "11,800,000", "+ 2.0%"},
            {"Tháng 08/2024", "7,960", "26,500,000", "505", "11,500,000", "- 1.2%"}
        };
        JTable table = DialogUtils.createStyledTable(new DefaultTableModel(data, cols));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }
}
