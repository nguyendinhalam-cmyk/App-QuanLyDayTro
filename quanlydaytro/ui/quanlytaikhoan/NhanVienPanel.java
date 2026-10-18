package com.quanlydaytro.ui.quanlytaikhoan;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class NhanVienPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaNV, txtHoTen, txtSdt, txtEmail, txtCccd, txtDiaChi;
    private JComboBox<String> cbChucVu, cbDayTro, cbTrangThai;

    public NhanVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initCenterTable();
        initSouthForm();
    }

    private void initHeader() {
        txtSearch = DialogUtils.createSearchField("Tìm theo mã, tên, SĐT, chức vụ...");
        JPanel headerPanel = DialogUtils.createHeaderPanel("👨‍💼 QUẢN LÝ NHÂN VIÊN & PHÂN CÔNG", txtSearch);
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
        String[] cols = {"STT", "Mã NV", "Họ tên", "Số ĐT", "Email", "CCCD", "Chức vụ", "Dãy trọ quản lý", "Trạng thái"};
        Object[][] data = {
            {"1", "NV001", "Trần Văn Bình", "0901234567", "binh.tran@roomhub.vn", "079090001234", "Quản lý dãy", "Dãy A - Khu Đông", "Đang làm việc"},
            {"2", "NV002", "Lê Thị Thu", "0912345678", "thu.le@roomhub.vn", "079192002345", "Kế toán", "Tất cả dãy", "Đang làm việc"},
            {"3", "NV003", "Nguyễn Tuấn Kiệt", "0987654321", "kiet.nguyen@roomhub.vn", "079088003456", "Bảo vệ / Kỹ thuật", "Dãy B - Khu Tây", "Đang làm việc"},
            {"4", "NV004", "Võ Minh Đức", "0933112233", "duc.vo@roomhub.vn", "079095004567", "Nhân viên vệ sinh", "Dãy C - VIP", "Nghỉ việc"}
        };
        tableModel = new DefaultTableModel(data, cols);
        table = DialogUtils.createStyledTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                txtMaNV.setText(tableModel.getValueAt(modelRow, 1).toString());
                txtHoTen.setText(tableModel.getValueAt(modelRow, 2).toString());
                txtSdt.setText(tableModel.getValueAt(modelRow, 3).toString());
                txtEmail.setText(tableModel.getValueAt(modelRow, 4).toString());
                txtCccd.setText(tableModel.getValueAt(modelRow, 5).toString());
                cbChucVu.setSelectedItem(tableModel.getValueAt(modelRow, 6).toString());
                cbDayTro.setSelectedItem(tableModel.getValueAt(modelRow, 7).toString());
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
            DialogUtils.createCardBorder("Thông Tin Nhân Viên & Phân Công", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaNV = DialogUtils.createFormField();
        txtMaNV.setEditable(false);
        txtHoTen = DialogUtils.createFormField();
        txtSdt = DialogUtils.createFormField();
        txtEmail = DialogUtils.createFormField();
        txtCccd = DialogUtils.createFormField();
        txtDiaChi = DialogUtils.createFormField();

        cbChucVu = DialogUtils.createStyledComboBox("Quản lý dãy", "Kế toán", "Bảo vệ / Kỹ thuật", "Nhân viên vệ sinh");
        cbDayTro = DialogUtils.createStyledComboBox("Tất cả dãy", "Dãy A - Khu Đông", "Dãy B - Khu Tây", "Dãy C - VIP");
        cbTrangThai = DialogUtils.createStyledComboBox("Đang làm việc", "Tạm nghỉ", "Nghỉ việc");

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã NV:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaNV, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Họ và tên:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtHoTen, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtSdt, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtEmail, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("CCCD / CMND:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtCccd, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Chức vụ:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(cbChucVu, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Phân công dãy:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(cbDayTro, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Trạng thái:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(cbTrangThai, gbc);

        // Row 4
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Địa chỉ thường trú:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 4; gbc.gridwidth = 3; formPanel.add(txtDiaChi, gbc);
        gbc.gridwidth = 1;

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnThem = DialogUtils.createStyledButton("➕ Thêm Nhân Viên", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("✏️ Cập Nhật", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("🗑️ Xóa", DialogUtils.DANGER);
        JButton btnPhanCong = DialogUtils.createStyledButton("📋 Phân Công Lại", DialogUtils.PRIMARY);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnThem.addActionListener(e -> DialogUtils.showSuccess(this, "Đã thêm mới nhân viên thành công!"));
        btnSua.addActionListener(e -> DialogUtils.showSuccess(this, "Đã cập nhật hồ sơ nhân viên!"));
        btnXoa.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Bạn có chắc chắn muốn xóa nhân viên này khỏi hệ thống?")) {
                DialogUtils.showSuccess(this, "Đã xóa nhân viên!");
            }
        });
        btnPhanCong.addActionListener(e -> DialogUtils.showSuccess(this, "Đã lưu thông tin phân công quản lý dãy trọ!"));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnThem);
        btnBar.add(btnSua);
        btnBar.add(btnXoa);
        btnBar.add(btnPhanCong);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtCccd.setText("");
        txtDiaChi.setText("");
        cbChucVu.setSelectedIndex(0);
        cbDayTro.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}
