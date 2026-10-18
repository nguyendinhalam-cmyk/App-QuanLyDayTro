package com.quanlydaytro.ui.quanlydiennuoc;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChiSoPanel extends JPanel {
    private JTable tableDien, tableNuoc;
    private DefaultTableModel modelDien, modelNuoc;
    private JComboBox<String> cbThang, cbNam, cbDayTro;
    private JTextField txtPhong, txtDienDau, txtDienCuoi, txtDienTieuThu;
    private JTextField txtNuocDau, txtNuocCuoi, txtNuocTieuThu;

    public ChiSoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initCenterTabs();
        initSouthForm();
    }

    private void initHeader() {
        JPanel headerContainer = new JPanel(new BorderLayout(0, 10));
        headerContainer.setOpaque(false);
        JPanel topHeader = DialogUtils.createHeaderPanel("📊 GHI NHẬN CHỈ SỐ ĐIỆN & NƯỚC HÀNG THÁNG", null);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        DialogUtils.styleToolbar(filterBar);

        filterBar.add(DialogUtils.createFormLabel("Kỳ hóa đơn:"));
        cbThang = DialogUtils.createStyledComboBox("Tháng 10", "Tháng 09", "Tháng 08", "Tháng 07", "Tháng 06");
        filterBar.add(cbThang);

        cbNam = DialogUtils.createStyledComboBox("2024", "2025", "2026");
        filterBar.add(cbNam);

        filterBar.add(DialogUtils.createFormLabel("Dãy trọ:"));
        cbDayTro = DialogUtils.createStyledComboBox("Tất cả", "Dãy A - Khu Đông", "Dãy B - Khu Tây", "Dãy C - VIP");
        filterBar.add(cbDayTro);

        JButton btnFilter = DialogUtils.createStyledButton("🔍 Lọc Danh Sách", DialogUtils.PRIMARY);
        btnFilter.addActionListener(e -> DialogUtils.showInfo(this, "Đã tải bảng chỉ số kỳ " + cbThang.getSelectedItem() + "/" + cbNam.getSelectedItem()));
        filterBar.add(btnFilter);

        headerContainer.add(topHeader, BorderLayout.NORTH);
        headerContainer.add(filterBar, BorderLayout.SOUTH);
        add(headerContainer, BorderLayout.NORTH);
    }

    private void initCenterTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(DialogUtils.HEADER_FONT);

        // Tab Dien
        String[] colsDien = {"STT", "Phòng", "Dãy Trọ", "Chỉ Số Đầu (kWh)", "Chỉ Số Cuối (kWh)", "Tiêu Thụ (kWh)", "Đơn Giá TB", "Thành Tiền (VNĐ)", "Trạng Thái"};
        Object[][] dataDien = {
            {"1", "Phòng 101", "Dãy A", "1250", "1380", "130", "3,500", "455,000", "Đã chốt"},
            {"2", "Phòng 102", "Dãy A", "2100", "2295", "195", "3,500", "682,500", "Đã chốt"},
            {"3", "Phòng 103", "Dãy A", "850", "850", "0", "3,500", "0", "Phòng trống"},
            {"4", "Phòng 201", "Dãy B", "3120", "3340", "220", "3,500", "770,000", "Chờ chốt"}
        };
        modelDien = new DefaultTableModel(dataDien, colsDien);
        tableDien = DialogUtils.createStyledTable(modelDien);

        // Tab Nuoc
        String[] colsNuoc = {"STT", "Phòng", "Dãy Trọ", "Chỉ Số Đầu (m³)", "Chỉ Số Cuối (m³)", "Tiêu Thụ (m³)", "Đơn Giá TB", "Thành Tiền (VNĐ)", "Trạng Thái"};
        Object[][] dataNuoc = {
            {"1", "Phòng 101", "Dãy A", "45", "53", "8", "16,000", "128,000", "Đã chốt"},
            {"2", "Phòng 102", "Dãy A", "80", "94", "14", "16,000", "224,000", "Đã chốt"},
            {"3", "Phòng 103", "Dãy A", "12", "12", "0", "16,000", "0", "Phòng trống"},
            {"4", "Phòng 201", "Dãy B", "110", "125", "15", "16,000", "240,000", "Chờ chốt"}
        };
        modelNuoc = new DefaultTableModel(dataNuoc, colsNuoc);
        tableNuoc = DialogUtils.createStyledTable(modelNuoc);

        tabs.addTab("⚡ Chỉ Số Điện", new JScrollPane(tableDien));
        tabs.addTab("💧 Chỉ Số Nước", new JScrollPane(tableNuoc));

        add(tabs, BorderLayout.CENTER);
    }

    private void initSouthForm() {
        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setBackground(DialogUtils.BG_CONTENT);

        JPanel form = new JPanel(new GridLayout(2, 6, 8, 8));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Nhập Đồng Hồ Nhanh Cho Phòng", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        txtPhong = DialogUtils.createFormField();
        txtDienDau = DialogUtils.createFormField();
        txtDienCuoi = DialogUtils.createFormField();
        txtDienTieuThu = DialogUtils.createFormField();
        txtDienTieuThu.setEditable(false);

        txtNuocDau = DialogUtils.createFormField();
        txtNuocCuoi = DialogUtils.createFormField();
        txtNuocTieuThu = DialogUtils.createFormField();
        txtNuocTieuThu.setEditable(false);

        // Listener auto calculate
        txtDienCuoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                try {
                    int dau = Integer.parseInt(txtDienDau.getText().trim());
                    int cuoi = Integer.parseInt(txtDienCuoi.getText().trim());
                    txtDienTieuThu.setText(String.valueOf(Math.max(0, cuoi - dau)));
                } catch (Exception ex) {}
            }
        });

        txtNuocCuoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                try {
                    int dau = Integer.parseInt(txtNuocDau.getText().trim());
                    int cuoi = Integer.parseInt(txtNuocCuoi.getText().trim());
                    txtNuocTieuThu.setText(String.valueOf(Math.max(0, cuoi - dau)));
                } catch (Exception ex) {}
            }
        });

        form.add(DialogUtils.createFormLabel("Chọn phòng:")); form.add(txtPhong);
        form.add(DialogUtils.createFormLabel("Điện đầu (kWh):")); form.add(txtDienDau);
        form.add(DialogUtils.createFormLabel("Điện cuối (kWh):")); form.add(txtDienCuoi);

        form.add(DialogUtils.createFormLabel("Tiêu thụ điện:")); form.add(txtDienTieuThu);
        form.add(DialogUtils.createFormLabel("Nước đầu (m³):")); form.add(txtNuocDau);
        form.add(DialogUtils.createFormLabel("Nước cuối (m³):")); form.add(txtNuocCuoi);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnBar.setOpaque(false);

        JButton btnLuu = DialogUtils.createStyledButton("💾 Ghi Chỉ Số", DialogUtils.SUCCESS);
        JButton btnChotAll = DialogUtils.createStyledButton("🔒 Chốt Toàn Bộ Dãy", DialogUtils.PRIMARY);
        btnLuu.addActionListener(e -> DialogUtils.showSuccess(this, "Đã ghi nhận chỉ số điện nước!"));
        btnChotAll.addActionListener(e -> DialogUtils.showSuccess(this, "Đã khóa số liệu để sẵn sàng tính tiền lập hóa đơn!"));

        btnBar.add(btnLuu);
        btnBar.add(btnChotAll);

        south.add(form, BorderLayout.CENTER);
        south.add(btnBar, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }
}
