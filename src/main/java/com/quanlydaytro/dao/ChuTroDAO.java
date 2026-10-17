package com.quanlydaytro.dao;

import com.quanlydaytro.model.ChuTro;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng CHUTRO; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class ChuTroDAO extends AbstractJdbcDAO<ChuTro> {
    @Override protected String tableName() { return "CHUTRO"; }
    @Override protected String idColumn() { return "MaChuTro"; }
    @Override protected String[] columns() { return new String[] {"HoTen", "SoDienThoai"}; }
    @Override protected Object[] values(ChuTro e) { return new Object[] {e.getHoTen(), e.getSoDienThoai()}; }
    @Override protected ChuTro mapRow(ResultSet rs) throws SQLException {
        ChuTro e = new ChuTro();
        e.setMaChuTro(rs.getInt("MaChuTro"));
        e.setHoTen(rs.getString("HoTen"));
        e.setSoDienThoai(rs.getString("SoDienThoai"));
        return e;
    }
}
