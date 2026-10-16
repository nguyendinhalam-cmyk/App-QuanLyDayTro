package com.quanlydaytro.dto;
public class KetQuaDangNhap {
 private final boolean thanhCong; private final String thongBao; private final int maTaiKhoan;
 public KetQuaDangNhap(boolean thanhCong,String thongBao,int maTaiKhoan) { this.thanhCong=thanhCong; this.thongBao=thongBao; this.maTaiKhoan=maTaiKhoan; }
 public boolean isThanhCong() { return thanhCong; }
 public String getThongBao() { return thongBao; }
 public int getMaTaiKhoan() { return maTaiKhoan; }
}
