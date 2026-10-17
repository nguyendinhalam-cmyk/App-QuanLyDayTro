package com.quanlydaytro.dao;

import com.quanlydaytro.model.Phong;
import com.quanlydaytro.enums.TrangThaiPhong;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

/** DAO cho bảng PHONG; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class PhongDAO extends AbstractJdbcDAO<Phong> {
    @Override protected String tableName() { return "PHONG"; }
    @Override protected String idColumn() { return "MaPhong"; }
    @Override protected String[] columns() { return new String[] {"TenPhong", "GiaThueHienTai", "SoNguoiO", "TrangThai", "MaDay"}; }
    @Override protected Object[] values(Phong e) { return new Object[] {e.getTenPhong(), e.getGiaThueHienTai(), e.getSoNguoiO(), giaTriTrangThai(e.getTrangThai()), e.getMaDay()}; }
    @Override protected Phong mapRow(ResultSet rs) throws SQLException {
        Phong e = new Phong();
        e.setMaPhong(rs.getInt("MaPhong"));
        e.setTenPhong(rs.getString("TenPhong"));
        e.setGiaThueHienTai(rs.getBigDecimal("GiaThueHienTai"));
        e.setSoNguoiO(rs.getInt("SoNguoiO"));
        e.setTrangThai(trangThaiTuCSDL(rs.getString("TrangThai")));
        e.setMaDay(rs.getInt("MaDay"));
        return e;
    }
    private String giaTriTrangThai(TrangThaiPhong trangThai) {
        return switch (trangThai) {
            case TRONG -> "Trong";
            case DANG_THUE -> "DangThue";
            case DANG_SUA -> "DangSua";
        };
    }

    private TrangThaiPhong trangThaiTuCSDL(String value) throws SQLException {
        return switch (value) {
            case "Trong" -> TrangThaiPhong.TRONG;
            case "DangThue" -> TrangThaiPhong.DANG_THUE;
            case "DangSua" -> TrangThaiPhong.DANG_SUA;
            default -> throw new SQLException("TrangThai PHONG không hợp lệ trong CSDL: " + value);
        };
    }

}
