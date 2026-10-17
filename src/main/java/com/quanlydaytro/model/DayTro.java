package com.quanlydaytro.model;

/**
 * Model ánh xạ bảng DAYTRO trong database QL_NHA_TRO.
 */
public class DayTro {
    private int maDay;
    private String tenDay;
    private String diaChi;
    private int maChuTro;

    public DayTro() {
    }

    public DayTro(int maDay, String tenDay, String diaChi, int maChuTro) {
        this.maDay = maDay;
        this.tenDay = tenDay;
        this.diaChi = diaChi;
        this.maChuTro = maChuTro;
    }

    public int getMaDay() { return maDay; }
    public void setMaDay(int maDay) { this.maDay = maDay; }
    public String getTenDay() { return tenDay; }
    public void setTenDay(String tenDay) { this.tenDay = tenDay; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public int getMaChuTro() { return maChuTro; }
    public void setMaChuTro(int maChuTro) { this.maChuTro = maChuTro; }
}
