package com.quanlydaytro.service;

import com.quanlydaytro.dao.BaoCaoDAO;

import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Kiểm tra kỳ báo cáo và điều phối truy vấn thống kê.
 */
public class BaoCaoService {
    private final BaoCaoDAO dao;

    public BaoCaoService() {
        this(new BaoCaoDAO());
    }

    public BaoCaoService(BaoCaoDAO dao) {
        this.dao = dao;
    }

    public void kiemTraKy(String tu, String den) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/yyyy");
        YearMonth a, b;
        try {
            a = YearMonth.parse(tu, f);
            b = YearMonth.parse(den, f);
        } catch (Exception e) {
            throw new IllegalArgumentException("Kỳ báo cáo phải có dạng MM/yyyy, ví dụ 01/2026.");
        }
        if (a.isAfter(b)) throw new IllegalArgumentException("Tháng bắt đầu không được sau tháng kết thúc.");
    }

    public List<Object[]> doanhThu(String tu, String den) throws SQLException {
        kiemTraKy(tu, den);
        return dao.layDoanhThu(tu, den);
    }

    public List<Object[]> congNo(String tu, String den) throws SQLException {
        kiemTraKy(tu, den);
        return dao.layCongNo(tu, den);
    }

    public List<Object[]> tinhTrangPhong() throws SQLException {
        return dao.layTinhTrangPhong();
    }

    public List<Object[]> tieuThu(String tu, String den) throws SQLException {
        kiemTraKy(tu, den);
        return dao.layTieuThu(tu, den);
    }
}
