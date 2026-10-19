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
        String sql = "SELECT dbo.FN_TongTienDaThanhToan(?) AS DaThanhToan";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Không tính được tổng tiền đã thanh toán.");
                return rs.getBigDecimal("DaThanhToan");
            }
        }
    }

    public BigDecimal conLaiTheoFunction(int maHoaDon) throws SQLException {
        String sql = "SELECT dbo.FN_TinhTienConLai(?) AS ConLai, " +
                "CASE WHEN EXISTS (SELECT 1 FROM HOADON WHERE MaHoaDon=?) THEN 1 ELSE 0 END AS TonTai";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
            ps.setInt(2, maHoaDon);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Không tính được số tiền còn lại.");
                if (rs.getInt("TonTai") != 1) throw new SQLException("Không tìm thấy hóa đơn " + maHoaDon);
                return rs.getBigDecimal("ConLai");
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

    /**
     * Ghi nhận thanh toán bằng transaction phía SQL Server.
     * TX_ThanhToanHoaDon khóa hóa đơn, kiểm tra công nợ và rollback nếu có lỗi.
     */
    public int ghiNhanVaCapNhatTrangThai(ThanhToan e) throws SQLException {
        String sql = "{call dbo.TX_ThanhToanHoaDon(?,?,?)}";
        try (Connection c = DatabaseConnection.getConnection(); CallableStatement cs = c.prepareCall(sql)) {
            cs.setInt(1, e.getMaHoaDon());
            cs.setBigDecimal(2, e.getSoTienTra());
            if (e.getGhiChu() == null || e.getGhiChu().isBlank()) cs.setNull(3, Types.NVARCHAR);
            else cs.setNString(3, e.getGhiChu());

            boolean hasResult = cs.execute();
            while (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs != null && rs.next()) {
                        int id = rs.getInt("MaThanhToan");
                        if (rs.wasNull() || id <= 0)
                            throw new SQLException("Transaction không trả về mã thanh toán hợp lệ.");
                        return id;
                    }
                }
                hasResult = cs.getMoreResults();
            }
        } catch (SQLException ex) {
            throw new SQLException("Không thể ghi nhận thanh toán: " + ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), ex);
        }
        throw new SQLException("Transaction không trả về mã thanh toán vừa tạo.");
    }

}
