package com.quanlydaytro.ui.quanlyhoadon;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThanhToanPanel extends JPanel {
    private JTable tableLichSu, tableCongNo;
    private DefaultTableModel modelLichSu, modelCongNo;
    private JTextField txtMaHD, txtPhong, txtTongTien, txtDaTra, txtConNo, txtSoTienTra, txtNguoiNop;
    private JComboBox<String> cbHinhThuc;
    private JTextArea txtGhiChu;

    public ThanhToanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = DialogUtils.createHeaderPanel("💳 QUẢN LÝ THANH TOÁN & THEO DÕI CÔNG NỢ", null);
        add(header, BorderLayout.NORTH);

        JSplitPane splitCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftLichSu(), createRightCongNo());
        splitCenter.setResizeWeight(0.6);
        splitCenter.setBorder(null);
        add(splitCenter, BorderLayout.CENTER);

        initSouthForm();
    }

    private JPanel createLeftLichSu() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Lịch Sử Thu Tiền & Giao Dịch", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        String[] cols = {"Mã Phiếu", "Mã HĐ", "Phòng", "Số Tiền Thu", "Hình Thức", "Ngày Thu", "Người Nộp"};
        Object[][] data = {
            {"PT001", "INV-1001", "Phòng 101", "3,533,000", "Chuyển khoản (Vietcombank)", "05/10/2024", "Nguyễn Văn An"},
            {"PT002", "INV-1002", "Phòng 102", "2,000,000", "Tiền mặt", "06/10/2024", "Trần Thị Mai Phương"}
        };
        modelLichSu = new DefaultTableModel(data, cols);
        tableLichSu = DialogUtils.createStyledTable(modelLichSu);
        panel.add(new JScrollPane(tableLichSu), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRightCongNo() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Danh Sách Còn Nợ Tiền Phòng", DialogUtils.DANGER),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        String[] cols = {"Phòng", "Hóa Đơn", "Tổng Nợ (VNĐ)", "Hạn Chót"};
        Object[][] data = {
            {"Phòng 102", "INV-1002", "2,606,500", "10/10/2024 (Quá hạn 2 ngày)"},
            {"Phòng 201", "INV-1003", "5,410,000", "10/10/2024 (Quá hạn 2 ngày)"}
        };
        modelCongNo = new DefaultTableModel(data, cols);
        tableCongNo = DialogUtils.createStyledTable(modelCongNo);

        tableCongNo.getSelectionModel().addListSelectionListener(e -> {
            int row = tableCongNo.getSelectedRow();
            if (row != -1) {
                txtPhong.setText(modelCongNo.getValueAt(row, 0).toString());
                txtMaHD.setText(modelCongNo.getValueAt(row, 1).toString());
                txtConNo.setText(modelCongNo.getValueAt(row, 2).toString());
                txtSoTienTra.setText(modelCongNo.getValueAt(row, 2).toString().replace(",", ""));
                txtTongTien.setText("4,606,500");
                txtDaTra.setText("2,000,000");
                txtNguoiNop.setText("Khách thuê phòng");
            }
        });

        panel.add(new JScrollPane(tableCongNo), BorderLayout.CENTER);
        return panel;
    }

    private void initSouthForm() {
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBackground(DialogUtils.BG_CONTENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Thực Hiện Thu Tiền & Ghi Nhận Công Nợ", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaHD = DialogUtils.createFormField();
        txtPhong = DialogUtils.createFormField();
        txtTongTien = DialogUtils.createFormField();
        txtTongTien.setEditable(false);
        txtDaTra = DialogUtils.createFormField();
        txtDaTra.setEditable(false);

        txtConNo = DialogUtils.createFormField();
        txtConNo.setEditable(false);
        txtConNo.setForeground(DialogUtils.DANGER);
        txtConNo.setFont(new Font(DialogUtils.FONT_FAMILY, Font.BOLD, 14));

        txtSoTienTra = DialogUtils.createFormField();
        txtNguoiNop = DialogUtils.createFormField();

        cbHinhThuc = DialogUtils.createStyledComboBox("Tiền mặt", "Chuyển khoản Vietcombank", "Chuyển khoản MBBank", "Ví MoMo", "VNPay");

        txtGhiChu = new JTextArea(2, 20);
        txtGhiChu.setFont(DialogUtils.BODY_FONT);
        txtGhiChu.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR), BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã hóa đơn:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaHD, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Phòng thu tiền:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtPhong, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Đã thu trước đó:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtDaTra, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Số còn nợ:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtConNo, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Số tiền nộp lần này:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtSoTienTra, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Hình thức trả:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(cbHinhThuc, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Người nộp tiền:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtNguoiNop, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Nội dung / Ghi chú:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(new JScrollPane(txtGhiChu), gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnXacNhan = DialogUtils.createStyledButton("💰 Xác Nhận Thu Tiền", DialogUtils.SUCCESS);
        JButton btnInPhieu = DialogUtils.createStyledButton("🖨️ In Phiếu Thu", DialogUtils.PRIMARY);
        JButton btnNhacNo = DialogUtils.createStyledButton("⏰ Nhắc Nợ Tự Động", DialogUtils.WARNING);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnXacNhan.addActionListener(e -> DialogUtils.showSuccess(this, "Đã ghi nhận thanh toán tiền phòng thành công!"));
        btnInPhieu.addActionListener(e -> DialogUtils.showInfo(this, "Đang xuất phiếu thu chi tiết có chữ ký thủ quỹ..."));
        btnNhacNo.addActionListener(e -> DialogUtils.showSuccess(this, "Đã gửi thông báo nhắc nợ tới người thuê phòng!"));
        btnLamMoi.addActionListener(e -> {
            txtMaHD.setText("");
            txtPhong.setText("");
            txtTongTien.setText("");
            txtDaTra.setText("");
            txtConNo.setText("");
            txtSoTienTra.setText("");
            txtNguoiNop.setText("");
            txtGhiChu.setText("");
        });

        btnBar.add(btnXacNhan);
        btnBar.add(btnInPhieu);
        btnBar.add(btnNhacNo);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }
}
