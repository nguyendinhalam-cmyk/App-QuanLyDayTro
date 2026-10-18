package com.quanlydaytro.ui.quanlyhopdong;

import com.quanlydaytro.dao.NguoiThueDAO;
import com.quanlydaytro.dao.PhongDAO;
import com.quanlydaytro.enums.TrangThaiHopDong;
import com.quanlydaytro.model.HopDong;
import com.quanlydaytro.model.NguoiThue;
import com.quanlydaytro.model.Phong;
import com.quanlydaytro.service.HopDongService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Giao diện quản lý hợp đồng: lập, cập nhật, kết thúc, tìm kiếm và tra cứu lịch sử.
 */
public class HopDongPanel extends JPanel {
    private final HopDongService service = new HopDongService();
    private final PhongDAO phongDAO = new PhongDAO();
    private final NguoiThueDAO nguoiThueDAO = new NguoiThueDAO();
    private final JComboBox<Phong> cboPhong = new JComboBox<>();
    private final JComboBox<NguoiThue> cboNguoiThue = new JComboBox<>();
    private final JTextField txtBatDau = new JTextField("2026-10-01", 10);
    private final JTextField txtKetThuc = new JTextField("2027-10-01", 10);
    private final JTextField txtGia = new JTextField(10), txtCoc = new JTextField(10), txtTim = new JTextField(15);
    private final JComboBox<TrangThaiHopDong> cboTrangThai = new JComboBox<>(TrangThaiHopDong.values());
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã HĐ", "Mã phòng", "Tên phòng", "Mã người thuê", "Họ tên", "Ngày bắt đầu", "Ngày kết thúc", "Giá thuê", "Tiền cọc", "Trạng thái"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private int maDangChon;

