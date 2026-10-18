package com.quanlydaytro.ui.quanlytaikhoan;

import com.quanlydaytro.model.NhanVien;
import com.quanlydaytro.service.NhanVienService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/**
 * Giao diện nghiệp vụ 2: quản lý nhân viên.
 */
public class NhanVienPanel extends JPanel {
    private final NhanVienService service = new NhanVienService();
    private final JTextField txtTimKiem = DialogUtils.createSearchField("Mã, họ tên, điện thoại, chức vụ...");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Mã nhân viên", "Họ và tên", "Số điện thoại", "Chức vụ"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(tableModel);

    public NhanVienPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel north = new JPanel(new BorderLayout(8, 10));
        north.setOpaque(false);
        north.add(DialogUtils.createHeaderPanel("👥 QUẢN LÝ NHÂN VIÊN", txtTimKiem), BorderLayout.NORTH);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        DialogUtils.styleToolbar(search);
        JButton btnTim = DialogUtils.createStyledButton("Tìm kiếm", DialogUtils.PRIMARY);
        JButton btnLamMoi = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN);
        search.add(btnTim);
        search.add(btnLamMoi);
        north.add(search, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(DialogUtils.createCardBorder(null, null));
        add(scroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        actions.setOpaque(false);
        JButton btnThem = DialogUtils.createStyledButton("Thêm nhân viên", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("Sửa thông tin", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("Xóa nhân viên", DialogUtils.DANGER);
        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        add(actions, BorderLayout.SOUTH);

        btnTim.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));
        txtTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDuLieu("");
        });
        btnThem.addActionListener(e -> hienThiForm(null));
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());
        taiDuLieu("");
    }

    private void taiDuLieu(String tuKhoa) {
        try {
            tableModel.setRowCount(0);
            for (NhanVien nv : service.timKiem(tuKhoa)) {
                tableModel.addRow(new Object[]{nv.getMaNhanVien(), nv.getHoTen(), nv.getSoDienThoai(), nv.getChucVu()});
            }
        } catch (Exception ex) {
            baoLoi("Không tải được danh sách nhân viên", ex);
        }
    }

    private void suaNhanVien() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            thongBao("Vui lòng chọn nhân viên cần sửa.");
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);
        int ma = ((Number) tableModel.getValueAt(row, 0)).intValue();
        try {
            service.timTheoMa(ma).ifPresentOrElse(this::hienThiForm,
                    () -> thongBao("Nhân viên không còn tồn tại. Vui lòng làm mới danh sách."));
        } catch (Exception ex) {
            baoLoi("Không lấy được thông tin nhân viên", ex);
        }
    }

    private void hienThiForm(NhanVien duLieu) {
        boolean dangSua = duLieu != null;
        JTextField hoTen = new JTextField(dangSua ? duLieu.getHoTen() : "", 22);
        JTextField soDienThoai = new JTextField(dangSua ? duLieu.getSoDienThoai() : "", 22);
        JTextField chucVu = new JTextField(dangSua ? duLieu.getChucVu() : "", 22);
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.add(new JLabel("Họ và tên:"));
        form.add(hoTen);
        form.add(new JLabel("Số điện thoại:"));
        form.add(soDienThoai);
        form.add(new JLabel("Chức vụ:"));
        form.add(chucVu);
        int result = JOptionPane.showConfirmDialog(this, form,
                dangSua ? "Cập nhật nhân viên" : "Thêm nhân viên",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        NhanVien nv = new NhanVien();
        nv.setHoTen(hoTen.getText());
        nv.setSoDienThoai(soDienThoai.getText());
        nv.setChucVu(chucVu.getText());
        try {
            if (dangSua) service.capNhat(duLieu.getMaNhanVien(), nv);
            else service.them(nv);
            thongBao(dangSua ? "Cập nhật nhân viên thành công." : "Thêm nhân viên thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (Exception ex) {
            baoLoi("Không thể lưu thông tin nhân viên", ex);
        }
    }

    private void xoaNhanVien() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            thongBao("Vui lòng chọn nhân viên cần xóa.");
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);
        int ma = ((Number) tableModel.getValueAt(row, 0)).intValue();
        String ten = String.valueOf(tableModel.getValueAt(row, 1));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhân viên '" + ten + "' (mã " + ma + ")?\n"
                        + "Chỉ xóa được khi nhân viên chưa gắn tài khoản và chưa được phân công dãy trọ.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            if (service.xoa(ma)) thongBao("Đã xóa nhân viên.");
            else thongBao("Nhân viên không còn tồn tại.");
            taiDuLieu(txtTimKiem.getText());
        } catch (Exception ex) {
            baoLoi("Không thể xóa nhân viên", ex);
        }
    }

    private void thongBao(String noiDung) {
        JOptionPane.showMessageDialog(this, noiDung, "Quản lý nhân viên", JOptionPane.INFORMATION_MESSAGE);
    }

    private void baoLoi(String noiDung, Exception ex) {
        JOptionPane.showMessageDialog(this, noiDung + ":\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
