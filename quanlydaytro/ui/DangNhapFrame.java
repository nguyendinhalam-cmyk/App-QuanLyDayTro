package com.quanlydaytro.ui;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import com.quanlydaytro.ui.dungchung.Icons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class DangNhapFrame extends JFrame {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JCheckBox chkGhiNho;
    private JButton btnDangNhap;

    private static final Color PRIMARY = DialogUtils.PRIMARY;
    private static final Color PRIMARY_DARK = DialogUtils.PRIMARY_DARK;
    private static final Color TEXT_PRIMARY = DialogUtils.TEXT_PRIMARY;
    private static final Color TEXT_SECONDARY = DialogUtils.TEXT_SECONDARY;

    public DangNhapFrame() {
        setTitle("RoomHub - Đăng nhập");
        setSize(1000, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(createBrandPanel());
        root.add(createFormPanel());
        setContentPane(root);

        // Enter key triggers login
        getRootPane().setDefaultButton(btnDangNhap);
    }

    // ===================== LEFT: BRAND =====================
    private JPanel createBrandPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                int w = getWidth(), h = getHeight();
                g2.setPaint(new GradientPaint(0, 0, new Color(67, 56, 202), w, h, new Color(124, 58, 237)));
                g2.fillRect(0, 0, w, h);
                // decorative circles
                g2.setColor(new Color(255, 255, 255, 22));
                g2.fillOval(-120, h - 260, 380, 380);
                g2.setColor(new Color(255, 255, 255, 16));
                g2.fillOval(w - 220, -140, 380, 380);
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(w - 140, h - 200, 220, 220);
                g2.dispose();
            }
        };
        p.setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        JComponent logo = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fill(new RoundRectangle2D.Double(0, 0, 64, 64, 20, 20));
                Icons.paint(g2, Icons.Type.HOME, 16, 16, 32, Color.WHITE);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(64, 64));
        logo.setMaximumSize(new Dimension(64, 64));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel appName = new JLabel("RoomHub");
        appName.setFont(DialogUtils.font(Font.BOLD, 42f));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("<html>Hệ thống quản lý dãy trọ<br>thông minh & hiện đại</html>");
        sub.setFont(DialogUtils.font(Font.PLAIN, 18f));
        sub.setForeground(new Color(224, 231, 255));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        box.add(logo);
        box.add(Box.createVerticalStrut(24));
        box.add(appName);
        box.add(Box.createVerticalStrut(10));
        box.add(sub);
        box.add(Box.createVerticalStrut(40));
        box.add(feature(Icons.Type.DOOR, "Quản lý phòng, người thuê & hợp đồng"));
        box.add(Box.createVerticalStrut(14));
        box.add(feature(Icons.Type.BOLT, "Ghi điện nước, lập hóa đơn tự động"));
        box.add(Box.createVerticalStrut(14));
        box.add(feature(Icons.Type.CHART, "Báo cáo doanh thu & công nợ trực quan"));

        p.add(box);
        return p;
    }

    private JPanel feature(Icons.Type type, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(420, 36));
        JComponent ic = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setColor(new Color(255, 255, 255, 38));
                g2.fillOval(0, 0, 34, 34);
                Icons.paint(g2, type, 8, 8, 18, Color.WHITE);
                g2.dispose();
            }
        };
        ic.setPreferredSize(new Dimension(34, 34));
        JLabel l = new JLabel(text);
        l.setFont(DialogUtils.font(Font.PLAIN, 14f));
        l.setForeground(Color.WHITE);
        row.add(ic);
        row.add(l);
        return row;
    }

    // ===================== RIGHT: FORM =====================
    private JPanel createFormPanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(360, 430));

        JLabel hello = new JLabel("Chào mừng trở lại");
        hello.setFont(DialogUtils.font(Font.BOLD, 26f));
        hello.setForeground(TEXT_PRIMARY);
        hello.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("Đăng nhập để tiếp tục quản lý dãy trọ của bạn");
        desc.setFont(DialogUtils.font(Font.PLAIN, 13.5f));
        desc.setForeground(TEXT_SECONDARY);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(hello);
        form.add(Box.createVerticalStrut(6));
        form.add(desc);
        form.add(Box.createVerticalStrut(32));

        // === Username ===
        form.add(label("Tên đăng nhập"));
        form.add(Box.createVerticalStrut(8));
        txtTenDangNhap = new JTextField();
        styleInput(txtTenDangNhap);
        txtTenDangNhap.putClientProperty("JTextField.placeholderText", "Nhập tên đăng nhập");
        txtTenDangNhap.putClientProperty("JTextField.leadingIcon", Icons.icon(Icons.Type.USER, 16, TEXT_SECONDARY));
        form.add(txtTenDangNhap);
        form.add(Box.createVerticalStrut(18));

        // === Password ===
        form.add(label("Mật khẩu"));
        form.add(Box.createVerticalStrut(8));
        txtMatKhau = new JPasswordField();
        styleInput(txtMatKhau);
        txtMatKhau.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");
        txtMatKhau.putClientProperty("JTextField.leadingIcon", Icons.icon(Icons.Type.LOCK, 16, TEXT_SECONDARY));
        txtMatKhau.putClientProperty("JPasswordField.showRevealButton", true);
        form.add(txtMatKhau);
        form.add(Box.createVerticalStrut(14));

        // === Remember + Forgot ===
        JPanel optPanel = new JPanel(new BorderLayout());
        optPanel.setOpaque(false);
        optPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        optPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkGhiNho = new JCheckBox("Ghi nhớ đăng nhập");
        chkGhiNho.setFont(DialogUtils.font(Font.PLAIN, 12.5f));
        chkGhiNho.setOpaque(false);
        chkGhiNho.setForeground(TEXT_SECONDARY);
        chkGhiNho.setFocusPainted(false);
        optPanel.add(chkGhiNho, BorderLayout.WEST);

        JLabel lblForgot = new JLabel("Quên mật khẩu?");
        lblForgot.setFont(DialogUtils.font(Font.BOLD, 12.5f));
        lblForgot.setForeground(PRIMARY);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(DangNhapFrame.this,
                        "Vui lòng liên hệ quản trị viên để đặt lại mật khẩu.",
                        "Quên mật khẩu", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblForgot.setText("<html><u>Quên mật khẩu?</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblForgot.setText("Quên mật khẩu?");
            }
        });
        optPanel.add(lblForgot, BorderLayout.EAST);
        form.add(optPanel);
        form.add(Box.createVerticalStrut(26));

        // === Login Button ===
        btnDangNhap = DialogUtils.createStyledButton("ĐĂNG NHẬP", PRIMARY);
        btnDangNhap.setFont(DialogUtils.font(Font.BOLD, 14.5f));
        btnDangNhap.setPreferredSize(new Dimension(360, 48));
        btnDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        form.add(btnDangNhap);
        form.add(Box.createVerticalGlue());

        // === Footer ===
        JLabel footer = new JLabel("© 2024 RoomHub - HoangViet");
        footer.setFont(DialogUtils.font(Font.PLAIN, 11.5f));
        footer.setForeground(new Color(148, 163, 184));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(Box.createVerticalStrut(20));
        form.add(footer);

        wrap.add(form);
        return wrap;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(DialogUtils.font(Font.BOLD, 13f));
        l.setForeground(TEXT_PRIMARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleInput(JTextField f) {
        f.setFont(DialogUtils.BODY_FONT);
        f.setPreferredSize(new Dimension(360, 46));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void xuLyDangNhap() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenDangNhap.requestFocus();
            return;
        }
        if (matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.requestFocus();
            return;
        }

        // Placeholder: accept any login
        TrangChinhFrame trangChinh = new TrangChinhFrame();
        trangChinh.setVisible(true);
        this.dispose();
    }
}
