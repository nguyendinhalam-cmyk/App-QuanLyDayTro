package com.quanlydaytro.dao;

import com.quanlydaytro.model.VaiTro;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng VAITRO; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class VaiTroDAO extends AbstractJdbcDAO<VaiTro> {
    @Override protected String tableName() { return "VAITRO"; }
    @Override protected String idColumn() { return "MaVaiTro"; }
    @Override protected String[] columns() { return new String[] {"TenVaiTro"}; }
    @Override protected Object[] values(VaiTro e) { return new Object[] {e.getTenVaiTro()}; }
    @Override protected VaiTro mapRow(ResultSet rs) throws SQLException {
        VaiTro e = new VaiTro();
        e.setMaVaiTro(rs.getInt("MaVaiTro"));
        e.setTenVaiTro(rs.getString("TenVaiTro"));
        return e;
    }
}
