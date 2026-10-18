package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.enums.TrangThaiPhong;
import com.quanlydaytro.model.Phong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho PHONG; ánh xạ theo database/schema.sql.
 */
public class PhongDAO extends AbstractJdbcDAO<Phong> {
    @Override
    protected String tableName() {
        return "PHONG";
    }

    @Override
    protected String idColumn() {
        return "MaPhong";
    }

    @Override
    protected String[] columns() {
        return new String[]{"TenPhong", "GiaThueHienTai", "SoNguoiO", "TrangThai", "MaDay"};
    }

    @Override
    protected Object[] values(Phong e) {
        return new Object[]{e.getTenPhong(), e.getGiaThueHienTai(), e.getSoNguoiO(), giaTriTrangThai(e.getTrangThai()), e.getMaDay()};
    }

    @Override
    protected Phong mapRow(ResultSet rs) throws SQLException {
        Phong e = new Phong();
        e.setMaPhong(rs.getInt("MaPhong"));
        e.setTenPhong(rs.getString("TenPhong"));
        e.setGiaThueHienTai(rs.getBigDecimal("GiaThueHienTai"));
        e.setSoNguoiO(rs.getInt("SoNguoiO"));
        e.setTrangThai(trangThaiTuCSDL(rs.getString("TrangThai")));
        e.setMaDay(rs.getInt("MaDay"));
        return e;
    }

    public List<Phong> timKiem(String tuKhoa) throws SQLException {
        String sql = "SELECT p.* FROM PHONG p LEFT JOIN DAYTRO d ON d.MaDay=p.MaDay " +
                "WHERE CAST(p.MaPhong AS VARCHAR(12)) LIKE ? OR p.TenPhong LIKE ? OR p.TrangThai LIKE ? " +
                "OR CAST(p.MaDay AS VARCHAR(12)) LIKE ? OR d.TenDay LIKE ? ORDER BY p.MaPhong";
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        List<Phong> ds = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) ps.setString(i, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ds.add(mapRow(rs));
            }
        }
        return ds;
    }

    public boolean tenPhongDaTonTai(int maDay, String tenPhong, int boQuaMaPhong) throws SQLException {
        String sql = "SELECT 1 FROM PHONG WHERE MaDay=? AND TenPhong=? AND MaPhong<>?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maDay);
            ps.setString(2, tenPhong);
            ps.setInt(3, boQuaMaPhong);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean coDuLieuPhuThuoc(int maPhong) throws SQLException {
        String sql = "SELECT 1 FROM HOPDONG WHERE MaPhong=? UNION ALL SELECT 1 FROM CHISODIEN WHERE MaPhong=? UNION ALL SELECT 1 FROM CHISONUOC WHERE MaPhong=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            ps.setInt(2, maPhong);
            ps.setInt(3, maPhong);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String giaTriTrangThai(TrangThaiPhong t) {
        if (t == null) throw new IllegalArgumentException("Vui lòng chọn trạng thái phòng.");
        return switch (t) {
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
            default -> throw new SQLException("Trạng thái phòng không hợp lệ trong CSDL: " + value);
        };
    }
}
