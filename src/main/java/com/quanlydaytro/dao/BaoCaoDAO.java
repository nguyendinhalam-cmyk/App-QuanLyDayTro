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
        // Dùng trực tiếp các Function của CSDL để tính tổng đã thu và số tiền còn lại theo từng hóa đơn.
        String sql = "SELECT h.ThangNam, SUM(COALESCE(f.DaThu,0)) AS DaThu, " +
                "SUM(h.TongTien) AS TongHoaDon, " +
                "SUM(CASE WHEN COALESCE(f.ConLai,0)>0 THEN f.ConLai ELSE 0 END) AS ConNo " +
                "FROM HOADON h " +
                "OUTER APPLY (SELECT dbo.FN_TongTienDaThanhToan(h.MaHoaDon) AS DaThu, " +
                "dbo.FN_TinhTienConLai(h.MaHoaDon) AS ConLai) f " +
                "WHERE TRY_CONVERT(date,'01/'+h.ThangNam,103) BETWEEN TRY_CONVERT(date,'01/'+?,103) " +
                "AND EOMONTH(TRY_CONVERT(date,'01/'+?,103)) " +
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

    /**
     * Báo cáo doanh thu theo dãy trọ, gọi trực tiếp dbo.SP_ThongKeDoanhThu.
     */
    public List<Object[]> layDoanhThuTheoDay(java.time.LocalDate tuNgay, java.time.LocalDate denNgay, Integer maDay) throws SQLException {
        if (tuNgay == null || denNgay == null) throw new IllegalArgumentException("Vui lòng chọn khoảng ngày báo cáo.");
        if (tuNgay.isAfter(denNgay)) throw new IllegalArgumentException("Từ ngày không được sau đến ngày.");
        String sql = "{call dbo.SP_ThongKeDoanhThu(?,?,?)}";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = moKetNoi(); CallableStatement cs = c.prepareCall(sql)) {
            cs.setDate(1, java.sql.Date.valueOf(tuNgay));
            cs.setDate(2, java.sql.Date.valueOf(denNgay));
            if (maDay == null || maDay <= 0) cs.setNull(3, Types.INTEGER);
            else cs.setInt(3, maDay);
            boolean hasResult = cs.execute();
            while (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs != null) while (rs.next()) {
                        rows.add(new Object[]{rs.getInt("MaDay"), rs.getString("TenDay"), rs.getInt("SoHoaDon"),
                                rs.getBigDecimal("TongTienHoaDon"), rs.getBigDecimal("TongDoanhThuThucThu")});
                    }
                }
                hasResult = cs.getMoreResults();
            }
        }
        return rows;
    }
}
