package com.quanlydaytro.service;

import com.quanlydaytro.dao.NhanVienDAO;
import com.quanlydaytro.model.NhanVien;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Nghiệp vụ quản lý nhân viên: kiểm tra dữ liệu, phân quyền và ràng buộc liên quan.
 */
public class NhanVienService {
    private static final Pattern SO_DIEN_THOAI = Pattern.compile("[0-9+() .-]{8,15}");
    private final NhanVienDAO dao;

    public NhanVienService() {
        this(new NhanVienDAO());
    }

    public NhanVienService(NhanVienDAO dao) {
        this.dao = dao;
    }

    public List<NhanVien> layTatCa() throws SQLException {
        yeuCauQuyen();
        return dao.findAll();
    }

    public List<NhanVien> timKiem(String tuKhoa) throws SQLException {
        yeuCauQuyen();
        return dao.timKiem(tuKhoa);
    }

    public Optional<NhanVien> timTheoMa(int ma) throws SQLException {
        yeuCauQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã nhân viên không hợp lệ.");
        return dao.findById(ma);
    }

    public int them(NhanVien nv) throws SQLException {
        yeuCauQuyen();
        kiemTra(nv);
        if (dao.soDienThoaiDaTonTai(nv.getSoDienThoai().trim(), 0))
            throw new IllegalArgumentException("Số điện thoại này đã được sử dụng cho nhân viên khác.");
        nv.setHoTen(nv.getHoTen().trim());
        nv.setSoDienThoai(nv.getSoDienThoai().trim());
        nv.setChucVu(nv.getChucVu().trim());
        return dao.insert(nv);
    }

    public boolean capNhat(int ma, NhanVien nv) throws SQLException {
        yeuCauQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã nhân viên không hợp lệ.");
        kiemTra(nv);
        if (dao.soDienThoaiDaTonTai(nv.getSoDienThoai().trim(), ma))
            throw new IllegalArgumentException("Số điện thoại này đã được sử dụng cho nhân viên khác.");
        nv.setMaNhanVien(ma);
        nv.setHoTen(nv.getHoTen().trim());
        nv.setSoDienThoai(nv.getSoDienThoai().trim());
        nv.setChucVu(nv.getChucVu().trim());
        return dao.update(ma, nv);
    }

    public boolean xoa(int ma) throws SQLException {
        yeuCauQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã nhân viên không hợp lệ.");
        if (dao.coTaiKhoan(ma))
            throw new IllegalArgumentException("Không thể xóa nhân viên đang được gắn với tài khoản. Hãy xử lý tài khoản trước.");
        if (dao.dangPhanCongDayTro(ma))
            throw new IllegalArgumentException("Không thể xóa nhân viên đang được phân công quản lý dãy trọ. Hãy gỡ phân công trước.");
        return dao.delete(ma);
    }

    private void kiemTra(NhanVien nv) {
        if (nv == null) throw new IllegalArgumentException("Dữ liệu nhân viên không được để trống.");
        if (nv.getHoTen() == null || nv.getHoTen().trim().isEmpty())
            throw new IllegalArgumentException("Họ tên không được để trống.");
        if (nv.getHoTen().trim().length() > 100)
            throw new IllegalArgumentException("Họ tên không được vượt quá 100 ký tự.");
        if (nv.getSoDienThoai() == null || !SO_DIEN_THOAI.matcher(nv.getSoDienThoai().trim()).matches())
            throw new IllegalArgumentException("Số điện thoại phải dài 8–15 ký tự và chỉ chứa số hoặc các ký tự + ( ) . -.");
        if (nv.getChucVu() == null || nv.getChucVu().trim().isEmpty())
            throw new IllegalArgumentException("Chức vụ không được để trống.");
        if (nv.getChucVu().trim().length() > 50)
            throw new IllegalArgumentException("Chức vụ không được vượt quá 50 ký tự.");
    }

    private void yeuCauQuyen() {
        if (!QuanLyPhienDangNhap.daDangNhap() || !QuanLyPhienDangNhap.coVaiTro("Admin"))
            throw new SecurityException("Chỉ tài khoản có vai trò Admin mới được quản lý nhân viên.");
    }
}
