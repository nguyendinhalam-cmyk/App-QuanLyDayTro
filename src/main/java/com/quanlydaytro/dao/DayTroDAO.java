package com.quanlydaytro.dao;

import com.quanlydaytro.model.DayTro;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng DAYTRO; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class DayTroDAO extends AbstractJdbcDAO<DayTro> {
    @Override protected String tableName() { return "DAYTRO"; }
    @Override protected String idColumn() { return "MaDay"; }
    @Override protected String[] columns() { return new String[] {"TenDay", "DiaChi", "MaChuTro"}; }
    @Override protected Object[] values(DayTro e) { return new Object[] {e.getTenDay(), e.getDiaChi(), e.getMaChuTro()}; }
    @Override protected DayTro mapRow(ResultSet rs) throws SQLException {
        DayTro e = new DayTro();
        e.setMaDay(rs.getInt("MaDay"));
        e.setTenDay(rs.getString("TenDay"));
        e.setDiaChi(rs.getString("DiaChi"));
        e.setMaChuTro(rs.getInt("MaChuTro"));
        return e;
    }
}
