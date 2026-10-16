package com.quanlydaytro.util;
public final class QuanLyPhienDangNhap {
 private static Integer maTaiKhoan; private static String tenDangNhap;
 private QuanLyPhienDangNhap() {}
 public static void batDau(Integer ma,String ten) { maTaiKhoan=ma; tenDangNhap=ten; }
 public static void ketThuc() { maTaiKhoan=null; tenDangNhap=null; }
 public static Integer getMaTaiKhoan() { return maTaiKhoan; }
 public static String getTenDangNhap() { return tenDangNhap; }
 public static boolean daDangNhap() { return maTaiKhoan!=null; }
}
