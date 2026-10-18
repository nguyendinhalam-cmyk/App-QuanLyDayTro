package com.quanlydaytro.ui.quanlyhoadon;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class HoaDonPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaHD, txtTienPhong, txtTienDien, txtTienNuoc, txtPhiDichVu, txtTongTien;
    private JComboBox<String> cbPhong, cbThang, cbNam, cbTrangThai, cbFilterStatus;

    public HoaDonPanel() {
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
        txtSearch = DialogUtils.createSearchField("Tìm theo mã HĐ, phòng, khách...");
        JPanel topHeader = DialogUtils.createHeaderPanel("📄 QUẢN LÝ HÓA ĐƠN TIỀN NHÀ", txtSearch);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        DialogUtils.styleToolbar(filterBar);

        filterBar.add(DialogUtils.createFormLabel("Lọc trạng thái:"));
        cbFilterStatus = DialogUtils.createStyledComboBox("Tất cả", "Chưa thanh toán", "Thanh toán một phần", "Đã thanh toán");
        filterBar.add(cbFilterStatus);

        JButton btnFilter = DialogUtils.createStyledButton("🔍 Lọc Hóa Đơn", DialogUtils.PRIMARY);
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
                String rowStatus = entry.getStringValue(10);
                String fullRow = entry.getStringValue(1) + " " + entry.getStringValue(2);
                boolean matchKey = keyword.isEmpty() || fullRow.toLowerCase().contains(keyword.toLowerCase());
                boolean matchStatus = status.equals("Tất cả") || rowStatus.equals(status);
                return matchKey && matchStatus;
            }
        });
    }

    private void initCenterTable() {
        String[] cols = {"STT", "Mã HĐ", "Phòng", "Tháng/Năm", "Tiền Phòng", "Tiền Điện", "Tiền Nước", "Phí Khác", "Tổng Tiền", "Đã Trả", "Trạng Thái"};
        Object[][] data = {
            {"1", "INV-1001", "Phòng 101", "10/2024", "2,800,000", "455,000", "128,000", "150,000", "3,533,000", "3,533,000", "Đã thanh toán"},
            {"2", "INV-1002", "Phòng 102", "10/2024", "3,500,000", "682,500", "224,000", "200,000", "4,606,500", "2,000,000", "Thanh toán một phần"},
            {"3", "INV-1003", "Phòng 201", "10/2024", "4,200,000", "770,000", "240,000", "200,000", "5,410,000", "0", "Chưa thanh toán"}
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
                txtTienPhong.setText(tableModel.getValueAt(modelRow, 4).toString());
                txtTienDien.setText(tableModel.getValueAt(modelRow, 5).toString());
                txtTienNuoc.setText(tableModel.getValueAt(modelRow, 6).toString());
                txtPhiDichVu.setText(tableModel.getValueAt(modelRow, 7).toString());
                txtTongTien.setText(tableModel.getValueAt(modelRow, 8).toString());
                cbTrangThai.setSelectedItem(tableModel.getValueAt(modelRow, 10).toString());
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
            DialogUtils.createCardBorder("Lập & Cập Nhật Hóa Đơn Hàng Tháng", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaHD = DialogUtils.createFormField();
        txtMaHD.setEditable(false);
        txtTienPhong = DialogUtils.createFormField();
        txtTienDien = DialogUtils.createFormField();
        txtTienNuoc = DialogUtils.createFormField();
        txtPhiDichVu = DialogUtils.createFormField();
        txtTongTien = DialogUtils.createFormField();
        txtTongTien.setEditable(false);
        txtTongTien.setForeground(DialogUtils.DANGER);
        txtTongTien.setFont(new Font(DialogUtils.FONT_FAMILY, Font.BOLD, 15));

        cbPhong = DialogUtils.createStyledComboBox("Phòng 101", "Phòng 102", "Phòng 103", "Phòng 201", "Phòng VIP 301");
        cbThang = DialogUtils.createStyledComboBox("Tháng 10", "Tháng 09", "Tháng 08", "Tháng 07");
        cbNam = DialogUtils.createStyledComboBox("2024", "2025");
        cbTrangThai = DialogUtils.createStyledComboBox("Chưa thanh toán", "Thanh toán một phần", "Đã thanh toán");

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã hóa đơn:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaHD, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Chọn phòng:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(cbPhong, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Tháng hóa đơn:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(cbThang, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Năm:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(cbNam, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Tiền phòng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtTienPhong, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Tiền điện:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtTienDien, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Tiền nước:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtTienNuoc, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Phí rác & mạng:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtPhiDichVu, gbc);

        // Row 4
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Tổng tiền (VNĐ):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(txtTongTien, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Trạng thái:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(cbTrangThai, gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnTaoHD = DialogUtils.createStyledButton("📝 Lập Hóa Đơn", DialogUtils.SUCCESS);
        JButton btnTinhToan = DialogUtils.createStyledButton("⚡ Tự Động Tính Tiền", DialogUtils.PRIMARY);
        JButton btnInHD = DialogUtils.createStyledButton("🖨️ In Hóa Đơn / Xuất PDF", DialogUtils.PURPLE);
        JButton btnGuiThongBao = DialogUtils.createStyledButton("📲 Gửi Zalo / SMS", DialogUtils.WARNING);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnTaoHD.addActionListener(e -> DialogUtils.showSuccess(this, "Đã lưu hóa đơn tháng mới!"));
        btnTinhToan.addActionListener(e -> {
            txtTienPhong.setText("2,800,000");
            txtTienDien.setText("455,000");
            txtTienNuoc.setText("128,000");
            txtPhiDichVu.setText("150,000");
            txtTongTien.setText("3,533,000 VNĐ");
            DialogUtils.showSuccess(this, "Hệ thống đã tự động tính từ chỉ số điện nước & hợp đồng!");
        });
        btnInHD.addActionListener(e -> DialogUtils.showInfo(this, "Đang kết xuất phiếu thu tiền phòng tháng 10/2024..."));
        btnGuiThongBao.addActionListener(e -> DialogUtils.showSuccess(this, "Đã gửi thông báo tiền phòng tới số điện thoại của người thuê!"));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnTaoHD);
        btnBar.add(btnTinhToan);
        btnBar.add(btnInHD);
        btnBar.add(btnGuiThongBao);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaHD.setText("");
        txtTienPhong.setText("");
        txtTienDien.setText("");
        txtTienNuoc.setText("");
        txtPhiDichVu.setText("");
        txtTongTien.setText("");
        cbPhong.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}
