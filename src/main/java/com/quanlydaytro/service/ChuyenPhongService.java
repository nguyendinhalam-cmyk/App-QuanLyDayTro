package com.quanlydaytro.service;

import com.quanlydaytro.dao.ChuyenPhongDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Quy tắc nghiệp vụ chuyển người thuê sang phòng khác.
 */
public class ChuyenPhongService {
    private final ChuyenPhongDAO dao;

    public ChuyenPhongService() {
        this(new ChuyenPhongDAO());
    }

    public ChuyenPhongService(ChuyenPhongDAO dao) {
        this.dao = dao;
    }

    public List<Object[]> danhSachHopDongHieuLuc() throws SQLException {
        return dao.danhSachHopDongHieuLuc();
    }

    public List<Object[]> danhSachPhongTrong(int maPhongHienTai) throws SQLException {
        if (maPhongHienTai <= 0) throw new IllegalArgumentException("Mã phòng hiện tại không hợp lệ.");
        return dao.danhSachPhongTrong(maPhongHienTai);
    }

    public void chuyenPhong(int maHopDong, int maPhongMoi, LocalDate ngayChuyen, BigDecimal giaThueMoi, BigDecimal tienCocMoi) throws SQLException {
        if (maHopDong <= 0 || maPhongMoi <= 0)
            throw new IllegalArgumentException("Vui lòng chọn hợp đồng và phòng mới.");
        if (ngayChuyen == null) throw new IllegalArgumentException("Vui lòng chọn ngày chuyển phòng.");
        if (giaThueMoi == null || giaThueMoi.signum() <= 0)
            throw new IllegalArgumentException("Giá thuê phải lớn hơn 0.");
        if (tienCocMoi == null || tienCocMoi.signum() <= 0)
            throw new IllegalArgumentException("Tiền cọc phải lớn hơn 0.");
        dao.chuyenPhong(maHopDong, maPhongMoi, ngayChuyen, giaThueMoi, tienCocMoi);
    }
}
