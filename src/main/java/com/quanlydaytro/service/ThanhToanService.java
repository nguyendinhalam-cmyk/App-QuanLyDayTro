package com.quanlydaytro.service;

import com.quanlydaytro.dao.ThanhToanDAO;
import com.quanlydaytro.model.ThanhToan;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Quy tắc nghiệp vụ thanh toán và theo dõi công nợ.
 */
public class ThanhToanService {
    private final ThanhToanDAO dao;

    public ThanhToanService() {
        this(new ThanhToanDAO());
    }

    public ThanhToanService(ThanhToanDAO dao) {
        this.dao = dao;
    }

    public List<ThanhToan> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public Optional<ThanhToan> timTheoMa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã thanh toán không hợp lệ");
        return dao.findById(ma);
    }

    public List<Object[]> danhSachCongNo() throws SQLException {
        return dao.danhSachCongNo();
    }

    public int ghiNhan(ThanhToan e) throws SQLException {
        if (e == null) throw new IllegalArgumentException("Dữ liệu thanh toán không được để trống");
        if (e.getMaHoaDon() <= 0) throw new IllegalArgumentException("Hãy chọn hóa đơn hợp lệ");
        if (e.getSoTienTra() == null || e.getSoTienTra().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
        if (e.getSoTienTra().scale() > 2)
            throw new IllegalArgumentException("Số tiền chỉ được có tối đa 2 chữ số thập phân");
        if (e.getGhiChu() != null && e.getGhiChu().length() > 200)
            throw new IllegalArgumentException("Ghi chú tối đa 200 ký tự");
        if (e.getNgayThanhToan() == null) e.setNgayThanhToan(LocalDateTime.now());
        return dao.ghiNhanVaCapNhatTrangThai(e);
    }

    public BigDecimal conNo(int maHoaDon) throws SQLException {
        if (maHoaDon <= 0) throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
        return dao.tongHoaDon(maHoaDon).subtract(dao.daThanhToan(maHoaDon)).max(BigDecimal.ZERO);
    }
}
