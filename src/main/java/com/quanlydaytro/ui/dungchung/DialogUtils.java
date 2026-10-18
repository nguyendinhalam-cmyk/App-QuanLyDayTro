package com.quanlydaytro.ui.dungchung;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Design system dùng chung cho toàn bộ ứng dụng RoomHub:
 * bảng màu, font, nút, bảng dữ liệu, badge trạng thái, card, hộp thoại...
 * Toàn bộ API cũ được giữ nguyên để các panel hiện có hoạt động như trước.
 */
public class DialogUtils {

    // =================== COLOR CONSTANTS ===================
    public static final Color PRIMARY = new Color(79, 70, 229);        // Indigo 600
    public static final Color PRIMARY_DARK = new Color(67, 56, 202);   // Indigo 700
    public static final Color PRIMARY_SOFT = new Color(238, 242, 255); // Indigo 50
    public static final Color SUCCESS = new Color(5, 150, 105);        // Emerald 600
    public static final Color DANGER = new Color(220, 38, 38);         // Red 600
    public static final Color WARNING = new Color(217, 119, 6);        // Amber 600
    public static final Color INFO = new Color(2, 132, 199);           // Sky 600
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);    // Slate 800
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Slate 500
    public static final Color BORDER_COLOR = new Color(226, 232, 240); // Slate 200
    public static final Color BG_CONTENT = new Color(241, 245, 249);   // Slate 100
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TABLE_ALT_ROW = new Color(248, 250, 252);
    public static final Color SELECT_BG = new Color(238, 242, 255);
    public static final Color SIDEBAR_BG = new Color(15, 23, 42);      // Slate 900
    public static final Color SIDEBAR_HOVER = new Color(30, 41, 59);   // Slate 800
    public static final Color SIDEBAR_ACTIVE = PRIMARY;
    public static final Color PURPLE = new Color(124, 58, 237);        // Violet 600
    public static final Color GRAY_BTN = new Color(100, 116, 139);

    // =================== FONT CONSTANTS ===================
    public static final String FONT_FAMILY = pickFontFamily();
    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font BODY_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font TABLE_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font TABLE_HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);

    private static String pickFontFamily() {
        try {
            var names = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
            for (String f : new String[]{"Segoe UI", "Inter", "SF Pro Text", "Roboto", "Helvetica Neue", "Noto Sans"}) {
                if (names.contains(f)) return f;
            }
        } catch (Throwable ignored) {
        }
        return "SansSerif";
    }

    public static Font font(int style, float size) {
        return new Font(FONT_FAMILY, style, 1).deriveFont(style, size);
    }

    /** Cấu hình giao diện toàn cục (gọi sau khi đã setLookAndFeel FlatLaf). */
    public static void installTheme() {
        UIManager.put("defaultFont", new FontUIResource(BODY_FONT));
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("CheckBox.arc", 5);
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.innerFocusWidth", 0);
        UIManager.put("Component.focusColor", PRIMARY);
        UIManager.put("Component.borderColor", new Color(203, 213, 225));
        UIManager.put("Component.focusedBorderColor", PRIMARY);
        UIManager.put("Component.accentColor", PRIMARY);
        UIManager.put("Component.hideMnemonics", true);
        UIManager.put("TextField.margin", new Insets(3, 10, 3, 10));
        UIManager.put("PasswordField.margin", new Insets(3, 10, 3, 10));
        UIManager.put("TextArea.margin", new Insets(4, 8, 4, 8));
        UIManager.put("ComboBox.padding", new Insets(3, 10, 3, 6));
        UIManager.put("ComboBox.selectionBackground", PRIMARY_SOFT);
        UIManager.put("ComboBox.selectionForeground", TEXT_PRIMARY);
        UIManager.put("List.selectionBackground", PRIMARY_SOFT);
        UIManager.put("List.selectionForeground", TEXT_PRIMARY);
        UIManager.put("TextComponent.selectionBackground", new Color(199, 210, 254));
        UIManager.put("ScrollBar.width", 11);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.thumb", new Color(203, 213, 225));
        UIManager.put("ScrollBar.hoverThumbColor", new Color(148, 163, 184));
        UIManager.put("ScrollBar.pressedThumbColor", new Color(100, 116, 139));
        UIManager.put("ScrollBar.track", new Color(0, 0, 0, 0));
        UIManager.put("TabbedPane.font", HEADER_FONT);
        UIManager.put("TabbedPane.tabHeight", 42);
        UIManager.put("TabbedPane.tabInsets", new Insets(4, 18, 4, 18));
        UIManager.put("TabbedPane.selectedForeground", PRIMARY);
        UIManager.put("TabbedPane.foreground", TEXT_SECONDARY);
        UIManager.put("TabbedPane.underlineColor", PRIMARY);
        UIManager.put("TabbedPane.inactiveUnderlineColor", PRIMARY);
        UIManager.put("TabbedPane.hoverColor", PRIMARY_SOFT);
        UIManager.put("TabbedPane.tabSelectionHeight", 3);
        UIManager.put("TabbedPane.showTabSeparators", false);
        UIManager.put("TabbedPane.background", CARD_BG);
        UIManager.put("TabbedPane.contentAreaColor", BORDER_COLOR);
        UIManager.put("SplitPane.dividerSize", 10);
        UIManager.put("SplitPaneDivider.gripColor", BG_CONTENT);
        UIManager.put("SplitPane.background", BG_CONTENT);
        UIManager.put("Panel.background", BG_CONTENT);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("OptionPane.messageFont", BODY_FONT);
        UIManager.put("OptionPane.buttonFont", BUTTON_FONT);
        UIManager.put("OptionPane.buttonMinimumWidth", 90);
        UIManager.put("Button.default.background", PRIMARY);
        UIManager.put("Button.default.foreground", Color.WHITE);
        UIManager.put("Button.default.focusedBackground", PRIMARY_DARK);
        UIManager.put("Button.margin", new Insets(6, 16, 6, 16));
        UIManager.put("ToolTip.background", SIDEBAR_BG);
        UIManager.put("ToolTip.foreground", Color.WHITE);
        UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder(6, 10, 6, 10));
        UIManager.put("TitlePane.unifiedBackground", true);
    }

    // =================== DIALOG METHODS ===================
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public static String showInput(Component parent, String message) {
        return JOptionPane.showInputDialog(parent, message, "Nhập dữ liệu", JOptionPane.PLAIN_MESSAGE);
    }

    /** Applies the shared RoomHub appearance to an existing functional screen without replacing its controls or listeners. */
    public static void styleComponentTree(Container root) {
        if (root == null) return;
        if (root instanceof JPanel panel) {
            if (panel.isOpaque() && (panel.getBackground() == null
                    || panel.getBackground().equals(UIManager.getColor("Panel.background")))) {
                panel.setBackground(BG_CONTENT);
            }
            // Keep existing screen layouts, while giving every page the same RoomHub
            // spacing and content surface. Individual white cards can override this.
            if (panel.getParent() == null && panel.getBorder() == null) {
                panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            }
            if (panel.getLayout() instanceof BorderLayout layout) {
                Component north = layout.getLayoutComponent(BorderLayout.NORTH);
                if (north instanceof JLabel heading && !Boolean.TRUE.equals(panel.getClientProperty("roomhub.header"))) {
                    panel.putClientProperty("roomhub.header", Boolean.TRUE);
                    panel.remove(north);
                    panel.add(createHeaderPanel(heading.getText(), null), BorderLayout.NORTH);
                }
            }
        }
        for (Component component : root.getComponents()) {
            if (component instanceof JButton button && !(button instanceof ModernButton)) {
                styleButton(button);
            } else if (component instanceof JTextField field) {
                field.setFont(BODY_FONT);
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR),
                        BorderFactory.createEmptyBorder(5, 9, 5, 9)));
                field.setPreferredSize(new Dimension(Math.max(140, field.getPreferredSize().width), 36));
            } else if (component instanceof JComboBox<?> combo) {
                combo.setFont(BODY_FONT);
                combo.setPreferredSize(new Dimension(Math.max(150, combo.getPreferredSize().width), 36));
            } else if (component instanceof JTable table) {
                table.setFont(TABLE_FONT);
                table.setRowHeight(Math.max(38, table.getRowHeight()));
                table.setFillsViewportHeight(true);
                table.setSelectionBackground(SELECT_BG);
                table.setSelectionForeground(TEXT_PRIMARY);
                if (table.getTableHeader() != null) {
                    table.getTableHeader().setFont(TABLE_HEADER_FONT);
                    table.getTableHeader().setPreferredSize(new Dimension(10, 42));
                }
            } else if (component instanceof JScrollPane scroll) {
                scroll.setBorder(new CardBorder(null, null));
                scroll.getViewport().setBackground(Color.WHITE);
                scroll.getVerticalScrollBar().setUnitIncrement(16);
            } else if (component instanceof JLabel label && label.getFont().getSize() < 12) {
                label.setFont(font(label.getFont().getStyle(), 12f));
                label.setForeground(TEXT_SECONDARY);
            }
            if (component instanceof Container child) styleComponentTree(child);
        }
    }

    private static void styleButton(JButton button) {
        String rawText = button.getText() == null ? "" : button.getText().toLowerCase();
        String text = java.text.Normalizer.normalize(rawText, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        Color color = PRIMARY;
        if (text.contains("xoa") || text.contains("delete")) color = DANGER;
        else if (text.contains("cap nhat") || text.contains("sua") || text.contains("update")) color = WARNING;
        else if (text.contains("them") || text.contains("tao moi") || text.contains("create")) color = SUCCESS;
        else if (text.contains("lam moi") || text.contains("bo loc") || text.contains("refresh") || text.contains("huy")) color = GRAY_BTN;
        else if (text.contains("tim") || text.contains("search")) color = INFO;
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setPreferredSize(new Dimension(Math.max(105, button.getPreferredSize().width), 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // =================== SHARED PAINT HELPERS ===================
    public static void aa(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    /** Baseline giúp canh chữ vào giữa theo chiều cao chữ hoa (không phụ thuộc metrics của font). */
    public static int centerBaseline(Graphics2D g2, int top, int height) {
        java.awt.geom.Rectangle2D r = g2.getFont().createGlyphVector(g2.getFontRenderContext(), "H").getVisualBounds();
        return (int) Math.round(top + (height + r.getHeight()) / 2.0);
    }

    public static Color alpha(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    /** Pha màu với trắng để tạo tông nền nhạt của một màu nhấn. */
    public static Color tint(Color c, float amount) {
        int r = (int) (c.getRed() + (255 - c.getRed()) * amount);
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * amount);
        int b = (int) (c.getBlue() + (255 - c.getBlue()) * amount);
        return new Color(r, g, b);
    }

    // =================== CARD / BORDER ===================

    /** Panel bo góc nền trắng, viền mảnh – dùng làm "card". */
    public static class RoundedPanel extends JPanel {
        private final int arc;
        private Color borderColor = BORDER_COLOR;

        public RoundedPanel(LayoutManager lm, int arc) {
            super(lm);
            this.arc = arc;
            setOpaque(false);
            setBackground(CARD_BG);
        }

        public RoundedPanel(int arc) {
            this(new BorderLayout(), arc);
        }

        public void setBorderColor(Color c) {
            this.borderColor = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 2, getHeight() - 2, arc, arc));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Viền bo góc có tiêu đề (thay thế TitledBorder cũ). Che 4 góc bằng màu nền của container cha. */
    public static class CardBorder extends AbstractBorder {
        private final String title;
        private final Color accent;
        private final int arc = 16;

        public CardBorder(String title, Color accent) {
            this.title = title;
            this.accent = accent == null ? PRIMARY : accent;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return title == null ? new Insets(2, 2, 2, 2) : new Insets(44, 6, 6, 6);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            Insets i = getBorderInsets(c);
            insets.set(i.top, i.left, i.bottom, i.right);
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            Container parent = c.getParent();
            Color mask = (parent != null && parent.getBackground() != null) ? parent.getBackground() : BG_CONTENT;
            Area corners = new Area(new Rectangle(x, y, w, h));
            corners.subtract(new Area(new RoundRectangle2D.Double(x, y, w - 1, h - 1, arc, arc)));
            g2.setColor(mask);
            g2.fill(corners);
            g2.setColor(BORDER_COLOR);
            g2.draw(new RoundRectangle2D.Double(x + 0.5, y + 0.5, w - 2, h - 2, arc, arc));
            if (title != null) {
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Double(x + 18, y + 14, 4, 18, 4, 4));
                g2.setFont(HEADER_FONT.deriveFont(Font.BOLD, 14.5f));
                g2.setColor(TEXT_PRIMARY);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(title, x + 30, y + 14 + (18 + fm.getAscent() - fm.getDescent()) / 2);
                g2.setColor(BORDER_COLOR);
                g2.drawLine(x + 1, y + 42, x + w - 2, y + 42);
            }
            g2.dispose();
        }
    }

    public static Border createCardBorder(String title, Color accent) {
        return new CardBorder(title, accent);
    }

    /** Bo góc cho panel dạng thanh công cụ / bộ lọc. */
    public static void styleToolbar(JPanel bar) {
        bar.setBackground(CARD_BG);
        bar.setBorder(new CardBorder(null, null));
    }

    // =================== BUTTON ===================
    public static class ModernButton extends JButton {
        private final Color base;

        private static Icons.Type iconFor(int cp) {
            return switch (cp) {
                case 0x2795 -> Icons.Type.PLUS;                                  // ➕
                case 0x270F -> Icons.Type.EDIT;                                  // ✏
                case 0x1F5D1 -> Icons.Type.TRASH;                                // 🗑
                case 0x1F504 -> Icons.Type.REFRESH;                              // 🔄
                case 0x1F4BE -> Icons.Type.SAVE;                                 // 💾
                case 0x1F6D1 -> Icons.Type.STOP;                                 // 🛑
                case 0x1F6AA -> Icons.Type.DOOR;                                 // 🚪
                case 0x1F5A8 -> Icons.Type.PRINT;                                // 🖨
                case 0x1F512 -> Icons.Type.LOCK;                                 // 🔒
                case 0x1F511 -> Icons.Type.KEY;                                  // 🔑
                case 0x1F50D -> Icons.Type.SEARCH;                               // 🔍
                case 0x1F4F2 -> Icons.Type.SEND;                                 // 📲
                case 0x1F4DD, 0x1F4DC, 0x1F4CB -> Icons.Type.FILE;               // 📝 📜 📋
                case 0x1F4CA -> Icons.Type.CHART;                                // 📊
                case 0x1F4B0 -> Icons.Type.MONEY;                                // 💰
                case 0x1F3D8 -> Icons.Type.BUILDING;                             // 🏘
                case 0x26A1 -> Icons.Type.BOLT;                                  // ⚡
                case 0x23F3 -> Icons.Type.CLOCK;                                 // ⏳
                case 0x23F0 -> Icons.Type.BELL;                                  // ⏰
                default -> null;
            };
        }

        public ModernButton(String text, Color base) {
            super(text);
            this.base = base;
            if (text != null && !text.isEmpty()) {
                int cp = text.codePointAt(0);
                Icons.Type it = Character.isLetterOrDigit(cp) ? null : iconFor(cp);
                if (it != null) {
                    int i = 0;
                    while (i < text.length() && !Character.isLetterOrDigit(text.charAt(i))) i++;
                    setText(text.substring(i).trim());
                    setIcon(Icons.icon(it, 16, Color.WHITE));
                    setIconTextGap(8);
                }
            }
            setFont(BUTTON_FONT);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(0, 0, 0, 0));
            setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
            Dimension d = super.getPreferredSize();
            setPreferredSize(new Dimension(d.width + 4, 38));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            Color c = base;
            if (!isEnabled()) c = new Color(203, 213, 225);
            else if (getModel().isPressed()) c = base.darker().darker();
            else if (getModel().isRollover()) c = base.darker();
            if (isEnabled()) {
                g2.setColor(alpha(base, 40));
                g2.fill(new RoundRectangle2D.Double(0, 2, getWidth(), getHeight() - 1, 12, 12));
            }
            g2.setColor(c);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() - 2, 12, 12));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static JButton createStyledButton(String text, Color bgColor) {
        return new ModernButton(text, bgColor);
    }

    // =================== TABLE ===================
    private static final Pattern NUMERIC = Pattern.compile(
            "^[+\\-]?\\s?[\\d.,]+(\\s?(VNĐ|đ|%|kWh|m³|m²|ngày|phòng|người)(/\\w+)?)?(\\s?\\(\\d+%\\))?$");

    private static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        String t = s.trim();
        if (t.matches("\\d{9,}")) return false; // số điện thoại / CCCD: căn trái như chữ
        return NUMERIC.matcher(t).matches();
    }

    private static boolean isStatusColumn(String name) {
        String n = name == null ? "" : name.toLowerCase();
        return n.equals("trạng thái") || n.equals("mức độ");
    }

    /** Căn lề theo nội dung cột: STT -> giữa, số -> phải, chữ -> trái. */
    private static int alignment(JTable table, int viewCol) {
        String name = table.getColumnName(viewCol);
        if (name.equalsIgnoreCase("STT")) return SwingConstants.CENTER;
        if (isStatusColumn(name)) return SwingConstants.CENTER;
        int modelCol = table.convertColumnIndexToModel(viewCol);
        TableModel m = table.getModel();
        for (int r = 0; r < Math.min(m.getRowCount(), 5); r++) {
            Object v = m.getValueAt(r, modelCol);
            if (v != null && !v.toString().trim().isEmpty()) {
                return isNumeric(v.toString()) ? SwingConstants.RIGHT : SwingConstants.LEFT;
            }
        }
        return SwingConstants.LEFT;
    }

    /** Trả về {màu chữ, màu nền} cho badge trạng thái; null nếu không phải trạng thái đã biết. */
    public static Color[] badgeColors(String status) {
        String s = status == null ? "" : status.trim().toLowerCase();
        Color fg;
        switch (s) {
            case "trống", "đang hiệu lực", "đã thanh toán", "hoạt động", "đang làm việc", "đang áp dụng", "đã chốt" -> fg = SUCCESS;
            case "đang thuê", "đang sử dụng" -> fg = PRIMARY;
            case "sắp hết hạn", "thanh toán một phần", "đang sửa chữa", "bảo trì", "chờ xử lý", "chờ chốt", "nhẹ" -> fg = WARNING;
            case "phòng trống" -> fg = INFO;
            case "chưa thanh toán", "hết hạn", "đã hủy", "khóa", "nghỉ việc", "dừng kinh doanh", "nghiêm trọng", "quá hạn" -> fg = DANGER;
            default -> {
                return null;
            }
        }
        return new Color[]{fg, tint(fg, 0.88f)};
    }

    /** Label vẽ badge dạng viên thuốc trong ô bảng. */
    private static class BadgeLabel extends JLabel {
        private Color fg = TEXT_SECONDARY;
        private Color pill = new Color(241, 245, 249);
        private Color rowBg = Color.WHITE;

        BadgeLabel() {
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            setFont(font(Font.BOLD, 12f));
        }

        void set(String text, Color fg, Color pill, Color rowBg) {
            setText(text);
            this.fg = fg;
            this.pill = pill;
            this.rowBg = rowBg;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            aa(g2);
            g2.setColor(rowBg);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String t = getText();
            int tw = fm.stringWidth(t);
            int pw = Math.min(getWidth() - 8, tw + 34), ph = 24;
            int px = (getWidth() - pw) / 2, py = (getHeight() - ph) / 2;
            g2.setColor(pill);
            g2.fillRoundRect(px, py, pw, ph, ph, ph);
            g2.setColor(fg);
            g2.fillOval(px + 11, py + ph / 2 - 3, 6, 6);
            g2.drawString(t, px + 22, centerBaseline(g2, py, ph));
            g2.dispose();
        }
    }

    private static class ModernCellRenderer extends DefaultTableCellRenderer {
        private final BadgeLabel badge = new BadgeLabel();
        private final int[] hover;

        ModernCellRenderer(int[] hover) {
            this.hover = hover;
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
            Color rowBg = sel ? SELECT_BG : (row == hover[0] ? TABLE_ALT_ROW : Color.WHITE);
            String text = val == null ? "" : val.toString();
            String colName = t.getColumnName(col);
            if (isStatusColumn(colName)) {
                Color[] bc = badgeColors(text);
                Color fg = bc != null ? bc[0] : TEXT_SECONDARY;
                Color bg = bc != null ? bc[1] : new Color(241, 245, 249);
                badge.set(text, fg, bg, rowBg);
                return badge;
            }
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, false, false, row, col);
            l.setOpaque(true);
            l.setBackground(rowBg);
            l.setForeground(TEXT_PRIMARY);
            l.setFont(TABLE_FONT);
            int al = alignment(t, col);
            l.setHorizontalAlignment(al);
            l.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
            if (colName.equalsIgnoreCase("STT")) l.setForeground(TEXT_SECONDARY);
            return l;
        }
    }

    private static class ModernHeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(t, val, false, false, row, col);
            l.setOpaque(true);
            l.setBackground(new Color(248, 250, 252));
            l.setForeground(TEXT_SECONDARY);
            l.setFont(font(Font.BOLD, 12f));
            l.setText(val == null ? "" : val.toString().toUpperCase());
            l.setHorizontalAlignment(alignment(t, col));
            l.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                    BorderFactory.createEmptyBorder(0, 14, 0, 14)));
            return l;
        }
    }

    public static JTable createStyledTable(DefaultTableModel model) {
        final int[] hover = {-1};
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public void addNotify() {
                super.addNotify();
                styleEnclosingScrollPane(this);
                if (!Boolean.TRUE.equals(getClientProperty("roomhub.sized"))) {
                    putClientProperty("roomhub.sized", Boolean.TRUE);
                    autoSizeColumns(this);
                }
            }
        };

        table.setFont(TABLE_FONT);
        table.setRowHeight(44);
        table.setGridColor(new Color(241, 245, 249));
        table.setSelectionBackground(SELECT_BG);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFocusable(true);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new ModernHeaderRenderer());

        table.setDefaultRenderer(Object.class, new ModernCellRenderer(hover));

        MouseAdapter hoverTracker = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r != hover[0]) {
                    hover[0] = r;
                    table.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover[0] = -1;
                table.repaint();
            }
        };
        table.addMouseMotionListener(hoverTracker);
        table.addMouseListener(hoverTracker);
        return table;
    }

    /** Đặt độ rộng ưu tiên của từng cột theo nội dung để không bị cắt chữ "...". */
    private static void autoSizeColumns(JTable table) {
        FontMetrics fm = table.getFontMetrics(TABLE_FONT);
        FontMetrics hm = table.getFontMetrics(font(Font.BOLD, 12f));
        TableModel m = table.getModel();
        for (int c = 0; c < table.getColumnCount(); c++) {
            String name = table.getColumnName(c);
            int w = hm.stringWidth(name.toUpperCase()) + 34;
            boolean status = isStatusColumn(name);
            for (int r = 0; r < m.getRowCount(); r++) {
                Object v = m.getValueAt(r, table.convertColumnIndexToModel(c));
                if (v != null) w = Math.max(w, fm.stringWidth(v.toString()) + (status ? 60 : 32));
            }
            w = Math.min(w, 340);
            TableColumn col = table.getColumnModel().getColumn(c);
            col.setPreferredWidth(name.equalsIgnoreCase("STT") ? Math.max(48, Math.min(w, 70)) : w);
            col.setMinWidth(name.equalsIgnoreCase("STT") ? 44 : 60);
        }
    }

    private static void styleEnclosingScrollPane(JTable table) {
        Container p = table.getParent();
        if (p instanceof JViewport vp && vp.getParent() instanceof JScrollPane sp) {
            if (Boolean.TRUE.equals(sp.getClientProperty("roomhub.styled"))) return;
            sp.putClientProperty("roomhub.styled", Boolean.TRUE);
            sp.setBorder(new CardBorder(null, null));
            sp.getViewport().setBackground(Color.WHITE);
            sp.setBackground(Color.WHITE);
            sp.getVerticalScrollBar().setUnitIncrement(18);
            JPanel corner = new JPanel();
            corner.setBackground(new Color(248, 250, 252));
            sp.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, corner);
        }
    }

    // =================== LABELS / FIELDS ===================
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(Font.BOLD, 20f));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(Font.PLAIN, 13f));
        label.setForeground(new Color(71, 85, 105));
        return label;
    }

    public static JTextField createFormField() {
        JTextField field = new JTextField();
        field.setFont(BODY_FONT);
        field.setPreferredSize(new Dimension(200, 36));
        return field;
    }

    public static JTextField createSearchField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(BODY_FONT);
        field.setPreferredSize(new Dimension(300, 38));
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JTextField.leadingIcon", Icons.icon(Icons.Type.SEARCH, 16, TEXT_SECONDARY));
        field.putClientProperty("JTextField.showClearButton", true);
        return field;
    }

    public static JComboBox<String> createStyledComboBox(String... items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(BODY_FONT);
        combo.setPreferredSize(new Dimension(200, 36));
        return combo;
    }

    public static String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(amount) + " VNĐ";
    }

    public static String formatDate(String date) {
        return date;
    }

    // =================== HEADER / CARDS ===================

    /** "🚪 QUẢN LÝ PHÒNG TRỌ" -> "Quản lý phòng trọ" (bỏ emoji đầu dòng, viết hoa chữ đầu). */
    public static String prettyTitle(String raw) {
        int i = 0;
        while (i < raw.length() && !Character.isLetterOrDigit(raw.charAt(i))) i++;
        String s = raw.substring(i).trim();
        if (s.equals(s.toUpperCase())) {
            s = s.toLowerCase();
        }
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /** Ô icon bo tròn tông màu nhạt. */
    public static JComponent createIconBadge(Icons.Type type, Color accent, int boxSize, int iconSize) {
        JComponent c = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                aa(g2);
                g2.setColor(tint(accent, 0.88f));
                g2.fill(new RoundRectangle2D.Double(0, 0, boxSize, boxSize, boxSize * 0.32, boxSize * 0.32));
                Icons.paint(g2, type, (boxSize - iconSize) / 2, (boxSize - iconSize) / 2, iconSize, accent);
                g2.dispose();
            }
        };
        c.setPreferredSize(new Dimension(boxSize, boxSize));
        c.setMinimumSize(new Dimension(boxSize, boxSize));
        c.setMaximumSize(new Dimension(boxSize, boxSize));
        return c;
    }

    public static JPanel createHeaderPanel(String title, JTextField searchField) {
        RoundedPanel panel = new RoundedPanel(new BorderLayout(14, 0), 16);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JPanel left = new JPanel(new BorderLayout(14, 0));
        left.setOpaque(false);
        left.add(createIconBadge(Icons.guess(title), PRIMARY, 42, 22), BorderLayout.WEST);

        JLabel titleLabel = new JLabel(prettyTitle(title));
        titleLabel.setFont(font(Font.BOLD, 20f));
        titleLabel.setForeground(TEXT_PRIMARY);
        left.add(titleLabel, BorderLayout.CENTER);
        panel.add(left, BorderLayout.WEST);

        if (searchField != null) {
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            searchPanel.setOpaque(false);
            searchPanel.add(searchField);
            panel.add(searchPanel, BorderLayout.EAST);
        }
        return panel;
    }

    public static JPanel createSummaryCard(String title, String value, Color accentColor) {
        return createSummaryCard(title, value, accentColor, Icons.guess(title));
    }

    public static JPanel createSummaryCard(String title, String value, Color accentColor, Icons.Type icon) {
        RoundedPanel card = new RoundedPanel(new BorderLayout(14, 0), 16);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 12));

        card.add(createIconBadge(icon, accentColor, 48, 24), BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(font(Font.PLAIN, 12.5f));
        titleLbl.setForeground(TEXT_SECONDARY);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(font(Font.BOLD, 21f));
        valueLbl.setForeground(TEXT_PRIMARY);
        // Tách đơn vị tiền tệ nhỏ lại để số tiền không bị cắt chữ
        for (String unit : new String[]{" VNĐ", " đ"}) {
            if (value.endsWith(unit)) {
                String num = value.substring(0, value.length() - unit.length());
                valueLbl.setText("<html><span style='font-size:" + 16 + "pt'>" + num + "</span>"
                        + "<span style='font-size:10pt;color:#64748B;font-weight:normal'>" + unit + "</span></html>");
                break;
            }
        }
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(titleLbl);
        contentPanel.add(Box.createVerticalStrut(3));
        contentPanel.add(valueLbl);
        contentPanel.add(Box.createVerticalGlue());

        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }

    /** Backward-compatible helpers used by the existing business panels. */
    public static void thongBao(Component parent, String message) {
        showInfo(parent, message);
    }

    public static void baoLoi(Component parent, String message) {
        showError(parent, message);
    }

}
