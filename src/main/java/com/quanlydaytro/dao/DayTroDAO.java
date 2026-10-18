package com.quanlydaytro.dao;

import com.quanlydaytro.model.DayTro;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO cho bảng DAYTRO; tên bảng/cột lấy trực tiếp từ database/schema.sql.
 */
public class DayTroDAO extends AbstractJdbcDAO<DayTro> {
    @Override
    protected String tableName() {
        return "DAYTRO";
    }

    @Override
    protected String idColumn() {
        return "MaDay";
    }

    @Override
    protected String[] columns() {
        return new String[]{"TenDay", "DiaChi", "MaChuTro"};
    }

    @Override
    protected Object[] values(DayTro e) {
        return new Object[]{e.getTenDay(), e.getDiaChi(), e.getMaChuTro()};
    }

    public java.util.List<DayTro> timKiem(String tuKhoa) throws SQLException {
        String sql = "SELECT d.MaDay, d.TenDay, d.DiaChi, d.MaChuTro FROM DAYTRO d LEFT JOIN CHUTRO c ON c.MaChuTro=d.MaChuTro WHERE CAST(d.MaDay AS VARCHAR(12)) LIKE ? OR d.TenDay LIKE ? OR d.DiaChi LIKE ? OR c.HoTen LIKE ? ORDER BY d.MaDay";
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        java.util.List<DayTro> ds = new java.util.ArrayList<>();
        try (java.sql.Connection c = com.quanlydaytro.connection.DatabaseConnection.getConnection(); java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) ps.setString(i, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ds.add(mapRow(rs));
            }
        }
        return ds;
    }

    public java.util.List<DayTro> layTatCaKemTenChuTro() throws SQLException {
        String sql = "SELECT d.MaDay,d.TenDay,d.DiaChi,d.MaChuTro,c.HoTen AS TenChuTro FROM DAYTRO d JOIN CHUTRO c ON c.MaChuTro=d.MaChuTro ORDER BY d.MaDay";
        java.util.List<DayTro> ds = new java.util.ArrayList<>();
        try (java.sql.Connection c = com.quanlydaytro.connection.DatabaseConnection.getConnection(); java.sql.PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(mapRow(rs));
        }
        return ds;
    }

    public boolean diaChiDaTonTai(String diaChi, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM DAYTRO WHERE DiaChi = ? AND MaDay <> ?";
        try (java.sql.Connection c = com.quanlydaytro.connection.DatabaseConnection.getConnection(); java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, diaChi);
            ps.setInt(2, boQuaMa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean coDuLieuPhuThuoc(int maDay) throws SQLException {
        String sql = "SELECT 1 FROM PHONG WHERE MaDay = ? UNION ALL SELECT 1 FROM NHANVIEN_DAYTRO WHERE MaDay = ?";
        try (java.sql.Connection c = com.quanlydaytro.connection.DatabaseConnection.getConnection(); java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maDay);
            ps.setInt(2, maDay);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    protected DayTro mapRow(ResultSet rs) throws SQLException {
        DayTro e = new DayTro();
        e.setMaDay(rs.getInt("MaDay"));
        e.setTenDay(rs.getString("TenDay"));
        e.setDiaChi(rs.getString("DiaChi"));
        e.setMaChuTro(rs.getInt("MaChuTro"));
        return e;
    }
}
