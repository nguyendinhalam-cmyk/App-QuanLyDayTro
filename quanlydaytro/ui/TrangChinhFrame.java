package com.quanlydaytro.ui;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import com.quanlydaytro.ui.dungchung.Icons;
import com.quanlydaytro.ui.quanlytaikhoan.TaiKhoanPanel;
import com.quanlydaytro.ui.quanlytaikhoan.NhanVienPanel;
import com.quanlydaytro.ui.quanlydaytro.ChuTroPanel;
import com.quanlydaytro.ui.quanlydaytro.DayTroPanel;
import com.quanlydaytro.ui.quanlyphong.PhongPanel;
import com.quanlydaytro.ui.quanlynguoithue.NguoiThuePanel;
import com.quanlydaytro.ui.quanlyhopdong.HopDongPanel;
import com.quanlydaytro.ui.quanlydiennuoc.BangGiaPanel;
import com.quanlydaytro.ui.quanlydiennuoc.ChiSoPanel;
import com.quanlydaytro.ui.quanlyhoadon.HoaDonPanel;
import com.quanlydaytro.ui.quanlyhoadon.ThanhToanPanel;
import com.quanlydaytro.ui.quanlytraphong.TraPhongPanel;
import com.quanlydaytro.ui.quanlychuyenphong.ChuyenPhongPanel;
import com.quanlydaytro.ui.thongke.BaoCaoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrangChinhFrame extends JFrame {

    private static final Color SIDEBAR_BG = DialogUtils.SIDEBAR_BG;
    private static final Color SIDEBAR_HOVER = DialogUtils.SIDEBAR_HOVER;
    private static final Color SIDEBAR_ACTIVE = DialogUtils.SIDEBAR_ACTIVE;
    private static final Color PRIMARY = DialogUtils.PRIMARY;
    private static final Color BG_CONTENT = DialogUtils.BG_CONTENT;
    private static final Color TEXT_PRIMARY = DialogUtils.TEXT_PRIMARY;
    private static final Color TEXT_SECONDARY = DialogUtils.TEXT_SECONDARY;
    private static final Color BORDER_COLOR = DialogUtils.BORDER_COLOR;
    private static final Color NAV_TEXT = new Color(203, 213, 225);
    private static final Color NAV_ICON = new Color(148, 163, 184);

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel lblHeaderTitle;
    private JLabel lblBreadcrumb;
    private Map<String, JPanel> panelCache = new HashMap<>();
    private NavItem activeMenuItem = null;
    private Map<String, JPanel> subMenuPanels = new LinkedHashMap<>();
    private Map<String, NavItem> navByKey = new HashMap<>();

    public TrangChinhFrame() {
        setTitle("RoomHub - Hệ thống quản lý dãy trọ");
        setSize(1400, 800);
        setMinimumSize(new Dimension(1200, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_CONTENT);

        add(createSidebar(), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(BG_CONTENT);
        right.add(createHeaderBar(), BorderLayout.NORTH);
        right.add(createContentArea(), BorderLayout.CENTER);
        add(right, BorderLayout.CENTER);

        NavItem home = navByKey.get("dashboard");
        if (home != null) setActiveItem(home);
        showPanel("dashboard", "Trang chủ");
    }

    // ====================== SIDEBAR ======================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(272, 0));
        sidebar.setBackground(SIDEBAR_BG);

        // Logo area
        JPanel logoPanel = new JPanel(new BorderLayout(12, 0));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(22, 22, 18, 20));

        JComponent mark = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setPaint(new GradientPaint(0, 0, new Color(99, 102, 241), 42, 42, new Color(139, 92, 246)));
                g2.fill(new RoundRectangle2D.Double(0, 0, 42, 42, 13, 13));
                Icons.paint(g2, Icons.Type.HOME, 9, 9, 24, Color.WHITE);
                g2.dispose();
            }
        };
        mark.setPreferredSize(new Dimension(42, 42));

        JPanel names = new JPanel();
        names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
        names.setOpaque(false);
        JLabel logoLabel = new JLabel("RoomHub");
        logoLabel.setFont(DialogUtils.font(Font.BOLD, 21f));
        logoLabel.setForeground(Color.WHITE);
        JLabel tagline = new JLabel("Quản lý dãy trọ thông minh");
        tagline.setFont(DialogUtils.font(Font.PLAIN, 11.5f));
        tagline.setForeground(NAV_ICON);
        names.add(Box.createVerticalGlue());
        names.add(logoLabel);
        names.add(tagline);
        names.add(Box.createVerticalGlue());

        logoPanel.add(mark, BorderLayout.WEST);
        logoPanel.add(names, BorderLayout.CENTER);

        // Navigation menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR_BG);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 10, 0));

        addSectionLabel(menuPanel, "TỔNG QUAN");
        addMenuItem(menuPanel, Icons.Type.DASHBOARD, "Trang chủ", "dashboard", null);
        addSectionLabel(menuPanel, "QUẢN LÝ");
        addMenuGroup(menuPanel, Icons.Type.USER, "Quản lý tài khoản", new String[][]{
                {"Tài khoản", "taikhoan"}, {"Nhân viên", "nhanvien"}
        });
        addMenuGroup(menuPanel, Icons.Type.BUILDING, "Quản lý dãy trọ", new String[][]{
                {"Chủ trọ", "chutro"}, {"Dãy trọ", "daytro"}
        });
        addMenuItem(menuPanel, Icons.Type.DOOR, "Quản lý phòng", "phong", null);
        addMenuItem(menuPanel, Icons.Type.PEOPLE, "Quản lý người thuê", "nguoithue", null);
        addMenuItem(menuPanel, Icons.Type.FILE, "Quản lý hợp đồng", "hopdong", null);
        addSectionLabel(menuPanel, "VẬN HÀNH");
        addMenuGroup(menuPanel, Icons.Type.BOLT, "Quản lý điện nước", new String[][]{
                {"Bảng giá", "banggia"}, {"Chỉ số điện nước", "chiso"}
        });
        addMenuGroup(menuPanel, Icons.Type.RECEIPT, "Quản lý hóa đơn", new String[][]{
                {"Hóa đơn", "hoadon"}, {"Thanh toán", "thanhtoan"}
        });
        addMenuItem(menuPanel, Icons.Type.KEY, "Trả phòng", "traphong", null);
        addMenuItem(menuPanel, Icons.Type.SWAP, "Chuyển phòng", "chuyenphong", null);
        addSectionLabel(menuPanel, "BÁO CÁO");
        addMenuItem(menuPanel, Icons.Type.CHART, "Thống kê & Báo cáo", "baocao", null);

        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.setBorder(null);
        menuScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuScroll.getVerticalScrollBar().setUnitIncrement(16);
        menuScroll.getVerticalScrollBar().putClientProperty("JScrollBar.showButtons", false);
        menuScroll.setBackground(SIDEBAR_BG);
        menuScroll.getViewport().setBackground(SIDEBAR_BG);

        sidebar.add(logoPanel, BorderLayout.NORTH);
        sidebar.add(menuScroll, BorderLayout.CENTER);
        sidebar.add(createUserPanel(), BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new BorderLayout(12, 0));
        userPanel.setBackground(SIDEBAR_BG);
        userPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(30, 41, 59)),
                BorderFactory.createEmptyBorder(16, 20, 16, 16)));

        JComponent avatar = createAvatar("AD", 40);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        JLabel userName = new JLabel("Admin");
        userName.setFont(DialogUtils.font(Font.BOLD, 13.5f));
        userName.setForeground(Color.WHITE);
        JLabel userRole = new JLabel("Quản trị viên");
        userRole.setFont(DialogUtils.font(Font.PLAIN, 11.5f));
        userRole.setForeground(NAV_ICON);
        namePanel.add(Box.createVerticalGlue());
        namePanel.add(userName);
        namePanel.add(userRole);
        namePanel.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton(Icons.icon(Icons.Type.LOGOUT, 20, new Color(248, 113, 113)));
        btnLogout.setToolTipText("Đăng xuất");
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(false);
        btnLogout.setPreferredSize(new Dimension(38, 38));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            if (DialogUtils.showConfirm(this, "Bạn có chắc chắn muốn đăng xuất?")) {
                dispose();
                new DangNhapFrame().setVisible(true);
            }
        });
        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogout.setIcon(Icons.icon(Icons.Type.LOGOUT, 20, new Color(254, 202, 202)));
            }

            public void mouseExited(MouseEvent e) {
                btnLogout.setIcon(Icons.icon(Icons.Type.LOGOUT, 20, new Color(248, 113, 113)));
            }
        });

        userPanel.add(avatar, BorderLayout.WEST);
        userPanel.add(namePanel, BorderLayout.CENTER);
        userPanel.add(btnLogout, BorderLayout.EAST);
        return userPanel;
    }

    private JComponent createAvatar(String initials, int size) {
        JComponent avatar = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setPaint(new GradientPaint(0, 0, new Color(99, 102, 241), size, size, new Color(168, 85, 247)));
                g2.fillOval(0, 0, size, size);
                g2.setColor(Color.WHITE);
                g2.setFont(DialogUtils.font(Font.BOLD, size * 0.36f));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(initials, (size - fm.stringWidth(initials)) / 2,
                        DialogUtils.centerBaseline(g2, 0, size));
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(size, size));
        return avatar;
    }

    private void addSectionLabel(JPanel parent, String text) {
        JLabel l = new JLabel(text);
        l.setFont(DialogUtils.font(Font.BOLD, 10.5f));
        l.setForeground(new Color(100, 116, 139));
        l.setBorder(BorderFactory.createEmptyBorder(14, 28, 6, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        parent.add(l);
    }

    /** Một dòng menu tự vẽ: icon + chữ + trạng thái hover/active. */
    private class NavItem extends JPanel {
        final Icons.Type icon;
        final String text;
        final boolean child;
        final boolean group;
        boolean expanded;
        boolean active;
        boolean hover;

        NavItem(Icons.Type icon, String text, boolean child, boolean group) {
            this.icon = icon;
            this.text = text;
            this.child = child;
            this.group = group;
            setOpaque(false);
            int h = child ? 40 : 44;
            setPreferredSize(new Dimension(272, h));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            DialogUtils.aa(g2);
            int w = getWidth(), h = getHeight();
            if (active) {
                g2.setColor(SIDEBAR_ACTIVE);
                g2.fill(new RoundRectangle2D.Double(14, 2, w - 28, h - 4, 12, 12));
            } else if (hover) {
                g2.setColor(SIDEBAR_HOVER);
                g2.fill(new RoundRectangle2D.Double(14, 2, w - 28, h - 4, 12, 12));
            }
            Color fg = active ? Color.WHITE : (hover ? Color.WHITE : NAV_TEXT);
            int tx;
            if (child) {
                g2.setColor(active ? Color.WHITE : new Color(100, 116, 139));
                g2.fillOval(44, h / 2 - 3, 6, 6);
                tx = 62;
            } else {
                Icons.paint(g2, icon, 30, (h - 20) / 2, 20, active ? Color.WHITE : (hover ? Color.WHITE : NAV_ICON));
                tx = 62;
            }
            g2.setColor(fg);
            g2.setFont(DialogUtils.font(active ? Font.BOLD : Font.PLAIN, 13.5f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, tx, DialogUtils.centerBaseline(g2, 0, h));
            if (group) {
                Icons.paint(g2, expanded ? Icons.Type.CHEVRON_DOWN : Icons.Type.CHEVRON_RIGHT,
                        w - 42, (h - 14) / 2, 14, NAV_ICON);
            }
            g2.dispose();
        }
    }

    private void addMenuItem(JPanel parent, Icons.Type icon, String text, String panelKey, String headerTitle) {
        NavItem item = new NavItem(icon, text, false, false);
        navByKey.put(panelKey, item);
        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                setActiveItem(item);
                showPanel(panelKey, headerTitle != null ? headerTitle : text);
            }
        });
        parent.add(item);
    }

    private void addMenuGroup(JPanel parent, Icons.Type icon, String groupText, String[][] children) {
        JPanel subMenu = new JPanel();
        subMenu.setLayout(new BoxLayout(subMenu, BoxLayout.Y_AXIS));
        subMenu.setBackground(SIDEBAR_BG);
        subMenu.setVisible(false);
        subMenu.setAlignmentX(Component.LEFT_ALIGNMENT);

        NavItem groupItem = new NavItem(icon, groupText, false, true);
        subMenuPanels.put(groupText, subMenu);

        groupItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                boolean visible = !subMenu.isVisible();
                subMenu.setVisible(visible);
                groupItem.expanded = visible;
                groupItem.repaint();
                parent.revalidate();
                parent.repaint();
            }
        });
        parent.add(groupItem);

        for (String[] child : children) {
            NavItem childItem = new NavItem(null, child[0], true, false);
            String childPanelKey = child[1];
            String childTitle = child[0];
            navByKey.put(childPanelKey, childItem);
            childItem.putClientProperty("group", groupItem);
            childItem.putClientProperty("groupText", groupText);
            childItem.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    setActiveItem(childItem);
                    showPanel(childPanelKey, childTitle);
                }
            });
            subMenu.add(childItem);
        }
        parent.add(subMenu);
    }

    private void setActiveItem(NavItem item) {
        if (activeMenuItem != null) {
            activeMenuItem.active = false;
            activeMenuItem.repaint();
        }
        activeMenuItem = item;
        item.active = true;
        item.repaint();

        // mở nhóm cha nếu chọn mục con
        Object g = item.getClientProperty("group");
        if (g instanceof NavItem groupItem) {
            JPanel sub = subMenuPanels.get((String) item.getClientProperty("groupText"));
            if (sub != null && !sub.isVisible()) {
                sub.setVisible(true);
                groupItem.expanded = true;
                groupItem.repaint();
                sub.getParent().revalidate();
            }
        }
    }

    // ====================== HEADER ======================
    private static String vietnameseDate(LocalDate d) {
        String[] days = {"Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy", "Chủ nhật"};
        return days[d.getDayOfWeek().getValue() - 1] + ", "
                + String.format("%02d/%02d/%d", d.getDayOfMonth(), d.getMonthValue(), d.getYear());
    }

    private JPanel createHeaderBar() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BORDER_COLOR);
                g.fillRect(0, getHeight() - 1, getWidth(), 1);
            }
        };
        header.setPreferredSize(new Dimension(0, 68));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 24));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        lblHeaderTitle = new JLabel("Trang chủ");
        lblHeaderTitle.setFont(DialogUtils.font(Font.BOLD, 19f));
        lblHeaderTitle.setForeground(TEXT_PRIMARY);
        lblBreadcrumb = new JLabel("RoomHub  /  Trang chủ");
        lblBreadcrumb.setFont(DialogUtils.font(Font.PLAIN, 12f));
        lblBreadcrumb.setForeground(TEXT_SECONDARY);
        titleBox.add(Box.createVerticalGlue());
        titleBox.add(lblHeaderTitle);
        titleBox.add(Box.createVerticalStrut(1));
        titleBox.add(lblBreadcrumb);
        titleBox.add(Box.createVerticalGlue());
        header.add(titleBox, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightPanel.setOpaque(false);

        // Ngày hôm nay
        JPanel datePill = new DialogUtils.RoundedPanel(new FlowLayout(FlowLayout.LEFT, 8, 8), 20);
        datePill.setBackground(new Color(248, 250, 252));
        datePill.add(new JLabel(Icons.icon(Icons.Type.CALENDAR, 16, TEXT_SECONDARY)));
        JLabel dateLbl = new JLabel(vietnameseDate(LocalDate.now()));
        dateLbl.setFont(DialogUtils.font(Font.PLAIN, 12.5f));
        dateLbl.setForeground(TEXT_SECONDARY);
        datePill.add(dateLbl);
        datePill.setPreferredSize(new Dimension(dateLbl.getPreferredSize().width + 54, 36));

        rightPanel.add(datePill);
        rightPanel.add(headerIconButton(Icons.Type.SEARCH, "Tìm kiếm", false));
        rightPanel.add(headerIconButton(Icons.Type.BELL, "Thông báo", true));

        JSeparator vsep = new JSeparator(SwingConstants.VERTICAL);
        vsep.setPreferredSize(new Dimension(1, 30));
        vsep.setForeground(BORDER_COLOR);
        rightPanel.add(vsep);

        JPanel userChip = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userChip.setOpaque(false);
        userChip.add(createAvatar("AD", 34));
        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);
        JLabel greeting = new JLabel("Xin chào, Admin");
        greeting.setFont(DialogUtils.font(Font.BOLD, 13f));
        greeting.setForeground(TEXT_PRIMARY);
        JLabel role = new JLabel("Quản trị viên");
        role.setFont(DialogUtils.font(Font.PLAIN, 11.5f));
        role.setForeground(TEXT_SECONDARY);
        txt.add(greeting);
        txt.add(role);
        userChip.add(txt);
        rightPanel.add(userChip);

        header.add(rightPanel, BorderLayout.EAST);
        // căn giữa theo chiều dọc
        rightPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
        return header;
    }

    private JComponent headerIconButton(Icons.Type type, String tip, boolean dot) {
        JComponent b = new JComponent() {
            boolean hover;

            {
                setPreferredSize(new Dimension(38, 38));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setToolTipText(tip);
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                if (hover) {
                    g2.setColor(new Color(241, 245, 249));
                    g2.fillOval(0, 0, 38, 38);
                }
                Icons.paint(g2, type, 9, 9, 20, TEXT_SECONDARY);
                if (dot) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(21, 6, 11, 11);
                    g2.setColor(DialogUtils.DANGER);
                    g2.fillOval(23, 8, 7, 7);
                }
                g2.dispose();
            }
        };
        return b;
    }

    // ====================== CONTENT ======================
    private JPanel createContentArea() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_CONTENT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add dashboard as default
        JPanel dash = createDashboard();
        panelCache.put("dashboard", dash);
        contentPanel.add(dash, "dashboard");
        return contentPanel;
    }

    /** Panel cuộn theo chiều dọc, luôn co giãn theo chiều ngang của viewport. */
    private static class ScrollablePanel extends JPanel implements Scrollable {
        ScrollablePanel(LayoutManager lm) {
            super(lm);
        }

        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 18; }
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 120; }
        public boolean getScrollableTracksViewportWidth() { return true; }
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport vp && vp.getHeight() > getPreferredSize().height;
        }
    }

    private JPanel createDashboard() {
        JPanel page = new ScrollablePanel(new GridBagLayout());
        page.setBackground(BG_CONTENT);
        page.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.weightx = 1;

        // --- Welcome banner ---
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setPaint(new GradientPaint(0, 0, new Color(67, 56, 202), getWidth(), getHeight(), new Color(124, 58, 237)));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(new Color(255, 255, 255, 22));
                g2.fillOval(getWidth() - 220, -90, 300, 300);
                g2.setColor(new Color(255, 255, 255, 16));
                g2.fillOval(getWidth() - 360, 40, 220, 220);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        banner.setOpaque(false);
        banner.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        JPanel bText = new JPanel();
        bText.setLayout(new BoxLayout(bText, BoxLayout.Y_AXIS));
        bText.setOpaque(false);
        JLabel welcome = new JLabel("Chào mừng đến với RoomHub!");
        welcome.setFont(DialogUtils.font(Font.BOLD, 26f));
        welcome.setForeground(Color.WHITE);
        JLabel desc = new JLabel("Tổng quan hệ thống quản lý dãy trọ  •  " + vietnameseDate(LocalDate.now()));
        desc.setFont(DialogUtils.font(Font.PLAIN, 14f));
        desc.setForeground(new Color(224, 231, 255));
        bText.add(welcome);
        bText.add(Box.createVerticalStrut(6));
        bText.add(desc);
        banner.add(bText, BorderLayout.CENTER);
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 18, 0);
        page.add(banner, gc);

        // --- Summary cards ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.add(DialogUtils.createSummaryCard("Tổng phòng", "50", PRIMARY, Icons.Type.DOOR));
        cardsPanel.add(DialogUtils.createSummaryCard("Phòng đang thuê", "42", DialogUtils.SUCCESS, Icons.Type.KEY));
        cardsPanel.add(DialogUtils.createSummaryCard("Phòng trống", "8", DialogUtils.WARNING, Icons.Type.HOME));
        cardsPanel.add(DialogUtils.createSummaryCard("Doanh thu tháng", "125,000,000 VNĐ", DialogUtils.PURPLE, Icons.Type.MONEY));
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 16, 0);
        page.add(cardsPanel, gc);

        JPanel cardsPanel2 = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsPanel2.setOpaque(false);
        cardsPanel2.add(DialogUtils.createSummaryCard("Người thuê", "68", DialogUtils.INFO, Icons.Type.PEOPLE));
        cardsPanel2.add(DialogUtils.createSummaryCard("Hợp đồng hiệu lực", "42", DialogUtils.SUCCESS, Icons.Type.FILE));
        cardsPanel2.add(DialogUtils.createSummaryCard("Hóa đơn chưa TT", "12", DialogUtils.WARNING, Icons.Type.RECEIPT));
        cardsPanel2.add(DialogUtils.createSummaryCard("Công nợ", "18,500,000 VNĐ", DialogUtils.DANGER, Icons.Type.MONEY));
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, 18, 0);
        page.add(cardsPanel2, gc);

        // --- Charts row ---
        JPanel chartsRow = new JPanel(new GridBagLayout());
        chartsRow.setOpaque(false);
        GridBagConstraints cc = new GridBagConstraints();
        cc.fill = GridBagConstraints.BOTH;
        cc.gridy = 0;
        cc.weighty = 1;
        cc.gridx = 0;
        cc.weightx = 0.62;
        cc.insets = new Insets(0, 0, 0, 16);
        chartsRow.add(chartCard("Doanh thu 6 tháng gần nhất (triệu đồng)", new RevenueChart()), cc);
        cc.gridx = 1;
        cc.weightx = 0.38;
        cc.insets = new Insets(0, 0, 0, 0);
        chartsRow.add(chartCard("Tình trạng phòng", new OccupancyDonut()), cc);
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, 18, 0);
        page.add(chartsRow, gc);

        // --- Recent activities ---
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
                DialogUtils.createCardBorder("Hoạt động gần đây", PRIMARY),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        String[][] recentData = {
                {"1", "Lập hóa đơn tháng 10/2024", "Phòng P101 - P110", "Hôm nay"},
                {"2", "Thanh toán tiền phòng", "Nguyễn Văn A - P101", "Hôm nay"},
                {"3", "Ghi chỉ số điện nước", "Dãy A - Tháng 10", "Hôm qua"},
                {"4", "Hợp đồng mới", "Trần Thị B - P205", "Hôm qua"},
                {"5", "Trả phòng", "Lê Văn C - P103", "2 ngày trước"},
        };
        String[] recentCols = {"STT", "Hoạt động", "Chi tiết", "Thời gian"};
        javax.swing.table.DefaultTableModel recentModel = new javax.swing.table.DefaultTableModel(recentData, recentCols);
        JTable recentTable = DialogUtils.createStyledTable(recentModel);
        recentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        recentTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        recentTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        recentTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        recentTable.setFillsViewportHeight(false);

        // Bảng nằm trực tiếp trong card: header + hàng, không cuộn riêng
        JPanel tableBox = new JPanel(new BorderLayout());
        tableBox.setOpaque(false);
        tableBox.add(recentTable.getTableHeader(), BorderLayout.NORTH);
        tableBox.add(recentTable, BorderLayout.CENTER);
        recentPanel.add(tableBox, BorderLayout.CENTER);

        gc.gridy = 4;
        gc.insets = new Insets(0, 0, 0, 0);
        page.add(recentPanel, gc);

        // phần đệm để nội dung luôn bám lên trên
        gc.gridy = 5;
        gc.weighty = 1;
        page.add(Box.createGlue(), gc);

        JScrollPane scroll = new JScrollPane(page);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getViewport().setBackground(BG_CONTENT);

        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(BG_CONTENT);
        dashboard.add(scroll, BorderLayout.CENTER);
        return dashboard;
    }

    private JPanel chartCard(String title, JComponent chart) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                DialogUtils.createCardBorder(title, PRIMARY),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.add(chart, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(300, 300));
        return card;
    }

    /** Biểu đồ cột doanh thu (vẽ bằng Java2D, có hiệu ứng hover). */
    private static class RevenueChart extends JComponent {
        private final String[] labels = {"T5", "T6", "T7", "T8", "T9", "T10"};
        private final double[] values = {148.0, 152.4, 157.5, 162.8, 165.9, 173.1};
        private int hoverIdx = -1;

        RevenueChart() {
            setOpaque(false);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int idx = indexAt(e.getX());
                    if (idx != hoverIdx) {
                        hoverIdx = idx;
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    hoverIdx = -1;
                    repaint();
                }
            });
        }

        private Rectangle plot() {
            return new Rectangle(46, 14, Math.max(10, getWidth() - 60), Math.max(10, getHeight() - 14 - 30));
        }

        private int indexAt(int x) {
            Rectangle p = plot();
            if (x < p.x || x > p.x + p.width) return -1;
            double slot = p.width / (double) values.length;
            return Math.min(values.length - 1, (int) ((x - p.x) / slot));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            DialogUtils.aa(g2);
            Rectangle p = plot();
            double max = 200;
            g2.setFont(DialogUtils.font(Font.PLAIN, 11.5f));
            FontMetrics fm = g2.getFontMetrics();

            for (int i = 0; i <= 4; i++) {
                double v = max / 4 * i;
                int y = (int) (p.y + p.height - v / max * p.height);
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f,
                        i == 0 ? null : new float[]{4f, 4f}, 0f));
                g2.draw(new Line2D.Double(p.x, y, p.x + p.width, y));
                g2.setColor(TEXT_SECONDARY);
                String s = String.valueOf((int) v);
                g2.drawString(s, p.x - 10 - fm.stringWidth(s), y + fm.getAscent() / 2 - 1);
            }
            g2.setStroke(new BasicStroke(1f));

            double slot = p.width / (double) values.length;
            double bw = Math.min(44, slot * 0.5);
            for (int i = 0; i < values.length; i++) {
                double bh = values[i] / max * p.height;
                double bx = p.x + slot * i + (slot - bw) / 2;
                double by = p.y + p.height - bh;
                boolean last = i == values.length - 1;
                Color top = last || i == hoverIdx ? new Color(99, 102, 241) : new Color(165, 180, 252);
                Color bottom = last || i == hoverIdx ? new Color(67, 56, 202) : new Color(199, 210, 254);
                g2.setPaint(new GradientPaint(0, (float) by, top, 0, (float) (by + bh), bottom));
                g2.fill(new RoundRectangle2D.Double(bx, by, bw, bh + 8, 10, 10));
                // che phần bo góc phía dưới
                g2.setColor(Color.WHITE);
                g2.fillRect((int) bx - 1, p.y + p.height, (int) bw + 2, 10);

                g2.setColor(i == hoverIdx || last ? TEXT_PRIMARY : TEXT_SECONDARY);
                g2.setFont(DialogUtils.font(i == hoverIdx || last ? Font.BOLD : Font.PLAIN, 11.5f));
                String lbl = labels[i];
                g2.drawString(lbl, (int) (bx + (bw - g2.getFontMetrics().stringWidth(lbl)) / 2), p.y + p.height + 20);

                if (i == hoverIdx || last) {
                    String v = String.format("%.1f", values[i]);
                    int tw = g2.getFontMetrics().stringWidth(v) + 16;
                    int tx = (int) (bx + bw / 2 - tw / 2.0);
                    int ty = (int) by - 28;
                    g2.setColor(TEXT_PRIMARY);
                    g2.fillRoundRect(tx, ty, tw, 22, 8, 8);
                    g2.setColor(Color.WHITE);
                    g2.drawString(v, tx + 8, ty + 15);
                }
            }
            g2.dispose();
        }
    }

    /** Biểu đồ tròn tình trạng phòng. */
    private static class OccupancyDonut extends JComponent {
        private final String[] names = {"Đang thuê", "Phòng trống", "Bảo trì"};
        private final int[] counts = {42, 6, 2};
        private final Color[] colors = {PRIMARY, DialogUtils.SUCCESS, DialogUtils.WARNING};

        OccupancyDonut() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            DialogUtils.aa(g2);
            int total = 0;
            for (int c : counts) total += c;

            int w = getWidth(), h = getHeight();
            int d = Math.max(60, Math.min(h - 70, w - 20));
            int x = (w - d) / 2, y = 6;
            float thick = Math.max(16f, d * 0.16f);

            g2.setStroke(new BasicStroke(thick, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            g2.setColor(new Color(241, 245, 249));
            g2.draw(new Arc2D.Double(x + thick / 2, y + thick / 2, d - thick, d - thick, 0, 360, Arc2D.OPEN));
            double start = 90;
            for (int i = 0; i < counts.length; i++) {
                double ext = -360.0 * counts[i] / total;
                g2.setColor(colors[i]);
                double gap = counts.length > 1 ? 2 : 0;
                g2.draw(new Arc2D.Double(x + thick / 2, y + thick / 2, d - thick, d - thick, start - gap / 2, ext + gap, Arc2D.OPEN));
                start += ext;
            }

            String pct = Math.round(100.0 * counts[0] / total) + "%";
            g2.setColor(TEXT_PRIMARY);
            g2.setFont(DialogUtils.font(Font.BOLD, Math.max(18f, d * 0.19f)));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(pct, x + (d - fm.stringWidth(pct)) / 2, DialogUtils.centerBaseline(g2, y, d) - 8);
            g2.setColor(TEXT_SECONDARY);
            g2.setFont(DialogUtils.font(Font.PLAIN, 12f));
            fm = g2.getFontMetrics();
            String sub = "Tỷ lệ lấp đầy";
            g2.drawString(sub, x + (d - fm.stringWidth(sub)) / 2, DialogUtils.centerBaseline(g2, y, d) + 18);

            // legend
            g2.setFont(DialogUtils.font(Font.PLAIN, 12.5f));
            fm = g2.getFontMetrics();
            int[] lw = new int[names.length];
            int totalW = 0;
            for (int i = 0; i < names.length; i++) {
                lw[i] = 14 + fm.stringWidth(names[i] + " " + counts[i]) + 18;
                totalW += lw[i];
            }
            int lx = Math.max(0, (w - totalW) / 2);
            int ly = y + d + 26;
            for (int i = 0; i < names.length; i++) {
                g2.setColor(colors[i]);
                g2.fillOval(lx, ly - 8, 9, 9);
                g2.setColor(TEXT_PRIMARY);
                g2.drawString(names[i] + " " + counts[i], lx + 14, ly);
                lx += lw[i];
            }
            g2.dispose();
        }
    }

    // ====================== PANEL LOADING ======================
    private void showPanel(String panelKey, String title) {
        lblHeaderTitle.setText(title);
        lblBreadcrumb.setText("RoomHub  /  " + title);

        if (!panelCache.containsKey(panelKey)) {
            JPanel panel = createPanel(panelKey);
            if (panel != null) {
                panelCache.put(panelKey, panel);
                contentPanel.add(wrapPage(panel), panelKey);
            }
        }
        cardLayout.show(contentPanel, panelKey);
    }

    /** Thêm lề ngoài đồng nhất cho các panel chức năng. */
    private JPanel wrapPage(JPanel panel) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG_CONTENT);
        wrap.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        wrap.add(panel, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel createPanel(String key) {
        return switch (key) {
            case "dashboard" -> createDashboard();
            case "taikhoan" -> new TaiKhoanPanel();
            case "nhanvien" -> new NhanVienPanel();
            case "chutro" -> new ChuTroPanel();
            case "daytro" -> new DayTroPanel();
            case "phong" -> new PhongPanel();
            case "nguoithue" -> new NguoiThuePanel();
            case "hopdong" -> new HopDongPanel();
            case "banggia" -> new BangGiaPanel();
            case "chiso" -> new ChiSoPanel();
            case "hoadon" -> new HoaDonPanel();
            case "thanhtoan" -> new ThanhToanPanel();
            case "traphong" -> new TraPhongPanel();
            case "chuyenphong" -> new ChuyenPhongPanel();
            case "baocao" -> new BaoCaoPanel();
            default -> null;
        };
    }
}
