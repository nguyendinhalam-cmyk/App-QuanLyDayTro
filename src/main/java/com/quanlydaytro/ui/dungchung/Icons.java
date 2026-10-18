package com.quanlydaytro.ui.dungchung;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Bộ icon vector vẽ bằng Java2D (không phụ thuộc font emoji, luôn sắc nét ở mọi độ phân giải).
 * Mỗi icon được thiết kế trên lưới 24x24 rồi scale theo kích thước yêu cầu.
 */
public final class Icons {

    public enum Type {
        DASHBOARD, USER, BUILDING, DOOR, PEOPLE, FILE, BOLT, RECEIPT, KEY, SWAP, CHART,
        MONEY, DROP, SEARCH, BELL, LOGOUT, CHEVRON_RIGHT, CHEVRON_DOWN, CALENDAR, HOME, LOCK, SHIELD, PLUS, EDIT, TRASH, REFRESH, SAVE, STOP, PRINT, SEND, CLOCK
    }

    private Icons() {}

    public static Icon icon(Type type, int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                paint(g2, type, x, y, size, color);
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }

            @Override
            public int getIconHeight() { return size; }
        };
    }

    /** Chọn icon phù hợp theo từ khóa trong tiêu đề (dùng cho card thống kê & tiêu đề trang). */
    public static Type guess(String text) {
        String t = text == null ? "" : text.toLowerCase();
        if (t.contains("doanh thu") || t.contains("công nợ") || t.contains("nợ") || t.contains("tiền") || t.contains("thanh toán") || t.contains("tài chính"))
            return Type.MONEY;
        if (t.contains("hóa đơn")) return Type.RECEIPT;
        if (t.contains("hợp đồng")) return Type.FILE;
        if (t.contains("người") || t.contains("nhân viên") || t.contains("chủ trọ")) return Type.PEOPLE;
        if (t.contains("tài khoản")) return Type.USER;
        if (t.contains("điện")) return Type.BOLT;
        if (t.contains("nước")) return Type.DROP;
        if (t.contains("trả phòng")) return Type.KEY;
        if (t.contains("chuyển")) return Type.SWAP;
        if (t.contains("dãy")) return Type.BUILDING;
        if (t.contains("phòng")) return Type.DOOR;
        if (t.contains("thống kê") || t.contains("báo cáo")) return Type.CHART;
        return Type.CHART;
    }

    public static void paint(Graphics2D g0, Type type, int x, int y, int size, Color color) {
        Graphics2D g2 = (Graphics2D) g0.create(); // không làm "rò" transform/stroke sang nơi gọi
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.translate(x, y);
        double s = size / 24.0;
        g2.scale(s, s);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (type) {
            case DASHBOARD -> {
                g2.draw(new RoundRectangle2D.Double(3.5, 3.5, 7, 7, 2, 2));
                g2.draw(new RoundRectangle2D.Double(13.5, 3.5, 7, 4.5, 2, 2));
                g2.draw(new RoundRectangle2D.Double(13.5, 11.5, 7, 9, 2, 2));
                g2.draw(new RoundRectangle2D.Double(3.5, 14, 7, 6.5, 2, 2));
            }
            case HOME -> {
                Path2D p = new Path2D.Double();
                p.moveTo(3.5, 11); p.lineTo(12, 3.8); p.lineTo(20.5, 11);
                g2.draw(p);
                Path2D b = new Path2D.Double();
                b.moveTo(5.5, 9.8); b.lineTo(5.5, 20); b.lineTo(18.5, 20); b.lineTo(18.5, 9.8);
                g2.draw(b);
                g2.draw(new Line2D.Double(10, 20, 10, 14));
                g2.draw(new Line2D.Double(14, 20, 14, 14));
                g2.draw(new Line2D.Double(10, 14, 14, 14));
            }
            case USER -> {
                g2.draw(new Ellipse2D.Double(8, 3.8, 8, 8));
                g2.draw(new Arc2D.Double(4.5, 14, 15, 13, 0, 180, Arc2D.OPEN));
            }
            case PEOPLE -> {
                g2.draw(new Ellipse2D.Double(5.5, 4.5, 6.5, 6.5));
                g2.draw(new Arc2D.Double(2.5, 14, 12.5, 11, 0, 180, Arc2D.OPEN));
                g2.draw(new Arc2D.Double(14.5, 4.5, 5.5, 5.5, 60, 240, Arc2D.OPEN));
                g2.draw(new Arc2D.Double(15, 13.8, 7, 8, 0, 100, Arc2D.OPEN));
            }
            case BUILDING -> {
                g2.draw(new RoundRectangle2D.Double(4.5, 3.5, 10, 17, 2, 2));
                Path2D p = new Path2D.Double();
                p.moveTo(14.5, 9.5); p.lineTo(19.5, 9.5); p.lineTo(19.5, 20.5); p.lineTo(14.5, 20.5);
                g2.draw(p);
                for (int r = 0; r < 3; r++) {
                    g2.fill(new Ellipse2D.Double(7.4, 7 + r * 4, 1.8, 1.8));
                    g2.fill(new Ellipse2D.Double(10.4, 7 + r * 4, 1.8, 1.8));
                }
            }
            case DOOR -> {
                g2.draw(new RoundRectangle2D.Double(6, 3.5, 12, 17, 2, 2));
                g2.fill(new Ellipse2D.Double(14, 11.2, 2.2, 2.2));
                g2.draw(new Line2D.Double(3.5, 20.5, 20.5, 20.5));
            }
            case FILE -> {
                Path2D p = new Path2D.Double();
                p.moveTo(14, 3.5); p.lineTo(6.5, 3.5); p.quadTo(5, 3.5, 5, 5); p.lineTo(5, 19);
                p.quadTo(5, 20.5, 6.5, 20.5); p.lineTo(17.5, 20.5); p.quadTo(19, 20.5, 19, 19);
                p.lineTo(19, 8.5); p.closePath();
                g2.draw(p);
                Path2D f = new Path2D.Double();
                f.moveTo(14, 3.5); f.lineTo(14, 8.5); f.lineTo(19, 8.5);
                g2.draw(f);
                g2.draw(new Line2D.Double(8.5, 13, 15.5, 13));
                g2.draw(new Line2D.Double(8.5, 16.5, 13.5, 16.5));
            }
            case RECEIPT -> {
                Path2D p = new Path2D.Double();
                p.moveTo(5.5, 3.5); p.lineTo(18.5, 3.5); p.lineTo(18.5, 20.5);
                p.lineTo(16, 18.8); p.lineTo(13.5, 20.5); p.lineTo(11, 18.8); p.lineTo(8.5, 20.5);
                p.lineTo(5.5, 18.8); p.closePath();
                g2.draw(p);
                g2.draw(new Line2D.Double(8.5, 8, 15.5, 8));
                g2.draw(new Line2D.Double(8.5, 11.5, 15.5, 11.5));
                g2.draw(new Line2D.Double(8.5, 15, 12, 15));
            }
            case BOLT -> {
                Path2D p = new Path2D.Double();
                p.moveTo(13.5, 2.5); p.lineTo(5, 13.5); p.lineTo(11, 13.5);
                p.lineTo(10, 21.5); p.lineTo(19, 10); p.lineTo(13, 10); p.closePath();
                g2.draw(p);
            }
            case DROP -> {
                Path2D p = new Path2D.Double();
                p.moveTo(12, 3);
                p.curveTo(12, 3, 5.5, 10, 5.5, 14.5);
                p.curveTo(5.5, 18.2, 8.4, 21, 12, 21);
                p.curveTo(15.6, 21, 18.5, 18.2, 18.5, 14.5);
                p.curveTo(18.5, 10, 12, 3, 12, 3);
                p.closePath();
                g2.draw(p);
            }
            case KEY -> {
                g2.draw(new Ellipse2D.Double(3.5, 11.5, 8, 8));
                g2.draw(new Line2D.Double(10.5, 12.5, 20, 3.5));
                g2.draw(new Line2D.Double(16, 7.5, 18.5, 10));
                g2.fill(new Ellipse2D.Double(6.3, 14.3, 2.4, 2.4));
            }
            case SWAP -> {
                g2.draw(new Line2D.Double(4, 8, 20, 8));
                Path2D a = new Path2D.Double();
                a.moveTo(16, 4); a.lineTo(20, 8); a.lineTo(16, 12);
                g2.draw(a);
                g2.draw(new Line2D.Double(20, 16.5, 4, 16.5));
                Path2D b = new Path2D.Double();
                b.moveTo(8, 12.5); b.lineTo(4, 16.5); b.lineTo(8, 20.5);
                g2.draw(b);
            }
            case CHART -> {
                g2.draw(new RoundRectangle2D.Double(4.5, 12, 4, 8.5, 1.5, 1.5));
                g2.draw(new RoundRectangle2D.Double(10, 7, 4, 13.5, 1.5, 1.5));
                g2.draw(new RoundRectangle2D.Double(15.5, 3.5, 4, 17, 1.5, 1.5));
            }
            case MONEY -> {
                g2.draw(new RoundRectangle2D.Double(2.5, 6, 19, 12, 3, 3));
                g2.draw(new Ellipse2D.Double(9, 9, 6, 6));
                g2.fill(new Ellipse2D.Double(5.2, 11.2, 1.6, 1.6));
                g2.fill(new Ellipse2D.Double(17.2, 11.2, 1.6, 1.6));
            }
            case SEARCH -> {
                g2.draw(new Ellipse2D.Double(4, 4, 11.5, 11.5));
                g2.draw(new Line2D.Double(13.5, 13.5, 20, 20));
            }
            case BELL -> {
                Path2D p = new Path2D.Double();
                p.moveTo(6, 16.5); p.lineTo(6, 11);
                p.curveTo(6, 7.5, 8.6, 5, 12, 5);
                p.curveTo(15.4, 5, 18, 7.5, 18, 11);
                p.lineTo(18, 16.5); p.lineTo(20, 18); p.lineTo(4, 18); p.closePath();
                g2.draw(p);
                g2.draw(new Arc2D.Double(9.8, 17.5, 4.4, 4, 180, 180, Arc2D.OPEN));
                g2.draw(new Line2D.Double(12, 3, 12, 5));
            }
            case LOGOUT -> {
                Path2D p = new Path2D.Double();
                p.moveTo(10, 4); p.lineTo(5.5, 4); p.quadTo(4.5, 4, 4.5, 5); p.lineTo(4.5, 19);
                p.quadTo(4.5, 20, 5.5, 20); p.lineTo(10, 20);
                g2.draw(p);
                g2.draw(new Line2D.Double(9.5, 12, 20, 12));
                Path2D a = new Path2D.Double();
                a.moveTo(16, 8); a.lineTo(20, 12); a.lineTo(16, 16);
                g2.draw(a);
            }
            case CHEVRON_RIGHT -> {
                Path2D p = new Path2D.Double();
                p.moveTo(9, 5.5); p.lineTo(15.5, 12); p.lineTo(9, 18.5);
                g2.draw(p);
            }
            case CHEVRON_DOWN -> {
                Path2D p = new Path2D.Double();
                p.moveTo(5.5, 9); p.lineTo(12, 15.5); p.lineTo(18.5, 9);
                g2.draw(p);
            }
            case CALENDAR -> {
                g2.draw(new RoundRectangle2D.Double(3.5, 5, 17, 15.5, 3, 3));
                g2.draw(new Line2D.Double(3.5, 10, 20.5, 10));
                g2.draw(new Line2D.Double(8, 3, 8, 6.5));
                g2.draw(new Line2D.Double(16, 3, 16, 6.5));
            }
            case LOCK -> {
                g2.draw(new RoundRectangle2D.Double(5, 10.5, 14, 10, 3, 3));
                g2.draw(new Arc2D.Double(7.5, 3.5, 9, 14, 0, 180, Arc2D.OPEN));
                g2.fill(new Ellipse2D.Double(10.9, 14, 2.2, 2.2));
            }
            case PLUS -> {
                g2.draw(new Line2D.Double(12, 5, 12, 19));
                g2.draw(new Line2D.Double(5, 12, 19, 12));
            }
            case EDIT -> {
                Path2D p = new Path2D.Double();
                p.moveTo(4.5, 19.5); p.lineTo(5.5, 15); p.lineTo(16, 4.5); p.lineTo(19.5, 8); p.lineTo(9, 18.5); p.closePath();
                g2.draw(p);
                g2.draw(new Line2D.Double(13.8, 6.8, 17.2, 10.2));
            }
            case TRASH -> {
                g2.draw(new Line2D.Double(4, 7, 20, 7));
                g2.draw(new Line2D.Double(9.5, 7, 9.5, 4.5));
                g2.draw(new Line2D.Double(14.5, 7, 14.5, 4.5));
                g2.draw(new Line2D.Double(9.5, 4.5, 14.5, 4.5));
                Path2D p = new Path2D.Double();
                p.moveTo(6, 7); p.lineTo(7, 19.5); p.quadTo(7.1, 20.5, 8.2, 20.5); p.lineTo(15.8, 20.5);
                p.quadTo(16.9, 20.5, 17, 19.5); p.lineTo(18, 7);
                g2.draw(p);
                g2.draw(new Line2D.Double(10, 11, 10, 16.5));
                g2.draw(new Line2D.Double(14, 11, 14, 16.5));
            }
            case REFRESH -> {
                g2.draw(new Arc2D.Double(4.5, 4.5, 15, 15, 40, 270, Arc2D.OPEN));
                Path2D a = new Path2D.Double();
                a.moveTo(15.5, 3.5); a.lineTo(18.2, 7.4); a.lineTo(13.6, 8.4);
                g2.draw(a);
            }
            case SAVE -> {
                Path2D p = new Path2D.Double();
                p.moveTo(5.5, 3.5); p.lineTo(16.5, 3.5); p.lineTo(20.5, 7.5); p.lineTo(20.5, 19);
                p.quadTo(20.5, 20.5, 19, 20.5); p.lineTo(5.5, 20.5); p.quadTo(3.5, 20.5, 3.5, 19);
                p.lineTo(3.5, 5.5); p.quadTo(3.5, 3.5, 5.5, 3.5); p.closePath();
                g2.draw(p);
                g2.draw(new Rectangle2D.Double(7.5, 3.5, 7, 5));
                g2.draw(new RoundRectangle2D.Double(7, 13, 10, 7.5, 2, 2));
            }
            case STOP -> {
                g2.draw(new Ellipse2D.Double(3.5, 3.5, 17, 17));
                g2.draw(new Line2D.Double(8.5, 8.5, 15.5, 15.5));
                g2.draw(new Line2D.Double(15.5, 8.5, 8.5, 15.5));
            }
            case PRINT -> {
                g2.draw(new RoundRectangle2D.Double(3.5, 8.5, 17, 8.5, 3, 3));
                g2.draw(new Rectangle2D.Double(7, 3.5, 10, 5));
                g2.draw(new Rectangle2D.Double(7, 14, 10, 6.5));
                g2.fill(new Ellipse2D.Double(16, 11, 1.6, 1.6));
            }
            case SEND -> {
                Path2D p = new Path2D.Double();
                p.moveTo(3.5, 11); p.lineTo(20.5, 3.5); p.lineTo(14, 20.5); p.lineTo(11, 13.5); p.closePath();
                g2.draw(p);
                g2.draw(new Line2D.Double(11, 13.5, 20.5, 3.5));
            }
            case CLOCK -> {
                g2.draw(new Ellipse2D.Double(3.5, 3.5, 17, 17));
                Path2D p = new Path2D.Double();
                p.moveTo(12, 7.5); p.lineTo(12, 12); p.lineTo(15.5, 14);
                g2.draw(p);
            }
            case SHIELD -> {
                Path2D p = new Path2D.Double();
                p.moveTo(12, 3); p.lineTo(19.5, 6); p.lineTo(19.5, 12);
                p.curveTo(19.5, 16.5, 16, 19.5, 12, 21);
                p.curveTo(8, 19.5, 4.5, 16.5, 4.5, 12);
                p.lineTo(4.5, 6); p.closePath();
                g2.draw(p);
                Path2D c = new Path2D.Double();
                c.moveTo(8.5, 12); c.lineTo(11, 14.5); c.lineTo(15.5, 9.5);
                g2.draw(c);
            }
        }
        g2.dispose();
    }
}
