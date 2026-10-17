package com.quanlydaytro.model;
/** Ánh xạ bảng liên kết NHANVIEN_DAYTRO có khóa chính ghép (MaNV, MaDay). */
public class NhanVienDayTro {
    private int maNhanVien;
    private int maDay;
    public NhanVienDayTro() {}
    public NhanVienDayTro(int maNhanVien, int maDay) { this.maNhanVien=maNhanVien; this.maDay=maDay; }
    public int getMaNhanVien(){return maNhanVien;} public void setMaNhanVien(int v){maNhanVien=v;}
    public int getMaDay(){return maDay;} public void setMaDay(int v){maDay=v;}
}
