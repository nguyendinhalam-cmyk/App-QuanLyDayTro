package com.quanlydaytro.util;
import java.math.BigDecimal;
public final class KiemTraDuLieu {
 private KiemTraDuLieu() {}
 public static boolean khongRong(String s) { return s != null && !s.trim().isEmpty(); }
 public static boolean laSoDuong(String s) { try { return s != null && new BigDecimal(s).signum() > 0; } catch (Exception e) { return false; } }
}
