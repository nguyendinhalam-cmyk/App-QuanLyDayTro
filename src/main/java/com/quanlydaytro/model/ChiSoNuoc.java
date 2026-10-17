package com.quanlydaytro.model;

import java.time.LocalDate;

/**
 * Model ánh xạ bảng CHISONUOC trong database QL_NHA_TRO.
 */
public class ChiSoNuoc {
    private int maChiSoNuoc;
    private int maPhong;
    private String thangNam;
    private int chiSoCu;
    private int chiSoMoi;
    private LocalDate ngayGhi;

    public ChiSoNuoc() {
    }

    public ChiSoNuoc(int maChiSoNuoc, int maPhong, String thangNam, int chiSoCu, int chiSoMoi, LocalDate ngayGhi) {
        this.maChiSoNuoc = maChiSoNuoc;
        this.maPhong = maPhong;
        this.thangNam = thangNam;
        this.chiSoCu = chiSoCu;
        this.chiSoMoi = chiSoMoi;
        this.ngayGhi = ngayGhi;
    }

    public int getMaChiSoNuoc() { return maChiSoNuoc; }
    public void setMaChiSoNuoc(int maChiSoNuoc) { this.maChiSoNuoc = maChiSoNuoc; }
    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }
    public String getThangNam() { return thangNam; }
    public void setThangNam(String thangNam) { this.thangNam = thangNam; }
    public int getChiSoCu() { return chiSoCu; }
    public void setChiSoCu(int chiSoCu) { this.chiSoCu = chiSoCu; }
    public int getChiSoMoi() { return chiSoMoi; }
    public void setChiSoMoi(int chiSoMoi) { this.chiSoMoi = chiSoMoi; }
    public LocalDate getNgayGhi() { return ngayGhi; }
    public void setNgayGhi(LocalDate ngayGhi) { this.ngayGhi = ngayGhi; }
}
