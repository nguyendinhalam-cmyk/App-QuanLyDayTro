package com.quanlydaytro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model ánh xạ bảng THANHTOAN trong database QL_NHA_TRO.
 */
public class ThanhToan {
    private int maThanhToan;
    private int maHoaDon;
    private LocalDateTime ngayThanhToan;
    private BigDecimal soTienTra;
    private String ghiChu;

    public ThanhToan() {
    }

    public ThanhToan(int maThanhToan, int maHoaDon, LocalDateTime ngayThanhToan, BigDecimal soTienTra, String ghiChu) {
        this.maThanhToan = maThanhToan;
        this.maHoaDon = maHoaDon;
        this.ngayThanhToan = ngayThanhToan;
        this.soTienTra = soTienTra;
        this.ghiChu = ghiChu;
    }

    public int getMaThanhToan() { return maThanhToan; }
    public void setMaThanhToan(int maThanhToan) { this.maThanhToan = maThanhToan; }
    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }
    public LocalDateTime getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }
    public BigDecimal getSoTienTra() { return soTienTra; }
    public void setSoTienTra(BigDecimal soTienTra) { this.soTienTra = soTienTra; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
