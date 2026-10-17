package com.quanlydaytro.dao;

import com.quanlydaytro.model.GiaDien;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

/** DAO cho bảng BANGGIADIEN; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class GiaDienDAO extends AbstractJdbcDAO<GiaDien> {
    @Override protected String tableName() { return "BANGGIADIEN"; }
    @Override protected String idColumn() { return "MaGiaDien"; }
    @Override protected String[] columns() { return new String[] {"DonGia", "NgayApDung"}; }
    @Override protected Object[] values(GiaDien e) { return new Object[] {e.getDonGia(), e.getNgayApDung()}; }
    @Override protected GiaDien mapRow(ResultSet rs) throws SQLException {
        GiaDien e = new GiaDien();
        e.setMaGiaDien(rs.getInt("MaGiaDien"));
        e.setDonGia(rs.getBigDecimal("DonGia"));
        e.setNgayApDung(localDate(rs, "NgayApDung"));
        return e;
    }
}
