package com.quanlydaytro.dao;

import com.quanlydaytro.model.ThanhToan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

/** DAO cho bảng THANHTOAN; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class ThanhToanDAO extends AbstractJdbcDAO<ThanhToan> {
    @Override protected String tableName() { return "THANHTOAN"; }
    @Override protected String idColumn() { return "MaThanhToan"; }
    @Override protected String[] columns() { return new String[] {"MaHoaDon", "NgayThanhToan", "SoTienTra", "GhiChu"}; }
    @Override protected Object[] values(ThanhToan e) { return new Object[] {e.getMaHoaDon(), e.getNgayThanhToan(), e.getSoTienTra(), e.getGhiChu()}; }
    @Override protected ThanhToan mapRow(ResultSet rs) throws SQLException {
        ThanhToan e = new ThanhToan();
        e.setMaThanhToan(rs.getInt("MaThanhToan"));
        e.setMaHoaDon(rs.getInt("MaHoaDon"));
        e.setNgayThanhToan(localDateTime(rs, "NgayThanhToan"));
        e.setSoTienTra(rs.getBigDecimal("SoTienTra"));
        e.setGhiChu(rs.getString("GhiChu"));
        return e;
    }
}