    public HopDongPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("QUẢN LÝ HỢP ĐỒNG THUÊ");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 5, 4, 5);
        g.anchor = GridBagConstraints.WEST;
        addField(form, g, 0, 0, "Phòng:", cboPhong);
        addField(form, g, 2, 0, "Người thuê:", cboNguoiThue);
        addField(form, g, 0, 1, "Ngày bắt đầu (yyyy-MM-dd):", txtBatDau);
        addField(form, g, 2, 1, "Ngày kết thúc (yyyy-MM-dd):", txtKetThuc);
        addField(form, g, 0, 2, "Giá thuê thỏa thuận:", txtGia);
        addField(form, g, 2, 2, "Tiền cọc:", txtCoc);
        addField(form, g, 0, 3, "Trạng thái:", cboTrangThai);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton them = new JButton("Lập hợp đồng"), sua = new JButton("Cập nhật"), dong = new JButton("Kết thúc hợp đồng"), xoa = new JButton("Xóa"), moi = new JButton("Làm mới");
        buttons.add(them);
        buttons.add(sua);
        buttons.add(dong);
        buttons.add(xoa);
        buttons.add(moi);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Tìm mã HĐ, phòng hoặc người thuê:"));
        search.add(txtTim);
        JButton tim = new JButton("Tìm kiếm"), tatCa = new JButton("Tất cả");
        search.add(tim);
        search.add(tatCa);
        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.NORTH);
        north.add(buttons, BorderLayout.CENTER);
        north.add(search, BorderLayout.SOUTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.add(north, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
        them.addActionListener(e -> luu(false));
        sua.addActionListener(e -> luu(true));
        dong.addActionListener(e -> ketThuc());
        xoa.addActionListener(e -> xoa());
        moi.addActionListener(e -> lamMoi());
        tim.addActionListener(e -> napDanhSach(txtTim.getText()));
        tatCa.addActionListener(e -> {
            txtTim.setText("");
            napDanhSach("");
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) chonDong();
        });
        napCombobox();
        napDanhSach("");
    }

    private void addField(JPanel p, GridBagConstraints g, int x, int y, String label, Component c) {
        g.gridx = x;
        g.gridy = y;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        p.add(new JLabel(label), g);
        g.gridx = x + 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        p.add(c, g);
    }

    private void napCombobox() {
        try {
            cboPhong.removeAllItems();
            for (Phong p : phongDAO.findAll()) cboPhong.addItem(p);
            cboNguoiThue.removeAllItems();
            for (NguoiThue n : nguoiThueDAO.findAll()) cboNguoiThue.addItem(n);
            cboPhong.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                    super.getListCellRendererComponent(l, v, i, s, f);
                    if (v instanceof Phong p)
                        setText("Phòng " + p.getMaPhong() + " - " + p.getTenPhong() + " (" + p.getTrangThai() + ")");
                    return this;
                }
            });
            cboNguoiThue.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
                    super.getListCellRendererComponent(l, v, i, s, f);
                    if (v instanceof NguoiThue n)
                        setText(n.getMaNguoiThue() + " - " + n.getHoTen() + " - " + n.getCCCD());
                    return this;
                }
            });
        } catch (Exception ex) {
            baoLoi("Không tải được danh sách phòng/người thuê: " + ex.getMessage());
        }
    }

    private void napDanhSach(String key) {
        try {
            List<Object[]> rows = service.layDanhSachKemTen();
            model.setRowCount(0);
            String k = key == null ? "" : key.trim().toLowerCase();
            for (Object[] row : rows) {
                HopDong h = (HopDong) row[0];
                String tenPhong = (String) row[1], hoTen = (String) row[2];
                String all = h.getMaHopDong() + " " + h.getMaPhong() + " " + tenPhong + " " + h.getMaNguoiThue() + " " + hoTen + " " + h.getTrangThai();
                if (k.isEmpty() || all.toLowerCase().contains(k))
                    model.addRow(new Object[]{h.getMaHopDong(), h.getMaPhong(), tenPhong, h.getMaNguoiThue(), hoTen, h.getNgayBatDau(), h.getNgayKetThuc(), h.getGiaThueThoaThuan(), h.getTienCoc(), h.getTrangThai() == TrangThaiHopDong.HIEU_LUC ? "Hiệu lực" : "Đã đóng"});
            }
        } catch (Exception ex) {
            baoLoi("Không tải được danh sách hợp đồng: " + ex.getMessage());
        }
    }

    private HopDong docForm() {
        Phong p = (Phong) cboPhong.getSelectedItem();
        NguoiThue n = (NguoiThue) cboNguoiThue.getSelectedItem();
        if (p == null) throw new IllegalArgumentException("Chưa có phòng để chọn.");
        if (n == null) throw new IllegalArgumentException("Chưa có người thuê để chọn.");
        HopDong h = new HopDong();
        h.setMaPhong(p.getMaPhong());
        h.setMaNguoiThue(n.getMaNguoiThue());
        try {
            h.setNgayBatDau(LocalDate.parse(txtBatDau.getText().trim()));
            h.setNgayKetThuc(LocalDate.parse(txtKetThuc.getText().trim()));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Ngày phải đúng định dạng yyyy-MM-dd, ví dụ 2026-10-18.");
        }
        try {
            h.setGiaThueThoaThuan(new BigDecimal(txtGia.getText().trim()));
            h.setTienCoc(new BigDecimal(txtCoc.getText().trim()));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Giá thuê và tiền cọc phải là số hợp lệ.");
        }
        h.setTrangThai((TrangThaiHopDong) cboTrangThai.getSelectedItem());
        return h;
    }

    private void luu(boolean capNhat) {
        try {
            HopDong h = docForm();
            if (capNhat) {
                if (maDangChon <= 0) throw new IllegalArgumentException("Hãy chọn hợp đồng cần cập nhật.");
                service.capNhat(maDangChon, h);
                JOptionPane.showMessageDialog(this, "Cập nhật hợp đồng thành công.");
            } else {
                int ma = service.them(h);
                JOptionPane.showMessageDialog(this, "Lập hợp đồng thành công. Mã hợp đồng: " + ma);
            }
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex.getMessage());
        }
    }

    private void chonDong() {
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        maDangChon = (int) model.getValueAt(r, 0);
        try {
            HopDong h = service.timTheoMa(maDangChon).orElse(null);
            if (h == null) return;
            chonPhong(h.getMaPhong());
            chonNguoiThue(h.getMaNguoiThue());
            txtBatDau.setText(h.getNgayBatDau().toString());
            txtKetThuc.setText(h.getNgayKetThuc().toString());
            txtGia.setText(h.getGiaThueThoaThuan().toPlainString());
            txtCoc.setText(h.getTienCoc().toPlainString());
            cboTrangThai.setSelectedItem(h.getTrangThai());
        } catch (Exception ex) {
            baoLoi(ex.getMessage());
        }
    }

    private void chonPhong(int ma) {
        for (int i = 0; i < cboPhong.getItemCount(); i++)
            if (cboPhong.getItemAt(i).getMaPhong() == ma) {
                cboPhong.setSelectedIndex(i);
                return;
            }
    }

    private void chonNguoiThue(int ma) {
        for (int i = 0; i < cboNguoiThue.getItemCount(); i++)
            if (cboNguoiThue.getItemAt(i).getMaNguoiThue() == ma) {
                cboNguoiThue.setSelectedIndex(i);
                return;
            }
    }

    private void ketThuc() {
        if (maDangChon <= 0) {
            baoLoi("Hãy chọn hợp đồng cần kết thúc.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn muốn chuyển hợp đồng này sang trạng thái Đã đóng?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        try {
            service.ketThuc(maDangChon);
            JOptionPane.showMessageDialog(this, "Đã kết thúc hợp đồng.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex.getMessage());
        }
    }

    private void xoa() {
        if (maDangChon <= 0) {
            baoLoi("Hãy chọn hợp đồng cần xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa hợp đồng đã chọn? Chỉ nên xóa hợp đồng nhập nhầm chưa phát sinh hóa đơn.", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        try {
            if (service.xoa(maDangChon)) JOptionPane.showMessageDialog(this, "Đã xóa hợp đồng.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex.getMessage());
        }
    }

    private void lamMoi() {
        maDangChon = 0;
        table.clearSelection();
        txtBatDau.setText(LocalDate.now().toString());
        txtKetThuc.setText(LocalDate.now().plusYears(1).toString());
        txtGia.setText("");
        txtCoc.setText("");
        cboTrangThai.setSelectedItem(TrangThaiHopDong.HIEU_LUC);
        napCombobox();
        napDanhSach("");
    }

    private void baoLoi(String msg) {
        JOptionPane.showMessageDialog(this, msg == null ? "Đã xảy ra lỗi." : msg, "Thông báo", JOptionPane.ERROR_MESSAGE);
    }
}
