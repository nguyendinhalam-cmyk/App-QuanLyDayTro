package com.quanlydaytro.ui.quanlydaytro;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class DayTroPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaDayTro, txtTenDayTro, txtDiaChi, txtSoTang, txtTongSoPhong;
    private JComboBox<String> cbChuTro, cbTrangThai;

    public DayTroPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initCenterTable();
        initSouthForm();
    }

    private void initHeader() {
        txtSearch = DialogUtils.createSearchField("Tìm theo tên dãy, địa chỉ, chủ trọ...");
        JPanel headerPanel = DialogUtils.createHeaderPanel("🏘️ QUẢN LÝ DÃY TRỌ", txtSearch);
        add(headerPanel, BorderLayout.NORTH);

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText().trim();
                if (text.isEmpty()) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });
    }

    private void initCenterTable() {
        String[] cols = {"STT", "Mã Dãy", "Tên Dãy Trọ", "Địa Chỉ", "Chủ Trọ", "Số Tầng", "Tổng Phòng", "Trống", "Trạng Thái"};
        Object[][] data = {
            {"1", "DT01", "Dãy A - Khu Đông", "123 Nguyễn Thị Thập, P. Tân Phong, Q7", "Lê Hoài Nam", "3", "20", "3", "Hoạt động"},
            {"2", "DT02", "Dãy B - Khu Tây", "456 Lê Văn Lương, P. Tân Hưng, Q7", "Lê Hoài Nam", "3", "20", "2", "Hoạt động"},
            {"3", "DT03", "Dãy C - VIP", "789 Huỳnh Tấn Phát, P. Phú Thuận, Q7", "Nguyễn Thị Mai", "2", "10", "3", "Hoạt động"}
        };
        tableModel = new DefaultTableModel(data, cols);
        table = DialogUtils.createStyledTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                txtMaDayTro.setText(tableModel.getValueAt(modelRow, 1).toString());
                txtTenDayTro.setText(tableModel.getValueAt(modelRow, 2).toString());
                txtDiaChi.setText(tableModel.getValueAt(modelRow, 3).toString());
                cbChuTro.setSelectedItem(tableModel.getValueAt(modelRow, 4).toString());
                txtSoTang.setText(tableModel.getValueAt(modelRow, 5).toString());
                txtTongSoPhong.setText(tableModel.getValueAt(modelRow, 6).toString());
                cbTrangThai.setSelectedItem(tableModel.getValueAt(modelRow, 8).toString());
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
            DialogUtils.createCardBorder("Thông Tin Dãy Trọ", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaDayTro = DialogUtils.createFormField();
        txtMaDayTro.setEditable(false);
        txtTenDayTro = DialogUtils.createFormField();
        txtDiaChi = DialogUtils.createFormField();
        txtSoTang = DialogUtils.createFormField();
        txtTongSoPhong = DialogUtils.createFormField();

        cbChuTro = DialogUtils.createStyledComboBox("Lê Hoài Nam", "Nguyễn Thị Mai", "Trần Đình Trọng");
        cbTrangThai = DialogUtils.createStyledComboBox("Hoạt động", "Bảo trì", "Dừng kinh doanh");

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã dãy trọ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaDayTro, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Tên dãy trọ:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtTenDayTro, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; gbc.gridwidth = 3; formPanel.add(txtDiaChi, gbc);
        gbc.gridwidth = 1;

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Chủ sở hữu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(cbChuTro, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Số tầng:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtSoTang, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Tổng số phòng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtTongSoPhong, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Trạng thái:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(cbTrangThai, gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnThem = DialogUtils.createStyledButton("➕ Thêm Dãy Trọ", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("✏️ Cập Nhật", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("🗑️ Xóa", DialogUtils.DANGER);
        JButton btnXemPhong = DialogUtils.createStyledButton("🚪 Xem Danh Sách Phòng", DialogUtils.PRIMARY);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnThem.addActionListener(e -> DialogUtils.showSuccess(this, "Đã thêm dãy trọ mới!"));
        btnSua.addActionListener(e -> DialogUtils.showSuccess(this, "Đã cập nhật thông tin dãy trọ!"));
        btnXoa.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Bạn chắc chắn muốn xóa dãy trọ này?")) {
                DialogUtils.showSuccess(this, "Đã xóa dãy trọ!");
            }
        });
        btnXemPhong.addActionListener(e -> DialogUtils.showInfo(this, "Dãy trọ gồm 20 phòng, hiện có 3 phòng trống."));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnThem);
        btnBar.add(btnSua);
        btnBar.add(btnXoa);
        btnBar.add(btnXemPhong);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaDayTro.setText("");
        txtTenDayTro.setText("");
        txtDiaChi.setText("");
        txtSoTang.setText("");
        txtTongSoPhong.setText("");
        cbChuTro.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}
