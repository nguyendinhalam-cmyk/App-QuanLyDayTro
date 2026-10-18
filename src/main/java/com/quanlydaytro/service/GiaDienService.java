package com.quanlydaytro.service;

import com.quanlydaytro.dao.GiaDienDAO;
import com.quanlydaytro.model.GiaDien;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Nghiệp vụ quản lý lịch sử đơn giá điện.
 */
public class GiaDienService {
    private final GiaDienDAO dao;

    public GiaDienService() {
        this(new GiaDienDAO());
    }

    public GiaDienService(GiaDienDAO dao) {
        this.dao = dao;
    }

    public List<GiaDien> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public List<GiaDien> timKiem(String tuKhoa) throws SQLException {
        return dao.timKiem(tuKhoa);
    }

    public Optional<GiaDien> timTheoMa(int ma) throws SQLException {
        return dao.findById(ma);
    }

    public int them(GiaDien e) throws SQLException {
        kiemTra(e);
        if (dao.tonTaiNgayApDung(e.getNgayApDung(), 0))
            throw new IllegalArgumentException("Đã có đơn giá điện áp dụng vào ngày này.");
        return dao.insert(e);
    }

    public boolean capNhat(int ma, GiaDien e) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã giá điện không hợp lệ.");
        kiemTra(e);
        if (dao.tonTaiNgayApDung(e.getNgayApDung(), ma))
            throw new IllegalArgumentException("Đã có đơn giá điện khác áp dụng vào ngày này.");
        return dao.update(ma, e);
    }

    public boolean xoa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã giá điện không hợp lệ.");
        return dao.delete(ma);
    }

    private void kiemTra(GiaDien e) {
        if (e == null) throw new IllegalArgumentException("Dữ liệu giá điện không được để trống.");
        BigDecimal gia = e.getDonGia();
        if (gia == null || gia.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Đơn giá điện phải lớn hơn 0.");
        if (gia.scale() > 2 && gia.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("Đơn giá điện chỉ được có tối đa 2 chữ số thập phân.");
        if (gia.precision() - gia.scale() > 8 || gia.precision() > 10)
            throw new IllegalArgumentException("Đơn giá điện vượt giới hạn DECIMAL(10,2).");
        LocalDate ngay = e.getNgayApDung();
        if (ngay == null) throw new IllegalArgumentException("Vui lòng nhập ngày áp dụng.");
    }
}
