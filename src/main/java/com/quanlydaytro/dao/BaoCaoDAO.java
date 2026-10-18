package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;

import java.sql.*;
import java.util.*;

/**
 * Truy vấn dữ liệu thống kê trực tiếp từ schema SQL Server hiện hành.
 */
public class BaoCaoDAO {
    protected Connection moKetNoi() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public List<Object[]> layDoanhThu(String tuThang, String denThang) throws SQLException {
        String sql = "SELECT h.ThangNam, COALESCE(SUM(t.SoTienTra),0) AS DaThu, " +
                "COALESCE(SUM(h.TongTien),0) AS TongHoaDon, " +
                "COALESCE(SUM(h.TongTien),0)-COALESCE(SUM(p.DaTra),0) AS ConNo " +
                "FROM HOADON h LEFT JOIN (SELECT MaHoaDon,SUM(SoTienTra) DaTra FROM THANHTOAN GROUP BY MaHoaDon) p ON p.MaHoaDon=h.MaHoaDon " +
                "LEFT JOIN THANHTOAN t ON t.MaHoaDon=h.MaHoaDon " +
                "WHERE TRY_CONVERT(date,'01/'+h.ThangNam,103) BETWEEN TRY_CONVERT(date,'01/'+?,103) AND EOMONTH(TRY_CONVERT(date,'01/'+?,103)) " +
                "GROUP BY h.ThangNam ORDER BY TRY_CONVERT(date,'01/'+h.ThangNam,103)";
        // Tính tiền đã thu riêng theo hóa đơn, tránh nhân tổng thanh toán khi join.
        sql = "SELECT h.ThangNam, SUM(COALESCE(x.DaThu,0)) DaThu, SUM(h.TongTien) TongHoaDon, SUM(CASE WHEN h.TongTien-COALESCE(x.DaThu,0)>0 THEN h.TongTien-COALESCE(x.DaThu,0) ELSE 0 END) ConNo " +
                "FROM HOADON h LEFT JOIN (SELECT MaHoaDon,SUM(SoTienTra) DaThu FROM THANHTOAN GROUP BY MaHoaDon) x ON x.MaHoaDon=h.MaHoaDon " +
                "WHERE TRY_CONVERT(date,'01/'+h.ThangNam,103) BETWEEN TRY_CONVERT(date,'01/'+?,103) AND EOMONTH(TRY_CONVERT(date,'01/'+?,103)) " +
                "GROUP BY h.ThangNam ORDER BY TRY_CONVERT(date,'01/'+h.ThangNam,103)";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = moKetNoi(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tuThang);
            ps.setString(2, denThang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    rows.add(new Object[]{rs.getString("ThangNam"), rs.getBigDecimal("DaThu"), rs.getBigDecimal("TongHoaDon"), rs.getBigDecimal("ConNo")});
            }
        }
        return rows;
    }

    public List<Object[]> layTinhTrangPhong() throws SQLException {
        String sql = "SELECT d.TenDay,p.TenPhong,p.TrangThai,p.SoNguoiO,p.GiaThueHienTai FROM PHONG p JOIN DAYTRO d ON d.MaDay=p.MaDay ORDER BY d.TenDay,p.TenPhong";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = moKetNoi(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                rows.add(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBigDecimal(5)});
        }
        return rows;
    }

    public List<Object[]> layTieuThu(String tuThang, String denThang) throws SQLException {
        String sql = "SELECT d.TenDay,p.TenPhong,di.ThangNam,COALESCE(di.ChiSoMoi-di.ChiSoCu,0) DienTieuThu,COALESCE(nu.ChiSoMoi-nu.ChiSoCu,0) NuocTieuThu " +
                "FROM PHONG p JOIN DAYTRO d ON d.MaDay=p.MaDay LEFT JOIN CHISODIEN di ON di.MaPhong=p.MaPhong LEFT JOIN CHISONUOC nu ON nu.MaPhong=p.MaPhong AND nu.ThangNam=di.ThangNam " +
                "WHERE di.ThangNam IS NOT NULL AND TRY_CONVERT(date,'01/'+di.ThangNam,103) BETWEEN TRY_CONVERT(date,'01/'+?,103) AND EOMONTH(TRY_CONVERT(date,'01/'+?,103)) " +
                "ORDER BY TRY_CONVERT(date,'01/'+di.ThangNam,103),d.TenDay,p.TenPhong";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = moKetNoi(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tuThang);
            ps.setString(2, denThang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    rows.add(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5)});
            }
        }
        return rows;
    }

    public List<Object[]> layCongNo(String tuThang, String denThang) throws SQLException {
        String sql = "SELECT h.MaHoaDon,h.ThangNam,p.TenPhong,n.HoTen,h.TongTien,COALESCE(t.DaTra,0) DaTra,CASE WHEN h.TongTien-COALESCE(t.DaTra,0)>0 THEN h.TongTien-COALESCE(t.DaTra,0) ELSE 0 END ConNo,h.TrangThai " +
                "FROM HOADON h JOIN HOPDONG hd ON hd.MaHopDong=h.MaHopDong JOIN PHONG p ON p.MaPhong=hd.MaPhong JOIN NGUOITHUE n ON n.MaNT=hd.MaNT LEFT JOIN (SELECT MaHoaDon,SUM(SoTienTra) DaTra FROM THANHTOAN GROUP BY MaHoaDon) t ON t.MaHoaDon=h.MaHoaDon " +
                "WHERE TRY_CONVERT(date,'01/'+h.ThangNam,103) BETWEEN TRY_CONVERT(date,'01/'+?,103) AND EOMONTH(TRY_CONVERT(date,'01/'+?,103)) AND h.TongTien>COALESCE(t.DaTra,0) ORDER BY h.ThangNam,p.TenPhong";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = moKetNoi(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tuThang);
            ps.setString(2, denThang);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    rows.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBigDecimal(5), rs.getBigDecimal(6), rs.getBigDecimal(7), rs.getString(8)});
            }
        }
        return rows;
    }
}
