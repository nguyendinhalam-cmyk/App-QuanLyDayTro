package com.quanlydaytro.service;

import com.quanlydaytro.dao.TraPhongDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Quy tắc nghiệp vụ trả phòng và quyết toán tiền cọc.
 */
public class TraPhongService {
    private final TraPhongDAO dao;

    public TraPhongService() {
        this(new TraPhongDAO());
    }

    public TraPhongService(TraPhongDAO dao) {
        this.dao = dao;
    }

    public List<Object[]> danhSachHopDongHieuLuc() throws SQLException {
        return dao.danhSachHopDongHieuLuc();
    }

    public Object[] thongTinHopDong(int maHopDong) throws SQLException {
        if (maHopDong <= 0) throw new IllegalArgumentException("Mã hợp đồng không hợp lệ.");
        return dao.thongTinHopDong(maHopDong);
    }

    public BigDecimal tinhTienHoanCoc(BigDecimal tienCoc, BigDecimal khauTru) {
        if (tienCoc == null || khauTru == null || tienCoc.signum() < 0 || khauTru.signum() < 0 || khauTru.compareTo(tienCoc) > 0)
            throw new IllegalArgumentException("Tiền cọc hoặc tiền khấu trừ không hợp lệ.");
        return tienCoc.subtract(khauTru);
    }

    public void quyetToan(int maHopDong, BigDecimal tienKhauTruCoc, String ghiChu) throws SQLException {
        if (maHopDong <= 0) throw new IllegalArgumentException("Vui lòng chọn hợp đồng cần trả phòng.");
        if (tienKhauTruCoc == null || tienKhauTruCoc.signum() < 0)
            throw new IllegalArgumentException("Tiền khấu trừ cọc phải lớn hơn hoặc bằng 0.");
        dao.quyetToan(maHopDong, tienKhauTruCoc, ghiChu);
    }
}
