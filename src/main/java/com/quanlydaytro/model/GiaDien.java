package com.quanlydaytro.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Model ánh xạ bảng BANGGIADIEN trong database QL_NHA_TRO.
 */
public class GiaDien {
    private int maGiaDien;
    private BigDecimal donGia;
    private LocalDate ngayApDung;

    public GiaDien() {
    }

    public GiaDien(int maGiaDien, BigDecimal donGia, LocalDate ngayApDung) {
        this.maGiaDien = maGiaDien;
        this.donGia = donGia;
        this.ngayApDung = ngayApDung;
    }

    public int getMaGiaDien() { return maGiaDien; }
    public void setMaGiaDien(int maGiaDien) { this.maGiaDien = maGiaDien; }
    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }
    public LocalDate getNgayApDung() { return ngayApDung; }
    public void setNgayApDung(LocalDate ngayApDung) { this.ngayApDung = ngayApDung; }
}
