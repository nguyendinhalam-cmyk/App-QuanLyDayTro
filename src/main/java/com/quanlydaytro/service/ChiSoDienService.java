package com.quanlydaytro.service;

import com.quanlydaytro.dao.ChiSoDienDAO;
import com.quanlydaytro.model.ChiSoDien;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ChiSoDienService {
    private final ChiSoDienDAO dao;

    public ChiSoDienService() {
        this(new ChiSoDienDAO());
    }

    public ChiSoDienService(ChiSoDienDAO d) {
        dao = d;
    }

    public List<ChiSoDien> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public List<ChiSoDien> timKiem(String k) throws SQLException {
        return dao.timKiem(k);
    }

    public Optional<ChiSoDien> timTheoMa(int m) throws SQLException {
        return dao.findById(m);
    }

    public int them(ChiSoDien e) throws SQLException {
        kiemTra(e);
        if (!dao.phongTonTai(e.getMaPhong())) throw new IllegalArgumentException("Phòng không tồn tại.");
        kiemTraChiSoKyTruoc(e);
        return dao.insert(e);
    }

    public boolean capNhat(int ma, ChiSoDien e) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã chỉ số không hợp lệ.");
        kiemTra(e);
        if (!dao.phongTonTai(e.getMaPhong())) throw new IllegalArgumentException("Phòng không tồn tại.");
        kiemTraChiSoKyTruoc(e);
        return dao.update(ma, e);
    }

    public boolean xoa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã chỉ số không hợp lệ.");
        return dao.delete(ma);
    }

    private void kiemTra(ChiSoDien e) {
        if (e == null) throw new IllegalArgumentException("Dữ liệu không được để trống.");
        if (e.getMaPhong() <= 0) throw new IllegalArgumentException("Mã phòng phải lớn hơn 0.");
        try {
            YearMonth.parse(e.getThangNam(), DateTimeFormatter.ofPattern("MM/uuuu"));
        } catch (Exception x) {
            throw new IllegalArgumentException("Kỳ ghi phải đúng định dạng MM/yyyy, ví dụ 10/2026.");
        }
        if (e.getChiSoCu() < 0 || e.getChiSoMoi() < e.getChiSoCu())
            throw new IllegalArgumentException("Chỉ số phải không âm và chỉ số mới phải lớn hơn hoặc bằng chỉ số cũ.");
        if (e.getNgayGhi() == null) e.setNgayGhi(LocalDate.now());
    }

    private void kiemTraChiSoKyTruoc(ChiSoDien e) throws SQLException {
        Optional<ChiSoDien> p = dao.kyTruoc(e.getMaPhong(), e.getThangNam());
        if (p.isPresent() && e.getChiSoCu() != p.get().getChiSoMoi())
            throw new IllegalArgumentException("Chỉ số cũ phải bằng chỉ số mới của kỳ gần nhất (" + p.get().getChiSoMoi() + ").");
    }
}
