package com.quanlydaytro.ui.quanlytaikhoan;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class TaiKhoanPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaTK, txtTenDangNhap, txtHoTen;
    private JPasswordField txtMatKhau, txtXacNhanMatKhau;
    private JComboBox<String> cbVaiTro, cbTrangThai;

    public TaiKhoanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initCenterTable();
        initSouthForm();
    }

    private void initHeader() {
        txtSearch = DialogUtils.createSearchField("Tìm theo tên đăng nhập, họ tên...");
        JPanel headerPanel = DialogUtils.createHeaderPanel("👤 QUẢN LÝ TÀI KHOẢN & PHÂN QUYỀN", txtSearch);
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
        String[] cols = {"STT", "Mã TK", "Tên đăng nhập", "Họ và tên", "Vai trò", "Trạng thái", "Ngày tạo"};
        Object[][] data = {
            {"1", "TK001", "admin", "Nguyễn Văn Quản Trị", "Quản trị viên", "Hoạt động", "01/01/2024"},
            {"2", "TK002", "nv_hoa", "Trần Thị Hoa", "Nhân viên", "Hoạt động", "15/01/2024"},
            {"3", "TK003", "chutro_nam", "Lê Hoài Nam", "Chủ trọ", "Hoạt động", "01/02/2024"},
            {"4", "TK004", "nv_tuan", "Phạm Anh Tuấn", "Nhân viên", "Khóa", "10/03/2024"}
        };
        tableModel = new DefaultTableModel(data, cols);
        table = DialogUtils.createStyledTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                txtMaTK.setText(tableModel.getValueAt(modelRow, 1).toString());
                txtTenDangNhap.setText(tableModel.getValueAt(modelRow, 2).toString());
                txtHoTen.setText(tableModel.getValueAt(modelRow, 3).toString());
                cbVaiTro.setSelectedItem(tableModel.getValueAt(modelRow, 4).toString());
                cbTrangThai.setSelectedItem(tableModel.getValueAt(modelRow, 5).toString());
                txtMatKhau.setText("");
                txtXacNhanMatKhau.setText("");
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
            DialogUtils.createCardBorder("Thông Tin Tài Khoản", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaTK = DialogUtils.createFormField();
        txtMaTK.setEditable(false);
        txtTenDangNhap = DialogUtils.createFormField();
        txtHoTen = DialogUtils.createFormField();
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(DialogUtils.BODY_FONT);
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR), BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        txtXacNhanMatKhau = new JPasswordField();
        txtXacNhanMatKhau.setFont(DialogUtils.BODY_FONT);
        txtXacNhanMatKhau.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR), BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        cbVaiTro = DialogUtils.createStyledComboBox("Quản trị viên", "Nhân viên", "Chủ trọ");
        cbTrangThai = DialogUtils.createStyledComboBox("Hoạt động", "Khóa");

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã tài khoản:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaTK, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtTenDangNhap, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Họ và tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtHoTen, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Vai trò:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(cbVaiTro, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtMatKhau, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Xác nhận MK:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtXacNhanMatKhau, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(cbTrangThai, gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnThem = DialogUtils.createStyledButton("➕ Thêm Mới", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("✏️ Cập Nhật", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("🗑️ Xóa", DialogUtils.DANGER);
        JButton btnResetPass = DialogUtils.createStyledButton("🔑 Đặt Lại Mật Khẩu", DialogUtils.PRIMARY);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnThem.addActionListener(e -> DialogUtils.showSuccess(this, "Đã thêm mới tài khoản thành công!"));
        btnSua.addActionListener(e -> DialogUtils.showSuccess(this, "Đã cập nhật thông tin tài khoản!"));
        btnXoa.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Bạn có chắc chắn muốn xóa tài khoản này?")) {
                DialogUtils.showSuccess(this, "Đã xóa tài khoản!");
            }
        });
        btnResetPass.addActionListener(e -> DialogUtils.showSuccess(this, "Mật khẩu mặc định đã đặt lại: 123456"));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnThem);
        btnBar.add(btnSua);
        btnBar.add(btnXoa);
        btnBar.add(btnResetPass);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaTK.setText("");
        txtTenDangNhap.setText("");
        txtHoTen.setText("");
        txtMatKhau.setText("");
        txtXacNhanMatKhau.setText("");
        cbVaiTro.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        table.clearSelection();
    }
}
