package com.quanlydaytro.dao;

import com.quanlydaytro.model.ChiSoDien;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng CHISODIEN; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class ChiSoDienDAO extends AbstractJdbcDAO<ChiSoDien> {
    @Override protected String tableName() { return "CHISODIEN"; }
    @Override protected String idColumn() { return "MaCSD"; }
    @Override protected String[] columns() { return new String[] {"MaPhong", "ThangNam", "ChiSoCu", "ChiSoMoi", "NgayGhi"}; }
    @Override protected Object[] values(ChiSoDien e) { return new Object[] {e.getMaPhong(), e.getThangNam(), e.getChiSoCu(), e.getChiSoMoi(), e.getNgayGhi()}; }
    @Override protected ChiSoDien mapRow(ResultSet rs) throws SQLException {
        ChiSoDien e = new ChiSoDien();
        e.setMaChiSoDien(rs.getInt("MaCSD"));
        e.setMaPhong(rs.getInt("MaPhong"));
        e.setThangNam(rs.getString("ThangNam"));
        e.setChiSoCu(rs.getInt("ChiSoCu"));
        e.setChiSoMoi(rs.getInt("ChiSoMoi"));
        e.setNgayGhi(localDate(rs, "NgayGhi"));
        return e;
    }
}
