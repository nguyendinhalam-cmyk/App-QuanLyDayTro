package com.quanlydaytro.dao;

import com.quanlydaytro.model.ChiSoNuoc;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng CHISONUOC; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class ChiSoNuocDAO extends AbstractJdbcDAO<ChiSoNuoc> {
    @Override protected String tableName() { return "CHISONUOC"; }
    @Override protected String idColumn() { return "MaCSN"; }
    @Override protected String[] columns() { return new String[] {"MaPhong", "ThangNam", "ChiSoCu", "ChiSoMoi", "NgayGhi"}; }
    @Override protected Object[] values(ChiSoNuoc e) { return new Object[] {e.getMaPhong(), e.getThangNam(), e.getChiSoCu(), e.getChiSoMoi(), e.getNgayGhi()}; }
    @Override protected ChiSoNuoc mapRow(ResultSet rs) throws SQLException {
        ChiSoNuoc e = new ChiSoNuoc();
        e.setMaChiSoNuoc(rs.getInt("MaCSN"));
        e.setMaPhong(rs.getInt("MaPhong"));
        e.setThangNam(rs.getString("ThangNam"));
        e.setChiSoCu(rs.getInt("ChiSoCu"));
        e.setChiSoMoi(rs.getInt("ChiSoMoi"));
        e.setNgayGhi(localDate(rs, "NgayGhi"));
        return e;
    }
}
