package com.quanlydaytro.ui.quanlynguoithue;

import com.quanlydaytro.model.NguoiThue;
import com.quanlydaytro.service.NguoiThueService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

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
    private final JTextField txtTimKiem = DialogUtils.createSearchField("Tìm theo mã, họ tên, CCCD, điện thoại, địa chỉ...");
    private final JTextField txtHoTen = DialogUtils.createFormField();
    private final JTextField txtCCCD = DialogUtils.createFormField();
    private final JTextField txtSDT = DialogUtils.createFormField();
    private final JTextField txtDiaChi = DialogUtils.createFormField();
    private int maDangChon;
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã người thuê", "Họ tên", "CCCD", "Số điện thoại", "Địa chỉ thường trú"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);

    public NguoiThuePanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        add(DialogUtils.createHeaderPanel("QUẢN LÝ NGƯỜI THUÊ", txtTimKiem), BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(DialogUtils.createCardBorder(null, null));
        tableScroll.getViewport().setBackground(Color.WHITE);
        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setOpaque(false);
        body.add(tableScroll, BorderLayout.CENTER);
        body.add(createDetailsCard(), BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        actions.setOpaque(false);
        JButton btnThem = DialogUtils.createStyledButton("Thêm mới", DialogUtils.SUCCESS);
        JButton btnSua = DialogUtils.createStyledButton("Cập nhật", DialogUtils.WARNING);
        JButton btnXoa = DialogUtils.createStyledButton("Xóa", DialogUtils.DANGER);
        JButton btnTaiLai = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN);
        actions.add(btnThem); actions.add(btnSua); actions.add(btnXoa); actions.add(btnTaiLai);
        add(actions, BorderLayout.SOUTH);

        txtTimKiem.addActionListener(e -> taiDuLieu(txtTimKiem.getText()));
        btnThem.addActionListener(e -> luu(false));
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnTaiLai.addActionListener(e -> {
            txtTimKiem.setText("");
            clearForm();
            taiDuLieu("");
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) loadSelection();
        });
        taiDuLieu("");
    }

    private JPanel createDetailsCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(DialogUtils.createCardBorder("Thông tin người thuê", DialogUtils.PRIMARY), BorderFactory.createEmptyBorder(8, 16, 12, 16)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 12); g.fill = GridBagConstraints.HORIZONTAL;
        addField(card, g, 0, 0, "Họ và tên:", txtHoTen);
        addField(card, g, 2, 0, "CCCD:", txtCCCD);
        addField(card, g, 0, 1, "Số điện thoại:", txtSDT);
        addField(card, g, 2, 1, "Địa chỉ thường trú:", txtDiaChi);
        for (JTextField field : new JTextField[]{txtHoTen, txtCCCD, txtSDT, txtDiaChi}) field.setPreferredSize(new Dimension(230, 36));
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints base, int x, int y, String label, JComponent field) {
        GridBagConstraints g = (GridBagConstraints) base.clone();
        g.gridx = x; g.gridy = y; g.weightx = 0; panel.add(DialogUtils.createFormLabel(label), g);
        g.gridx = x + 1; g.weightx = 1; panel.add(field, g);
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
        maDangChon = ((Number) model.getValueAt(row, 0)).intValue();
        luu(true);
    }

    private void luu(boolean sua) {
        int ma = maDangChon;
        NguoiThue nt = new NguoiThue(sua ? ma : 0, txtHoTen.getText(), txtCCCD.getText(), txtSDT.getText(), txtDiaChi.getText());
        try {
            if (sua) {
                if (ma <= 0) { thongBao("Vui lòng chọn người thuê cần cập nhật."); return; }
                service.capNhat(ma, nt);
            }
            else service.them(nt);
            thongBao(sua ? "Cập nhật người thuê thành công." : "Thêm người thuê thành công.");
            taiDuLieu(txtTimKiem.getText());
            if (sua) chonDong(ma); else clearForm();
        } catch (Exception ex) {
            baoLoi("Không thể lưu thông tin người thuê", ex);
        }
    }

    private void loadSelection() {
        int view = table.getSelectedRow();
        if (view < 0) return;
        int row = table.convertRowIndexToModel(view);
        maDangChon = ((Number) model.getValueAt(row, 0)).intValue();
        txtHoTen.setText(String.valueOf(model.getValueAt(row, 1)));
        txtCCCD.setText(String.valueOf(model.getValueAt(row, 2)));
        txtSDT.setText(String.valueOf(model.getValueAt(row, 3)));
        txtDiaChi.setText(String.valueOf(model.getValueAt(row, 4)));
    }

    private void chonDong(int ma) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (((Number) model.getValueAt(row, 0)).intValue() == ma) {
                int view = table.convertRowIndexToView(row);
                if (view >= 0) table.setRowSelectionInterval(view, view);
                return;
            }
        }
    }

    private void clearForm() {
        maDangChon = 0;
        txtHoTen.setText(""); txtCCCD.setText(""); txtSDT.setText(""); txtDiaChi.setText("");
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
            maDangChon = 0;
            table.clearSelection();
            clearForm();
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
