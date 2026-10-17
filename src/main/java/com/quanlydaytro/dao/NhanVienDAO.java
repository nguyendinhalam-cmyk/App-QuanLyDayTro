package com.quanlydaytro.dao;

import com.quanlydaytro.model.NhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng NHANVIEN; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class NhanVienDAO extends AbstractJdbcDAO<NhanVien> {
    @Override protected String tableName() { return "NHANVIEN"; }
    @Override protected String idColumn() { return "MaNV"; }
    @Override protected String[] columns() { return new String[] {"HoTen", "SoDienThoai", "ChucVu"}; }
    @Override protected Object[] values(NhanVien e) { return new Object[] {e.getHoTen(), e.getSoDienThoai(), e.getChucVu()}; }
    @Override protected NhanVien mapRow(ResultSet rs) throws SQLException {
        NhanVien e = new NhanVien();
        e.setMaNhanVien(rs.getInt("MaNV"));
        e.setHoTen(rs.getString("HoTen"));
        e.setSoDienThoai(rs.getString("SoDienThoai"));
        e.setChucVu(rs.getString("ChucVu"));
        return e;
    }
}
