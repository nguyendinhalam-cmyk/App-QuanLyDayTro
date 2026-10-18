package com.quanlydaytro.service;

import com.quanlydaytro.dao.TaiKhoanDAO;
import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.dto.KetQuaDangNhap;
import com.quanlydaytro.util.MaHoaMatKhau;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import java.sql.*;
import java.util.Optional;

import com.quanlydaytro.model.TaiKhoan;

/**
 * Xác thực thông tin đăng nhập và tạo phiên làm việc.
 */
public class DangNhapService {
    private final TaiKhoanDAO taiKhoanDAO;

    public DangNhapService() {
        this(new TaiKhoanDAO());
    }

    public DangNhapService(TaiKhoanDAO dao) {
        this.taiKhoanDAO = dao;
    }

    public KetQuaDangNhap dangNhap(String tenDangNhap, char[] matKhau) throws SQLException {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty() || matKhau == null || matKhau.length == 0)
            return new KetQuaDangNhap(false, "Vui lòng nhập tên đăng nhập và mật khẩu", 0);
        Optional<TaiKhoan> opt = taiKhoanDAO.timTheoTenDangNhap(tenDangNhap.trim());
        if (opt.isEmpty() || !MaHoaMatKhau.kiemTra(matKhau, opt.get().getMatKhau()))
            return new KetQuaDangNhap(false, "Tên đăng nhập hoặc mật khẩu không đúng", 0);
        TaiKhoan tk = opt.get();
        String sql = "SELECT n.HoTen,v.TenVaiTro FROM NHANVIEN n CROSS JOIN VAITRO v WHERE n.MaNV=? AND v.MaVaiTro=?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, tk.getMaNV());
            ps.setInt(2, tk.getMaVaiTro());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return new KetQuaDangNhap(false, "Tài khoản không có thông tin nhân viên hoặc vai trò hợp lệ", 0);
                String role = rs.getString("TenVaiTro");
                QuanLyPhienDangNhap.batDau(tk.getMaTaiKhoan(), tk.getTenDangNhap(), tk.getMaNV(), tk.getMaVaiTro(), role, rs.getString("HoTen"));
                return new KetQuaDangNhap(true, "Đăng nhập thành công", tk.getMaTaiKhoan(), tk.getTenDangNhap(), tk.getMaNV(), tk.getMaVaiTro(), role);
            }
        }
    }
}
