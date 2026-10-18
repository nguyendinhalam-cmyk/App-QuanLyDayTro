package com.quanlydaytro.ui.quanlychuyenphong;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChuyenPhongPanel extends JPanel {
    private JComboBox<String> cbPhongCu, cbPhongMoi;
    private JTextField txtKhachThue, txtTienCocCu, txtGiaThueCu, txtCongNoCu;
    private JTextField txtDienTichMoi, txtGiaThueMoi, txtTienCocMoi, txtChenhLechCoc, txtNgayChuyen;
    private JTable tableLichSu;
    private DefaultTableModel modelLichSu;

    public ChuyenPhongPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = DialogUtils.createHeaderPanel("🔄 QUẢN LÝ CHUYỂN PHÒNG & ĐIỀU CHUYỂN HỢP ĐỒNG", null);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 15, 0));
        center.setBackground(DialogUtils.BG_CONTENT);
        center.add(createLeftPhongCu());
        center.add(createRightPhongMoi());

        add(center, BorderLayout.CENTER);
        initSouthHistory();
    }

    private JPanel createLeftPhongCu() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("1. Phòng Hiện Tại (Chuyển Đi)", DialogUtils.DANGER),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBackground(Color.WHITE);

        cbPhongCu = DialogUtils.createStyledComboBox("Phòng 101 (Dãy A)", "Phòng 102 (Dãy A)", "Phòng 201 (Dãy B)");
        txtKhachThue = DialogUtils.createFormField(); txtKhachThue.setText("Nguyễn Văn An"); txtKhachThue.setEditable(false);
        txtTienCocCu = DialogUtils.createFormField(); txtTienCocCu.setText("2,800,000"); txtTienCocCu.setEditable(false);
        txtGiaThueCu = DialogUtils.createFormField(); txtGiaThueCu.setText("2,800,000"); txtGiaThueCu.setEditable(false);
        txtCongNoCu = DialogUtils.createFormField(); txtCongNoCu.setText("0 VNĐ (Không nợ)"); txtCongNoCu.setEditable(false);
        txtCongNoCu.setForeground(DialogUtils.SUCCESS);

        form.add(DialogUtils.createFormLabel("Chọn phòng hiện tại:")); form.add(cbPhongCu);
        form.add(DialogUtils.createFormLabel("Người đại diện ở:")); form.add(txtKhachThue);
        form.add(DialogUtils.createFormLabel("Tiền cọc phòng cũ:")); form.add(txtTienCocCu);
        form.add(DialogUtils.createFormLabel("Giá thuê phòng cũ:")); form.add(txtGiaThueCu);
        form.add(DialogUtils.createFormLabel("Tình trạng công nợ:")); form.add(txtCongNoCu);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRightPhongMoi() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("2. Phòng Mới (Chuyển Đến)", DialogUtils.SUCCESS),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBackground(Color.WHITE);

        cbPhongMoi = DialogUtils.createStyledComboBox("Phòng 103 (Trống - Dãy A)", "Phòng VIP 301 (Trống - Dãy C)");
        txtDienTichMoi = DialogUtils.createFormField(); txtDienTichMoi.setText("35 m² - Ban công"); txtDienTichMoi.setEditable(false);
        txtGiaThueMoi = DialogUtils.createFormField(); txtGiaThueMoi.setText("5,500,000 VNĐ"); txtGiaThueMoi.setEditable(false);
        txtTienCocMoi = DialogUtils.createFormField(); txtTienCocMoi.setText("5,500,000 VNĐ"); txtTienCocMoi.setEditable(false);
        txtChenhLechCoc = DialogUtils.createFormField(); txtChenhLechCoc.setText("+ 2,700,000 VNĐ (Khách nộp thêm)"); txtChenhLechCoc.setEditable(false);
        txtChenhLechCoc.setForeground(DialogUtils.WARNING);
        txtChenhLechCoc.setFont(new Font(DialogUtils.FONT_FAMILY, Font.BOLD, 13));

        form.add(DialogUtils.createFormLabel("Chọn phòng mới:")); form.add(cbPhongMoi);
        form.add(DialogUtils.createFormLabel("Đặc điểm phòng:")); form.add(txtDienTichMoi);
        form.add(DialogUtils.createFormLabel("Giá thuê phòng mới:")); form.add(txtGiaThueMoi);
        form.add(DialogUtils.createFormLabel("Tiền cọc yêu cầu:")); form.add(txtTienCocMoi);
        form.add(DialogUtils.createFormLabel("Chênh lệch cọc:")); form.add(txtChenhLechCoc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnRow.setBackground(Color.WHITE);
        JButton btnKiemTra = DialogUtils.createStyledButton("🔍 Kiểm Tra Đối Soát", DialogUtils.PRIMARY);
        JButton btnXacNhanChuyen = DialogUtils.createStyledButton("🔄 Xác Nhận Chuyển", DialogUtils.SUCCESS);

        btnKiemTra.addActionListener(e -> DialogUtils.showInfo(this, "Phòng cũ không có công nợ tồn đọng. Chênh lệch cọc cần thu thêm: 2,700,000 VNĐ"));
        btnXacNhanChuyen.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Hệ thống sẽ thanh lý hợp đồng cũ và tự động lập hợp đồng mới cho Phòng VIP 301. Tiếp tục?")) {
                DialogUtils.showSuccess(this, "Đã hoàn tất thủ tục chuyển phòng thành công!");
            }
        });

        btnRow.add(btnKiemTra);
        btnRow.add(btnXacNhanChuyen);

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow, BorderLayout.SOUTH);
        return panel;
    }

    private void initSouthHistory() {
        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setPreferredSize(new Dimension(0, 180));
        south.setBackground(Color.WHITE);
        south.setBorder(BorderFactory.createCompoundBorder(
            DialogUtils.createCardBorder("Lịch Sử Chuyển Phòng", DialogUtils.PRIMARY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        String[] cols = {"Mã Chuyển", "Khách Thuê", "Phòng Cũ", "Phòng Mới", "Ngày Chuyển", "Chênh Lệch Cọc", "Người Thực Hiện"};
        Object[][] data = {
            {"CP001", "Lê Quốc Bảo", "Phòng 101", "Phòng 102", "15/05/2024", "+ 700,000 VNĐ", "Admin"},
            {"CP002", "Vũ Hoàng My", "Phòng 202", "Phòng 201", "20/07/2024", "0 VNĐ", "Admin"}
        };
        modelLichSu = new DefaultTableModel(data, cols);
        tableLichSu = DialogUtils.createStyledTable(modelLichSu);
        south.add(new JScrollPane(tableLichSu), BorderLayout.CENTER);

        add(south, BorderLayout.SOUTH);
    }
}
