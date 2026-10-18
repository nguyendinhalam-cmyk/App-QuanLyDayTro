package com.quanlydaytro.ui.quanlyphong;

import com.quanlydaytro.dao.DayTroDAO;
import com.quanlydaytro.enums.TrangThaiPhong;
import com.quanlydaytro.model.DayTro;
import com.quanlydaytro.model.Phong;
import com.quanlydaytro.service.PhongService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Giao diện CRUD phòng trọ.
 */
public class PhongPanel extends JPanel {
    private final PhongService service = new PhongService();
    private final JComboBox<DayTro> cboDay = new JComboBox<>();
    private final JComboBox<TrangThaiPhong> cboTrangThai = new JComboBox<>(TrangThaiPhong.values());
    private final JTextField txtTen = new JTextField(12), txtGia = new JTextField(12), txtSoNguoi = new JTextField("0", 5), txtTim = new JTextField(16);
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã phòng", "Tên phòng", "Giá thuê", "Số người ở", "Trạng thái", "Mã dãy", "Tên dãy"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);
    private int maDangChon;

    public PhongPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(DialogUtils.createHeaderPanel("🚪 QUẢN LÝ PHÒNG TRỌ", null), BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(DialogUtils.createCardBorder("Thông tin phòng", DialogUtils.PRIMARY), BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 5, 4, 5);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0;
        g.gridy = 0;
        form.add(new JLabel("Tên phòng:"), g);
        g.gridx = 1;
        form.add(txtTen, g);
        g.gridx = 2;
        form.add(new JLabel("Giá thuê:"), g);
        g.gridx = 3;
        form.add(txtGia, g);
        g.gridx = 0;
        g.gridy = 1;
        form.add(new JLabel("Số người ở:"), g);
        g.gridx = 1;
        form.add(txtSoNguoi, g);
        g.gridx = 2;
        form.add(new JLabel("Trạng thái:"), g);
        g.gridx = 3;
        form.add(cboTrangThai, g);
        g.gridx = 0;
        g.gridy = 2;
        form.add(new JLabel("Dãy trọ:"), g);
        g.gridx = 1;
        g.gridwidth = 3;
        g.fill = GridBagConstraints.HORIZONTAL;
        form.add(cboDay, g);
        g.gridwidth = 1;
        g.fill = GridBagConstraints.NONE;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        buttons.setOpaque(false);
        JButton them = DialogUtils.createStyledButton("Thêm", DialogUtils.SUCCESS), sua = DialogUtils.createStyledButton("Cập nhật", DialogUtils.WARNING), xoa = DialogUtils.createStyledButton("Xóa", DialogUtils.DANGER), moi = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN);
        buttons.add(them);
        buttons.add(sua);
        buttons.add(xoa);
        buttons.add(moi);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        DialogUtils.styleToolbar(search);
        search.add(new JLabel("Tìm theo mã, tên phòng, trạng thái hoặc dãy:"));
        search.add(txtTim);
        JButton tim = DialogUtils.createStyledButton("Tìm kiếm", DialogUtils.PRIMARY), tatCa = DialogUtils.createStyledButton("Tất cả", DialogUtils.GRAY_BTN);
        search.add(tim);
        search.add(tatCa);
        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.NORTH);
        north.add(buttons, BorderLayout.CENTER);
        north.add(search, BorderLayout.SOUTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setOpaque(false);
        body.add(north, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
        them.addActionListener(e -> luu(false));
        sua.addActionListener(e -> luu(true));
        xoa.addActionListener(e -> xoa());
        moi.addActionListener(e -> lamMoi());
        tim.addActionListener(e -> napDanhSach(txtTim.getText()));
        tatCa.addActionListener(e -> {
            txtTim.setText("");
            napDanhSach("");
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int r = table.convertRowIndexToModel(table.getSelectedRow());
                maDangChon = ((Number) model.getValueAt(r, 0)).intValue();
                txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
                txtGia.setText(String.valueOf(model.getValueAt(r, 2)));
                txtSoNguoi.setText(String.valueOf(model.getValueAt(r, 3)));
                cboTrangThai.setSelectedItem(trangThaiTuText(String.valueOf(model.getValueAt(r, 4))));
                chonDay(((Number) model.getValueAt(r, 5)).intValue());
            }
        });
        napDay();
        napDanhSach("");
    }

    private void napDay() {
        try {
            cboDay.removeAllItems();
            for (DayTro d : new DayTroDAO().findAll()) cboDay.addItem(d);
            cboDay.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                    super.getListCellRendererComponent(list, value, index, selected, focus);
                    if (value instanceof DayTro d) setText(d.getMaDay() + " - " + d.getTenDay());
                    return this;
                }
            });
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void chonDay(int ma) {
        for (int i = 0; i < cboDay.getItemCount(); i++)
            if (cboDay.getItemAt(i).getMaDay() == ma) {
                cboDay.setSelectedIndex(i);
                return;
            }
    }

    private void napDanhSach(String key) {
        try {
            model.setRowCount(0);
            List<Phong> ds = (key == null || key.isBlank()) ? service.layTatCa() : service.timKiem(key);
            DayTroDAO ddao = new DayTroDAO();
            for (Phong p : ds) {
                String tenDay = ddao.findById(p.getMaDay()).map(DayTro::getTenDay).orElse("(Không xác định)");
                model.addRow(new Object[]{p.getMaPhong(), p.getTenPhong(), p.getGiaThueHienTai(), p.getSoNguoiO(), trangThaiHienThi(p.getTrangThai()), p.getMaDay(), tenDay});
            }
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void luu(boolean sua) {
        try {
            DayTro d = (DayTro) cboDay.getSelectedItem();
            if (d == null) throw new IllegalArgumentException("Cần tạo dãy trọ trước khi thêm phòng.");
            Phong p = new Phong();
            p.setTenPhong(txtTen.getText());
            try {
                p.setGiaThueHienTai(new BigDecimal(txtGia.getText().trim()));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Giá thuê phải là số hợp lệ.");
            }
            try {
                p.setSoNguoiO(Integer.parseInt(txtSoNguoi.getText().trim()));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Số người ở phải là số nguyên.");
            }
            p.setTrangThai((TrangThaiPhong) cboTrangThai.getSelectedItem());
            p.setMaDay(d.getMaDay());
            if (sua) {
                if (maDangChon <= 0) throw new IllegalArgumentException("Hãy chọn phòng cần cập nhật.");
                service.capNhat(maDangChon, p);
            } else service.them(p);
            JOptionPane.showMessageDialog(this, sua ? "Đã cập nhật phòng." : "Đã thêm phòng.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void xoa() {
        if (maDangChon <= 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn phòng cần xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa phòng đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        try {
            service.xoa(maDangChon);
            JOptionPane.showMessageDialog(this, "Đã xóa phòng.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void lamMoi() {
        maDangChon = 0;
        table.clearSelection();
        txtTen.setText("");
        txtGia.setText("");
        txtSoNguoi.setText("0");
        txtTim.setText("");
        cboTrangThai.setSelectedItem(TrangThaiPhong.TRONG);
        napDay();
        napDanhSach("");
    }

    private String trangThaiHienThi(TrangThaiPhong t) {
        return switch (t) {
            case TRONG -> "Trống";
            case DANG_THUE -> "Đang thuê";
            case DANG_SUA -> "Đang sửa";
        };
    }

    private TrangThaiPhong trangThaiTuText(String s) {
        return switch (s) {
            case "Trống" -> TrangThaiPhong.TRONG;
            case "Đang thuê" -> TrangThaiPhong.DANG_THUE;
            case "Đang sửa" -> TrangThaiPhong.DANG_SUA;
            default -> TrangThaiPhong.TRONG;
        };
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage() == null ? "Có lỗi xảy ra." : ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
}
