package com.quanlydaytro.ui.quanlydaytro;

import com.quanlydaytro.dao.ChuTroDAO;
import com.quanlydaytro.model.ChuTro;
import com.quanlydaytro.model.DayTro;
import com.quanlydaytro.service.DayTroService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Giao diện CRUD dãy trọ, chọn chủ sở hữu từ danh mục CHUTRO.
 */
public class DayTroPanel extends JPanel {
    private final DayTroService service = new DayTroService();
    private final JComboBox<ChuTro> cboChuTro = new JComboBox<>();
    private final JTextField txtTen = new JTextField(18), txtDiaChi = new JTextField(28), txtTim = new JTextField(18);
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã dãy", "Tên dãy", "Địa chỉ", "Mã chủ trọ", "Tên chủ trọ"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private int maDangChon = 0;

    public DayTroPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("QUẢN LÝ DÃY TRỌ");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 5, 4, 5);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = 0;
        g.gridy = 0;
        form.add(new JLabel("Tên dãy:"), g);
        g.gridx = 1;
        form.add(txtTen, g);
        g.gridx = 2;
        form.add(new JLabel("Địa chỉ:"), g);
        g.gridx = 3;
        form.add(txtDiaChi, g);
        g.gridx = 0;
        g.gridy = 1;
        form.add(new JLabel("Chủ trọ:"), g);
        g.gridx = 1;
        g.gridwidth = 3;
        g.fill = GridBagConstraints.HORIZONTAL;
        form.add(cboChuTro, g);
        g.gridwidth = 1;
        g.fill = GridBagConstraints.NONE;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton them = new JButton("Thêm"), sua = new JButton("Cập nhật"), xoa = new JButton("Xóa"), moi = new JButton("Làm mới");
        buttons.add(them);
        buttons.add(sua);
        buttons.add(xoa);
        buttons.add(moi);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Tìm theo mã, tên dãy, địa chỉ, chủ trọ:"));
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
                maDangChon = (int) model.getValueAt(r, 0);
                txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
                txtDiaChi.setText(String.valueOf(model.getValueAt(r, 2)));
                chonChuTro((int) model.getValueAt(r, 3));
            }
        });
        napChuTro();
        napDanhSach("");
    }

    private void napChuTro() {
        try {
            cboChuTro.removeAllItems();
            List<ChuTro> ds = new ChuTroDAO().findAll();
            for (ChuTro c : ds) cboChuTro.addItem(c);
            cboChuTro.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus) {
                    super.getListCellRendererComponent(list, value, index, selected, focus);
                    if (value instanceof ChuTro c)
                        setText(c.getMaChuTro() + " - " + c.getHoTen() + " (" + c.getSoDienThoai() + ")");
                    return this;
                }
            });
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void chonChuTro(int ma) {
        for (int i = 0; i < cboChuTro.getItemCount(); i++)
            if (cboChuTro.getItemAt(i).getMaChuTro() == ma) {
                cboChuTro.setSelectedIndex(i);
                return;
            }
    }

    private void napDanhSach(String key) {
        try {
            model.setRowCount(0);
            List<DayTro> ds = (key == null || key.isBlank()) ? service.layTatCa() : service.timKiem(key);
            ChuTroDAO cdao = new ChuTroDAO();
            for (DayTro d : ds) {
                String tenChuTro = cdao.findById(d.getMaChuTro()).map(ChuTro::getHoTen).orElse("(Không xác định)");
                model.addRow(new Object[]{d.getMaDay(), d.getTenDay(), d.getDiaChi(), d.getMaChuTro(), tenChuTro});
            }
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void luu(boolean sua) {
        try {
            ChuTro c = (ChuTro) cboChuTro.getSelectedItem();
            if (c == null) throw new IllegalArgumentException("Cần tạo chủ trọ trước khi thêm dãy trọ.");
            DayTro d = new DayTro();
            d.setTenDay(txtTen.getText());
            d.setDiaChi(txtDiaChi.getText());
            d.setMaChuTro(c.getMaChuTro());
            if (sua) {
                if (maDangChon <= 0) throw new IllegalArgumentException("Hãy chọn dãy trọ cần cập nhật.");
                service.capNhat(maDangChon, d);
            } else service.them(d);
            JOptionPane.showMessageDialog(this, sua ? "Đã cập nhật dãy trọ." : "Đã thêm dãy trọ.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void xoa() {
        if (maDangChon <= 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn dãy trọ cần xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa dãy trọ đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
            return;
        try {
            service.xoa(maDangChon);
            JOptionPane.showMessageDialog(this, "Đã xóa dãy trọ.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void lamMoi() {
        maDangChon = 0;
        table.clearSelection();
        txtTen.setText("");
        txtDiaChi.setText("");
        txtTim.setText("");
        napChuTro();
        napDanhSach("");
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage() == null ? "Có lỗi xảy ra." : ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
}
