package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.model.NguoiThue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng NGUOITHUE; giữ nguyên tên bảng/cột theo schema SQL.
 */
public class NguoiThueDAO extends AbstractJdbcDAO<NguoiThue> {
    @Override
    protected String tableName() {
        return "NGUOITHUE";
    }

    @Override
    protected String idColumn() {
        return "MaNT";
    }

    @Override
    protected String[] columns() {
        return new String[]{"HoTen", "CCCD", "SoDienThoai", "DiaChiThuongTru"};
    }

    @Override
    protected Object[] values(NguoiThue e) {
        return new Object[]{e.getHoTen(), e.getCCCD(), e.getSoDienThoai(), e.getDiaChiThuongTru()};
    }

    @Override
    protected NguoiThue mapRow(ResultSet rs) throws SQLException {
        return new NguoiThue(rs.getInt("MaNT"), rs.getString("HoTen"), rs.getString("CCCD"),
                rs.getString("SoDienThoai"), rs.getString("DiaChiThuongTru"));
    }

    /**
     * Tìm theo mã, họ tên, CCCD hoặc số điện thoại. Chuỗi rỗng trả về toàn bộ danh sách.
     */
    public List<NguoiThue> timKiem(String tuKhoa) throws SQLException {
        String keyword = tuKhoa == null ? "" : tuKhoa.trim();
        if (keyword.isEmpty()) return findAll();
        String sql = "SELECT * FROM NGUOITHUE WHERE HoTen LIKE ? OR CCCD LIKE ? OR SoDienThoai LIKE ? "
                + "OR DiaChiThuongTru LIKE ? OR CONVERT(VARCHAR(12), MaNT) LIKE ? ORDER BY MaNT";
        String pattern = "%" + keyword + "%";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) ps.setString(i, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                List<NguoiThue> result = new ArrayList<>();
                while (rs.next()) result.add(mapRow(rs));
                return result;
            }
        }
    }

    public boolean daCoCCCD(String cccd, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM NGUOITHUE WHERE CCCD = ? AND MaNT <> ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cccd);
            ps.setInt(2, boQuaMa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
