package com.quanlydaytro.dao;

import com.quanlydaytro.model.ChiSoDien;
import com.quanlydaytro.connection.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ChiSoDienDAO extends AbstractJdbcDAO<ChiSoDien> {
    protected String tableName() {
        return "CHISODIEN";
    }

    protected String idColumn() {
        return "MaCSD";
    }

    protected String[] columns() {
        return new String[]{"MaPhong", "ThangNam", "ChiSoCu", "ChiSoMoi", "NgayGhi"};
    }

    protected Object[] values(ChiSoDien e) {
        return new Object[]{e.getMaPhong(), e.getThangNam(), e.getChiSoCu(), e.getChiSoMoi(), e.getNgayGhi()};
    }

    protected ChiSoDien mapRow(ResultSet r) throws SQLException {
        ChiSoDien e = new ChiSoDien();
        e.setMaChiSoDien(r.getInt("MaCSD"));
        e.setMaPhong(r.getInt("MaPhong"));
        e.setThangNam(r.getString("ThangNam"));
        e.setChiSoCu(r.getInt("ChiSoCu"));
        e.setChiSoMoi(r.getInt("ChiSoMoi"));
        e.setNgayGhi(localDate(r, "NgayGhi"));
        return e;
    }

    public List<ChiSoDien> timKiem(String key) throws SQLException {
        String k = "%" + (key == null ? "" : key.trim()) + "%";
        String sql = "SELECT * FROM CHISODIEN WHERE CAST(MaCSD AS VARCHAR(12)) LIKE ? OR CAST(MaPhong AS VARCHAR(12)) LIKE ? OR ThangNam LIKE ? ORDER BY ThangNam DESC, MaPhong";
        List<ChiSoDien> ds = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            for (int i = 1; i <= 3; i++) p.setString(i, k);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) ds.add(mapRow(r));
            }
        }
        return ds;
    }

    public boolean phongTonTai(int ma) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement("SELECT 1 FROM PHONG WHERE MaPhong=?")) {
            p.setInt(1, ma);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public Optional<ChiSoDien> kyTruoc(int maPhong, String thang) throws SQLException {
        String sql = "SELECT TOP 1 * FROM CHISODIEN WHERE MaPhong=? AND TRY_CONVERT(date,'01/'+ThangNam,103)<TRY_CONVERT(date,'01/'+?,103) ORDER BY TRY_CONVERT(date,'01/'+ThangNam,103) DESC";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, maPhong);
            p.setString(2, thang);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? Optional.of(mapRow(r)) : Optional.empty();
            }
        }
    }
}
