package com.quanlydaytro.util;

/**
 * Lưu thông tin phiên đăng nhập hiện tại trong bộ nhớ ứng dụng.
 */
public final class QuanLyPhienDangNhap {
    private static Integer maTaiKhoan, maNhanVien, maVaiTro;
    private static String tenDangNhap, tenVaiTro, hoTen;

    private QuanLyPhienDangNhap() {
    }

    public static synchronized void batDau(Integer ma, String ten) {
        maTaiKhoan = ma;
        tenDangNhap = ten;
    }

    public static synchronized void batDau(Integer ma, String ten, Integer maNV, Integer maVT, String vaiTro, String tenNV) {
        maTaiKhoan = ma;
        tenDangNhap = ten;
        maNhanVien = maNV;
        maVaiTro = maVT;
        tenVaiTro = vaiTro;
        hoTen = tenNV;
    }

    public static synchronized void ketThuc() {
        maTaiKhoan = null;
        maNhanVien = null;
        maVaiTro = null;
        tenDangNhap = null;
        tenVaiTro = null;
        hoTen = null;
    }

    public static Integer getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public static String getTenDangNhap() {
        return tenDangNhap;
    }

    public static Integer getMaNhanVien() {
        return maNhanVien;
    }

    public static Integer getMaVaiTro() {
        return maVaiTro;
    }

    public static String getTenVaiTro() {
        return tenVaiTro;
    }

    public static String getHoTen() {
        return hoTen;
    }

    public static boolean daDangNhap() {
        return maTaiKhoan != null;
    }

    public static boolean coVaiTro(String... roles) {
        if (tenVaiTro == null) return false;
        for (String role : roles) if (tenVaiTro.equalsIgnoreCase(role)) return true;
        return false;
    }
}
