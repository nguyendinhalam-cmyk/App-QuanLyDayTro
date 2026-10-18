package com.quanlydaytro.dao;

import com.quanlydaytro.model.ChuTro;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO cho bảng CHUTRO; tên bảng/cột lấy trực tiếp từ database/schema.sql.
 */
public class ChuTroDAO extends AbstractJdbcDAO<ChuTro> {
    @Override
    protected String tableName() {
        return "CHUTRO";
    }

    @Override
    protected String idColumn() {
        return "MaChuTro";
    }

    @Override
    protected String[] columns() {
        return new String[]{"HoTen", "SoDienThoai"};
    }

    @Override
    protected Object[] values(ChuTro e) {
        return new Object[]{e.getHoTen(), e.getSoDienThoai()};
    }

    public java.util.List<ChuTro> timKiem(String tuKhoa) throws SQLException {
        String sql = "SELECT MaChuTro, HoTen, SoDienThoai FROM CHUTRO WHERE CAST(MaChuTro AS VARCHAR(12)) LIKE ? OR HoTen LIKE ? OR SoDienThoai LIKE ? ORDER BY MaChuTro";
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        java.util.List<ChuTro> ds = new java.util.ArrayList<>();
        try (java.sql.Connection c = com.quanlydaytro.connection.DatabaseConnection.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ds.add(mapRow(rs));
            }
        }
        return ds;
    }

    public boolean soDienThoaiDaTonTai(String sdt, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM CHUTRO WHERE SoDienThoai = ? AND MaChuTro <> ?";
        try (java.sql.Connection c = com.quanlydaytro.connection.DatabaseConnection.getConnection(); java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, sdt);
            ps.setInt(2, boQuaMa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean dangSoHuuDayTro(int maChuTro) throws SQLException {
        String sql = "SELECT 1 FROM DAYTRO WHERE MaChuTro = ?";
        try (java.sql.Connection c = com.quanlydaytro.connection.DatabaseConnection.getConnection(); java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maChuTro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    protected ChuTro mapRow(ResultSet rs) throws SQLException {
        ChuTro e = new ChuTro();
        e.setMaChuTro(rs.getInt("MaChuTro"));
        e.setHoTen(rs.getString("HoTen"));
        e.setSoDienThoai(rs.getString("SoDienThoai"));
        return e;
    }
}
