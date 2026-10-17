package com.quanlydaytro.model;

import com.quanlydaytro.enums.TrangThaiHopDong;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Model ánh xạ bảng HOPDONG trong database QL_NHA_TRO.
 */
public class HopDong {
    private int maHopDong;
    private int maPhong;
    private int maNguoiThue;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private BigDecimal giaThueThoaThuan;
    private BigDecimal tienCoc;
    private TrangThaiHopDong trangThai = TrangThaiHopDong.HIEU_LUC;

    public HopDong() {
    }

    public HopDong(int maHopDong, int maPhong, int maNguoiThue, LocalDate ngayBatDau, LocalDate ngayKetThuc, BigDecimal giaThueThoaThuan, BigDecimal tienCoc, TrangThaiHopDong trangThai) {
        this.maHopDong = maHopDong;
        this.maPhong = maPhong;
        this.maNguoiThue = maNguoiThue;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giaThueThoaThuan = giaThueThoaThuan;
        this.tienCoc = tienCoc;
        this.trangThai = trangThai;
    }

    public int getMaHopDong() { return maHopDong; }
    public void setMaHopDong(int maHopDong) { this.maHopDong = maHopDong; }
    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }
    public int getMaNguoiThue() { return maNguoiThue; }
    public void setMaNguoiThue(int maNguoiThue) { this.maNguoiThue = maNguoiThue; }
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public BigDecimal getGiaThueThoaThuan() { return giaThueThoaThuan; }
    public void setGiaThueThoaThuan(BigDecimal giaThueThoaThuan) { this.giaThueThoaThuan = giaThueThoaThuan; }
    public BigDecimal getTienCoc() { return tienCoc; }
    public void setTienCoc(BigDecimal tienCoc) { this.tienCoc = tienCoc; }
    public TrangThaiHopDong getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiHopDong trangThai) { this.trangThai = trangThai; }
}
