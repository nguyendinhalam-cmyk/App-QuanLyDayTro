package com.quanlydaytro.ui.quanlydaytro;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class ChuTroPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaChuTro, txtHoTen, txtSdt, txtEmail, txtCccd, txtStk, txtNganHang;
    private JTextArea txtDiaChi;

    public ChuTroPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initCenterTable();
        initSouthForm();
    }

    private void initHeader() {
        txtSearch = DialogUtils.createSearchField("Tìm kiếm chủ trọ...");
        JPanel headerPanel = DialogUtils.createHeaderPanel("🏠 QUẢN LÝ CHỦ TRỌ", txtSearch);
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
        String[] cols = {"STT", "Mã Chủ Trọ", "Họ tên", "Số ĐT", "Email", "CCCD", "Số TK", "Ngân hàng", "Số dãy quản lý"};
        Object[][] data = {
            {"1", "CT001", "Lê Hoài Nam", "0908889999", "nam.le@gmail.com", "079080009999", "19034567890", "Techcombank", "2 dãy (40 phòng)"},
            {"2", "CT002", "Nguyễn Thị Mai", "0918777666", "mai.nguyen@gmail.com", "079175008888", "0071001234567", "Vietcombank", "1 dãy (20 phòng)"},
            {"3", "CT003", "Trần Đình Trọng", "0934555444", "trong.tran@yahoo.com", "079085007777", "102868686868", "VietinBank", "1 dãy (15 phòng)"}
        };
        tableModel = new DefaultTableModel(data, cols);
        table = DialogUtils.createStyledTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                txtMaChuTro.setText(tableModel.getValueAt(modelRow, 1).toString());
                txtHoTen.setText(tableModel.getValueAt(modelRow, 2).toString());
                txtSdt.setText(tableModel.getValueAt(modelRow, 3).toString());
                txtEmail.setText(tableModel.getValueAt(modelRow, 4).toString());
                txtCccd.setText(tableModel.getValueAt(modelRow, 5).toString());
                txtStk.setText(tableModel.getValueAt(modelRow, 6).toString());
                txtNganHang.setText(tableModel.getValueAt(modelRow, 7).toString());
                txtDiaChi.setText("Quận 7, TP. Hồ Chí Minh");
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
            DialogUtils.createCardBorder("Thông Tin Chủ Trọ", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaChuTro = DialogUtils.createFormField();
        txtMaChuTro.setEditable(false);
        txtHoTen = DialogUtils.createFormField();
        txtSdt = DialogUtils.createFormField();
        txtEmail = DialogUtils.createFormField();
        txtCccd = DialogUtils.createFormField();
        txtStk = DialogUtils.createFormField();
        txtNganHang = DialogUtils.createFormField();

        txtDiaChi = new JTextArea(2, 20);
        txtDiaChi.setFont(DialogUtils.BODY_FONT);
        txtDiaChi.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR), BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã chủ trọ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaChuTro, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Họ và tên:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtHoTen, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtSdt, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtEmail, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("CCCD:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtCccd, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Số tài khoản:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtStk, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Ngân hàng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtNganHang, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Địa chỉ thường trú:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(new JScrollPane(txtDiaChi), gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnThem = DialogUtils.createStyledButton("➕ Thêm Chủ Trọ", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("✏️ Cập Nhật", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("🗑️ Xóa", DialogUtils.DANGER);
        JButton btnXemDay = DialogUtils.createStyledButton("🏘️ Xem Danh Sách Dãy Trọ", DialogUtils.PRIMARY);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnThem.addActionListener(e -> DialogUtils.showSuccess(this, "Đã thêm mới chủ trọ!"));
        btnSua.addActionListener(e -> DialogUtils.showSuccess(this, "Đã cập nhật thông tin chủ trọ!"));
        btnXoa.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Bạn có muốn xóa chủ trọ này khỏi hệ thống?")) {
                DialogUtils.showSuccess(this, "Đã xóa chủ trọ!");
            }
        });
        btnXemDay.addActionListener(e -> DialogUtils.showInfo(this, "Chủ trọ hiện đang sở hữu 2 dãy trọ với tổng cộng 40 phòng trọ hoạt động."));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnThem);
        btnBar.add(btnSua);
        btnBar.add(btnXoa);
        btnBar.add(btnXemDay);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaChuTro.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtCccd.setText("");
        txtStk.setText("");
        txtNganHang.setText("");
        txtDiaChi.setText("");
        table.clearSelection();
    }
}
