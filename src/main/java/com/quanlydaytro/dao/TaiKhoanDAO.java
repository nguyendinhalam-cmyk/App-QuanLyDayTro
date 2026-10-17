package com.quanlydaytro.dao;

import com.quanlydaytro.model.TaiKhoan;
import java.sql.ResultSet;
import java.sql.SQLException;

/** DAO cho bảng TAIKHOAN; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class TaiKhoanDAO extends AbstractJdbcDAO<TaiKhoan> {
    @Override protected String tableName() { return "TAIKHOAN"; }
    @Override protected String idColumn() { return "MaTaiKhoan"; }
    @Override protected String[] columns() { return new String[] {"TenDangNhap", "MatKhau", "MaNV", "MaVaiTro"}; }
    @Override protected Object[] values(TaiKhoan e) { return new Object[] {e.getTenDangNhap(), e.getMatKhau(), e.getMaNV(), e.getMaVaiTro()}; }
    @Override protected TaiKhoan mapRow(ResultSet rs) throws SQLException {
        TaiKhoan e = new TaiKhoan();
        e.setMaTaiKhoan(rs.getInt("MaTaiKhoan"));
        e.setTenDangNhap(rs.getString("TenDangNhap"));
        e.setMatKhau(rs.getString("MatKhau"));
        e.setMaNV(rs.getInt("MaNV"));
        e.setMaVaiTro(rs.getInt("MaVaiTro"));
        return e;
    }
}
