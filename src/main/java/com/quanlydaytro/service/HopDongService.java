package com.quanlydaytro.service;

import com.quanlydaytro.dao.HopDongDAO;
import com.quanlydaytro.dao.NguoiThueDAO;
import com.quanlydaytro.dao.PhongDAO;
import com.quanlydaytro.enums.TrangThaiHopDong;
import com.quanlydaytro.model.HopDong;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Xử lý quy tắc nghiệp vụ quản lý hợp đồng thuê.
 */
public class HopDongService {
    private final HopDongDAO dao;
    private final NguoiThueDAO nguoiThueDAO;
    private final PhongDAO phongDAO;

    public HopDongService() {
        this(new HopDongDAO(), new NguoiThueDAO(), new PhongDAO());
    }

    public HopDongService(HopDongDAO dao) {
        this(dao, new NguoiThueDAO(), new PhongDAO());
    }

    public HopDongService(HopDongDAO dao, NguoiThueDAO nguoiThueDAO, PhongDAO phongDAO) {
        this.dao = dao;
        this.nguoiThueDAO = nguoiThueDAO;
        this.phongDAO = phongDAO;
    }

    public List<HopDong> layTatCa() throws SQLException {
        return dao.findAll();
    }

    public List<HopDong> timKiem(String key) throws SQLException {
        return dao.timKiem(key);
    }

    public List<Object[]> layDanhSachKemTen() throws SQLException {
        return dao.danhSachKemTen();
    }

    public Optional<HopDong> timTheoMa(int ma) throws SQLException {
        return dao.findById(ma);
    }

    public int them(HopDong h) throws SQLException {
        kiemTra(h, 0);
        return dao.taoHopDongBangTransaction(h);
    }

    public boolean capNhat(int ma, HopDong h) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã hợp đồng không hợp lệ.");
        kiemTra(h, ma);
        return dao.update(ma, h);
    }

    public boolean xoa(int ma) throws SQLException {
        if (ma <= 0) throw new IllegalArgumentException("Mã hợp đồng không hợp lệ.");
        if (dao.coDuLieuPhuThuoc(ma))
            throw new IllegalStateException("Không thể xóa hợp đồng đã phát sinh hóa đơn. Hãy chuyển trạng thái sang Đã đóng nếu cần.");
        return dao.delete(ma);
    }

    public boolean ketThuc(int ma) throws SQLException {
        HopDong h = dao.findById(ma).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hợp đồng."));
        h.setTrangThai(TrangThaiHopDong.DA_DONG);
        return dao.update(ma, h);
    }

    private void kiemTra(HopDong h, int boQuaMa) throws SQLException {
        if (h == null) throw new IllegalArgumentException("Dữ liệu hợp đồng không được để trống.");
        if (h.getMaPhong() <= 0 || phongDAO.findById(h.getMaPhong()).isEmpty())
            throw new IllegalArgumentException("Phòng không tồn tại.");
        if (h.getMaNguoiThue() <= 0 || nguoiThueDAO.findById(h.getMaNguoiThue()).isEmpty())
            throw new IllegalArgumentException("Người thuê không tồn tại.");
        LocalDate bd = h.getNgayBatDau(), kt = h.getNgayKetThuc();
        if (bd == null || kt == null)
            throw new IllegalArgumentException("Vui lòng nhập ngày bắt đầu và ngày kết thúc.");
        if (!kt.isAfter(bd)) throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu.");
        if (h.getGiaThueThoaThuan() == null || h.getGiaThueThoaThuan().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Giá thuê thỏa thuận phải lớn hơn 0.");
        if (h.getTienCoc() == null || h.getTienCoc().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Tiền cọc phải lớn hơn 0 theo ràng buộc CSDL.");
        if (h.getTrangThai() == null) throw new IllegalArgumentException("Vui lòng chọn trạng thái hợp đồng.");
        if (h.getTrangThai() == TrangThaiHopDong.HIEU_LUC && dao.phongCoHopDongChongLap(h.getMaPhong(), bd, kt, boQuaMa))
            throw new IllegalArgumentException("Phòng đã có hợp đồng hiệu lực trong khoảng thời gian này.");
    }
}
