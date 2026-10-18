package com.quanlydaytro.ui.quanlyhopdong;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class HopDongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaHD, txtNgayBatDau, txtNgayKetThuc, txtTienCoc, txtGiaThueThang;
    private JComboBox<String> cbPhong, cbNguoiThue, cbChuKyTT, cbTrangThai, cbFilterStatus;
    private JTextArea txtDieuKhoan;

    public HopDongPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initCenterTable();
        initSouthForm();
    }

    private void initHeader() {
        JPanel headerContainer = new JPanel(new BorderLayout(0, 10));
        headerContainer.setOpaque(false);
        txtSearch = DialogUtils.createSearchField("Tìm hợp đồng, phòng, người thuê...");
        JPanel topHeader = DialogUtils.createHeaderPanel("📋 QUẢN LÝ HỢP ĐỒNG THUÊ PHÒNG", txtSearch);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        DialogUtils.styleToolbar(filterBar);

        filterBar.add(DialogUtils.createFormLabel("Lọc theo trạng thái:"));
        cbFilterStatus = DialogUtils.createStyledComboBox("Tất cả", "Đang hiệu lực", "Sắp hết hạn", "Đã kết thúc");
        filterBar.add(cbFilterStatus);

        JButton btnFilter = DialogUtils.createStyledButton("🔍 Lọc Hợp Đồng", DialogUtils.PRIMARY);
        btnFilter.addActionListener(e -> applyFilter());
        filterBar.add(btnFilter);

        headerContainer.add(topHeader, BorderLayout.NORTH);
        headerContainer.add(filterBar, BorderLayout.SOUTH);
        add(headerContainer, BorderLayout.NORTH);

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });
    }

    private void applyFilter() {
        String keyword = txtSearch.getText().trim();
        String status = cbFilterStatus.getSelectedItem().toString();

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String rowStatus = entry.getStringValue(8);
                String fullRow = entry.getStringValue(1) + " " + entry.getStringValue(2) + " " + entry.getStringValue(3);
                boolean matchKey = keyword.isEmpty() || fullRow.toLowerCase().contains(keyword.toLowerCase());
                boolean matchStatus = status.equals("Tất cả") || rowStatus.equals(status);
                return matchKey && matchStatus;
            }
        });
    }

    private void initCenterTable() {
        String[] cols = {"STT", "Mã HĐ", "Phòng", "Người Thuê", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Tiền Cọc", "Giá Thuê/Tháng", "Trạng Thái"};
        Object[][] data = {
            {"1", "HD202401", "Phòng 101", "Nguyễn Văn An", "01/01/2024", "01/01/2025", "2,800,000 VNĐ", "2,800,000 VNĐ", "Đang hiệu lực"},
            {"2", "HD202402", "Phòng 102", "Trần Thị Mai Phương", "15/01/2024", "15/07/2024", "3,500,000 VNĐ", "3,500,000 VNĐ", "Đang hiệu lực"},
            {"3", "HD202305", "Phòng 103", "Phạm Quốc Huy", "01/06/2023", "01/06/2024", "2,800,000 VNĐ", "2,800,000 VNĐ", "Đã kết thúc"},
            {"4", "HD202408", "Phòng 201", "Hoàng Thùy Linh", "01/03/2024", "01/09/2024", "4,200,000 VNĐ", "4,200,000 VNĐ", "Sắp hết hạn"}
        };
        tableModel = new DefaultTableModel(data, cols);
        table = DialogUtils.createStyledTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                txtMaHD.setText(tableModel.getValueAt(modelRow, 1).toString());
                cbPhong.setSelectedItem(tableModel.getValueAt(modelRow, 2).toString());
                cbNguoiThue.setSelectedItem(tableModel.getValueAt(modelRow, 3).toString());
                txtNgayBatDau.setText(tableModel.getValueAt(modelRow, 4).toString());
                txtNgayKetThuc.setText(tableModel.getValueAt(modelRow, 5).toString());
                txtTienCoc.setText(tableModel.getValueAt(modelRow, 6).toString().replace(" VNĐ", "").replace(",", ""));
                txtGiaThueThang.setText(tableModel.getValueAt(modelRow, 7).toString().replace(" VNĐ", "").replace(",", ""));
                cbTrangThai.setSelectedItem(tableModel.getValueAt(modelRow, 8).toString());
                cbChuKyTT.setSelectedItem("Hàng tháng (Ngày 05)");
                txtDieuKhoan.setText("Hợp đồng có thời hạn 1 năm. Đóng tiền nhà ngày 5 hàng tháng.");
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR));
        add(scroll, BorderLayout.CENTER);
    }

    private void initSouthForm() {
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBackground(DialogUtils.BG_CONTENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Chi Tiết Hợp Đồng Thuê", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaHD = DialogUtils.createFormField();
        txtMaHD.setEditable(false);
        txtNgayBatDau = DialogUtils.createFormField();
        txtNgayKetThuc = DialogUtils.createFormField();
        txtTienCoc = DialogUtils.createFormField();
        txtGiaThueThang = DialogUtils.createFormField();

        cbPhong = DialogUtils.createStyledComboBox("Phòng 101", "Phòng 102", "Phòng 103", "Phòng 201", "Phòng VIP 301");
        cbNguoiThue = DialogUtils.createStyledComboBox("Nguyễn Văn An", "Trần Thị Mai Phương", "Lê Quốc Bảo", "Hoàng Thùy Linh");
        cbChuKyTT = DialogUtils.createStyledComboBox("Hàng tháng (Ngày 05)", "Hàng quý (3 tháng/lần)", "6 tháng/lần");
        cbTrangThai = DialogUtils.createStyledComboBox("Đang hiệu lực", "Sắp hết hạn", "Đã kết thúc", "Chờ ký kết");

        txtDieuKhoan = new JTextArea(2, 20);
        txtDieuKhoan.setFont(DialogUtils.BODY_FONT);
        txtDieuKhoan.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR), BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã hợp đồng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaHD, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Phòng thuê:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(cbPhong, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Khách thuê:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(cbNguoiThue, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Ngày bắt đầu:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtNgayBatDau, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Ngày kết thúc:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtNgayKetThuc, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Tiền đặt cọc (VNĐ):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtTienCoc, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Giá thuê/tháng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtGiaThueThang, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Kỳ thanh toán:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(cbChuKyTT, gbc);

        // Row 4
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(cbTrangThai, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Ghi chú / Điều khoản:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(new JScrollPane(txtDieuKhoan), gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnLapHD = DialogUtils.createStyledButton("📝 Lập Hợp Đồng", DialogUtils.SUCCESS);
        JButton btnGiaHan = DialogUtils.createStyledButton("⏳ Gia Hạn", DialogUtils.WARNING);
        JButton btnKetThuc = DialogUtils.createStyledButton("🛑 Thanh Lý / Kết Thúc", DialogUtils.DANGER);
        JButton btnIn = DialogUtils.createStyledButton("🖨️ In Hợp Đồng", DialogUtils.PURPLE);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnLapHD.addActionListener(e -> DialogUtils.showSuccess(this, "Đã lập và kích hoạt hợp đồng thuê thành công!"));
        btnGiaHan.addActionListener(e -> DialogUtils.showSuccess(this, "Đã gia hạn hợp đồng thêm 06 tháng!"));
        btnKetThuc.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Xác nhận kết thúc hợp đồng và chuyển sang quyết toán cọc?")) {
                DialogUtils.showSuccess(this, "Hợp đồng đã được chuyển sang trạng thái đã thanh lý!");
            }
        });
        btnIn.addActionListener(e -> DialogUtils.showInfo(this, "Đang kết nối máy in xuất file hợp đồng chuẩn mẫu..."));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnLapHD);
        btnBar.add(btnGiaHan);
        btnBar.add(btnKetThuc);
        btnBar.add(btnIn);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaHD.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtTienCoc.setText("");
        txtGiaThueThang.setText("");
        txtDieuKhoan.setText("");
        cbPhong.setSelectedIndex(0);
        cbNguoiThue.setSelectedIndex(0);
        cbChuKyTT.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}
