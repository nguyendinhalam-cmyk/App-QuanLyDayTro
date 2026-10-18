package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.model.GiaNuoc;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * DAO cho bảng BANGGIANUOC.
 */
public class GiaNuocDAO extends AbstractJdbcDAO<GiaNuoc> {
    @Override
    protected String tableName() {
        return "BANGGIANUOC";
    }

    @Override
    protected String idColumn() {
        return "MaGiaNuoc";
    }

    @Override
    protected String[] columns() {
        return new String[]{"DonGia", "NgayApDung"};
    }

    @Override
    protected Object[] values(GiaNuoc e) {
        return new Object[]{e.getDonGia(), e.getNgayApDung() == null ? null : java.sql.Date.valueOf(e.getNgayApDung())};
    }

    @Override
    protected GiaNuoc mapRow(ResultSet rs) throws SQLException {
        return new GiaNuoc(rs.getInt("MaGiaNuoc"), rs.getBigDecimal("DonGia"), localDate(rs, "NgayApDung"));
    }

    public boolean tonTaiNgayApDung(LocalDate ngay, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM BANGGIANUOC WHERE NgayApDung = ? AND MaGiaNuoc <> ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(ngay));
            ps.setInt(2, boQuaMa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<GiaNuoc> timKiem(String tuKhoa) throws SQLException {
        String sql = "SELECT * FROM BANGGIANUOC WHERE CAST(MaGiaNuoc AS VARCHAR(20)) LIKE ? OR CAST(DonGia AS VARCHAR(40)) LIKE ? OR CONVERT(VARCHAR(10), NgayApDung, 23) LIKE ? ORDER BY NgayApDung DESC";
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            try (ResultSet rs = ps.executeQuery()) {
                List<GiaNuoc> list = new ArrayList<>();
                while (rs.next()) list.add(mapRow(rs));
                return list;
            }
        }
    }
}
