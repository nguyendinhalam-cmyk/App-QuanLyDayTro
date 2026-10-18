package com.quanlydaytro.service;

import com.quanlydaytro.dao.TaiKhoanDAO;
import com.quanlydaytro.model.TaiKhoan;
import com.quanlydaytro.util.MaHoaMatKhau;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Nghiệp vụ quản lý tài khoản; không trả hash mật khẩu ra giao diện.
 */
public class TaiKhoanService {
    private final TaiKhoanDAO dao;

    public TaiKhoanService() {
        this(new TaiKhoanDAO());
    }

    public TaiKhoanService(TaiKhoanDAO dao) {
        this.dao = dao;
    }

    public List<Object[]> layDanhSachHienThi() throws SQLException {
        yeuCauAdmin();
        return dao.layDanhSachHienThi();
    }

    public List<Object[]> layNhanVienChuaCoTaiKhoan(int maTaiKhoanDangSua) throws SQLException {
        yeuCauAdmin();
        return dao.layNhanVienChuaCoTaiKhoan(maTaiKhoanDangSua);
    }

    public List<Object[]> layVaiTro() throws SQLException {
        yeuCauAdmin();
        // Truy vấn danh mục vai trò chỉ đọc, không thêm enum cố định trong Java.
        return new com.quanlydaytro.dao.VaiTroDanhMucDAO().layDanhMuc();
    }

    public int them(String tenDangNhap, char[] matKhau, int maNV, int maVaiTro) throws SQLException {
        yeuCauAdmin();
        kiemTra(tenDangNhap, matKhau, maNV, maVaiTro);
        if (dao.tenDangNhapDaTonTai(tenDangNhap.trim(), 0))
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        if (dao.nhanVienDaCoTaiKhoan(maNV, 0)) throw new IllegalArgumentException("Nhân viên này đã có tài khoản.");
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(tenDangNhap.trim());
        tk.setMatKhau(MaHoaMatKhau.bamMatKhau(matKhau));
        tk.setMaNV(maNV);
        tk.setMaVaiTro(maVaiTro);
        return dao.insert(tk);
    }

    public boolean capNhat(int ma, String tenDangNhap, char[] matKhauMoi, int maNV, int maVaiTro) throws SQLException {
        yeuCauAdmin();
        if (ma <= 0) throw new IllegalArgumentException("Mã tài khoản không hợp lệ.");
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty())
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        if (maNV <= 0 || maVaiTro <= 0) throw new IllegalArgumentException("Vui lòng chọn nhân viên và vai trò.");
        // Không cho tài khoản Admin tự đổi vai trò của chính mình trong phiên hiện tại,
        // tránh phiên đang đăng nhập tiếp tục giữ quyền Admin sau khi bị hạ quyền.
        if (java.util.Objects.equals(com.quanlydaytro.util.QuanLyPhienDangNhap.getMaTaiKhoan(), ma)
                && !java.util.Objects.equals(com.quanlydaytro.util.QuanLyPhienDangNhap.getMaVaiTro(), maVaiTro)) {
            throw new IllegalArgumentException("Không thể tự thay đổi vai trò của tài khoản đang đăng nhập. Hãy dùng một tài khoản Admin khác.");
        }
        if (dao.tenDangNhapDaTonTai(tenDangNhap.trim(), ma))
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        if (dao.nhanVienDaCoTaiKhoan(maNV, ma))
            throw new IllegalArgumentException("Nhân viên này đã có tài khoản khác.");
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(tenDangNhap.trim());
        tk.setMaNV(maNV);
        tk.setMaVaiTro(maVaiTro);
        if (matKhauMoi != null && matKhauMoi.length > 0) tk.setMatKhau(MaHoaMatKhau.bamMatKhau(matKhauMoi));
        else {
            Optional<TaiKhoan> cu = dao.findById(ma);
            if (cu.isEmpty()) return false;
            tk.setMatKhau(cu.get().getMatKhau());
        }
        return dao.update(ma, tk);
    }

    public boolean xoa(int ma) throws SQLException {
        yeuCauAdmin();
        if (ma <= 0) throw new IllegalArgumentException("Mã tài khoản không hợp lệ.");
        if (java.util.Objects.equals(com.quanlydaytro.util.QuanLyPhienDangNhap.getMaTaiKhoan(), ma))
            throw new IllegalArgumentException("Không thể xóa tài khoản đang đăng nhập.");
        return dao.delete(ma);
    }

    private void yeuCauAdmin() {
        if (!com.quanlydaytro.util.QuanLyPhienDangNhap.daDangNhap()
                || !com.quanlydaytro.util.QuanLyPhienDangNhap.coVaiTro("Admin")) {
            throw new SecurityException("Chỉ tài khoản có vai trò Admin mới được quản lý tài khoản.");
        }
    }

    private void kiemTra(String ten, char[] pass, int maNV, int maVT) {
        if (ten == null || ten.trim().length() < 3 || ten.trim().length() > 50)
            throw new IllegalArgumentException("Tên đăng nhập phải có từ 3 đến 50 ký tự.");
        if (pass == null || pass.length < 8) throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự.");
        if (maNV <= 0 || maVT <= 0) throw new IllegalArgumentException("Vui lòng chọn nhân viên và vai trò.");
    }
}
