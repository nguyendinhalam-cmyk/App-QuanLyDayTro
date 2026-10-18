package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.enums.TrangThaiHopDong;
import com.quanlydaytro.model.HopDong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng HOPDONG; ánh xạ đúng schema.sql.
 */
public class HopDongDAO extends AbstractJdbcDAO<HopDong> {
    @Override
    protected String tableName() {
        return "HOPDONG";
    }

    @Override
    protected String idColumn() {
        return "MaHopDong";
    }

    @Override
    protected String[] columns() {
        return new String[]{"MaPhong", "MaNT", "NgayBatDau", "NgayKetThuc", "GiaThueThoaThuan", "TienCoc", "TrangThai"};
    }

    @Override
    protected Object[] values(HopDong e) {
        return new Object[]{e.getMaPhong(), e.getMaNguoiThue(), e.getNgayBatDau(), e.getNgayKetThuc(), e.getGiaThueThoaThuan(), e.getTienCoc(), toDb(e.getTrangThai())};
    }

    @Override
    protected HopDong mapRow(ResultSet rs) throws SQLException {
        HopDong e = new HopDong();
        e.setMaHopDong(rs.getInt("MaHopDong"));
        e.setMaPhong(rs.getInt("MaPhong"));
        e.setMaNguoiThue(rs.getInt("MaNT"));
        e.setNgayBatDau(localDate(rs, "NgayBatDau"));
        e.setNgayKetThuc(localDate(rs, "NgayKetThuc"));
        e.setGiaThueThoaThuan(rs.getBigDecimal("GiaThueThoaThuan"));
        e.setTienCoc(rs.getBigDecimal("TienCoc"));
        e.setTrangThai(fromDb(rs.getString("TrangThai")));
        return e;
    }

    private String toDb(TrangThaiHopDong t) {
        if (t == null) throw new IllegalArgumentException("Vui lòng chọn trạng thái hợp đồng.");
        return switch (t) {
            case HIEU_LUC -> "HieuLuc";
            case DA_DONG -> "DaDong";
        };
    }

    private TrangThaiHopDong fromDb(String s) throws SQLException {
        return switch (s) {
            case "HieuLuc" -> TrangThaiHopDong.HIEU_LUC;
            case "DaDong" -> TrangThaiHopDong.DA_DONG;
            default -> throw new SQLException("Trạng thái hợp đồng không hợp lệ: " + s);
        };
    }

    public List<HopDong> timKiem(String tuKhoa) throws SQLException {
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        String sql = "SELECT h.* FROM HOPDONG h LEFT JOIN NGUOITHUE n ON n.MaNT=h.MaNT LEFT JOIN PHONG p ON p.MaPhong=h.MaPhong " +
                "WHERE CAST(h.MaHopDong AS VARCHAR(12)) LIKE ? OR CAST(h.MaPhong AS VARCHAR(12)) LIKE ? OR p.TenPhong LIKE ? " +
                "OR CAST(h.MaNT AS VARCHAR(12)) LIKE ? OR n.HoTen LIKE ? OR h.TrangThai LIKE ? ORDER BY h.MaHopDong DESC";
        List<HopDong> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) ps.setString(i, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        }
        return out;
    }

    public boolean phongCoHopDongChongLap(int maPhong, java.time.LocalDate batDau, java.time.LocalDate ketThuc, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM HOPDONG WHERE MaPhong=? AND TrangThai=N'HieuLuc' AND MaHopDong<>? AND NgayBatDau < ? AND NgayKetThuc > ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maPhong);
            ps.setInt(2, boQuaMa);
            ps.setDate(3, Date.valueOf(ketThuc));
            ps.setDate(4, Date.valueOf(batDau));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean coDuLieuPhuThuoc(int ma) throws SQLException {
        String sql = "SELECT 1 FROM HOADON WHERE MaHopDong=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ma);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Object[]> danhSachKemTen() throws SQLException {
        String sql = "SELECT h.*, p.TenPhong, n.HoTen FROM HOPDONG h JOIN PHONG p ON p.MaPhong=h.MaPhong JOIN NGUOITHUE n ON n.MaNT=h.MaNT ORDER BY h.MaHopDong DESC";
        List<Object[]> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new Object[]{mapRow(rs), rs.getString("TenPhong"), rs.getString("HoTen")});
        }
        return out;
    }
}
