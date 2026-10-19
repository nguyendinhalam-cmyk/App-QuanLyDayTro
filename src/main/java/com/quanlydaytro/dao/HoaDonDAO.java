package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.enums.TrangThaiHoaDon;
import com.quanlydaytro.model.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho HOADON, giữ nguyên tên bảng/cột theo database/schema.sql.
 */
public class HoaDonDAO extends AbstractJdbcDAO<HoaDon> {
    @Override
    protected String tableName() {
        return "HOADON";
    }

    @Override
    protected String idColumn() {
        return "MaHoaDon";
    }

    @Override
    protected String[] columns() {
        return new String[]{"MaHopDong", "ThangNam", "TienPhong", "TienDien", "TienNuoc", "TongTien", "HanThanhToan", "TrangThai"};
    }

    @Override
    protected Object[] values(HoaDon e) {
        return new Object[]{e.getMaHopDong(), e.getThangNam(), e.getTienPhong(), e.getTienDien(), e.getTienNuoc(), e.getTongTien(), e.getHanThanhToan(), toDb(e.getTrangThai())};
    }

    @Override
    protected HoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon e = new HoaDon();
        e.setMaHoaDon(rs.getInt("MaHoaDon"));
        e.setMaHopDong(rs.getInt("MaHopDong"));
        e.setThangNam(rs.getString("ThangNam"));
        e.setTienPhong(rs.getBigDecimal("TienPhong"));
        e.setTienDien(rs.getBigDecimal("TienDien"));
        e.setTienNuoc(rs.getBigDecimal("TienNuoc"));
        e.setTongTien(rs.getBigDecimal("TongTien"));
        e.setHanThanhToan(localDate(rs, "HanThanhToan"));
        e.setTrangThai(fromDb(rs.getString("TrangThai")));
        return e;
    }

    private String toDb(TrangThaiHoaDon t) {
        if (t == null) throw new IllegalArgumentException("Vui lòng chọn trạng thái hóa đơn.");
        return switch (t) {
            case CHUA_THANH_TOAN -> "ChuaThanhToan";
            case DANG_THANH_TOAN -> "DangThanhToan";
            case HOAN_TAT -> "HoanTat";
        };
    }

    private TrangThaiHoaDon fromDb(String s) throws SQLException {
        return switch (s) {
            case "ChuaThanhToan" -> TrangThaiHoaDon.CHUA_THANH_TOAN;
            case "DangThanhToan" -> TrangThaiHoaDon.DANG_THANH_TOAN;
            case "HoanTat" -> TrangThaiHoaDon.HOAN_TAT;
            default -> throw new SQLException("Trạng thái hóa đơn không hợp lệ: " + s);
        };
    }

    public List<HoaDon> timKiem(String tuKhoa) throws SQLException {
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        String sql = "SELECT * FROM HOADON WHERE CAST(MaHoaDon AS VARCHAR(12)) LIKE ? OR CAST(MaHopDong AS VARCHAR(12)) LIKE ? OR ThangNam LIKE ? OR TrangThai LIKE ? ORDER BY MaHoaDon DESC";
        List<HoaDon> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) ps.setString(i, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        }
        return out;
    }

    /**
     * Lập hóa đơn thông qua Stored Procedure dbo.SP_LapHoaDon.
     * Phần tính tiền điện/nước được thực hiện bằng các Function trong SQL Server.
     */
    public int lapHoaDon(int maHopDong, String thangNam, LocalDate hanThanhToan) throws SQLException {
        String sql = "{call dbo.SP_LapHoaDon(?,?,?)}";
        try (Connection c = DatabaseConnection.getConnection(); CallableStatement cs = c.prepareCall(sql)) {
            cs.setInt(1, maHopDong);
            cs.setString(2, thangNam);
            cs.setDate(3, Date.valueOf(hanThanhToan));
            boolean hasResult = cs.execute();
            while (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs != null && rs.next()) {
                        int id = rs.getInt("MaHoaDon");
                        if (rs.wasNull() || id <= 0) {
                            throw new SQLException("Stored Procedure không trả về mã hóa đơn hợp lệ.");
                        }
                        return id;
                    }
                }
                hasResult = cs.getMoreResults();
            }
        } catch (SQLException ex) {
            throw new SQLException("Không thể lập hóa đơn: " + ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), ex);
        }
        throw new SQLException("Stored Procedure không trả về mã hóa đơn vừa tạo.");
    }

}
