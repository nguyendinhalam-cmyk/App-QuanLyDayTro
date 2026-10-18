package com.quanlydaytro.ui.quanlytaikhoan;

import com.quanlydaytro.service.TaiKhoanService;
import com.quanlydaytro.ui.dungchung.DialogUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/** Màn hình quản lý tài khoản; mọi thao tác dữ liệu đi qua TaiKhoanService. */
public class TaiKhoanPanel extends JPanel {
    private final TaiKhoanService service = new TaiKhoanService();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"STT", "Mã tài khoản", "Tên đăng nhập", "Nhân viên", "Vai trò",
                    "__MaTaiKhoan", "__MaNhanVien", "__MaVaiTro"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = DialogUtils.createStyledTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private final JTextField search = DialogUtils.createSearchField("Tìm theo tên đăng nhập, nhân viên...");
    private final JLabel idValue = new JLabel("Chưa chọn tài khoản");
    private final JTextField username = DialogUtils.createFormField();
    private final JPasswordField password = new JPasswordField();
    private final JComboBox<LuaChon> employee = new JComboBox<>();
    private final JComboBox<LuaChon> role = new JComboBox<>();
    private boolean loadingSelection;

    public TaiKhoanPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(DialogUtils.BG_CONTENT);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        add(DialogUtils.createHeaderPanel("QUẢN LÝ TÀI KHOẢN & PHÂN QUYỀN", search), BorderLayout.NORTH);

