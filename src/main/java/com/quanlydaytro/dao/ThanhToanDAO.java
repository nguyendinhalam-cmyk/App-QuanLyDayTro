package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.model.ThanhToan;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * DAO chuyên biệt cho ghi nhận thanh toán và tổng hợp công nợ hóa đơn.
 */
public class ThanhToanDAO extends AbstractJdbcDAO<ThanhToan> {
    @Override
    protected String tableName() {
        return "THANHTOAN";
    }

    @Override
    protected String idColumn() {
        return "MaThanhToan";
    }

    @Override
    protected String[] columns() {
        return new String[]{"MaHoaDon", "NgayThanhToan", "SoTienTra", "GhiChu"};
    }

    @Override
    protected Object[] values(ThanhToan e) {
        return new Object[]{e.getMaHoaDon(), e.getNgayThanhToan(), e.getSoTienTra(), e.getGhiChu()};
    }

    @Override
    protected ThanhToan mapRow(ResultSet rs) throws SQLException {
        ThanhToan e = new ThanhToan();
        e.setMaThanhToan(rs.getInt("MaThanhToan"));
        e.setMaHoaDon(rs.getInt("MaHoaDon"));
        e.setNgayThanhToan(localDateTime(rs, "NgayThanhToan"));
        e.setSoTienTra(rs.getBigDecimal("SoTienTra"));
        e.setGhiChu(rs.getString("GhiChu"));
        return e;
    }

    public List<Object[]> danhSachCongNo() throws SQLException {
        String sql = "SELECT h.MaHoaDon,h.MaHopDong,h.ThangNam,h.TongTien,COALESCE(SUM(t.SoTienTra),0) DaThanhToan, h.TrangThai FROM HOADON h LEFT JOIN THANHTOAN t ON t.MaHoaDon=h.MaHoaDon GROUP BY h.MaHoaDon,h.MaHopDong,h.ThangNam,h.TongTien,h.TrangThai ORDER BY h.MaHoaDon DESC";
        List<Object[]> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BigDecimal tong = rs.getBigDecimal("TongTien"), da = rs.getBigDecimal("DaThanhToan");
                out.add(new Object[]{rs.getInt("MaHoaDon"), rs.getInt("MaHopDong"), rs.getString("ThangNam"), tong, da, tong.subtract(da).max(BigDecimal.ZERO), rs.getString("TrangThai")});
            }
        }
        return out;
    }

    public BigDecimal daThanhToan(int maHoaDon) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COALESCE(SUM(SoTienTra),0) FROM THANHTOAN WHERE MaHoaDon=?")) {
            ps.setInt(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        }
    }

    public BigDecimal tongHoaDon(int maHoaDon) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT TongTien FROM HOADON WHERE MaHoaDon=?")) {
            ps.setInt(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Không tìm thấy hóa đơn " + maHoaDon);
                return rs.getBigDecimal(1);
            }
        }
    }

    public int ghiNhanVaCapNhatTrangThai(ThanhToan e) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                BigDecimal tong;
                try (PreparedStatement ps = c.prepareStatement("SELECT TongTien FROM HOADON WITH (UPDLOCK, ROWLOCK) WHERE MaHoaDon=?")) {
                    ps.setInt(1, e.getMaHoaDon());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Hóa đơn không tồn tại.");
                        tong = rs.getBigDecimal(1);
                    }
                }
                BigDecimal da;
                try (PreparedStatement ps = c.prepareStatement("SELECT COALESCE(SUM(SoTienTra),0) FROM THANHTOAN WHERE MaHoaDon=?")) {
                    ps.setInt(1, e.getMaHoaDon());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        da = rs.getBigDecimal(1);
                    }
                }
                if (e.getSoTienTra().compareTo(tong.subtract(da)) > 0)
                    throw new SQLException("Số tiền trả vượt quá công nợ còn lại: " + tong.subtract(da));
                int id;
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO THANHTOAN(MaHoaDon,NgayThanhToan,SoTienTra,GhiChu) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, e.getMaHoaDon());
                    if (e.getNgayThanhToan() == null)
                        ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
                    else ps.setTimestamp(2, Timestamp.valueOf(e.getNgayThanhToan()));
                    ps.setBigDecimal(3, e.getSoTienTra());
                    ps.setString(4, e.getGhiChu());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        id = rs.getInt(1);
                    }
                }
                BigDecimal daMoi = da.add(e.getSoTienTra());
                String trangThai = daMoi.compareTo(tong) >= 0 ? "HoanTat" : (daMoi.signum() > 0 ? "DangThanhToan" : "ChuaThanhToan");
                try (PreparedStatement ps = c.prepareStatement("UPDATE HOADON SET TrangThai=? WHERE MaHoaDon=?")) {
                    ps.setString(1, trangThai);
                    ps.setInt(2, e.getMaHoaDon());
                    ps.executeUpdate();
                }
                c.commit();
                return id;
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException) throw (SQLException) ex;
                throw new SQLException(ex);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }
}
