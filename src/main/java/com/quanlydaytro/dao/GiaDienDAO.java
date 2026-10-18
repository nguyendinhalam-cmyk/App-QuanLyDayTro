package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.model.GiaDien;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * DAO cho bảng BANGGIADIEN.
 */
public class GiaDienDAO extends AbstractJdbcDAO<GiaDien> {
    @Override
    protected String tableName() {
        return "BANGGIADIEN";
    }

    @Override
    protected String idColumn() {
        return "MaGiaDien";
    }

    @Override
    protected String[] columns() {
        return new String[]{"DonGia", "NgayApDung"};
    }

    @Override
    protected Object[] values(GiaDien e) {
        return new Object[]{e.getDonGia(), e.getNgayApDung() == null ? null : java.sql.Date.valueOf(e.getNgayApDung())};
    }

    @Override
    protected GiaDien mapRow(ResultSet rs) throws SQLException {
        return new GiaDien(rs.getInt("MaGiaDien"), rs.getBigDecimal("DonGia"), localDate(rs, "NgayApDung"));
    }

    public boolean tonTaiNgayApDung(LocalDate ngay, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM BANGGIADIEN WHERE NgayApDung = ? AND MaGiaDien <> ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(ngay));
            ps.setInt(2, boQuaMa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<GiaDien> timKiem(String tuKhoa) throws SQLException {
        String sql = "SELECT * FROM BANGGIADIEN WHERE CAST(MaGiaDien AS VARCHAR(20)) LIKE ? OR CAST(DonGia AS VARCHAR(40)) LIKE ? OR CONVERT(VARCHAR(10), NgayApDung, 23) LIKE ? ORDER BY NgayApDung DESC";
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            try (ResultSet rs = ps.executeQuery()) {
                List<GiaDien> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        }
    }
}
