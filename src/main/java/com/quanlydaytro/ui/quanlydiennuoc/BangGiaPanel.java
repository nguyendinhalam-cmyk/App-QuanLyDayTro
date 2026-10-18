package com.quanlydaytro.ui.quanlydiennuoc;

import com.quanlydaytro.model.GiaDien;
import com.quanlydaytro.model.GiaNuoc;
import com.quanlydaytro.service.BangGiaService;
import com.quanlydaytro.service.GiaDienService;
import com.quanlydaytro.service.GiaNuocService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Giao diện quản lý giá điện và giá nước, bao gồm lịch sử đơn giá áp dụng.
 */
public class BangGiaPanel extends JPanel {
    private final BangGiaService service = new BangGiaService();
    private final DefaultTableModel dienModel = modelBang();
    private final DefaultTableModel nuocModel = modelBang();
    private final JTable dienTable = new JTable(dienModel);
    private final JTable nuocTable = new JTable(nuocModel);
    private final JTextField dienSearch = new JTextField(18);
    private final JTextField nuocSearch = new JTextField(18);

    public BangGiaPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(14, 14, 14, 14));
        JLabel title = new JLabel("QUẢN LÝ GIÁ ĐIỆN, NƯỚC", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 19f));
        add(title, BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Giá điện", taoTab(true));
        tabs.addTab("Giá nước", taoTab(false));
        add(tabs, BorderLayout.CENTER);
        taiDien("");
        taiNuoc("");
    }

    private DefaultTableModel modelBang() {
        return new DefaultTableModel(new Object[]{"Mã giá", "Đơn giá (VNĐ)", "Ngày áp dụng"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel taoTab(boolean laDien) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel tim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField search = laDien ? dienSearch : nuocSearch;
        tim.add(new JLabel("Tìm theo mã, đơn giá hoặc ngày (yyyy-MM-dd):"));
        tim.add(search);
        JButton btnTim = new JButton("Tìm kiếm"), btnBoLoc = new JButton("Bỏ lọc");
        tim.add(btnTim);
        tim.add(btnBoLoc);
        panel.add(tim, BorderLayout.NORTH);
        JTable table = laDien ? dienTable : nuocTable;
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton them = new JButton("Thêm đơn giá"), sua = new JButton("Sửa đơn giá"), xoa = new JButton("Xóa đơn giá"), taiLai = new JButton("Làm mới");
        actions.add(them);
        actions.add(sua);
        actions.add(xoa);
        actions.add(taiLai);
        panel.add(actions, BorderLayout.SOUTH);
        btnTim.addActionListener(e -> tai(laDien, search.getText()));
        search.addActionListener(e -> tai(laDien, search.getText()));
        btnBoLoc.addActionListener(e -> {
            search.setText("");
            tai(laDien, "");
        });
        taiLai.addActionListener(e -> {
            search.setText("");
            tai(laDien, "");
        });
        them.addActionListener(e -> hienThiForm(laDien, null));
        sua.addActionListener(e -> sua(laDien));
        xoa.addActionListener(e -> xoa(laDien));
        return panel;
    }

    private void tai(boolean laDien, String key) {
        if (laDien) taiDien(key);
        else taiNuoc(key);
    }

    private void taiDien(String key) {
        try {
            dienModel.setRowCount(0);
            for (GiaDien g : service.getGiaDienService().timKiem(key))
                dienModel.addRow(new Object[]{g.getMaGiaDien(), g.getDonGia(), g.getNgayApDung()});
        } catch (SQLException ex) {
            baoLoi("Không tải được bảng giá điện", ex);
        }
    }

    private void taiNuoc(String key) {
        try {
            nuocModel.setRowCount(0);
            for (GiaNuoc g : service.getGiaNuocService().timKiem(key))
                nuocModel.addRow(new Object[]{g.getMaGiaNuoc(), g.getDonGia(), g.getNgayApDung()});
        } catch (SQLException ex) {
            baoLoi("Không tải được bảng giá nước", ex);
        }
    }

    private void sua(boolean laDien) {
        JTable table = laDien ? dienTable : nuocTable;
        int vr = table.getSelectedRow();
        if (vr < 0) {
            thongBao("Vui lòng chọn đơn giá cần sửa.");
            return;
        }
        int row = table.convertRowIndexToModel(vr);
        int ma = ((Number) table.getModel().getValueAt(row, 0)).intValue();
        try {
            if (laDien) {
                GiaDien g = service.getGiaDienService().timTheoMa(ma).orElse(null);
                if (g != null) hienThiForm(true, g);
            } else {
                GiaNuoc g = service.getGiaNuocService().timTheoMa(ma).orElse(null);
                if (g != null) hienThiForm(false, g);
            }
        } catch (SQLException ex) {
            baoLoi("Không đọc được đơn giá đã chọn", ex);
        }
    }

    private void hienThiForm(boolean laDien, Object duLieu) {
        boolean sua = duLieu != null;
        BigDecimal giaHienTai = sua ? (laDien ? ((GiaDien) duLieu).getDonGia() : ((GiaNuoc) duLieu).getDonGia()) : null;
        LocalDate ngayHienTai = sua ? (laDien ? ((GiaDien) duLieu).getNgayApDung() : ((GiaNuoc) duLieu).getNgayApDung()) : null;
        JTextField gia = new JTextField(giaHienTai == null ? "" : giaHienTai.toPlainString(), 18);
        JTextField ngay = new JTextField(ngayHienTai == null ? LocalDate.now().toString() : ngayHienTai.toString(), 18);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 9));
        form.add(new JLabel("Đơn giá (VNĐ, > 0):"));
        form.add(gia);
        form.add(new JLabel("Ngày áp dụng (yyyy-MM-dd):"));
        form.add(ngay);
        int result = JOptionPane.showConfirmDialog(this, form, (laDien ? "Giá điện" : "Giá nước") + (sua ? " - Cập nhật" : " - Thêm mới"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        try {
            BigDecimal donGia = new BigDecimal(gia.getText().trim());
            LocalDate ngayApDung = LocalDate.parse(ngay.getText().trim());
            if (laDien) {
                GiaDien entity = new GiaDien(sua ? ((GiaDien) duLieu).getMaGiaDien() : 0, donGia, ngayApDung);
                if (sua) service.getGiaDienService().capNhat(entity.getMaGiaDien(), entity);
                else service.getGiaDienService().them(entity);
            } else {
                GiaNuoc entity = new GiaNuoc(sua ? ((GiaNuoc) duLieu).getMaGiaNuoc() : 0, donGia, ngayApDung);
                if (sua) service.getGiaNuocService().capNhat(entity.getMaGiaNuoc(), entity);
                else service.getGiaNuocService().them(entity);
            }
            thongBao(sua ? "Cập nhật đơn giá thành công." : "Thêm đơn giá thành công.");
            tai(laDien, (laDien ? dienSearch : nuocSearch).getText());
        } catch (NumberFormatException ex) {
            thongBao("Đơn giá phải là số hợp lệ, ví dụ: 3500 hoặc 3500.50.");
        } catch (DateTimeParseException ex) {
            thongBao("Ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd, ví dụ 2026-10-01.");
        } catch (IllegalArgumentException ex) {
            thongBao(ex.getMessage());
        } catch (Exception ex) {
            baoLoi("Không thể lưu đơn giá", ex);
        }
    }

    private void xoa(boolean laDien) {
        JTable table = laDien ? dienTable : nuocTable;
        int vr = table.getSelectedRow();
        if (vr < 0) {
            thongBao("Vui lòng chọn đơn giá cần xóa.");
            return;
        }
        int row = table.convertRowIndexToModel(vr);
        int ma = ((Number) table.getModel().getValueAt(row, 0)).intValue();
        String ngay = String.valueOf(table.getModel().getValueAt(row, 2));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa đơn giá ngày " + ngay + "?\nNên giữ lại các mức giá đã được dùng để đối chiếu lịch sử hóa đơn.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            boolean ok = laDien ? service.getGiaDienService().xoa(ma) : service.getGiaNuocService().xoa(ma);
            thongBao(ok ? "Đã xóa đơn giá." : "Đơn giá không còn tồn tại.");
            tai(laDien, (laDien ? dienSearch : nuocSearch).getText());
        } catch (Exception ex) {
            baoLoi("Không thể xóa đơn giá", ex);
        }
    }

    private void thongBao(String text) {
        JOptionPane.showMessageDialog(this, text, "Quản lý giá điện, nước", JOptionPane.INFORMATION_MESSAGE);
    }

    private void baoLoi(String text, Exception ex) {
        String detail = ex.getMessage() == null ? ex.toString() : ex.getMessage();
        JOptionPane.showMessageDialog(this, text + ":\n" + detail, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
