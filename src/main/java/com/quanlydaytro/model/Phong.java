package com.quanlydaytro.model;

import com.quanlydaytro.enums.TrangThaiPhong;

import java.math.BigDecimal;

/**
 * Model ánh xạ bảng PHONG trong database QL_NHA_TRO.
 * Không đổi tên cột SQL tại đây; ánh xạ được xử lý trong DAO.
 */
public class Phong {
    private int maPhong;
    private String tenPhong;
    private BigDecimal giaThueHienTai;
    private int soNguoiO;
    private TrangThaiPhong trangThai = TrangThaiPhong.TRONG;
    private int maDay;

    public Phong() {
    }

    public Phong(int maPhong, String tenPhong, BigDecimal giaThueHienTai, int soNguoiO, TrangThaiPhong trangThai, int maDay) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaThueHienTai = giaThueHienTai;
        this.soNguoiO = soNguoiO;
        this.trangThai = trangThai;
        this.maDay = maDay;
    }

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    public BigDecimal getGiaThueHienTai() { return giaThueHienTai; }
    public void setGiaThueHienTai(BigDecimal giaThueHienTai) { this.giaThueHienTai = giaThueHienTai; }
    public int getSoNguoiO() { return soNguoiO; }
    public void setSoNguoiO(int soNguoiO) { this.soNguoiO = soNguoiO; }
    public TrangThaiPhong getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiPhong trangThai) { this.trangThai = trangThai; }
    public int getMaDay() { return maDay; }
    public void setMaDay(int maDay) { this.maDay = maDay; }
}
