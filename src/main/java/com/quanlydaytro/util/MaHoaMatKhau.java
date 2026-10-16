package com.quanlydaytro.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
/** Mẫu minh họa SHA-256; triển khai thực tế nên dùng BCrypt/Argon2 có salt. */
public final class MaHoaMatKhau {
 private MaHoaMatKhau() {}
 public static String bamSHA256(String s) {
  try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8))); }
  catch (Exception e) { throw new IllegalStateException(e); }
 }
}
