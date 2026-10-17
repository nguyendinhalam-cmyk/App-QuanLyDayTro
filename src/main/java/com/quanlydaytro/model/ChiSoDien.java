package com.quanlydaytro.model;

import java.time.LocalDate;

/**
 * Model ánh xạ bảng CHISODIEN trong database QL_NHA_TRO.
 */
public class ChiSoDien {
    private int maChiSoDien;
    private int maPhong;
    private String thangNam;
    private int chiSoCu;
    private int chiSoMoi;
    private LocalDate ngayGhi;

    public ChiSoDien() {
    }

    public ChiSoDien(int maChiSoDien, int maPhong, String thangNam, int chiSoCu, int chiSoMoi, LocalDate ngayGhi) {
        this.maChiSoDien = maChiSoDien;
        this.maPhong = maPhong;
        this.thangNam = thangNam;
        this.chiSoCu = chiSoCu;
        this.chiSoMoi = chiSoMoi;
        this.ngayGhi = ngayGhi;
    }

    public int getMaChiSoDien() { return maChiSoDien; }
    public void setMaChiSoDien(int maChiSoDien) { this.maChiSoDien = maChiSoDien; }
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
