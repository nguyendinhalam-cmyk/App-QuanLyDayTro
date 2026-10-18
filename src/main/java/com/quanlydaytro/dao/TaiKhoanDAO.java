package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.model.TaiKhoan;

import java.sql.*;
import java.util.*;

/**
 * DAO truy cập TAIKHOAN; các truy vấn dùng PreparedStatement.
 */
public class TaiKhoanDAO extends AbstractJdbcDAO<TaiKhoan> {
    @Override
    protected String tableName() {
        return "TAIKHOAN";
    }

    @Override
    protected String idColumn() {
        return "MaTaiKhoan";
    }

    @Override
    protected String[] columns() {
        return new String[]{"TenDangNhap", "MatKhau", "MaNV", "MaVaiTro"};
    }

    @Override
    protected Object[] values(TaiKhoan e) {
        return new Object[]{e.getTenDangNhap(), e.getMatKhau(), e.getMaNV(), e.getMaVaiTro()};
    }

    @Override
    protected TaiKhoan mapRow(ResultSet rs) throws SQLException {
        TaiKhoan e = new TaiKhoan();
        e.setMaTaiKhoan(rs.getInt("MaTaiKhoan"));
        e.setTenDangNhap(rs.getString("TenDangNhap"));
        e.setMatKhau(rs.getString("MatKhau"));
        e.setMaNV(rs.getInt("MaNV"));
        e.setMaVaiTro(rs.getInt("MaVaiTro"));
        return e;
    }

    public Optional<TaiKhoan> timTheoTenDangNhap(String ten) throws SQLException {
        String sql = "SELECT * FROM TAIKHOAN WHERE TenDangNhap = ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ten);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    public List<Object[]> layDanhSachHienThi() throws SQLException {
        String sql = "SELECT t.MaTaiKhoan,t.TenDangNhap,t.MaNV,n.HoTen,t.MaVaiTro,v.TenVaiTro FROM TAIKHOAN t JOIN NHANVIEN n ON n.MaNV=t.MaNV JOIN VAITRO v ON v.MaVaiTro=t.MaVaiTro ORDER BY t.MaTaiKhoan";
        List<Object[]> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                out.add(new Object[]{rs.getInt("MaTaiKhoan"), rs.getString("TenDangNhap"), rs.getInt("MaNV"), rs.getString("HoTen"), rs.getInt("MaVaiTro"), rs.getString("TenVaiTro")});
        }
        return out;
    }

    public List<Object[]> layNhanVienChuaCoTaiKhoan(int boQuaMaTaiKhoan) throws SQLException {
        String sql = "SELECT n.MaNV,n.HoTen FROM NHANVIEN n WHERE NOT EXISTS (SELECT 1 FROM TAIKHOAN t WHERE t.MaNV=n.MaNV AND t.MaTaiKhoan<>?) ORDER BY n.HoTen";
        List<Object[]> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, boQuaMaTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new Object[]{rs.getInt("MaNV"), rs.getString("HoTen")});
            }
        }
        return out;
    }

    public boolean tenDangNhapDaTonTai(String ten, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM TAIKHOAN WHERE TenDangNhap=? AND MaTaiKhoan<>?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ten);
            ps.setInt(2, boQuaMa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean nhanVienDaCoTaiKhoan(int maNV, int boQuaMa) throws SQLException {
        String sql = "SELECT 1 FROM TAIKHOAN WHERE MaNV=? AND MaTaiKhoan<>?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            ps.setInt(2, boQuaMa);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
