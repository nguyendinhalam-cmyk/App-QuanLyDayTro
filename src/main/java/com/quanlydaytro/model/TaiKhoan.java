package com.quanlydaytro.model;

/**
 * Model ánh xạ bảng TAIKHOAN trong database QL_NHA_TRO.
 */
public class TaiKhoan {
    private int maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    private int maNV;
    private int maVaiTro;

    public TaiKhoan() {
    }

    public TaiKhoan(int maTaiKhoan, String tenDangNhap, String matKhau, int maNV, int maVaiTro) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
        this.maVaiTro = maVaiTro;
    }

    public int getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(int maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    public int getMaVaiTro() { return maVaiTro; }
    public void setMaVaiTro(int maVaiTro) { this.maVaiTro = maVaiTro; }
}
