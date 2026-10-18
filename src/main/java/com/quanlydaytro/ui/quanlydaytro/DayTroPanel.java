package com.quanlydaytro.ui.quanlydaytro;

import com.quanlydaytro.dao.ChuTroDAO;
import com.quanlydaytro.model.ChuTro;
import com.quanlydaytro.model.DayTro;
import com.quanlydaytro.service.DayTroService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

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
    private final JTextField txtTen = DialogUtils.createFormField(), txtDiaChi = DialogUtils.createFormField();
    private final JTextField txtTim = DialogUtils.createSearchField("Tìm theo mã, tên dãy, địa chỉ, chủ trọ...");
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã dãy", "Tên dãy", "Địa chỉ", "Mã chủ trọ", "Tên chủ trọ"}, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = com.quanlydaytro.ui.dungchung.DialogUtils.createStyledTable(model);
    private int maDangChon = 0;

    public DayTroPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(DialogUtils.createHeaderPanel("QUẢN LÝ DÃY TRỌ", txtTim), BorderLayout.NORTH);
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
        JButton them = DialogUtils.createStyledButton("Thêm mới", DialogUtils.SUCCESS);
        JButton sua = DialogUtils.createStyledButton("Cập nhật", DialogUtils.WARNING);
        JButton xoa = DialogUtils.createStyledButton("Xóa", DialogUtils.DANGER);
        JButton moi = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN);
        actions.add(them); actions.add(sua); actions.add(xoa); actions.add(moi);
        add(actions, BorderLayout.SOUTH);
        them.addActionListener(e -> luu(false));
        sua.addActionListener(e -> luu(true));
        xoa.addActionListener(e -> xoa());
        moi.addActionListener(e -> lamMoi());
        txtTim.addActionListener(e -> napDanhSach(txtTim.getText()));
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

    private JPanel createDetailsCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(DialogUtils.createCardBorder("Thông tin dãy trọ", DialogUtils.PRIMARY), BorderFactory.createEmptyBorder(8, 16, 12, 16)));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 12); g.fill = GridBagConstraints.HORIZONTAL;
        addField(card, g, 0, "Tên dãy:", txtTen);
        addField(card, g, 2, "Địa chỉ:", txtDiaChi);
        addField(card, g, 4, "Chủ trọ:", cboChuTro);
        txtTen.setPreferredSize(new Dimension(200, 36));
        txtDiaChi.setPreferredSize(new Dimension(250, 36));
        cboChuTro.setPreferredSize(new Dimension(280, 36));
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints base, int x, String label, Component field) {
        GridBagConstraints g = (GridBagConstraints) base.clone();
        g.gridx = x; g.gridy = 0; g.weightx = 0;
        panel.add(DialogUtils.createFormLabel(label), g);
        g.gridx = x + 1; g.weightx = 1;
        panel.add(field, g);
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
