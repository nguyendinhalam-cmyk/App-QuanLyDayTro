package com.quanlydaytro.ui.quanlyhopdong;

import com.quanlydaytro.dao.NguoiThueDAO;
import com.quanlydaytro.dao.PhongDAO;
import com.quanlydaytro.enums.TrangThaiHopDong;
import com.quanlydaytro.model.HopDong;
import com.quanlydaytro.model.NguoiThue;
import com.quanlydaytro.model.Phong;
import com.quanlydaytro.service.HopDongService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

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
    private final JTextField txtGia = new JTextField(10), txtCoc = new JTextField(10);
    private final JTextField txtTim;
    private final JComboBox<TrangThaiHopDong> cboTrangThai = new JComboBox<>(TrangThaiHopDong.values());
    private final JTextField txtMaHD = new JTextField(10);
    private final JComboBox<String> cboLocTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đang hiệu lực", "Đã đóng"});
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"STT", "Mã HĐ", "Phòng", "Người thuê", "Ngày bắt đầu", "Ngày kết thúc", "Tiền cọc", "Giá thuê/tháng", "Trạng thái", "__MaHopDong", "__MaPhong", "__MaNguoiThue"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = DialogUtils.createStyledTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private int maDangChon;

    public HopDongPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        txtTim = DialogUtils.createSearchField("Tìm hợp đồng, phòng, người thuê...");
        txtMaHD.setEditable(false);
        txtMaHD.setBackground(new Color(241, 245, 249));

        JPanel header = new JPanel(new BorderLayout(0, 10));
        header.setOpaque(false);
        header.add(DialogUtils.createHeaderPanel("QUẢN LÝ HỢP ĐỒNG THUÊ PHÒNG", txtTim), BorderLayout.NORTH);
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        DialogUtils.styleToolbar(filter);
        filter.add(DialogUtils.createFormLabel("Lọc theo trạng thái:"));
        filter.add(cboLocTrangThai);
        JButton applyFilter = DialogUtils.createStyledButton("Lọc hợp đồng", DialogUtils.PRIMARY);
        filter.add(applyFilter);
        header.add(filter, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSorter(sorter);
        for (int i = model.getColumnCount() - 1; i >= 9; i--) table.removeColumn(table.getColumnModel().getColumn(i));
        table.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(DialogUtils.createCardBorder(null, null));
        tableScroll.getViewport().setBackground(Color.WHITE);
        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setOpaque(false);
        body.add(tableScroll, BorderLayout.CENTER);
        body.add(createDetailsCard(), BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        actions.setOpaque(false);
        JButton them = DialogUtils.createStyledButton("Lập hợp đồng", DialogUtils.SUCCESS);
        JButton sua = DialogUtils.createStyledButton("Cập nhật", DialogUtils.WARNING);
        JButton dong = DialogUtils.createStyledButton("Kết thúc", DialogUtils.DANGER);
        JButton xoa = DialogUtils.createStyledButton("Xóa", DialogUtils.DANGER);
        JButton moi = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN);
        actions.add(them); actions.add(sua); actions.add(dong); actions.add(xoa); actions.add(moi);
        add(actions, BorderLayout.SOUTH);
        them.addActionListener(e -> luu(false));
        sua.addActionListener(e -> luu(true));
        dong.addActionListener(e -> ketThuc());
        xoa.addActionListener(e -> xoa());
        moi.addActionListener(e -> lamMoi());
        applyFilter.addActionListener(e -> applyFilter());
        cboLocTrangThai.addActionListener(e -> applyFilter());
        cboTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                super.getListCellRendererComponent(list, value, index, selected, focus);
                if (value == TrangThaiHopDong.HIEU_LUC) setText("Đang hiệu lực");
                else if (value == TrangThaiHopDong.DA_DONG) setText("Đã đóng");
                return this;
            }
        });
        txtTim.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) chonDong();
        });
        txtBatDau.setText(LocalDate.now().toString());
        txtKetThuc.setText(LocalDate.now().plusYears(1).toString());
        napCombobox();
        napDanhSach("");
    }

    private JPanel createDetailsCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(DialogUtils.createCardBorder("Chi tiết hợp đồng thuê", DialogUtils.PRIMARY), BorderFactory.createEmptyBorder(8, 14, 12, 14)));
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 12);
        g.fill = GridBagConstraints.HORIZONTAL;
        addField(fields, g, 0, 0, "Mã hợp đồng:", txtMaHD);
        addField(fields, g, 2, 0, "Phòng thuê:", cboPhong);
        addField(fields, g, 0, 1, "Người thuê:", cboNguoiThue);
        addField(fields, g, 2, 1, "Ngày bắt đầu (yyyy-MM-dd):", txtBatDau);
        addField(fields, g, 0, 2, "Ngày kết thúc:", txtKetThuc);
        addField(fields, g, 2, 2, "Tiền đặt cọc (VNĐ):", txtCoc);
        addField(fields, g, 0, 3, "Giá thuê/tháng (VNĐ):", txtGia);
        addField(fields, g, 2, 3, "Trạng thái:", cboTrangThai);
        Dimension size = new Dimension(220, 36);
        for (JComponent field : new JComponent[]{txtMaHD, txtBatDau, txtKetThuc, txtGia, txtCoc, cboPhong, cboNguoiThue, cboTrangThai}) field.setPreferredSize(size);
        card.add(fields, BorderLayout.CENTER);
        return card;
    }

    private void addField(JPanel p, GridBagConstraints base, int x, int y, String label, Component component) {
        GridBagConstraints g = (GridBagConstraints) base.clone();
        g.gridx = x; g.gridy = y; g.weightx = 0;
        p.add(DialogUtils.createFormLabel(label), g);
        g.gridx = x + 1; g.weightx = 1;
        p.add(component, g);
    }

    private void applyFilter() {
        String keyword = txtTim.getText().trim();
        String status = String.valueOf(cboLocTrangThai.getSelectedItem());
        sorter.setRowFilter(new RowFilter<>() {
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                boolean textMatch = keyword.isEmpty() || entry.getStringValue(1).toLowerCase().contains(keyword.toLowerCase())
                        || entry.getStringValue(2).toLowerCase().contains(keyword.toLowerCase())
                        || entry.getStringValue(3).toLowerCase().contains(keyword.toLowerCase())
                        || entry.getStringValue(0).toLowerCase().contains(keyword.toLowerCase());
                boolean statusMatch = status.equals("Tất cả") || entry.getStringValue(8).equals(status);
                return textMatch && statusMatch;
            }
        });
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
                    if (v instanceof Phong p) {
                        String status = switch (p.getTrangThai()) {
                            case TRONG -> "Trống";
                            case DANG_THUE -> "Đang thuê";
                            case DANG_SUA -> "Đang sửa";
                        };
                        setText("Phòng " + p.getMaPhong() + " - " + p.getTenPhong() + " (" + status + ")");
                    }
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
            int stt = 1;
            for (Object[] row : rows) {
                HopDong h = (HopDong) row[0];
                String tenPhong = (String) row[1], hoTen = (String) row[2];
                String all = h.getMaHopDong() + " " + h.getMaPhong() + " " + tenPhong + " " + h.getMaNguoiThue() + " " + hoTen + " " + h.getTrangThai();
                String status = h.getTrangThai() == TrangThaiHopDong.HIEU_LUC ? "Đang hiệu lực" : "Đã đóng";
                if (k.isEmpty() || all.toLowerCase().contains(k))
                    model.addRow(new Object[]{stt++, String.format(Locale.ROOT, "HD%06d", h.getMaHopDong()), tenPhong, hoTen,
                            h.getNgayBatDau() == null ? "" : h.getNgayBatDau().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            h.getNgayKetThuc() == null ? "" : h.getNgayKetThuc().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            formatMoney(h.getTienCoc()), formatMoney(h.getGiaThueThoaThuan()), status,
                            h.getMaHopDong(), h.getMaPhong(), h.getMaNguoiThue()});
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
        maDangChon = ((Number) model.getValueAt(r, 9)).intValue();
        try {
            HopDong h = service.timTheoMa(maDangChon).orElse(null);
            if (h == null) return;
            chonPhong(h.getMaPhong());
            chonNguoiThue(h.getMaNguoiThue());
            txtMaHD.setText(String.format(Locale.ROOT, "HD%06d", h.getMaHopDong()));
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
        txtMaHD.setText("");
        txtTim.setText("");
        cboLocTrangThai.setSelectedItem("Tất cả");
        table.clearSelection();
        txtBatDau.setText(LocalDate.now().toString());
        txtKetThuc.setText(LocalDate.now().plusYears(1).toString());
        txtGia.setText("");
        txtCoc.setText("");
        cboTrangThai.setSelectedItem(TrangThaiHopDong.HIEU_LUC);
        napCombobox();
        napDanhSach("");
    }

    private String formatMoney(BigDecimal value) {
        return value == null ? "" : NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(value) + " VNĐ";
    }

    private void baoLoi(String msg) {
        JOptionPane.showMessageDialog(this, msg == null ? "Đã xảy ra lỗi." : msg, "Thông báo", JOptionPane.ERROR_MESSAGE);
    }
}
