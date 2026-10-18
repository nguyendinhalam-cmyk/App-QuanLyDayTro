package com.quanlydaytro.service;

import com.quanlydaytro.dao.NguoiThueDAO;
import com.quanlydaytro.model.NguoiThue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Nghiệp vụ quản lý người thuê: kiểm tra dữ liệu trước khi gọi DAO.
 */
public class NguoiThueService {
    private final NguoiThueDAO dao;

    public NguoiThueService() {
        this(new NguoiThueDAO());
    }

    public NguoiThueService(NguoiThueDAO dao) {
        this.dao = dao;
    }

    public List<NguoiThue> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public List<NguoiThue> timKiem(String tuKhoa) throws SQLException {
        return dao.timKiem(tuKhoa);
    }

    public Optional<NguoiThue> timTheoMa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã người thuê phải lớn hơn 0.");
        return dao.findById(ma);
    }

    public int them(NguoiThue entity) throws SQLException {
        kiemTra(entity);
        if (dao.daCoCCCD(entity.getCCCD(), 0))
            throw new IllegalArgumentException("CCCD này đã tồn tại trong hệ thống.");
        try {
            return dao.insert(entity);
        } catch (SQLException ex) {
            throw chuanHoaLoi(ex);
        }
    }

    public boolean capNhat(int ma, NguoiThue entity) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã người thuê không hợp lệ.");
        kiemTra(entity);
        if (dao.daCoCCCD(entity.getCCCD(), ma))
            throw new IllegalArgumentException("CCCD này đã được người thuê khác sử dụng.");
        entity.setMaNguoiThue(ma);
        try {
            return dao.update(ma, entity);
        } catch (SQLException ex) {
            throw chuanHoaLoi(ex);
        }
    }

    public boolean xoa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã người thuê không hợp lệ.");
        try {
            return dao.delete(ma);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 547)
                throw new SQLException("Không thể xóa người thuê này vì đã có hợp đồng hoặc dữ liệu liên quan. Hãy giữ lại lịch sử thuê.", ex);
            throw ex;
        }
    }

    private void kiemTra(NguoiThue e) {
        if (e == null) throw new IllegalArgumentException("Dữ liệu người thuê không được để trống.");
        String hoTen = trim(e.getHoTen()), cccd = trim(e.getCCCD()), sdt = trim(e.getSoDienThoai());
        String diaChi = trim(e.getDiaChiThuongTru());
        if (hoTen.isEmpty()) throw new IllegalArgumentException("Họ tên là bắt buộc.");
        if (hoTen.length() > 100) throw new IllegalArgumentException("Họ tên không được vượt quá 100 ký tự.");
        if (!cccd.matches("[0-9]{9}|[0-9]{12}")) throw new IllegalArgumentException("CCCD phải gồm 9 hoặc 12 chữ số.");
        if (!sdt.matches("[0-9]{9,15}")) throw new IllegalArgumentException("Số điện thoại phải gồm 9–15 chữ số.");
        if (diaChi.length() > 200) throw new IllegalArgumentException("Địa chỉ không được vượt quá 200 ký tự.");
        e.setHoTen(hoTen);
        e.setCCCD(cccd);
        e.setSoDienThoai(sdt);
        e.setDiaChiThuongTru(diaChi.isEmpty() ? null : diaChi);
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private SQLException chuanHoaLoi(SQLException ex) {
        if (ex.getErrorCode() == 2627 || ex.getErrorCode() == 2601)
            return new SQLException("CCCD đã tồn tại. Vui lòng kiểm tra lại.", ex);
        return ex;
    }
}
