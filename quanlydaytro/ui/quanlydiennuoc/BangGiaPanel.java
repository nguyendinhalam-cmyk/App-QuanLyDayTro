package com.quanlydaytro.ui.quanlydiennuoc;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BangGiaPanel extends JPanel {
    private JTable tableDien, tableNuoc;
    private DefaultTableModel modelDien, modelNuoc;
    private JTextField txtBacDien, txtTuDien, txtDenDien, txtDonGiaDien, txtNgayApDungDien;
    private JTextField txtBacNuoc, txtTuNuoc, txtDenNuoc, txtDonGiaNuoc, txtNgayApDungNuoc;

    public BangGiaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = DialogUtils.createHeaderPanel("⚡ QUẢN LÝ ĐƠN GIÁ ĐIỆN & NƯỚC", null);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(DialogUtils.HEADER_FONT);
        tabbedPane.addTab("⚡ Biểu Phí Điện", createTabDien());
        tabbedPane.addTab("💧 Biểu Phí Nước", createTabNuoc());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTabDien() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DialogUtils.BG_CONTENT);

        String[] cols = {"STT", "Mã Biểu Phí", "Tên Mức / Bậc", "Từ (kWh)", "Đến (kWh)", "Đơn Giá (VNĐ/kWh)", "Ngày Áp Dụng", "Trạng Thái"};
        Object[][] data = {
            {"1", "BD01", "Bậc 1: Sinh hoạt cơ bản", "0", "50", "1,800", "01/01/2024", "Đang áp dụng"},
            {"2", "BD02", "Bậc 2: Tiêu dùng chuẩn", "51", "100", "2,300", "01/01/2024", "Đang áp dụng"},
            {"3", "BD03", "Bậc 3: Tiêu dùng cao", "101", "200", "2,800", "01/01/2024", "Đang áp dụng"},
            {"4", "BD04", "Bậc 4: Tiêu dùng vượt trần", "201", "9999", "3,500", "01/01/2024", "Đang áp dụng"},
            {"5", "BD00", "Đồng giá phòng dịch vụ", "0", "9999", "3,500", "01/01/2024", "Đang áp dụng"}
        };
        modelDien = new DefaultTableModel(data, cols);
        tableDien = DialogUtils.createStyledTable(modelDien);

        panel.add(new JScrollPane(tableDien), BorderLayout.CENTER);

        // Form
        JPanel south = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Cấu Hình Bảng Giá Điện", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        txtBacDien = DialogUtils.createFormField();
        txtTuDien = DialogUtils.createFormField();
        txtDenDien = DialogUtils.createFormField();
        txtDonGiaDien = DialogUtils.createFormField();
        txtNgayApDungDien = DialogUtils.createFormField();
        txtNgayApDungDien.setText("01/01/2024");

        form.add(DialogUtils.createFormLabel("Tên bậc:")); form.add(txtBacDien);
        form.add(DialogUtils.createFormLabel("Từ số (kWh):")); form.add(txtTuDien);
        form.add(DialogUtils.createFormLabel("Đến số (kWh):")); form.add(txtDenDien);
        form.add(DialogUtils.createFormLabel("Đơn giá:")); form.add(txtDonGiaDien);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnBar.setOpaque(false);
        JButton btnAdd = DialogUtils.createStyledButton("➕ Cập Nhật Đơn Giá", DialogUtils.SUCCESS);
        JButton btnLuu = DialogUtils.createStyledButton("💾 Lưu Thay Đổi", DialogUtils.PRIMARY);
        btnAdd.addActionListener(e -> DialogUtils.showSuccess(this, "Đã lưu mức giá điện mới!"));
        btnLuu.addActionListener(e -> DialogUtils.showSuccess(this, "Hệ thống đã cập nhật biểu phí điện!"));
        btnBar.add(btnAdd);
        btnBar.add(btnLuu);

        south.add(form, BorderLayout.CENTER);
        south.add(btnBar, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTabNuoc() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DialogUtils.BG_CONTENT);

        String[] cols = {"STT", "Mã Biểu Phí", "Đối Tượng / Bậc", "Từ (m³)", "Đến (m³)", "Đơn Giá (VNĐ/m³)", "Ngày Áp Dụng", "Trạng Thái"};
        Object[][] data = {
            {"1", "BN01", "Nước sinh hoạt mức 1", "0", "10", "12,000", "01/01/2024", "Đang áp dụng"},
            {"2", "BN02", "Nước sinh hoạt mức 2", "11", "20", "16,000", "01/01/2024", "Đang áp dụng"},
            {"3", "BN03", "Nước vượt định mức", "21", "9999", "22,000", "01/01/2024", "Đang áp dụng"},
            {"4", "BN00", "Thu khoán theo người", "0", "0", "100,000 VNĐ/người", "01/01/2024", "Đang áp dụng"}
        };
        modelNuoc = new DefaultTableModel(data, cols);
        tableNuoc = DialogUtils.createStyledTable(modelNuoc);

        panel.add(new JScrollPane(tableNuoc), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Cấu Hình Bảng Giá Nước", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        txtBacNuoc = DialogUtils.createFormField();
        txtTuNuoc = DialogUtils.createFormField();
        txtDenNuoc = DialogUtils.createFormField();
        txtDonGiaNuoc = DialogUtils.createFormField();

        form.add(DialogUtils.createFormLabel("Tên bậc:")); form.add(txtBacNuoc);
        form.add(DialogUtils.createFormLabel("Từ m³:")); form.add(txtTuNuoc);
        form.add(DialogUtils.createFormLabel("Đến m³:")); form.add(txtDenNuoc);
        form.add(DialogUtils.createFormLabel("Đơn giá:")); form.add(txtDonGiaNuoc);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnBar.setOpaque(false);
        JButton btnAdd = DialogUtils.createStyledButton("➕ Cập Nhật Đơn Giá", DialogUtils.SUCCESS);
        JButton btnLuu = DialogUtils.createStyledButton("💾 Lưu Thay Đổi", DialogUtils.PRIMARY);
        btnAdd.addActionListener(e -> DialogUtils.showSuccess(this, "Đã lưu mức giá nước mới!"));
        btnLuu.addActionListener(e -> DialogUtils.showSuccess(this, "Hệ thống đã cập nhật biểu phí nước!"));
        btnBar.add(btnAdd);
        btnBar.add(btnLuu);

        south.add(form, BorderLayout.CENTER);
        south.add(btnBar, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }
}
