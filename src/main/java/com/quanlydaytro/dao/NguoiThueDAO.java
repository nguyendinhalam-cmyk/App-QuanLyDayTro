package com.quanlydaytro.dao;

import com.quanlydaytro.model.NguoiThue;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng NGUOITHUE; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class NguoiThueDAO extends AbstractJdbcDAO<NguoiThue> {
    @Override protected String tableName() { return "NGUOITHUE"; }
    @Override protected String idColumn() { return "MaNT"; }
    @Override protected String[] columns() { return new String[] {"HoTen", "CCCD", "SoDienThoai", "DiaChiThuongTru"}; }
    @Override protected Object[] values(NguoiThue e) { return new Object[] {e.getHoTen(), e.getCCCD(), e.getSoDienThoai(), e.getDiaChiThuongTru()}; }
    @Override protected NguoiThue mapRow(ResultSet rs) throws SQLException {
        NguoiThue e = new NguoiThue();
        e.setMaNguoiThue(rs.getInt("MaNT"));
        e.setHoTen(rs.getString("HoTen"));
        e.setCCCD(rs.getString("CCCD"));
        e.setSoDienThoai(rs.getString("SoDienThoai"));
        e.setDiaChiThuongTru(rs.getString("DiaChiThuongTru"));
        return e;
    }
}
