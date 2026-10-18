package com.quanlydaytro.ui;

import com.quanlydaytro.ui.dungchung.DialogUtils;
import com.quanlydaytro.ui.dungchung.Icons;
import com.quanlydaytro.ui.quanlytaikhoan.NhanVienPanel;
import com.quanlydaytro.ui.quanlytaikhoan.TaiKhoanPanel;
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
import com.quanlydaytro.ui.tracuu.TraCuuPanel;
import com.quanlydaytro.ui.thongke.BaoCaoPanel;
import com.quanlydaytro.service.BaoCaoService;
import com.quanlydaytro.service.PhongService;
import com.quanlydaytro.service.HopDongService;
import com.quanlydaytro.service.NguoiThueService;
import com.quanlydaytro.service.HoaDonService;
import com.quanlydaytro.model.Phong;
import com.quanlydaytro.model.HopDong;
import com.quanlydaytro.model.HoaDon;
import com.quanlydaytro.enums.TrangThaiPhong;
import com.quanlydaytro.enums.TrangThaiHopDong;
import com.quanlydaytro.enums.TrangThaiHoaDon;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.YearMonth;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Khung RoomHub với sidebar và các panel nghiệp vụ hiện có. */
public class TrangChinhFrame extends JFrame {
    private static final Color SIDEBAR = new Color(15, 23, 42);
    private static final Color SIDEBAR_HOVER = new Color(30, 41, 59);
    private static final Color ACTIVE = DialogUtils.PRIMARY;
    private static final Color CONTENT = new Color(241, 245, 249);
    private static final Color TEXT = new Color(30, 41, 59);
    private static final Color MUTED = new Color(100, 116, 139);
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final JLabel title = new JLabel("Trang chủ");
    private final JLabel breadcrumb = new JLabel("RoomHub  /  Trang chủ");
    private final Map<String, Supplier<? extends JPanel>> pages = new LinkedHashMap<>();
    private final Map<String, JPanel> cache = new LinkedHashMap<>();
    private final Map<String, NavButton> navButtons = new LinkedHashMap<>();