        table.setRowSorter(sorter);
        for (int i = model.getColumnCount() - 1; i >= 5; i--) {
            table.removeColumn(table.getColumnModel().getColumn(i));
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !loadingSelection) loadSelection();
        });
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(DialogUtils.createCardBorder(null, null));
        tableScroll.getViewport().setBackground(Color.WHITE);

        JPanel body = new JPanel(new BorderLayout(0, 14));
        body.setOpaque(false);
        body.add(tableScroll, BorderLayout.CENTER);
        body.add(createDetailsCard(), BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);
        add(createActions(), BorderLayout.SOUTH);
        installSearch();
        napDanhMuc(0);
        taiDuLieu();
    }

    private JPanel createDetailsCard() {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                DialogUtils.createCardBorder("Thông tin tài khoản", DialogUtils.PRIMARY),
                BorderFactory.createEmptyBorder(8, 16, 14, 16)));
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 8, 5, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        addField(fields, c, 0, 0, "Mã tài khoản:", idValue);
        addField(fields, c, 2, 0, "Tên đăng nhập:", username);
        addField(fields, c, 0, 1, "Mật khẩu mới:", password);
        addField(fields, c, 2, 1, "Nhân viên:", employee);
        addField(fields, c, 0, 2, "Vai trò:", role);
        JLabel hint = new JLabel("Để trống mật khẩu khi cập nhật nếu muốn giữ mật khẩu hiện tại.");
        hint.setForeground(DialogUtils.TEXT_SECONDARY);
        hint.setFont(DialogUtils.SMALL_FONT);
        c.gridx = 2; c.gridy = 2; c.gridwidth = 2; c.weightx = 1;
        fields.add(hint, c);
        username.setPreferredSize(new Dimension(220, 36));
        password.setPreferredSize(new Dimension(220, 36));
        employee.setPreferredSize(new Dimension(250, 36));
        role.setPreferredSize(new Dimension(220, 36));
        card.add(fields, BorderLayout.CENTER);
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints base, int x, int y, String label, JComponent field) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x; c.gridy = y; c.gridwidth = 1; c.weightx = 0;
        JLabel l = DialogUtils.createFormLabel(label);
        panel.add(l, c);
        c.gridx = x + 1; c.weightx = 1;
        panel.add(field, c);
    }

    private JPanel createActions() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        actions.setOpaque(false);
        JButton add = DialogUtils.createStyledButton("Thêm mới", DialogUtils.SUCCESS);
        JButton update = DialogUtils.createStyledButton("Cập nhật", DialogUtils.WARNING);
        JButton delete = DialogUtils.createStyledButton("Xóa", DialogUtils.DANGER);
        JButton refresh = DialogUtils.createStyledButton("Làm mới", DialogUtils.GRAY_BTN);
        add.addActionListener(e -> them());
        update.addActionListener(e -> capNhat());
        delete.addActionListener(e -> xoa());
        refresh.addActionListener(e -> lamMoi());
        actions.add(add); actions.add(update); actions.add(delete); actions.add(refresh);
        return actions;
    }

    private void installSearch() {
        search.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String value = search.getText().trim();
                sorter.setRowFilter(value.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(value)));
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
    }

    private void taiDuLieu() {
        try {
            model.setRowCount(0);
            int stt = 1;
            for (Object[] row : service.layDanhSachHienThi()) {
                int maTaiKhoan = ((Number) row[0]).intValue();
                model.addRow(new Object[]{stt++, String.format(Locale.ROOT, "TK%03d", maTaiKhoan), row[1],
                        row[3], row[5], maTaiKhoan, row[2], row[4]});
            }
        } catch (SQLException ex) {
            baoLoi("Không tải được danh sách tài khoản", ex);
        }
    }

    private void napDanhMuc(int maTaiKhoan) {
        try {
            employee.removeAllItems();
            role.removeAllItems();
            List<Object[]> employees = service.layNhanVienChuaCoTaiKhoan(maTaiKhoan);
            for (Object[] row : employees)
                employee.addItem(new LuaChon((Integer) row[0], row[1] + " (Mã NV: " + row[0] + ")"));
            for (Object[] row : service.layVaiTro())
                role.addItem(new LuaChon((Integer) row[0], String.valueOf(row[1])));
        } catch (SQLException ex) {
            baoLoi("Không tải được danh mục nhân viên/vai trò", ex);
        }
    }

    private void loadSelection() {
        int view = table.getSelectedRow();
        if (view < 0) return;
        int row = table.convertRowIndexToModel(view);
        int ma = ((Number) model.getValueAt(row, 5)).intValue();
        loadingSelection = true;
        idValue.setText(String.valueOf(ma));
        username.setText(String.valueOf(model.getValueAt(row, 2)));
        password.setText("");
        napDanhMuc(ma);
        chonMa(employee, ((Number) model.getValueAt(row, 6)).intValue());
        chonMa(role, ((Number) model.getValueAt(row, 7)).intValue());
        loadingSelection = false;
    }

    private void them() {
        LuaChon nv = (LuaChon) employee.getSelectedItem();
        LuaChon vt = (LuaChon) role.getSelectedItem();
        char[] pw = password.getPassword();
        try {
            if (nv == null || vt == null) throw new IllegalArgumentException("Cần chọn nhân viên và vai trò.");
            service.them(username.getText(), pw, nv.ma, vt.ma);
            thongBao("Đã tạo tài khoản.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi("Không thể tạo tài khoản", ex);
        } finally {
            Arrays.fill(pw, '\0');
        }
    }

    private void capNhat() {
        int ma = selectedId();
        if (ma <= 0) { thongBao("Vui lòng chọn tài khoản cần cập nhật."); return; }
        LuaChon nv = (LuaChon) employee.getSelectedItem();
        LuaChon vt = (LuaChon) role.getSelectedItem();
        char[] pw = password.getPassword();
        try {
            if (nv == null || vt == null) throw new IllegalArgumentException("Cần chọn nhân viên và vai trò.");
            service.capNhat(ma, username.getText(), pw, nv.ma, vt.ma);
            thongBao("Đã cập nhật tài khoản.");
            taiDuLieu();
            chonDong(ma);
        } catch (Exception ex) {
            baoLoi("Không thể cập nhật tài khoản", ex);
        } finally {
            Arrays.fill(pw, '\0');
        }
    }

    private void xoa() {
        int ma = selectedId();
        if (ma <= 0) { thongBao("Vui lòng chọn tài khoản cần xóa."); return; }
        int answer = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài khoản này?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            if (service.xoa(ma)) thongBao("Đã xóa tài khoản.");
            else thongBao("Không tìm thấy tài khoản cần xóa.");
            lamMoi();
        } catch (Exception ex) {
            baoLoi("Không thể xóa tài khoản", ex);
        }
    }

    private int selectedId() {
        int view = table.getSelectedRow();
        if (view < 0) return 0;
        int row = table.convertRowIndexToModel(view);
        return ((Number) model.getValueAt(row, 5)).intValue();
    }

    private void chonDong(int ma) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (((Number) model.getValueAt(row, 5)).intValue() == ma) {
                int view = table.convertRowIndexToView(row);
                if (view >= 0) table.setRowSelectionInterval(view, view);
                return;
            }
        }
    }

    private void chonMa(JComboBox<LuaChon> combo, int ma) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).ma == ma) { combo.setSelectedIndex(i); return; }
        }
    }

    private void lamMoi() {
        loadingSelection = true;
        table.clearSelection();
        idValue.setText("Tài khoản mới");
        username.setText("");
        password.setText("");
        search.setText("");
        sorter.setRowFilter(null);
        napDanhMuc(0);
        loadingSelection = false;
    }

    private void thongBao(String message) {
        JOptionPane.showMessageDialog(this, message, "Quản lý tài khoản", JOptionPane.INFORMATION_MESSAGE);
    }

    private void baoLoi(String message, Exception ex) {
        JOptionPane.showMessageDialog(this, message + ":\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private static class LuaChon {
        final int ma;
        final String ten;
        LuaChon(int ma, String ten) { this.ma = ma; this.ten = ten; }
        @Override public String toString() { return ten; }
    }
}
