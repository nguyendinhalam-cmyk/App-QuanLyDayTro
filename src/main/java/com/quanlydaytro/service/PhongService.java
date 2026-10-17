package com.quanlydaytro.service;

import com.quanlydaytro.dao.PhongDAO;
import com.quanlydaytro.dao.DayTroDAO;
import com.quanlydaytro.enums.TrangThaiPhong;
import com.quanlydaytro.model.Phong;
import com.quanlydaytro.util.QuanLyPhienDangNhap;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Quy tắc nghiệp vụ quản lý phòng; chỉ Admin/Manager được thao tác.
 */
public class PhongService {
    private final PhongDAO dao;
    private final DayTroDAO dayTroDAO;

    public PhongService() {
        this(new PhongDAO(), new DayTroDAO());
    }

    public PhongService(PhongDAO dao) {
        this(dao, new DayTroDAO());
    }

    public PhongService(PhongDAO dao, DayTroDAO dayTroDAO) {
        this.dao = dao;
        this.dayTroDAO = dayTroDAO;
    }

    public List<Phong> layTatCa() throws SQLException {
        kiemTraQuyen();
        return dao.findAll();
    }

    public List<Phong> timKiem(String key) throws SQLException {
        kiemTraQuyen();
        return dao.timKiem(key);
    }

    public Optional<Phong> timTheoMa(int ma) throws SQLException {
        kiemTraQuyen();
        return dao.findById(ma);
    }

    public int them(Phong e) throws SQLException {
        kiemTraQuyen();
        kiemTra(e);
        if (dao.tenPhongDaTonTai(e.getMaDay(), e.getTenPhong().trim(), 0))
            throw new IllegalArgumentException("Tên phòng đã tồn tại trong dãy trọ này.");
        e.setTenPhong(e.getTenPhong().trim());
        return dao.insert(e);
    }

    public boolean capNhat(int ma, Phong e) throws SQLException {
        kiemTraQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã phòng không hợp lệ.");
        kiemTra(e);
        if (dao.tenPhongDaTonTai(e.getMaDay(), e.getTenPhong().trim(), ma))
            throw new IllegalArgumentException("Tên phòng đã tồn tại trong dãy trọ này.");
        e.setTenPhong(e.getTenPhong().trim());
        return dao.update(ma, e);
    }

    public boolean xoa(int ma) throws SQLException {
        kiemTraQuyen();
        if (ma <= 0) throw new IllegalArgumentException("Mã phòng không hợp lệ.");
        if (dao.coDuLieuPhuThuoc(ma))
            throw new IllegalArgumentException("Không thể xóa phòng đã có hợp đồng hoặc chỉ số điện/nước.");
        return dao.delete(ma);
    }

    private void kiemTra(Phong e) throws SQLException {
        if (e == null) throw new IllegalArgumentException("Dữ liệu phòng không được để trống.");
        if (e.getTenPhong() == null || e.getTenPhong().trim().isEmpty())
            throw new IllegalArgumentException("Vui lòng nhập tên phòng.");
        if (e.getTenPhong().trim().length() > 20)
            throw new IllegalArgumentException("Tên phòng không được vượt quá 20 ký tự.");
        BigDecimal gia = e.getGiaThueHienTai();
        if (gia == null || gia.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Giá thuê phải lớn hơn 0.");
        if (e.getSoNguoiO() < 0 || e.getSoNguoiO() > 3)
            throw new IllegalArgumentException("Số người ở phải nằm trong khoảng 0–3.");
        if (e.getTrangThai() == null) throw new IllegalArgumentException("Vui lòng chọn trạng thái phòng.");
        if (e.getMaDay() <= 0 || dayTroDAO.findById(e.getMaDay()).isEmpty())
            throw new IllegalArgumentException("Vui lòng chọn dãy trọ hợp lệ.");
        if (e.getTrangThai() == TrangThaiPhong.TRONG && e.getSoNguoiO() > 0)
            throw new IllegalArgumentException("Phòng trống không thể có người ở.");
        if (e.getTrangThai() == TrangThaiPhong.DANG_THUE && e.getSoNguoiO() == 0)
            throw new IllegalArgumentException("Phòng đang thuê phải có ít nhất một người ở.");
    }

    private void kiemTraQuyen() {
        if (!QuanLyPhienDangNhap.coVaiTro("Admin", "Manager"))
            throw new SecurityException("Bạn không có quyền quản lý phòng.");
    }
}