    public TrangChinhFrame() {
        setTitle("RoomHub - Hệ thống quản lý dãy trọ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1440, 860);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CONTENT);
        add(createSidebar(), BorderLayout.WEST);
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(CONTENT);
        right.add(createHeader(), BorderLayout.NORTH);
        content.setBackground(CONTENT);
        JPanel home = createHome();
        content.add(home, "home");
        cache.put("home", home);
        right.add(content, BorderLayout.CENTER);
        add(right, BorderLayout.CENTER);
        open("home", "Trang chủ");
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(278, 0));
        side.setBackground(SIDEBAR);
        JPanel brand = new JPanel(new BorderLayout(12, 0));
        brand.setOpaque(false);
        brand.setBorder(BorderFactory.createEmptyBorder(22, 22, 20, 14));
        brand.add(iconBadge(Icons.Type.HOME, ACTIVE, 46, 25), BorderLayout.WEST);
        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("RoomHub");
        logo.setForeground(Color.WHITE);
        logo.setFont(DialogUtils.font(Font.BOLD, 22f));
        JLabel tagline = new JLabel("Quản lý dãy trọ thông minh");
        tagline.setForeground(new Color(148, 163, 184));
        tagline.setFont(DialogUtils.font(Font.PLAIN, 11.5f));
        brandText.add(Box.createVerticalGlue());
        brandText.add(logo);
        brandText.add(tagline);
        brandText.add(Box.createVerticalGlue());
        brand.add(brandText, BorderLayout.CENTER);
        side.add(brand, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setBackground(SIDEBAR);
        menu.setBorder(BorderFactory.createEmptyBorder(4, 12, 14, 12));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        section(menu, "TỔNG QUAN");
        addNav(menu, "Trang chủ", "home", null, Icons.Type.DASHBOARD, this::createHome);
        section(menu, "QUẢN LÝ");
        addNav(menu, "Tài khoản", "taikhoan", "AdminOnly", Icons.Type.USER, TaiKhoanPanel::new);
        addNav(menu, "Nhân viên", "nhanvien", "AdminOnly", Icons.Type.PEOPLE, NhanVienPanel::new);
        addNav(menu, "Chủ trọ", "chutro", "Admin", Icons.Type.USER, ChuTroPanel::new);
        addNav(menu, "Dãy trọ", "daytro", "Admin", Icons.Type.BUILDING, DayTroPanel::new);
        addNav(menu, "Phòng", "phong", "Admin", Icons.Type.DOOR, PhongPanel::new);
        addNav(menu, "Người thuê", "nguoithue", "Admin", Icons.Type.PEOPLE, NguoiThuePanel::new);
        addNav(menu, "Hợp đồng", "hopdong", "Admin", Icons.Type.FILE, HopDongPanel::new);
        section(menu, "VẬN HÀNH");
        addNav(menu, "Bảng giá điện nước", "banggia", "Admin", Icons.Type.BOLT, BangGiaPanel::new);
        addNav(menu, "Chỉ số điện nước", "chiso", "Admin", Icons.Type.BOLT, ChiSoPanel::new);
        addNav(menu, "Hóa đơn", "hoadon", "Admin", Icons.Type.RECEIPT, HoaDonPanel::new);
        addNav(menu, "Thanh toán & công nợ", "thanhtoan", "Admin", Icons.Type.MONEY, ThanhToanPanel::new);
        addNav(menu, "Trả phòng", "traphong", "Admin", Icons.Type.KEY, TraPhongPanel::new);
        addNav(menu, "Chuyển phòng", "chuyenphong", "Admin", Icons.Type.SWAP, ChuyenPhongPanel::new);
        addNav(menu, "Tra cứu", "tracuu", "Admin", Icons.Type.SEARCH, TraCuuPanel::new);
        section(menu, "BÁO CÁO");
        addNav(menu, "Thống kê & báo cáo", "baocao", "Admin", Icons.Type.CHART, BaoCaoPanel::new);

        JScrollPane scroll = new JScrollPane(menu);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(SIDEBAR);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        side.add(scroll, BorderLayout.CENTER);
        side.add(createUserFooter(), BorderLayout.SOUTH);
        return side;
    }

    private JPanel createUserFooter() {
        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setBackground(SIDEBAR);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(51, 65, 85)),
                BorderFactory.createEmptyBorder(14, 18, 14, 12)));
        String name = QuanLyPhienDangNhap.getHoTen();
        if (name == null || name.isBlank()) name = QuanLyPhienDangNhap.getTenDangNhap();
        String initials = name == null || name.isBlank() ? "RH" : name.substring(0, 1).toUpperCase();
        JLabel avatar = new JLabel(initials, SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(124, 58, 237));
        avatar.setForeground(Color.WHITE);
        avatar.setFont(DialogUtils.font(Font.BOLD, 15f));
        avatar.setPreferredSize(new Dimension(40, 40));
        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));
        JLabel user = new JLabel(name == null ? "Người dùng" : name);
        user.setForeground(Color.WHITE);
        user.setFont(DialogUtils.font(Font.BOLD, 12.5f));
        JLabel role = new JLabel(QuanLyPhienDangNhap.getTenVaiTro());
        role.setForeground(new Color(148, 163, 184));
        role.setFont(DialogUtils.font(Font.PLAIN, 11f));
        identity.add(Box.createVerticalGlue());
        identity.add(user);
        identity.add(role);
        identity.add(Box.createVerticalGlue());
        JButton logout = new JButton(Icons.icon(Icons.Type.LOGOUT, 19, new Color(248, 113, 113)));
        logout.setToolTipText("Đăng xuất");
        logout.setBorderPainted(false);
        logout.setFocusPainted(false);
        logout.setContentAreaFilled(false);
        logout.addActionListener(e -> {
            QuanLyPhienDangNhap.ketThuc();
            dispose();
            new DangNhapFrame().setVisible(true);
        });
        footer.add(avatar, BorderLayout.WEST);
        footer.add(identity, BorderLayout.CENTER);
        footer.add(logout, BorderLayout.EAST);
        return footer;
    }

    private void section(JPanel menu, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(100, 116, 139));
        label.setFont(DialogUtils.font(Font.BOLD, 10.5f));
        label.setBorder(BorderFactory.createEmptyBorder(15, 10, 7, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(label);
    }

    private void addNav(JPanel menu, String text, String key, String role, Icons.Type icon,
                        Supplier<? extends JPanel> factory) {
        if ("AdminOnly".equals(role) && !QuanLyPhienDangNhap.coVaiTro("Admin")) return;
        if (role != null && !"AdminOnly".equals(role)
                && !QuanLyPhienDangNhap.coVaiTro(role, "Manager")) return;
        pages.put(key, factory);
        NavButton button = new NavButton(text, icon);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.addActionListener(e -> open(key, text));
        navButtons.put(key, button);
        menu.add(button);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(13, 24, 13, 24)));
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        title.setFont(DialogUtils.font(Font.BOLD, 20f));
        title.setForeground(TEXT);
        breadcrumb.setText("RoomHub  /  Trang chủ");
        breadcrumb.setName("breadcrumb");
        breadcrumb.setForeground(MUTED);
        breadcrumb.setFont(DialogUtils.font(Font.PLAIN, 11.5f));
        titleBlock.add(title);
        titleBlock.add(breadcrumb);
        header.add(titleBlock, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 5));
        right.setOpaque(false);
        JLabel date = new JLabel("▢  " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")));
        date.setForeground(MUTED);
        date.setFont(DialogUtils.font(Font.PLAIN, 12f));
        JLabel greeting = new JLabel("Xin chào, " + (QuanLyPhienDangNhap.getHoTen() == null
                ? QuanLyPhienDangNhap.getTenDangNhap() : QuanLyPhienDangNhap.getHoTen()));
        greeting.setForeground(TEXT);
        greeting.setFont(DialogUtils.font(Font.BOLD, 12.5f));
        right.add(date);
        right.add(greeting);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createHome() {
        JPanel dashboard = new JPanel();
        dashboard.setBackground(CONTENT);
        dashboard.setBorder(BorderFactory.createEmptyBorder(22, 26, 24, 26));
        dashboard.setLayout(new BoxLayout(dashboard, BoxLayout.Y_AXIS));

        JPanel banner = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                DialogUtils.aa(g2);
                g2.setPaint(new GradientPaint(0, 0, new Color(67, 56, 202), getWidth(), getHeight(), new Color(124, 58, 237)));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(new Color(255, 255, 255, 24));
                g2.fillOval(getWidth() - 220, -100, 300, 300);
                g2.fillOval(getWidth() - 60, 35, 190, 190);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        banner.setOpaque(false);
        banner.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        JPanel bannerText = new JPanel();
        bannerText.setOpaque(false);
        bannerText.setLayout(new BoxLayout(bannerText, BoxLayout.Y_AXIS));
        JLabel welcome = new JLabel("Chào mừng đến với RoomHub!");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(DialogUtils.font(Font.BOLD, 26f));
        JLabel desc = new JLabel("Tổng quan hệ thống quản lý dãy trọ  •  " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")));
        desc.setForeground(new Color(224, 231, 255));
        desc.setFont(DialogUtils.font(Font.PLAIN, 14f));
        bannerText.add(welcome);
        bannerText.add(Box.createVerticalStrut(7));
        bannerText.add(desc);
        banner.add(bannerText, BorderLayout.CENTER);
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashboard.add(banner);
        dashboard.add(Box.createVerticalStrut(18));

        DashboardData data = loadDashboardData();
        JPanel metrics = new JPanel(new GridLayout(2, 4, 14, 14));
        metrics.setOpaque(false);
        metrics.setAlignmentX(Component.LEFT_ALIGNMENT);
        metrics.add(metricCard("Tổng phòng", data.roomsAvailable ? String.valueOf(data.totalRooms) : "—", Icons.Type.DOOR, new Color(79, 70, 229)));
        metrics.add(metricCard("Phòng đang thuê", data.roomsAvailable ? String.valueOf(data.rentedRooms) : "—", Icons.Type.KEY, new Color(5, 150, 105)));
        metrics.add(metricCard("Phòng trống", data.roomsAvailable ? String.valueOf(data.emptyRooms) : "—", Icons.Type.HOME, new Color(217, 119, 6)));
        metrics.add(metricCard("Doanh thu tháng", data.revenueAvailable ? money(data.monthlyIncome) : "—", Icons.Type.MONEY, new Color(124, 58, 237)));
        metrics.add(metricCard("Người thuê", data.peopleAvailable ? String.valueOf(data.tenants) : "—", Icons.Type.PEOPLE, new Color(8, 145, 178)));
        metrics.add(metricCard("Hợp đồng hiệu lực", data.contractsAvailable ? String.valueOf(data.activeContracts) : "—", Icons.Type.FILE, new Color(5, 150, 105)));
        metrics.add(metricCard("Hóa đơn chưa hoàn tất", data.invoiceAvailable ? String.valueOf(data.pendingInvoices) : "—", Icons.Type.RECEIPT, new Color(217, 119, 6)));
        metrics.add(metricCard("Công nợ 6 tháng", data.revenueAvailable ? money(data.debt) : "—", Icons.Type.MONEY, new Color(220, 38, 38)));
        dashboard.add(metrics);
        dashboard.add(Box.createVerticalStrut(18));

        JPanel charts = new JPanel(new GridLayout(1, 2, 16, 0));
        charts.setOpaque(false);
        charts.setAlignmentX(Component.LEFT_ALIGNMENT);
        charts.add(chartCard("Doanh thu 6 tháng gần nhất (triệu đồng)", new RevenueChart(data.revenueAvailable ? data.revenueLabels : List.of(), data.revenueAvailable ? data.revenueValues : List.of())));
        charts.add(chartCard("Tình trạng phòng", new OccupancyChart(data.roomsAvailable ? data.rentedRooms : -1, data.roomsAvailable ? data.emptyRooms : -1, data.roomsAvailable ? data.maintenanceRooms : -1)));
        dashboard.add(charts);
        dashboard.add(Box.createVerticalStrut(18));

        JPanel recent = recentInvoices(data.recentInvoices);
        recent.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashboard.add(recent);

        JScrollPane scroll = new JScrollPane(dashboard);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CONTENT);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return new JPanel(new BorderLayout()) {{ setBackground(CONTENT); add(scroll, BorderLayout.CENTER); }};
    }

    private DashboardData loadDashboardData() {
        DashboardData d = new DashboardData();
        try {
            List<Phong> rooms = new PhongService().layTatCa();
            d.totalRooms = rooms.size();
            for (Phong p : rooms) {
                if (p.getTrangThai() == TrangThaiPhong.DANG_THUE) d.rentedRooms++;
                else if (p.getTrangThai() == TrangThaiPhong.TRONG) d.emptyRooms++;
                else if (p.getTrangThai() == TrangThaiPhong.DANG_SUA) d.maintenanceRooms++;
            }
        } catch (Exception ex) { d.roomsAvailable = false; }
        try {
            d.tenants = new NguoiThueService().layTatCa().size();
        } catch (Exception ex) { d.peopleAvailable = false; }
        try { for (HopDong h : new HopDongService().layTatCa()) if (h.getTrangThai() == TrangThaiHopDong.HIEU_LUC) d.activeContracts++; }
        catch (Exception ex) { d.contractsAvailable = false; }
        try {
            List<HoaDon> invoices = new HoaDonService().layTatCa();
            d.recentInvoices = new ArrayList<>(invoices);
            d.recentInvoices.sort((a, b) -> Integer.compare(b.getMaHoaDon(), a.getMaHoaDon()));
            if (d.recentInvoices.size() > 5) d.recentInvoices = d.recentInvoices.subList(0, 5);
            for (HoaDon h : invoices) if (h.getTrangThai() != TrangThaiHoaDon.HOAN_TAT) d.pendingInvoices++;
        } catch (Exception ex) { d.invoiceAvailable = false; }
        try {
            YearMonth end = YearMonth.now();
            YearMonth start = end.minusMonths(5);
            List<Object[]> rows = new BaoCaoService().doanhThu(start.format(DateTimeFormatter.ofPattern("MM/yyyy")), end.format(DateTimeFormatter.ofPattern("MM/yyyy")));
            Map<String, BigDecimal> income = new LinkedHashMap<>();
            for (int i = 0; i < 6; i++) income.put(start.plusMonths(i).format(DateTimeFormatter.ofPattern("MM/yyyy")), BigDecimal.ZERO);
            for (Object[] row : rows) if (row[0] != null && income.containsKey(row[0].toString())) {
                BigDecimal value = row[1] instanceof BigDecimal ? (BigDecimal) row[1] : BigDecimal.ZERO;
                income.put(row[0].toString(), value);
                if (row[0].toString().equals(end.format(DateTimeFormatter.ofPattern("MM/yyyy")))) d.monthlyIncome = value;
                if (row[3] instanceof BigDecimal) d.debt = d.debt.add((BigDecimal) row[3]);
            }
            income.forEach((label, value) -> { d.revenueLabels.add(YearMonth.parse(label, DateTimeFormatter.ofPattern("MM/yyyy")).format(DateTimeFormatter.ofPattern("M/yy"))); d.revenueValues.add(value.doubleValue() / 1_000_000d); });
        } catch (Exception ex) { d.revenueAvailable = false; }
        return d;
    }

    private JComponent metricCard(String label, String value, Icons.Type icon, Color accent) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)), BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        card.add(iconBadge(icon, accent, 48, 24), BorderLayout.WEST);
        JPanel text = new JPanel(); text.setOpaque(false); text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(label); title.setForeground(MUTED); title.setFont(DialogUtils.font(Font.PLAIN, 12f));
        JLabel amount = new JLabel(value); amount.setForeground(TEXT); amount.setFont(DialogUtils.font(Font.BOLD, 21f));
        text.add(Box.createVerticalGlue()); text.add(title); text.add(Box.createVerticalStrut(4)); text.add(amount); text.add(Box.createVerticalGlue());
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel chartCard(String title, JComponent chart) {
        JPanel card = new JPanel(new BorderLayout()); card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        JPanel heading = new JPanel(new BorderLayout()); heading.setBackground(Color.WHITE);
        heading.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)), BorderFactory.createEmptyBorder(13, 16, 13, 12)));
        JLabel accent = new JLabel(" "); accent.setOpaque(true); accent.setBackground(ACTIVE); accent.setPreferredSize(new Dimension(4, 21));
        JLabel label = new JLabel(title); label.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 0)); label.setForeground(TEXT); label.setFont(DialogUtils.font(Font.BOLD, 14f));
        JPanel titleBox = new JPanel(new BorderLayout()); titleBox.setOpaque(false); titleBox.add(accent, BorderLayout.WEST); titleBox.add(label, BorderLayout.CENTER);
        heading.add(titleBox, BorderLayout.WEST); card.add(heading, BorderLayout.NORTH); card.add(chart, BorderLayout.CENTER);
        chart.setPreferredSize(new Dimension(400, 270));
        return card;
    }

    private JPanel recentInvoices(List<HoaDon> invoices) {
        JPanel card = new JPanel(new BorderLayout()); card.setBackground(Color.WHITE); card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        JPanel heading = new JPanel(new BorderLayout()); heading.setBackground(Color.WHITE); heading.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(226,232,240)), BorderFactory.createEmptyBorder(13,16,13,12)));
        JLabel accent = new JLabel(" "); accent.setOpaque(true); accent.setBackground(ACTIVE); accent.setPreferredSize(new Dimension(4,21));
        JLabel title = new JLabel("Hóa đơn gần đây"); title.setBorder(BorderFactory.createEmptyBorder(0,9,0,0)); title.setForeground(TEXT); title.setFont(DialogUtils.font(Font.BOLD,14f));
        JPanel titleBox = new JPanel(new BorderLayout()); titleBox.setOpaque(false); titleBox.add(accent,BorderLayout.WEST); titleBox.add(title,BorderLayout.CENTER); heading.add(titleBox,BorderLayout.WEST); card.add(heading,BorderLayout.NORTH);
        String[] cols = {"Mã hóa đơn", "Tháng", "Tổng tiền", "Hạn thanh toán", "Trạng thái"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r,int c){return false;} };
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (HoaDon h : invoices) model.addRow(new Object[]{h.getMaHoaDon(), h.getThangNam(), money(h.getTongTien()), h.getHanThanhToan()==null?"—":h.getHanThanhToan().format(dateFormat), h.getTrangThai()});
        JTable table = DialogUtils.createStyledTable(model); JScrollPane tableScroll = new JScrollPane(table); tableScroll.setBorder(null); tableScroll.setPreferredSize(new Dimension(600, 190)); card.add(tableScroll,BorderLayout.CENTER);
        if (invoices.isEmpty()) { JLabel empty = new JLabel("Chưa có hóa đơn để hiển thị",SwingConstants.CENTER); empty.setForeground(MUTED); card.add(empty,BorderLayout.SOUTH); }
        return card;
    }

    private static String money(BigDecimal value) {
        if (value == null) value = BigDecimal.ZERO;
        return NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(value) + " ₫";
    }

    private static final class DashboardData {
        int totalRooms, rentedRooms, emptyRooms, maintenanceRooms, tenants, activeContracts, pendingInvoices;
        BigDecimal monthlyIncome = BigDecimal.ZERO, debt = BigDecimal.ZERO;
        List<String> revenueLabels = new ArrayList<>(); List<Double> revenueValues = new ArrayList<>(); List<HoaDon> recentInvoices = new ArrayList<>();
        boolean roomsAvailable = true, peopleAvailable = true, contractsAvailable = true, invoiceAvailable = true, revenueAvailable = true;
    }

    private static final class RevenueChart extends JPanel {
        private final List<String> labels; private final List<Double> values;
        RevenueChart(List<String> labels, List<Double> values) { this.labels=labels; this.values=values; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g); Graphics2D g2=(Graphics2D)g.create(); DialogUtils.aa(g2);
            int left=48, right=getWidth()-20, top=24, bottom=getHeight()-38; double max=1;
            for(double v:values) max=Math.max(max,v); max=Math.ceil(max/50d)*50d;
            g2.setFont(DialogUtils.font(Font.PLAIN,10f)); g2.setColor(MUTED);
            for(int i=0;i<=4;i++){ int y=bottom-(bottom-top)*i/4; g2.setColor(new Color(226,232,240)); g2.drawLine(left,y,right,y); g2.setColor(MUTED); g2.drawString(String.valueOf((int)(max*i/4)),8,y+4); }
            if(values.isEmpty()){ g2.setColor(MUTED); g2.drawString("Chưa có dữ liệu doanh thu",left+15,(top+bottom)/2); g2.dispose(); return; }
            int slot=Math.max(1,(right-left)/values.size()); int bar=Math.min(44,Math.max(14,slot/2));
            for(int i=0;i<values.size();i++){ double v=values.get(i); int h=(int)((bottom-top)*v/max); int x=left+i*slot+(slot-bar)/2; g2.setPaint(new GradientPaint(x,bottom-h, new Color(129,140,248),x,bottom,new Color(79,70,229))); g2.fillRoundRect(x,bottom-h,bar,h,8,8); g2.setColor(MUTED); if(i<labels.size()) g2.drawString(labels.get(i),x-2,bottom+19); }
            g2.dispose();
        }
    }

    private static final class OccupancyChart extends JPanel {
        private final int rented, empty, maintenance;
        OccupancyChart(int rented,int empty,int maintenance){this.rented=rented;this.empty=empty;this.maintenance=maintenance;setOpaque(false);}
        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g); Graphics2D g2=(Graphics2D)g.create(); DialogUtils.aa(g2);
            if (rented < 0) { g2.setColor(MUTED);g2.setFont(DialogUtils.font(Font.PLAIN,12f));g2.drawString("Không tải được dữ liệu phòng",20,getHeight()/2);g2.dispose();return; }
            int total=rented+empty+maintenance, d=Math.min(190,Math.min(getWidth()-70,getHeight()-70)), x=(getWidth()-d)/2, y=16;
            Color[] colors={new Color(79,70,229),new Color(5,150,105),new Color(217,119,6)}; int[] vals={rented,empty,maintenance};
            if(total==0){g2.setColor(new Color(226,232,240));g2.setStroke(new BasicStroke(24));g2.drawOval(x,y,d,d);}
            else {int start=90; for(int i=0;i<vals.length;i++){int arc=(int)Math.round(360d*vals[i]/total);g2.setColor(colors[i]);g2.setStroke(new BasicStroke(24,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND));g2.drawArc(x,y,d,d,start, -arc);start-=arc;}}
            g2.setColor(TEXT);g2.setFont(DialogUtils.font(Font.BOLD,26f));String pct=(total==0?"0":Math.round(100d*rented/total))+"%";FontMetrics fm=g2.getFontMetrics();g2.drawString(pct,(getWidth()-fm.stringWidth(pct))/2,y+d/2+4);
            g2.setColor(MUTED);g2.setFont(DialogUtils.font(Font.PLAIN,11f));String caption="Tỷ lệ phòng đang thuê";g2.drawString(caption,(getWidth()-g2.getFontMetrics().stringWidth(caption))/2,y+d/2+23);
            int ly=y+d+20;
            String[] labels={"Đang thuê "+rented,"Phòng trống "+empty,"Bảo trì "+maintenance};
            Font legendFont=DialogUtils.font(Font.PLAIN,11f);
            g2.setFont(legendFont);
            FontMetrics legendMetrics=g2.getFontMetrics();
            int itemGap=18, legendWidth=0;
            for(String label:labels) legendWidth+=9+6+legendMetrics.stringWidth(label);
            legendWidth+=itemGap*2;
            int xx=Math.max(10,(getWidth()-legendWidth)/2);
            for(int i=0;i<labels.length;i++){
                g2.setColor(colors[i]);
                g2.fillOval(xx,ly,9,9);
                g2.setColor(TEXT);
                g2.drawString(labels[i],xx+15,ly+9);
                xx+=9+6+legendMetrics.stringWidth(labels[i])+itemGap;
            }
            g2.dispose();
        }
    }

    private JComponent iconBadge(Icons.Type type, Color accent, int boxSize, int iconSize) {
        return DialogUtils.createIconBadge(type, accent, boxSize, iconSize);
    }

    private void open(String key, String pageTitle) {
        title.setText(pageTitle);
        breadcrumb.setText("RoomHub  /  " + pageTitle);
        for (Map.Entry<String, NavButton> entry : navButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(key));
        }
        if (!cache.containsKey(key) && pages.containsKey(key)) {
            JPanel panel = pages.get(key).get();
            DialogUtils.styleComponentTree(panel);
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(CONTENT);
            wrapper.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            wrapper.add(panel, BorderLayout.CENTER);
            cache.put(key, wrapper);
            content.add(wrapper, key);
        }
        cards.show(content, key);
    }

    private static final class NavButton extends JButton {
        private final Icons.Type icon;
        private boolean selected;
        private boolean hovered;

        NavButton(String text, Icons.Type icon) {
            super(text);
            this.icon = icon;
            setHorizontalAlignment(SwingConstants.LEFT);
            setFont(DialogUtils.font(Font.PLAIN, 13f));
            setForeground(new Color(203, 213, 225));
            setBorder(new EmptyBorder(0, 40, 0, 10));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { hovered = false; repaint(); }
            });
        }

        void setActive(boolean selected) {
            this.selected = selected;
            setForeground(selected ? Color.WHITE : new Color(203, 213, 225));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            DialogUtils.aa(g2);
            if (selected || hovered) {
                g2.setColor(selected ? ACTIVE : SIDEBAR_HOVER);
                g2.fill(new RoundRectangle2D.Double(0, 1, getWidth() - 1, getHeight() - 2, 10, 10));
            }
            Icons.paint(g2, icon, 12, (getHeight() - 18) / 2, 18,
                    selected ? Color.WHITE : new Color(148, 163, 184));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
