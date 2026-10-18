package com.quanlydaytro.ui.quanlyphong;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class PhongPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaPhong, txtTenPhong, txtTang, txtDienTich, txtGiaThue, txtSoNguoiToiDa;
    private JComboBox<String> cbDayTro, cbLoaiPhong, cbTrangThai, cbFilterDay, cbFilterTrangThai;
    private JTextArea txtTienNghi;

    public PhongPanel() {
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
        txtSearch = DialogUtils.createSearchField("Tìm theo số phòng, dãy, giá...");
        JPanel topHeader = DialogUtils.createHeaderPanel("🚪 QUẢN LÝ PHÒNG TRỌ", txtSearch);

        // Filter toolbar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        DialogUtils.styleToolbar(filterBar);

        filterBar.add(DialogUtils.createFormLabel("Lọc theo dãy:"));
        cbFilterDay = DialogUtils.createStyledComboBox("Tất cả", "Dãy A - Khu Đông", "Dãy B - Khu Tây", "Dãy C - VIP");
        filterBar.add(cbFilterDay);

        filterBar.add(DialogUtils.createFormLabel("Trạng thái:"));
        cbFilterTrangThai = DialogUtils.createStyledComboBox("Tất cả", "Trống", "Đang thuê", "Đang sửa chữa");
        filterBar.add(cbFilterTrangThai);

        JButton btnFilter = DialogUtils.createStyledButton("🔍 Lọc", DialogUtils.PRIMARY);
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
        String day = cbFilterDay.getSelectedItem().toString();
        String status = cbFilterTrangThai.getSelectedItem().toString();

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String rowDay = entry.getStringValue(3);
                String rowStatus = entry.getStringValue(7);
                String fullRow = entry.getStringValue(1) + " " + entry.getStringValue(2) + " " + entry.getStringValue(6);

                boolean matchKeyword = keyword.isEmpty() || fullRow.toLowerCase().contains(keyword.toLowerCase());
                boolean matchDay = day.equals("Tất cả") || rowDay.equals(day);
                boolean matchStatus = status.equals("Tất cả") || rowStatus.equals(status);

                return matchKeyword && matchDay && matchStatus;
            }
        });
    }

    private void initCenterTable() {
        String[] cols = {"STT", "Mã Phòng", "Tên Phòng", "Dãy Trọ", "Loại Phòng", "Diện Tích", "Giá Thuê/Tháng", "Trạng Thái"};
        Object[][] data = {
            {"1", "P101", "Phòng 101", "Dãy A - Khu Đông", "Phòng đơn", "18 m²", "2,800,000 VNĐ", "Đang thuê"},
            {"2", "P102", "Phòng 102", "Dãy A - Khu Đông", "Phòng đôi", "25 m²", "3,500,000 VNĐ", "Đang thuê"},
            {"3", "P103", "Phòng 103", "Dãy A - Khu Đông", "Phòng đơn", "18 m²", "2,800,000 VNĐ", "Trống"},
            {"4", "P201", "Phòng 201", "Dãy B - Khu Tây", "Phòng ghép", "30 m²", "4,200,000 VNĐ", "Đang sửa chữa"},
            {"5", "P301", "Phòng VIP 301", "Dãy C - VIP", "Căn hộ mini", "35 m²", "5,500,000 VNĐ", "Trống"}
        };
        tableModel = new DefaultTableModel(data, cols);
        table = DialogUtils.createStyledTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Highlight status column

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                txtMaPhong.setText(tableModel.getValueAt(modelRow, 1).toString());
                txtTenPhong.setText(tableModel.getValueAt(modelRow, 2).toString());
                cbDayTro.setSelectedItem(tableModel.getValueAt(modelRow, 3).toString());
                cbLoaiPhong.setSelectedItem(tableModel.getValueAt(modelRow, 4).toString());
                txtDienTich.setText(tableModel.getValueAt(modelRow, 5).toString().replace(" m²", ""));
                txtGiaThue.setText(tableModel.getValueAt(modelRow, 6).toString().replace(" VNĐ", "").replace(",", ""));
                cbTrangThai.setSelectedItem(tableModel.getValueAt(modelRow, 7).toString());
                txtTang.setText("1");
                txtSoNguoiToiDa.setText("2");
                txtTienNghi.setText("Điều hòa, Bình nóng lạnh, Giường nệm, Tủ quần áo");
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
            DialogUtils.createCardBorder("Thông Tin Phòng Trọ", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaPhong = DialogUtils.createFormField();
        txtMaPhong.setEditable(false);
        txtTenPhong = DialogUtils.createFormField();
        txtTang = DialogUtils.createFormField();
        txtDienTich = DialogUtils.createFormField();
        txtGiaThue = DialogUtils.createFormField();
        txtSoNguoiToiDa = DialogUtils.createFormField();

        cbDayTro = DialogUtils.createStyledComboBox("Dãy A - Khu Đông", "Dãy B - Khu Tây", "Dãy C - VIP");
        cbLoaiPhong = DialogUtils.createStyledComboBox("Phòng đơn", "Phòng đôi", "Phòng ghép", "Căn hộ mini");
        cbTrangThai = DialogUtils.createStyledComboBox("Trống", "Đang thuê", "Đang sửa chữa");

        txtTienNghi = new JTextArea(2, 20);
        txtTienNghi.setFont(DialogUtils.BODY_FONT);
        txtTienNghi.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR), BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã phòng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaPhong, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Tên phòng:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtTenPhong, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Thuộc dãy:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(cbDayTro, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Loại phòng:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(cbLoaiPhong, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Tầng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtTang, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Diện tích (m²):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtDienTich, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Giá thuê (VNĐ):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtGiaThue, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Số người tối đa:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtSoNguoiToiDa, gbc);

        // Row 4
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(cbTrangThai, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Tiện nghi:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(new JScrollPane(txtTienNghi), gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnThem = DialogUtils.createStyledButton("➕ Thêm Phòng", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("✏️ Cập Nhật", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("🗑️ Xóa", DialogUtils.DANGER);
        JButton btnChuyenTrangThai = DialogUtils.createStyledButton("🔄 Đổi Trạng Thái", DialogUtils.PRIMARY);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnThem.addActionListener(e -> DialogUtils.showSuccess(this, "Đã thêm phòng mới!"));
        btnSua.addActionListener(e -> DialogUtils.showSuccess(this, "Đã cập nhật thông tin phòng!"));
        btnXoa.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Xác nhận xóa phòng này?")) {
                DialogUtils.showSuccess(this, "Đã xóa phòng!");
            }
        });
        btnChuyenTrangThai.addActionListener(e -> DialogUtils.showSuccess(this, "Đã chuyển trạng thái phòng sang: " + cbTrangThai.getSelectedItem()));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnThem);
        btnBar.add(btnSua);
        btnBar.add(btnXoa);
        btnBar.add(btnChuyenTrangThai);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaPhong.setText("");
        txtTenPhong.setText("");
        txtTang.setText("");
        txtDienTich.setText("");
        txtGiaThue.setText("");
        txtSoNguoiToiDa.setText("");
        txtTienNghi.setText("");
        cbDayTro.setSelectedIndex(0);
        cbLoaiPhong.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}
