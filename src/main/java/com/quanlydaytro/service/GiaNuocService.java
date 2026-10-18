package com.quanlydaytro.service;

import com.quanlydaytro.dao.GiaNuocDAO;
import com.quanlydaytro.model.GiaNuoc;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Nghiệp vụ quản lý lịch sử đơn giá nước.
 */
public class GiaNuocService {
    private final GiaNuocDAO dao;

    public GiaNuocService() {
        this(new GiaNuocDAO());
    }

    public GiaNuocService(GiaNuocDAO dao) {
        this.dao = dao;
    }

    public List<GiaNuoc> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public List<GiaNuoc> timKiem(String tuKhoa) throws SQLException {
        return dao.timKiem(tuKhoa);
    }

    public Optional<GiaNuoc> timTheoMa(int ma) throws SQLException {
        return dao.findById(ma);
    }

    public int them(GiaNuoc e) throws SQLException {
        kiemTra(e);
        if (dao.tonTaiNgayApDung(e.getNgayApDung(), 0))
            throw new IllegalArgumentException("Đã có đơn giá nước áp dụng vào ngày này.");
        return dao.insert(e);
    }

    public boolean capNhat(int ma, GiaNuoc e) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã giá nước không hợp lệ.");
        kiemTra(e);
        if (dao.tonTaiNgayApDung(e.getNgayApDung(), ma))
            throw new IllegalArgumentException("Đã có đơn giá nước khác áp dụng vào ngày này.");
        return dao.update(ma, e);
    }

    public boolean xoa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã giá nước không hợp lệ.");
        return dao.delete(ma);
    }

    private void kiemTra(GiaNuoc e) {
        if (e == null) throw new IllegalArgumentException("Dữ liệu giá nước không được để trống.");
        BigDecimal gia = e.getDonGia();
        if (gia == null || gia.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Đơn giá nước phải lớn hơn 0.");
        if (gia.scale() > 2 && gia.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("Đơn giá nước chỉ được có tối đa 2 chữ số thập phân.");
        if (gia.precision() - gia.scale() > 8 || gia.precision() > 10)
            throw new IllegalArgumentException("Đơn giá nước vượt giới hạn DECIMAL(10,2).");
        LocalDate ngay = e.getNgayApDung();
        if (ngay == null) throw new IllegalArgumentException("Vui lòng nhập ngày áp dụng.");
    }
}
