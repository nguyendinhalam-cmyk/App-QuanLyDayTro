package com.quanlydaytro.service;

import com.quanlydaytro.dao.DayTroDAO;
import com.quanlydaytro.model.DayTro;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Nghiệp vụ quản lý dãy trọ. Chỉ Admin/Manager được thao tác.
 */
public class DayTroService {
    private final DayTroDAO dao;

    public DayTroService() {
        this(new DayTroDAO());
    }

    public DayTroService(DayTroDAO dao) {
        this.dao = dao;
    }

    public List<DayTro> layTatCa() throws SQLException {
        kiemTraQuyen();
        return dao.findAll();
    }

    public List<DayTro> timKiem(String tuKhoa) throws SQLException {
        kiemTraQuyen();
        return dao.timKiem(tuKhoa);
    }

    public Optional<DayTro> timTheoMa(int ma) throws SQLException {
        kiemTraQuyen();
        return dao.findById(ma);
    }

    public int them(DayTro e) throws SQLException {
        kiemTraQuyen();
        kiemTra(e);
        if (dao.diaChiDaTonTai(e.getDiaChi().trim(), 0))
            throw new IllegalArgumentException("Địa chỉ dãy trọ đã tồn tại.");
        e.setTenDay(e.getTenDay().trim());
        e.setDiaChi(e.getDiaChi().trim());
        return dao.insert(e);
    }

    public boolean capNhat(int ma, DayTro e) throws SQLException {
        kiemTraQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã dãy trọ không hợp lệ.");
        kiemTra(e);
        e.setTenDay(e.getTenDay().trim());
        e.setDiaChi(e.getDiaChi().trim());
        if (dao.diaChiDaTonTai(e.getDiaChi(), ma))
            throw new IllegalArgumentException("Địa chỉ này đã được sử dụng cho dãy trọ khác.");
        return dao.update(ma, e);
    }

    public boolean xoa(int ma) throws SQLException {
        kiemTraQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã dãy trọ không hợp lệ.");
        if (dao.coDuLieuPhuThuoc(ma))
            throw new IllegalArgumentException("Không thể xóa dãy trọ đang có phòng hoặc nhân viên được phân công.");
        return dao.delete(ma);
    }

    private void kiemTra(DayTro e) {
        if (e == null) throw new IllegalArgumentException("Dữ liệu dãy trọ không được để trống.");
        if (e.getTenDay() == null || e.getTenDay().trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập tên dãy trọ.");
        if (e.getTenDay().trim().length() > 100)
            throw new IllegalArgumentException("Tên dãy trọ không được vượt quá 100 ký tự.");
        if (e.getDiaChi() == null || e.getDiaChi().trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập địa chỉ.");
        if (e.getDiaChi().trim().length() > 200)
            throw new IllegalArgumentException("Địa chỉ không được vượt quá 200 ký tự.");
        if (e.getMaChuTro() <= 0) throw new IllegalArgumentException("Vui lòng chọn chủ trọ.");
    }

    private void kiemTraQuyen() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin", "Manager"))
            throw new SecurityException("Bạn không có quyền quản lý dãy trọ.");
    }
}
