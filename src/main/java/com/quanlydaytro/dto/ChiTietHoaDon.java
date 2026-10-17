package com.quanlydaytro.dto;

import java.math.BigDecimal;

/** DTO tổng hợp các khoản tiền hiển thị trên hóa đơn. */
public class ChiTietHoaDon {
    private int maHoaDon;
    private BigDecimal tienPhong, tienDien, tienNuoc, tongTien;
    public ChiTietHoaDon() {}
    public ChiTietHoaDon(int maHoaDon, BigDecimal tienPhong, BigDecimal tienDien, BigDecimal tienNuoc, BigDecimal tongTien) {
        this.maHoaDon=maHoaDon; this.tienPhong=tienPhong; this.tienDien=tienDien; this.tienNuoc=tienNuoc; this.tongTien=tongTien;
    }
    public int getMaHoaDon(){return maHoaDon;} public void setMaHoaDon(int v){maHoaDon=v;}
    public BigDecimal getTienPhong(){return tienPhong;} public void setTienPhong(BigDecimal v){tienPhong=v;}
    public BigDecimal getTienDien(){return tienDien;} public void setTienDien(BigDecimal v){tienDien=v;}
    public BigDecimal getTienNuoc(){return tienNuoc;} public void setTienNuoc(BigDecimal v){tienNuoc=v;}
    public BigDecimal getTongTien(){return tongTien;} public void setTongTien(BigDecimal v){tongTien=v;}
}
