package com.quanlydaytro.model;

/**
 * Model ánh xạ bảng NGUOITHUE trong database QL_NHA_TRO.
 */
public class NguoiThue {
    private int maNguoiThue;
    private String hoTen;
    private String cCCD;
    private String soDienThoai;
    private String diaChiThuongTru;

    public NguoiThue() {
    }

    public NguoiThue(int maNguoiThue, String hoTen, String cCCD, String soDienThoai, String diaChiThuongTru) {
        this.maNguoiThue = maNguoiThue;
        this.hoTen = hoTen;
        this.cCCD = cCCD;
        this.soDienThoai = soDienThoai;
        this.diaChiThuongTru = diaChiThuongTru;
    }

    public int getMaNguoiThue() { return maNguoiThue; }
    public void setMaNguoiThue(int maNguoiThue) { this.maNguoiThue = maNguoiThue; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getCCCD() { return cCCD; }
    public void setCCCD(String cCCD) { this.cCCD = cCCD; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getDiaChiThuongTru() { return diaChiThuongTru; }
    public void setDiaChiThuongTru(String diaChiThuongTru) { this.diaChiThuongTru = diaChiThuongTru; }
}
