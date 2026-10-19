package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Truy vấn và giao dịch chuyển phòng theo schema QL_NHA_TRO.
 */
public class ChuyenPhongDAO {
    public List<Object[]> danhSachHopDongHieuLuc() throws SQLException {
        String sql = "SELECT h.MaHopDong, h.MaPhong, p.TenPhong, h.MaNT, n.HoTen, h.NgayBatDau, h.NgayKetThuc, h.GiaThueThoaThuan, h.TienCoc " +
                "FROM HOPDONG h JOIN PHONG p ON p.MaPhong=h.MaPhong JOIN NGUOITHUE n ON n.MaNT=h.MaNT " +
                "WHERE h.TrangThai=N'HieuLuc' ORDER BY h.MaHopDong DESC";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                rows.add(new Object[]{rs.getInt("MaHopDong"), rs.getInt("MaPhong"), rs.getString("TenPhong"), rs.getInt("MaNT"), rs.getString("HoTen"), rs.getDate("NgayBatDau").toLocalDate(), rs.getDate("NgayKetThuc").toLocalDate(), rs.getBigDecimal("GiaThueThoaThuan"), rs.getBigDecimal("TienCoc")});
        }
        return rows;
    }

    public List<Object[]> danhSachPhongTrong(int maPhongHienTai) throws SQLException {
        // SP_TimPhongTrong là nguồn truy vấn chuẩn; lọc phòng hiện tại ở Java sau khi nhận kết quả.
        String sql = "{call dbo.SP_TimPhongTrong(?,?)}";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); CallableStatement cs = c.prepareCall(sql)) {
            cs.setNull(1, Types.INTEGER);
            cs.setNull(2, Types.NVARCHAR);
            boolean hasResult = cs.execute();
            while (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs != null) while (rs.next()) {
                        int maPhong = rs.getInt("MaPhong");
                        if (maPhong != maPhongHienTai) {
                            rows.add(new Object[]{maPhong, rs.getString("TenPhong"), rs.getInt("MaDay"), rs.getBigDecimal("GiaThueHienTai")});
                        }
                    }
                }
                hasResult = cs.getMoreResults();
            }
        }
        return rows;
    }

    /**
     * Chuyển phòng thông qua transaction TX_ChuyenPhong trong SQL Server.
     * Giữ nguyên hợp đồng mới có thời hạn một năm như luồng giao diện hiện tại.
     */
    public void chuyenPhong(int maHopDong, int maPhongMoi, LocalDate ngayChuyen, BigDecimal giaThueMoi, BigDecimal tienCocMoi) throws SQLException {
        if (maHopDong <= 0 || maPhongMoi <= 0)
            throw new IllegalArgumentException("Vui lòng chọn hợp đồng và phòng mới.");
        if (ngayChuyen == null || giaThueMoi == null || giaThueMoi.signum() <= 0 || tienCocMoi == null || tienCocMoi.signum() <= 0)
            throw new IllegalArgumentException("Ngày chuyển, giá thuê và tiền cọc phải hợp lệ.");

        String sql = "{call dbo.TX_ChuyenPhong(?,?,?,?,?,?)}";
        try (Connection c = DatabaseConnection.getConnection(); CallableStatement cs = c.prepareCall(sql)) {
            cs.setInt(1, maHopDong);
            cs.setInt(2, maPhongMoi);
            cs.setDate(3, Date.valueOf(ngayChuyen));
            cs.setDate(4, Date.valueOf(ngayChuyen.plusYears(1)));
            cs.setBigDecimal(5, giaThueMoi);
            cs.setBigDecimal(6, tienCocMoi);
            cs.execute();
        } catch (SQLException ex) {
            throw new SQLException("Không thể chuyển phòng: " + ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), ex);
        }
    }

}
