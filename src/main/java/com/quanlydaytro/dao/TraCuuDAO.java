package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;

import java.sql.*;
import java.util.*;

/**
 * Truy vấn tổng hợp phục vụ nghiệp vụ 13; chỉ đọc dữ liệu, dùng PreparedStatement.
 */
public class TraCuuDAO {
    public List<Object[]> traCuu(String loai, String tuKhoa) throws SQLException {
        String key = tuKhoa == null ? "" : tuKhoa.trim();
        List<Object[]> rows = new ArrayList<>();
        if ("Tất cả".equals(loai) || loai == null) {
            for (String type : List.of("Phòng", "Người thuê", "Hợp đồng", "Hóa đơn", "Công nợ"))
                rows.addAll(traCuu(type, key));
            return rows;
        }
        String like = "%" + key + "%";
        String sql;
        switch (loai) {
            case "Phòng" ->
                    sql = "SELECT TOP 300 CAST(p.MaPhong AS NVARCHAR(30)) Ma, CONCAT(p.TenPhong, N' - ', d.TenDay) ThongTin, d.DiaChi ChiTiet, p.TrangThai TrangThai, p.GiaThueHienTai SoTien, CAST(NULL AS DECIMAL(12,2)) CongNo FROM PHONG p JOIN DAYTRO d ON d.MaDay=p.MaDay WHERE p.TenPhong LIKE ? OR d.TenDay LIKE ? OR d.DiaChi LIKE ? OR p.TrangThai LIKE ? ORDER BY p.MaPhong DESC";
            case "Người thuê" ->
                    sql = "SELECT TOP 300 CAST(nt.MaNT AS NVARCHAR(30)) Ma, nt.HoTen ThongTin, CONCAT(N'CCCD: ',nt.CCCD,N' | SĐT: ',nt.SoDienThoai,N' | ',COALESCE(nt.DiaChiThuongTru,N'')) ChiTiet, CAST(NULL AS NVARCHAR(30)) TrangThai, CAST(NULL AS DECIMAL(12,2)) SoTien, CAST(NULL AS DECIMAL(12,2)) CongNo FROM NGUOITHUE nt WHERE nt.HoTen LIKE ? OR nt.CCCD LIKE ? OR nt.SoDienThoai LIKE ? OR COALESCE(nt.DiaChiThuongTru,N'') LIKE ? ORDER BY nt.MaNT DESC";
            case "Hợp đồng" ->
                    sql = "SELECT TOP 300 CAST(h.MaHopDong AS NVARCHAR(30)) Ma, CONCAT(N'HĐ ',h.MaHopDong,N' - ',nt.HoTen) ThongTin, CONCAT(N'Phòng ',p.TenPhong,N' | ',h.NgayBatDau,N' đến ',h.NgayKetThuc) ChiTiet, h.TrangThai TrangThai, h.GiaThueThoaThuan SoTien, h.TienCoc CongNo FROM HOPDONG h JOIN NGUOITHUE nt ON nt.MaNT=h.MaNT JOIN PHONG p ON p.MaPhong=h.MaPhong WHERE CAST(h.MaHopDong AS NVARCHAR(20)) LIKE ? OR nt.HoTen LIKE ? OR p.TenPhong LIKE ? OR h.TrangThai LIKE ? ORDER BY h.MaHopDong DESC";
            case "Hóa đơn" ->
                    sql = "SELECT TOP 300 CAST(hd.MaHoaDon AS NVARCHAR(30)) Ma, CONCAT(N'Hóa đơn ',hd.MaHoaDon,N' - ',nt.HoTen) ThongTin, CONCAT(N'Phòng ',p.TenPhong,N' | Kỳ ',hd.ThangNam,N' | Hạn ',hd.HanThanhToan) ChiTiet, hd.TrangThai TrangThai, hd.TongTien SoTien, CAST(NULL AS DECIMAL(12,2)) CongNo FROM HOADON hd JOIN HOPDONG h ON h.MaHopDong=hd.MaHopDong JOIN NGUOITHUE nt ON nt.MaNT=h.MaNT JOIN PHONG p ON p.MaPhong=h.MaPhong WHERE CAST(hd.MaHoaDon AS NVARCHAR(20)) LIKE ? OR nt.HoTen LIKE ? OR p.TenPhong LIKE ? OR hd.ThangNam LIKE ? OR hd.TrangThai LIKE ? ORDER BY hd.MaHoaDon DESC";
            case "Công nợ" ->
                    sql = "SELECT TOP 300 CAST(hd.MaHoaDon AS NVARCHAR(30)) Ma, CONCAT(N'Hóa đơn ',hd.MaHoaDon,N' - ',nt.HoTen) ThongTin, CONCAT(N'Phòng ',p.TenPhong,N' | Kỳ ',hd.ThangNam) ChiTiet, hd.TrangThai TrangThai, hd.TongTien SoTien, CASE WHEN hd.TongTien-COALESCE(tt.DaTra,0)>0 THEN hd.TongTien-COALESCE(tt.DaTra,0) ELSE 0 END CongNo FROM HOADON hd JOIN HOPDONG h ON h.MaHopDong=hd.MaHopDong JOIN NGUOITHUE nt ON nt.MaNT=h.MaNT JOIN PHONG p ON p.MaPhong=h.MaPhong LEFT JOIN (SELECT MaHoaDon,SUM(SoTienTra) DaTra FROM THANHTOAN GROUP BY MaHoaDon) tt ON tt.MaHoaDon=hd.MaHoaDon WHERE hd.TongTien>COALESCE(tt.DaTra,0) AND (CAST(hd.MaHoaDon AS NVARCHAR(20)) LIKE ? OR nt.HoTen LIKE ? OR p.TenPhong LIKE ? OR hd.ThangNam LIKE ? OR hd.TrangThai LIKE ?) ORDER BY hd.MaHoaDon DESC";
            default -> throw new IllegalArgumentException("Loại tra cứu không hợp lệ.");
        }
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            int count = switch (loai) {
                case "Hóa đơn", "Công nợ" -> 5;
                case "Phòng", "Người thuê", "Hợp đồng" -> 4;
                default -> 0;
            };
            for (int i = 1; i <= count; i++) ps.setString(i, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    rows.add(new Object[]{rs.getString("Ma"), rs.getString("ThongTin"), rs.getString("ChiTiet"), rs.getString("TrangThai"), rs.getBigDecimal("SoTien"), rs.getBigDecimal("CongNo")});
            }
        }
        return rows;
    }
}
