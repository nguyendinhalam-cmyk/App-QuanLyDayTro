package com.quanlydaytro.service;

import com.quanlydaytro.dao.ChiSoNuocDAO;
import com.quanlydaytro.model.ChiSoNuoc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChiSoNuocService {
    private final ChiSoNuocDAO dao;

    public ChiSoNuocService() {
        this(new ChiSoNuocDAO());
    }

    public ChiSoNuocService(ChiSoNuocDAO d) {
        dao = d;
    }

    public List<ChiSoNuoc> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public List<ChiSoNuoc> timKiem(String k) throws SQLException {
        return dao.timKiem(k);
    }

    public Optional<ChiSoNuoc> timTheoMa(int m) throws SQLException {
        return dao.findById(m);
    }

    public int them(ChiSoNuoc e) throws SQLException {
        kiemTra(e);
        if (!dao.phongTonTai(e.getMaPhong())) throw new IllegalArgumentException("Phòng không tồn tại.");
        kiemTraChiSoKyTruoc(e);
        return dao.insert(e);
    }

    public boolean capNhat(int ma, ChiSoNuoc e) throws SQLException {
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

    private void kiemTra(ChiSoNuoc e) {
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

    private void kiemTraChiSoKyTruoc(ChiSoNuoc e) throws SQLException {
        Optional<ChiSoNuoc> p = dao.kyTruoc(e.getMaPhong(), e.getThangNam());
        if (p.isPresent() && e.getChiSoCu() != p.get().getChiSoMoi())
            throw new IllegalArgumentException("Chỉ số cũ phải bằng chỉ số mới của kỳ gần nhất (" + p.get().getChiSoMoi() + ").");
    }
}
