package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.model.NhanVien;

import java.sql.*;
import java.util.*;

/**
 * DAO truy cập bảng NHANVIEN theo schema.sql.
 */
public class NhanVienDAO extends AbstractJdbcDAO<NhanVien> {
    @Override
    protected String tableName() {
        return "NHANVIEN";
    }

    @Override
    protected String idColumn() {
        return "MaNV";
    }

    @Override
    protected String[] columns() {
        return new String[]{"HoTen", "SoDienThoai", "ChucVu"};
    }

    @Override
    protected Object[] values(NhanVien e) {
        return new Object[]{e.getHoTen(), e.getSoDienThoai(), e.getChucVu()};
    }

    @Override
    protected NhanVien mapRow(ResultSet rs) throws SQLException {
        return new NhanVien(rs.getInt("MaNV"), rs.getString("HoTen"),
                rs.getString("SoDienThoai"), rs.getString("ChucVu"));
    }

    /**
     * Tìm theo mã, họ tên, số điện thoại hoặc chức vụ; chuỗi tìm kiếm được tham số hóa.
     */
    public List<NhanVien> timKiem(String tuKhoa) throws SQLException {
        String sql = "SELECT MaNV, HoTen, SoDienThoai, ChucVu FROM NHANVIEN "
                + "WHERE CAST(MaNV AS VARCHAR(12)) LIKE ? OR HoTen LIKE ? "
                + "OR SoDienThoai LIKE ? OR ChucVu LIKE ? ORDER BY MaNV";
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        List<NhanVien> result = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) ps.setString(i, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        }
        return result;
    }

    public boolean soDienThoaiDaTonTai(String soDienThoai, int boQuaMaNV) throws SQLException {
        String sql = "SELECT 1 FROM NHANVIEN WHERE SoDienThoai = ? AND MaNV <> ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, soDienThoai);
            ps.setInt(2, boQuaMaNV);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean coTaiKhoan(int maNV) throws SQLException {
        return tonTai("SELECT 1 FROM TAIKHOAN WHERE MaNV = ?", maNV);
    }

    public boolean dangPhanCongDayTro(int maNV) throws SQLException {
        return tonTai("SELECT 1 FROM NHANVIEN_DAYTRO WHERE MaNV = ?", maNV);
    }

    private boolean tonTai(String sql, int maNV) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
