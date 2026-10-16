package com.quanlydaytro.config;
import java.io.InputStream;
import java.util.Properties;
public final class AppConfig {
 private static final Properties P = new Properties();
 static { try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) { if (in != null) P.load(in); } catch (Exception e) { throw new ExceptionInInitializerError(e); } }
 private AppConfig() {}
 public static String get(String key, String fallback) { return P.getProperty(key, fallback); }
}
