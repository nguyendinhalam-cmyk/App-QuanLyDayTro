package com.quanlydaytro.dao;

import com.quanlydaytro.model.GiaNuoc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

/** DAO cho bảng BANGGIANUOC; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class GiaNuocDAO extends AbstractJdbcDAO<GiaNuoc> {
    @Override protected String tableName() { return "BANGGIANUOC"; }
    @Override protected String idColumn() { return "MaGiaNuoc"; }
    @Override protected String[] columns() { return new String[] {"DonGia", "NgayApDung"}; }
    @Override protected Object[] values(GiaNuoc e) { return new Object[] {e.getDonGia(), e.getNgayApDung()}; }
    @Override protected GiaNuoc mapRow(ResultSet rs) throws SQLException {
        GiaNuoc e = new GiaNuoc();
        e.setMaGiaNuoc(rs.getInt("MaGiaNuoc"));
        e.setDonGia(rs.getBigDecimal("DonGia"));
        e.setNgayApDung(localDate(rs, "NgayApDung"));
        return e;
    }
}
