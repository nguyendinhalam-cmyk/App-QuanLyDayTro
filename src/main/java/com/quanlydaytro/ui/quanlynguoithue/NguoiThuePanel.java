package com.quanlydaytro.ui.quanlynguoithue;

import com.quanlydaytro.model.NguoiThue;
import com.quanlydaytro.service.NguoiThueService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

/**
 * Giao diện quản lý người thuê: xem, tìm kiếm, thêm, sửa, xóa và làm mới.
 */
public class NguoiThuePanel extends JPanel {
    private final NguoiThueService service = new NguoiThueService();
    private final JTextField txtTimKiem = new JTextField(24);
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã người thuê", "Họ tên", "CCCD", "Số điện thoại", "Địa chỉ thường trú"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public NguoiThuePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        JLabel title = new JLabel("QUẢN LÝ NGƯỜI THUÊ", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 19f));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Tìm theo mã, họ tên, CCCD, số điện thoại hoặc địa chỉ:"));
        search.add(txtTimKiem);
        JButton btnTim = new JButton("Tìm kiếm"), btnBoLoc = new JButton("Bỏ lọc");
        search.add(btnTim);
        search.add(btnBoLoc);
        content.add(search, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        content.add(new JScrollPane(table), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThem = new JButton("Thêm người thuê"), btnSua = new JButton("Sửa thông tin"),
                btnXoa = new JButton("Xóa người thuê"), btnTaiLai = new JButton("Làm mới");
        buttons.add(btnThem);
        buttons.add(btnSua);
        buttons.add(btnXoa);
        buttons.add(btnTaiLai);
        add(buttons, BorderLayout.SOUTH);

        btnTim.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));
        txtTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));
        btnBoLoc.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDuLieu("");
        });
        btnThem.addActionListener(e -> hienThiForm(null));
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnTaiLai.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDuLieu("");
        });
        taiDuLieu("");
    }

    private void taiDuLieu(String tuKhoa) {
        try {
            model.setRowCount(0);
            for (NguoiThue nt : service.timKiem(tuKhoa)) {
                model.addRow(new Object[]{nt.getMaNguoiThue(), nt.getHoTen(), nt.getCCCD(),
                        nt.getSoDienThoai(), nt.getDiaChiThuongTru() == null ? "" : nt.getDiaChiThuongTru()});
            }
        } catch (SQLException ex) {
            baoLoi("Không tải được danh sách người thuê", ex);
        }
    }

    private void sua() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            thongBao("Vui lòng chọn người thuê cần sửa.");
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);
        NguoiThue nt = new NguoiThue(((Number) model.getValueAt(row, 0)).intValue(),
                String.valueOf(model.getValueAt(row, 1)), String.valueOf(model.getValueAt(row, 2)),
                String.valueOf(model.getValueAt(row, 3)), String.valueOf(model.getValueAt(row, 4)));
        hienThiForm(nt);
    }

    private void hienThiForm(NguoiThue duLieu) {
        boolean sua = duLieu != null;
        JTextField hoTen = new JTextField(sua ? duLieu.getHoTen() : "", 22);
        JTextField cccd = new JTextField(sua ? duLieu.getCCCD() : "", 22);
        JTextField sdt = new JTextField(sua ? duLieu.getSoDienThoai() : "", 22);
        JTextField diaChi = new JTextField(sua && duLieu.getDiaChiThuongTru() != null ? duLieu.getDiaChiThuongTru() : "", 22);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 9));
        form.add(new JLabel("Họ và tên (*):"));
        form.add(hoTen);
        form.add(new JLabel("CCCD (9 hoặc 12 số) (*):"));
        form.add(cccd);
        form.add(new JLabel("Số điện thoại (*):"));
        form.add(sdt);
        form.add(new JLabel("Địa chỉ thường trú:"));
        form.add(diaChi);
        int result = JOptionPane.showConfirmDialog(this, form, sua ? "Cập nhật người thuê" : "Thêm người thuê",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        NguoiThue nt = new NguoiThue(sua ? duLieu.getMaNguoiThue() : 0,
                hoTen.getText(), cccd.getText(), sdt.getText(), diaChi.getText());
        try {
            if (sua) service.capNhat(duLieu.getMaNguoiThue(), nt);
            else service.them(nt);
            thongBao(sua ? "Cập nhật người thuê thành công." : "Thêm người thuê thành công.");
            taiDuLieu(txtTimKiem.getText());
        } catch (Exception ex) {
            baoLoi("Không thể lưu thông tin người thuê", ex);
        }
    }

    private void xoa() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            thongBao("Vui lòng chọn người thuê cần xóa.");
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);
        int ma = ((Number) model.getValueAt(row, 0)).intValue();
        String ten = String.valueOf(model.getValueAt(row, 1));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa người thuê '" + ten + "' (mã " + ma + ")?\nNgười thuê đã có hợp đồng sẽ không thể xóa.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            if (service.xoa(ma)) thongBao("Đã xóa người thuê.");
            else thongBao("Người thuê không còn tồn tại.");
            taiDuLieu(txtTimKiem.getText());
        } catch (Exception ex) {
            baoLoi("Không thể xóa người thuê", ex);
        }
    }

    private void thongBao(String noiDung) {
        JOptionPane.showMessageDialog(this, noiDung, "Quản lý người thuê", JOptionPane.INFORMATION_MESSAGE);
    }

    private void baoLoi(String thongDiep, Exception ex) {
        String detail = ex.getMessage() == null ? ex.toString() : ex.getMessage();
        JOptionPane.showMessageDialog(this, thongDiep + ":\n" + detail, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
