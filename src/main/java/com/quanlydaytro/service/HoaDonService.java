package com.quanlydaytro.service;

import com.quanlydaytro.dao.HoaDonDAO;
import com.quanlydaytro.model.HoaDon;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Quy tắc nghiệp vụ lập và tra cứu hóa đơn.
 */
public class HoaDonService {
    private final HoaDonDAO dao;

    public HoaDonService() {
        this(new HoaDonDAO());
    }

    public HoaDonService(HoaDonDAO dao) {
        this.dao = dao;
    }

    public List<HoaDon> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public List<HoaDon> timKiem(String tuKhoa) throws SQLException {
        return dao.timKiem(tuKhoa);
    }

    public Optional<HoaDon> timTheoMa(int ma) throws SQLException {
        return dao.findById(ma);
    }

    public int lapHoaDon(int maHopDong, String thangNam, LocalDate hanThanhToan) throws SQLException {
        if (maHopDong <= 0) throw new IllegalArgumentException("Mã hợp đồng phải lớn hơn 0.");
        if (thangNam == null) throw new IllegalArgumentException("Vui lòng nhập tháng/năm theo định dạng MM/yyyy.");
        try {
            YearMonth.parse(thangNam, DateTimeFormatter.ofPattern("MM/uuuu"));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Tháng/năm không hợp lệ; nhập theo định dạng MM/yyyy.");
        }
        if (hanThanhToan == null) throw new IllegalArgumentException("Vui lòng chọn hạn thanh toán.");
        if (hanThanhToan.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Hạn thanh toán không được ở trước ngày hiện tại.");
        return dao.lapHoaDon(maHopDong, thangNam, hanThanhToan);
    }

    public boolean capNhat(int ma, HoaDon e) throws SQLException {
        if (ma <= 0 || e == null) throw new IllegalArgumentException("Dữ liệu hóa đơn không hợp lệ.");
        if (e.getTienPhong() == null || e.getTienDien() == null || e.getTienNuoc() == null || e.getTongTien() == null)
            throw new IllegalArgumentException("Các khoản tiền không được để trống.");
        if (e.getTienPhong().signum() < 0 || e.getTienDien().signum() < 0 || e.getTienNuoc().signum() < 0)
            throw new IllegalArgumentException("Các khoản tiền không được âm.");
        if (e.getTongTien().compareTo(e.getTienPhong().add(e.getTienDien()).add(e.getTienNuoc())) != 0)
            throw new IllegalArgumentException("Tổng tiền phải bằng tiền phòng + tiền điện + tiền nước.");
        return dao.update(ma, e);
    }

    public boolean xoa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã hóa đơn không hợp lệ.");
        return dao.delete(ma);
    }

    public java.math.BigDecimal tinhTienDuKien(int maHopDong, int maPhong, String thangNam) throws SQLException {
        if (maHopDong <= 0 || maPhong <= 0) throw new IllegalArgumentException("Hợp đồng/phòng không hợp lệ.");
        if (thangNam == null) throw new IllegalArgumentException("Vui lòng nhập tháng/năm theo định dạng MM/yyyy.");
        try {
            YearMonth.parse(thangNam, DateTimeFormatter.ofPattern("MM/uuuu"));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Tháng/năm không hợp lệ; nhập theo định dạng MM/yyyy.");
        }
        return dao.tinhTienDuKien(maHopDong, maPhong, thangNam);
    }
}
