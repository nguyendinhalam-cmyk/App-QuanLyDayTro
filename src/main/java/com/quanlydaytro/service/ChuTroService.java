package com.quanlydaytro.service;

import com.quanlydaytro.dao.ChuTroDAO;
import com.quanlydaytro.model.ChuTro;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Nghiệp vụ quản lý chủ trọ. Chỉ Admin/Manager được thao tác.
 */
public class ChuTroService {
    private final ChuTroDAO dao;

    public ChuTroService() {
        this(new ChuTroDAO());
    }

    public ChuTroService(ChuTroDAO dao) {
        this.dao = dao;
    }

    public List<ChuTro> layTatCa() throws SQLException {
        kiemTraQuyen();
        return dao.findAll();
    }

    public List<ChuTro> timKiem(String tuKhoa) throws SQLException {
        kiemTraQuyen();
        return dao.timKiem(tuKhoa);
    }

    public Optional<ChuTro> timTheoMa(int ma) throws SQLException {
        kiemTraQuyen();
        return dao.findById(ma);
    }

    public int them(ChuTro e) throws SQLException {
        kiemTraQuyen();
        kiemTra(e);
        if (dao.soDienThoaiDaTonTai(e.getSoDienThoai().trim(), 0))
            throw new IllegalArgumentException("Số điện thoại chủ trọ đã tồn tại.");
        e.setHoTen(e.getHoTen().trim());
        e.setSoDienThoai(e.getSoDienThoai().trim());
        return dao.insert(e);
    }

    public boolean capNhat(int ma, ChuTro e) throws SQLException {
        kiemTraQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã chủ trọ không hợp lệ.");
        kiemTra(e);
        e.setHoTen(e.getHoTen().trim());
        e.setSoDienThoai(e.getSoDienThoai().trim());
        if (dao.soDienThoaiDaTonTai(e.getSoDienThoai(), ma))
            throw new IllegalArgumentException("Số điện thoại chủ trọ đã được sử dụng.");
        return dao.update(ma, e);
    }

    public boolean xoa(int ma) throws SQLException {
        kiemTraQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã chủ trọ không hợp lệ.");
        if (dao.dangSoHuuDayTro(ma))
            throw new IllegalArgumentException("Không thể xóa chủ trọ đang sở hữu dãy trọ. Hãy chuyển chủ sở hữu hoặc xóa dãy trọ liên quan trước.");
        return dao.delete(ma);
    }

    private void kiemTra(ChuTro e) {
        if (e == null) throw new IllegalArgumentException("Dữ liệu chủ trọ không được để trống.");
        if (e.getHoTen() == null || e.getHoTen().trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập họ tên chủ trọ.");
        if (e.getHoTen().trim().length() > 100)
            throw new IllegalArgumentException("Họ tên không được vượt quá 100 ký tự.");
        String s = e.getSoDienThoai() == null ? "" : e.getSoDienThoai().trim();
        if (!s.matches("[0-9]{9,15}")) throw new IllegalArgumentException("Số điện thoại phải gồm 9–15 chữ số.");
    }

    private void kiemTraQuyen() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin", "Manager"))
            throw new SecurityException("Bạn không có quyền quản lý chủ trọ.");
    }
}
