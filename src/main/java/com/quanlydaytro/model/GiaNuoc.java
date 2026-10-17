package com.quanlydaytro.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Model ánh xạ bảng BANGGIANUOC trong database QL_NHA_TRO.
 */
public class GiaNuoc {
    private int maGiaNuoc;
    private BigDecimal donGia;
    private LocalDate ngayApDung;

    public GiaNuoc() {
    }

    public GiaNuoc(int maGiaNuoc, BigDecimal donGia, LocalDate ngayApDung) {
        this.maGiaNuoc = maGiaNuoc;
        this.donGia = donGia;
        this.ngayApDung = ngayApDung;
    }

    public int getMaGiaNuoc() { return maGiaNuoc; }
    public void setMaGiaNuoc(int maGiaNuoc) { this.maGiaNuoc = maGiaNuoc; }
    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }
    public LocalDate getNgayApDung() { return ngayApDung; }
    public void setNgayApDung(LocalDate ngayApDung) { this.ngayApDung = ngayApDung; }
}
