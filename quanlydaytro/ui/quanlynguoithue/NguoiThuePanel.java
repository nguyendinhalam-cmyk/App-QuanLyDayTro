package com.quanlydaytro.ui.quanlynguoithue;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class NguoiThuePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch, txtMaNT, txtHoTen, txtSdt, txtCccd, txtQueQuan, txtNgheNghiep, txtNgaySinh, txtEmail;
    private JComboBox<String> cbGioiTinh, cbPhongHienTai;
    private JTextArea txtGhiChu;

    public NguoiThuePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initCenterTable();
        initSouthForm();
    }

    private void initHeader() {
        txtSearch = DialogUtils.createSearchField("Tìm theo tên, SĐT, CCCD, quê quán...");
        JPanel headerPanel = DialogUtils.createHeaderPanel("👥 QUẢN LÝ NGƯỜI THUÊ PHÒNG", txtSearch);
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
        String[] cols = {"STT", "Mã Khách", "Họ tên", "Số ĐT", "CCCD", "Giới tính", "Quê quán", "Nghề nghiệp", "Phòng hiện tại"};
        Object[][] data = {
            {"1", "NT001", "Nguyễn Văn An", "0971234567", "034098001111", "Nam", "Thái Bình", "Lập trình viên", "Phòng 101"},
            {"2", "NT002", "Trần Thị Mai Phương", "0982345678", "036195002222", "Nữ", "Nam Định", "Kế toán viên", "Phòng 102"},
            {"3", "NT003", "Lê Quốc Bảo", "0963456789", "038096003333", "Nam", "Thanh Hóa", "Kỹ sư xây dựng", "Phòng 102"},
            {"4", "NT004", "Hoàng Thùy Linh", "0914567890", "040199004444", "Nữ", "Hải Phòng", "Sinh viên", "Phòng 201"}
        };
        tableModel = new DefaultTableModel(data, cols);
        table = DialogUtils.createStyledTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                txtMaNT.setText(tableModel.getValueAt(modelRow, 1).toString());
                txtHoTen.setText(tableModel.getValueAt(modelRow, 2).toString());
                txtSdt.setText(tableModel.getValueAt(modelRow, 3).toString());
                txtCccd.setText(tableModel.getValueAt(modelRow, 4).toString());
                cbGioiTinh.setSelectedItem(tableModel.getValueAt(modelRow, 5).toString());
                txtQueQuan.setText(tableModel.getValueAt(modelRow, 6).toString());
                txtNgheNghiep.setText(tableModel.getValueAt(modelRow, 7).toString());
                cbPhongHienTai.setSelectedItem(tableModel.getValueAt(modelRow, 8).toString());
                txtNgaySinh.setText("15/08/1998");
                txtEmail.setText("khachthue@gmail.com");
                txtGhiChu.setText("Đã nộp đầy đủ CCCD bản sao có công chứng và tạm trú");
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
            DialogUtils.createCardBorder("Hồ Sơ Người Thuê", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaNT = DialogUtils.createFormField();
        txtMaNT.setEditable(false);
        txtHoTen = DialogUtils.createFormField();
        txtSdt = DialogUtils.createFormField();
        txtCccd = DialogUtils.createFormField();
        txtNgaySinh = DialogUtils.createFormField();
        txtEmail = DialogUtils.createFormField();
        txtQueQuan = DialogUtils.createFormField();
        txtNgheNghiep = DialogUtils.createFormField();

        cbGioiTinh = DialogUtils.createStyledComboBox("Nam", "Nữ", "Khác");
        cbPhongHienTai = DialogUtils.createStyledComboBox("Chưa gán", "Phòng 101", "Phòng 102", "Phòng 103", "Phòng 201", "Phòng VIP 301");

        txtGhiChu = new JTextArea(2, 20);
        txtGhiChu.setFont(DialogUtils.BODY_FONT);
        txtGhiChu.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(DialogUtils.BORDER_COLOR), BorderFactory.createEmptyBorder(2, 8, 2, 8)));

        // Row 0
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Mã khách thuê:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtMaNT, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 0; formPanel.add(DialogUtils.createFormLabel("Họ và tên:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 0; formPanel.add(txtHoTen, gbc);

        // Row 1
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtSdt, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 1; formPanel.add(DialogUtils.createFormLabel("CCCD / Hộ chiếu:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 1; formPanel.add(txtCccd, gbc);

        // Row 2
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Ngày sinh:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(txtNgaySinh, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 2; formPanel.add(DialogUtils.createFormLabel("Giới tính:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 2; formPanel.add(cbGioiTinh, gbc);

        // Row 3
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Quê quán:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtQueQuan, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 3; formPanel.add(DialogUtils.createFormLabel("Nghề nghiệp:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 3; formPanel.add(txtNgheNghiep, gbc);

        // Row 4
        gbc.gridx = 0; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Phòng hiện tại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(cbPhongHienTai, gbc);
        gbc.gridx = 2; gbc.weightx = 0.1; gbc.gridy = 4; formPanel.add(DialogUtils.createFormLabel("Ghi chú / Tạm trú:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4; gbc.gridy = 4; formPanel.add(new JScrollPane(txtGhiChu), gbc);

        // Button bar
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setOpaque(false);

        JButton btnThem = DialogUtils.createStyledButton("➕ Thêm Khách", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("✏️ Cập Nhật", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("🗑️ Xóa", DialogUtils.DANGER);
        JButton btnLichSu = DialogUtils.createStyledButton("📜 Lịch Sử Thuê", DialogUtils.PRIMARY);
        JButton btnLamMoi = DialogUtils.createStyledButton("🔄 Làm Mới", DialogUtils.GRAY_BTN);

        btnThem.addActionListener(e -> DialogUtils.showSuccess(this, "Đã lưu hồ sơ người thuê mới!"));
        btnSua.addActionListener(e -> DialogUtils.showSuccess(this, "Đã cập nhật thông tin khách thuê!"));
        btnXoa.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Bạn có chắc muốn xóa hồ sơ khách thuê này?")) {
                DialogUtils.showSuccess(this, "Đã xóa khách thuê!");
            }
        });
        btnLichSu.addActionListener(e -> DialogUtils.showInfo(this, "Khách thuê đã ở 14 tháng, thanh toán đầy đủ, không vi phạm nội quy."));
        btnLamMoi.addActionListener(e -> clearForm());

        btnBar.add(btnThem);
        btnBar.add(btnSua);
        btnBar.add(btnXoa);
        btnBar.add(btnLichSu);
        btnBar.add(btnLamMoi);

        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(btnBar, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void clearForm() {
        txtMaNT.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtCccd.setText("");
        txtNgaySinh.setText("");
        txtEmail.setText("");
        txtQueQuan.setText("");
        txtNgheNghiep.setText("");
        txtGhiChu.setText("");
        cbGioiTinh.setSelectedIndex(0);
        cbPhongHienTai.setSelectedIndex(0);
        table.clearSelection();
    }
}
