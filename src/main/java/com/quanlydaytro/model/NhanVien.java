package com.quanlydaytro.model;

/**
 * Model ánh xạ bảng NHANVIEN trong database QL_NHA_TRO.
 */
public class NhanVien {
    private int maNhanVien;
    private String hoTen;
    private String soDienThoai;
    private String chucVu;

    public NhanVien() {
    }

    public NhanVien(int maNhanVien, String hoTen, String soDienThoai, String chucVu) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.chucVu = chucVu;
    }

    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
}
