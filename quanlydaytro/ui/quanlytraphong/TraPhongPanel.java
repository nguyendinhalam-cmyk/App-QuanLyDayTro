package com.quanlydaytro.ui.quanlytraphong;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraPhongPanel extends JPanel {
    private JComboBox<String> cbPhongTra;
    private JLabel lblKhachThue, lblNgayVao, lblTienCoc, lblGiaThueThang;
    private JTextField txtDienCuoi, txtNuocCuoi, txtTienDien, txtTienNuoc, txtCongNoCu, txtPhiPhatSinh, txtTienCocHoan, txtTongQuyetToan;
    private JCheckBox chkHoanCoc;
    private JTextArea txtLyDoGhiChu;
    private JTable tableLichSu;
    private DefaultTableModel modelLichSu;

    public TraPhongPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = DialogUtils.createHeaderPanel("🔑 QUẢN LÝ TRẢ PHÒNG & QUYẾT TOÁN CỌC", null);
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(DialogUtils.BG_CONTENT);
        centerPanel.add(createLeftInfo());
        centerPanel.add(createRightQuyetToan());

        add(centerPanel, BorderLayout.CENTER);
        initSouthHistory();
    }

    private JPanel createLeftInfo() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("1. Thông Tin Phòng Đang Thuê", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel selectRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        selectRow.setBackground(Color.WHITE);
        selectRow.add(DialogUtils.createFormLabel("Chọn phòng trả:"));
        cbPhongTra = DialogUtils.createStyledComboBox("Phòng 101 - Nguyễn Văn An", "Phòng 102 - Trần Thị Mai Phương", "Phòng 201 - Hoàng Thùy Linh");
        selectRow.add(cbPhongTra);
        panel.add(selectRow, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(5, 2, 8, 8));
        details.setBackground(Color.WHITE);

        lblKhachThue = new JLabel("Nguyễn Văn An (0971234567)");
        lblKhachThue.setFont(DialogUtils.HEADER_FONT);
        lblNgayVao = new JLabel("01/01/2024 (Đã ở: 9 tháng)");
        lblNgayVao.setFont(DialogUtils.BODY_FONT);
        lblTienCoc = new JLabel("2,800,000 VNĐ");
        lblTienCoc.setFont(DialogUtils.HEADER_FONT);
        lblTienCoc.setForeground(DialogUtils.PRIMARY);
        lblGiaThueThang = new JLabel("2,800,000 VNĐ");
        lblGiaThueThang.setFont(DialogUtils.BODY_FONT);

        details.add(DialogUtils.createFormLabel("Người đại diện thuê:")); details.add(lblKhachThue);
        details.add(DialogUtils.createFormLabel("Thời gian bắt đầu ở:")); details.add(lblNgayVao);
        details.add(DialogUtils.createFormLabel("Tiền đặt cọc ban đầu:")); details.add(lblTienCoc);
        details.add(DialogUtils.createFormLabel("Đơn giá phòng/tháng:")); details.add(lblGiaThueThang);
        details.add(DialogUtils.createFormLabel("Tình trạng nội quy:")); details.add(new JLabel("Đầy đủ chìa khóa, không hư hại"));

        panel.add(details, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRightQuyetToan() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("2. Quyết Toán Chi Phí & Hoàn Cọc", DialogUtils.WARNING),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        form.setBackground(Color.WHITE);

        txtDienCuoi = DialogUtils.createFormField(); txtDienCuoi.setText("1420");
        txtNuocCuoi = DialogUtils.createFormField(); txtNuocCuoi.setText("58");
        txtTienDien = DialogUtils.createFormField(); txtTienDien.setText("140,000"); txtTienDien.setEditable(false);
        txtTienNuoc = DialogUtils.createFormField(); txtNuocCuoi.setText("58");
        txtTienNuoc.setText("80,000"); txtTienNuoc.setEditable(false);
        txtCongNoCu = DialogUtils.createFormField(); txtCongNoCu.setText("0"); txtCongNoCu.setEditable(false);
        txtPhiPhatSinh = DialogUtils.createFormField(); txtPhiPhatSinh.setText("50,000");
        txtTongQuyetToan = DialogUtils.createFormField();
        txtTongQuyetToan.setText("+ 2,530,000 VNĐ (Trả lại khách)");
        txtTongQuyetToan.setEditable(false);
        txtTongQuyetToan.setFont(new Font(DialogUtils.FONT_FAMILY, Font.BOLD, 14));
        txtTongQuyetToan.setForeground(DialogUtils.SUCCESS);

        chkHoanCoc = new JCheckBox("Khấu trừ tiền điện nước trực tiếp vào cọc", true);
        chkHoanCoc.setBackground(Color.WHITE);

        form.add(DialogUtils.createFormLabel("Chỉ số điện chốt:")); form.add(txtDienCuoi);
        form.add(DialogUtils.createFormLabel("Chỉ số nước chốt:")); form.add(txtNuocCuoi);
        form.add(DialogUtils.createFormLabel("Tiền điện + nước còn thiếu:")); form.add(txtTienDien);
        form.add(DialogUtils.createFormLabel("Phí phát sinh / dọn vệ sinh:")); form.add(txtPhiPhatSinh);
        form.add(DialogUtils.createFormLabel("Công nợ cũ còn thiếu:")); form.add(txtCongNoCu);
        form.add(DialogUtils.createFormLabel("Tổng tiền cọc hoàn lại:")); form.add(txtTongQuyetToan);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnRow.setBackground(Color.WHITE);
        JButton btnTinh = DialogUtils.createStyledButton("⚡ Quyết Toán Tiền", DialogUtils.WARNING);
        JButton btnXacNhanTra = DialogUtils.createStyledButton("🔑 Xác Nhận Trả Phòng", DialogUtils.SUCCESS);
        JButton btnInBienBan = DialogUtils.createStyledButton("🖨️ In Biên Bản Thanh Lý", DialogUtils.PURPLE);

        btnTinh.addActionListener(e -> {
            txtTongQuyetToan.setText("+ 2,530,000 VNĐ (Trả lại khách)");
            DialogUtils.showSuccess(this, "Đã tính toán: Cọc (2.8tr) - Điện nước (220k) - Phí (50k) = Hoàn khách 2,530,000 VNĐ");
        });
        btnXacNhanTra.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Xác nhận thu hồi phòng, kết thúc hợp đồng và cập nhật phòng sang trạng thái Trống?")) {
                DialogUtils.showSuccess(this, "Đã hoàn tất trả phòng thành công!");
            }
        });
        btnInBienBan.addActionListener(e -> DialogUtils.showInfo(this, "Đang xuất biên bản thanh lý hợp đồng và bàn giao hiện trạng phòng..."));

        btnRow.add(btnTinh);
        btnRow.add(btnXacNhanTra);
        btnRow.add(btnInBienBan);

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow, BorderLayout.SOUTH);
        return panel;
    }

    private void initSouthHistory() {
        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setPreferredSize(new Dimension(0, 180));
        south.setBackground(Color.WHITE);
        south.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Lịch Sử Các Phòng Đã Trả", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        String[] cols = {"Mã Trả", "Phòng", "Người Trả", "Ngày Trả", "Chỉ Số Điện", "Chỉ Số Nước", "Tiền Cọc Hoàn", "Tình Trạng Phòng"};
        Object[][] data = {
            {"TP01", "Phòng 103", "Phạm Quốc Huy", "01/06/2024", "850", "12", "2,650,000", "Đạt chuẩn bàn giao"},
            {"TP02", "Phòng 204", "Bùi Tiến Đạt", "15/07/2024", "1980", "45", "3,100,000", "Hư hỏng nhẹ đã trừ 200k"}
        };
        modelLichSu = new DefaultTableModel(data, cols);
        tableLichSu = DialogUtils.createStyledTable(modelLichSu);
        south.add(new JScrollPane(tableLichSu), BorderLayout.CENTER);

        add(south, BorderLayout.SOUTH);
    }
}
