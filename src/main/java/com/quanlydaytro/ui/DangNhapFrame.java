package com.quanlydaytro.ui;

import com.quanlydaytro.dto.KetQuaDangNhap;
import com.quanlydaytro.service.DangNhapService;
import com.quanlydaytro.ui.dungchung.DialogUtils;
import com.quanlydaytro.ui.dungchung.Icons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

/** Giao diện đăng nhập RoomHub, xác thực bằng DangNhapService. */
public class DangNhapFrame extends JFrame {
    private static final Color PRIMARY = DialogUtils.PRIMARY;
    private static final Color TEXT_PRIMARY = DialogUtils.TEXT_PRIMARY;
    private static final Color TEXT_SECONDARY = DialogUtils.TEXT_SECONDARY;

    private final JTextField txtTenDangNhap = new JTextField();
    private final JPasswordField txtMatKhau = new JPasswordField();
    private final JButton btnDangNhap = DialogUtils.createStyledButton("ĐĂNG NHẬP", PRIMARY);
    private final DangNhapService service = new DangNhapService();

    public DangNhapFrame() {
        setTitle("RoomHub - Đăng nhập");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 700);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);
        setContentPane(createRoot());
        getRootPane().setDefaultButton(btnDangNhap);
    }

    private JPanel createRoot() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(createBrandPanel());
        root.add(createFormPanel());
        return root;
    }

    private JPanel createBrandPanel() {
        JPanel brand = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setPaint(new GradientPaint(0, 0, new Color(67, 56, 202), getWidth(), getHeight(), new Color(124, 58, 237)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 22));
                g2.fillOval(-110, getHeight() - 260, 380, 380);
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fillOval(getWidth() - 240, -150, 400, 400);
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(getWidth() - 190, getHeight() - 210, 240, 240);
                g2.dispose();
            }
        };
        JPanel items = new JPanel();
        items.setOpaque(false);
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        items.setBorder(new EmptyBorder(35, 56, 35, 35));

        JComponent mark = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setColor(new Color(255, 255, 255, 38));
                g2.fill(new RoundRectangle2D.Double(0, 0, 64, 64, 18, 18));
                Icons.paint(g2, Icons.Type.HOME, 16, 16, 32, Color.WHITE);
                g2.dispose();
            }
        };
        mark.setPreferredSize(new Dimension(64, 64));
        mark.setMaximumSize(new Dimension(64, 64));
        mark.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel name = new JLabel("RoomHub");
        name.setFont(DialogUtils.font(Font.BOLD, 44f));
        name.setForeground(Color.WHITE);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel tagline = new JLabel("<html>Hệ thống quản lý dãy trọ<br>thông minh &amp; hiện đại</html>");
        tagline.setFont(DialogUtils.font(Font.PLAIN, 19f));
        tagline.setForeground(new Color(224, 231, 255));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        items.add(mark);
        items.add(Box.createVerticalStrut(28));
        items.add(name);
        items.add(Box.createVerticalStrut(10));
        items.add(tagline);
        items.add(Box.createVerticalStrut(42));
        items.add(feature(Icons.Type.DOOR, "Quản lý phòng, người thuê & hợp đồng"));
        items.add(Box.createVerticalStrut(18));
        items.add(feature(Icons.Type.BOLT, "Ghi điện nước, lập hóa đơn tự động"));
        items.add(Box.createVerticalStrut(18));
        items.add(feature(Icons.Type.CHART, "Báo cáo doanh thu & công nợ trực quan"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(0, 12, 0, 12);
        brand.add(items, gc);
        return brand;
    }

    private JPanel feature(Icons.Type icon, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(460, 40));
        JComponent badge = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setColor(new Color(255, 255, 255, 38));
                g2.fillOval(0, 0, 36, 36);
                Icons.paint(g2, icon, 9, 9, 18, Color.WHITE);
                g2.dispose();
            }
        };
        badge.setPreferredSize(new Dimension(36, 36));
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(DialogUtils.font(Font.PLAIN, 14f));
        row.add(badge);
        row.add(label);
        return row;
    }

    private JPanel createFormPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(Color.WHITE);
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(390, 450));

        JLabel heading = new JLabel("Chào mừng trở lại");
        heading.setFont(DialogUtils.font(Font.BOLD, 28f));
        heading.setForeground(TEXT_PRIMARY);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel description = new JLabel("Đăng nhập để tiếp tục quản lý dãy trọ của bạn");
        description.setFont(DialogUtils.font(Font.PLAIN, 14f));
        description.setForeground(TEXT_SECONDARY);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(heading);
        form.add(Box.createVerticalStrut(8));
        form.add(description);
        form.add(Box.createVerticalStrut(38));

        form.add(fieldLabel("Tên đăng nhập"));
        form.add(Box.createVerticalStrut(9));
        styleField(txtTenDangNhap, "Nhập tên đăng nhập", Icons.Type.USER);
        form.add(txtTenDangNhap);
        form.add(Box.createVerticalStrut(22));

        form.add(fieldLabel("Mật khẩu"));
        form.add(Box.createVerticalStrut(9));
        styleField(txtMatKhau, "Nhập mật khẩu", Icons.Type.LOCK);
        form.add(txtMatKhau);
        form.add(Box.createVerticalStrut(30));

        btnDangNhap.setFont(DialogUtils.font(Font.BOLD, 15f));
        btnDangNhap.setPreferredSize(new Dimension(390, 50));
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        form.add(btnDangNhap);
        form.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("RoomHub · Quản lý dãy trọ");
        footer.setFont(DialogUtils.font(Font.PLAIN, 12f));
        footer.setForeground(new Color(148, 163, 184));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(Box.createVerticalStrut(22));
        form.add(footer);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        wrap.add(form, gc);
        return wrap;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(DialogUtils.font(Font.BOLD, 13f));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field, String placeholder, Icons.Type icon) {
        field.setFont(DialogUtils.BODY_FONT);
        field.setPreferredSize(new Dimension(390, 48));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JTextField.leadingIcon", Icons.icon(icon, 17, TEXT_SECONDARY));
        if (field instanceof JPasswordField) {
            field.putClientProperty("JPasswordField.showRevealButton", true);
        }
    }

    private void xuLyDangNhap() {
        btnDangNhap.setEnabled(false);
        char[] password = txtMatKhau.getPassword();
        try {
            KetQuaDangNhap result = service.dangNhap(txtTenDangNhap.getText().trim(), password);
            if (!result.isThanhCong()) {
                JOptionPane.showMessageDialog(this, result.getThongBao(), "Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
                txtMatKhau.setText("");
                txtMatKhau.requestFocus();
                return;
            }
            JOptionPane.showMessageDialog(this,
                    "Xin chào " + com.quanlydaytro.util.QuanLyPhienDangNhap.getHoTen() + " (" + result.getTenVaiTro() + ")",
                    "Đăng nhập thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new TrangChinhFrame().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Không thể đăng nhập. Hãy kiểm tra kết nối SQL Server và dữ liệu tài khoản.\nChi tiết: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(password, '\0');
            btnDangNhap.setEnabled(true);
        }
    }
}