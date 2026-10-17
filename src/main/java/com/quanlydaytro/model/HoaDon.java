package com.quanlydaytro.model;

import com.quanlydaytro.enums.TrangThaiHoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Model ánh xạ bảng HOADON trong database QL_NHA_TRO.
 */
public class HoaDon {
    private int maHoaDon;
    private int maHopDong;
    private String thangNam;
    private BigDecimal tienPhong;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal tongTien;
    private LocalDate hanThanhToan;
    private TrangThaiHoaDon trangThai = TrangThaiHoaDon.CHUA_THANH_TOAN;

    public HoaDon() {
    }

    public HoaDon(int maHoaDon, int maHopDong, String thangNam, BigDecimal tienPhong, BigDecimal tienDien, BigDecimal tienNuoc, BigDecimal tongTien, LocalDate hanThanhToan, TrangThaiHoaDon trangThai) {
        this.maHoaDon = maHoaDon;
        this.maHopDong = maHopDong;
        this.thangNam = thangNam;
        this.tienPhong = tienPhong;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tongTien = tongTien;
        this.hanThanhToan = hanThanhToan;
        this.trangThai = trangThai;
    }

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }
    public int getMaHopDong() { return maHopDong; }
    public void setMaHopDong(int maHopDong) { this.maHopDong = maHopDong; }
    public String getThangNam() { return thangNam; }
    public void setThangNam(String thangNam) { this.thangNam = thangNam; }
    public BigDecimal getTienPhong() { return tienPhong; }
    public void setTienPhong(BigDecimal tienPhong) { this.tienPhong = tienPhong; }
    public BigDecimal getTienDien() { return tienDien; }
    public void setTienDien(BigDecimal tienDien) { this.tienDien = tienDien; }
    public BigDecimal getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(BigDecimal tienNuoc) { this.tienNuoc = tienNuoc; }
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    public LocalDate getHanThanhToan() { return hanThanhToan; }
    public void setHanThanhToan(LocalDate hanThanhToan) { this.hanThanhToan = hanThanhToan; }
    public TrangThaiHoaDon getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiHoaDon trangThai) { this.trangThai = trangThai; }
}
