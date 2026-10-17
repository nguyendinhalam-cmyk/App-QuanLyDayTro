package com.quanlydaytro.model;

/**
 * Model ánh xạ bảng CHUTRO trong database QL_NHA_TRO.
 */
public class ChuTro {
    private int maChuTro;
    private String hoTen;
    private String soDienThoai;

    public ChuTro() {
    }

    public ChuTro(int maChuTro, String hoTen, String soDienThoai) {
        this.maChuTro = maChuTro;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
    }

    public int getMaChuTro() { return maChuTro; }
    public void setMaChuTro(int maChuTro) { this.maChuTro = maChuTro; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
}
