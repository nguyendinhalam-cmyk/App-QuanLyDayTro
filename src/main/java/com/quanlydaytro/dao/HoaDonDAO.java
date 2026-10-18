package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.enums.TrangThaiHoaDon;
import com.quanlydaytro.model.HoaDon;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho HOADON, giữ nguyên tên bảng/cột theo database/schema.sql.
 */
public class HoaDonDAO extends AbstractJdbcDAO<HoaDon> {
    @Override
    protected String tableName() {
        return "HOADON";
    }

    @Override
    protected String idColumn() {
        return "MaHoaDon";
    }

    @Override
    protected String[] columns() {
        return new String[]{"MaHopDong", "ThangNam", "TienPhong", "TienDien", "TienNuoc", "TongTien", "HanThanhToan", "TrangThai"};
    }

    @Override
    protected Object[] values(HoaDon e) {
        return new Object[]{e.getMaHopDong(), e.getThangNam(), e.getTienPhong(), e.getTienDien(), e.getTienNuoc(), e.getTongTien(), e.getHanThanhToan(), toDb(e.getTrangThai())};
    }

    @Override
    protected HoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon e = new HoaDon();
        e.setMaHoaDon(rs.getInt("MaHoaDon"));
        e.setMaHopDong(rs.getInt("MaHopDong"));
        e.setThangNam(rs.getString("ThangNam"));
        e.setTienPhong(rs.getBigDecimal("TienPhong"));
        e.setTienDien(rs.getBigDecimal("TienDien"));
        e.setTienNuoc(rs.getBigDecimal("TienNuoc"));
        e.setTongTien(rs.getBigDecimal("TongTien"));
        e.setHanThanhToan(localDate(rs, "HanThanhToan"));
        e.setTrangThai(fromDb(rs.getString("TrangThai")));
        return e;
    }

    private String toDb(TrangThaiHoaDon t) {
        if (t == null) throw new IllegalArgumentException("Vui lòng chọn trạng thái hóa đơn.");
        return switch (t) {
            case CHUA_THANH_TOAN -> "ChuaThanhToan";
            case DANG_THANH_TOAN -> "DangThanhToan";
            case HOAN_TAT -> "HoanTat";
        };
    }

    private TrangThaiHoaDon fromDb(String s) throws SQLException {
        return switch (s) {
            case "ChuaThanhToan" -> TrangThaiHoaDon.CHUA_THANH_TOAN;
            case "DangThanhToan" -> TrangThaiHoaDon.DANG_THANH_TOAN;
            case "HoanTat" -> TrangThaiHoaDon.HOAN_TAT;
            default -> throw new SQLException("Trạng thái hóa đơn không hợp lệ: " + s);
        };
    }

    public List<HoaDon> timKiem(String tuKhoa) throws SQLException {
        String key = "%" + (tuKhoa == null ? "" : tuKhoa.trim()) + "%";
        String sql = "SELECT * FROM HOADON WHERE CAST(MaHoaDon AS VARCHAR(12)) LIKE ? OR CAST(MaHopDong AS VARCHAR(12)) LIKE ? OR ThangNam LIKE ? OR TrangThai LIKE ? ORDER BY MaHoaDon DESC";
        List<HoaDon> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) ps.setString(i, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        }
        return out;
    }

    /**
     * Lập hóa đơn từ hợp đồng, chỉ số và đơn giá có hiệu lực trong tháng; mọi phép tính dùng BigDecimal.
     */
    public int lapHoaDon(int maHopDong, String thangNam, LocalDate hanThanhToan) throws SQLException {
        String sql = "SELECT h.MaPhong,h.GiaThueThoaThuan, cd.ChiSoCu DienCu,cd.ChiSoMoi DienMoi, cn.ChiSoCu NuocCu,cn.ChiSoMoi NuocMoi, gd.DonGia GiaDien, gn.DonGia GiaNuoc " +
                "FROM HOPDONG h LEFT JOIN CHISODIEN cd ON cd.MaPhong=h.MaPhong AND cd.ThangNam=? LEFT JOIN CHISONUOC cn ON cn.MaPhong=h.MaPhong AND cn.ThangNam=? " +
                "OUTER APPLY (SELECT TOP 1 DonGia FROM BANGGIADIEN WHERE NgayApDung<=EOMONTH(CONVERT(date,'01/'+?,103)) ORDER BY NgayApDung DESC) gd " +
                "OUTER APPLY (SELECT TOP 1 DonGia FROM BANGGIANUOC WHERE NgayApDung<=EOMONTH(CONVERT(date,'01/'+?,103)) ORDER BY NgayApDung DESC) gn " +
                "WHERE h.MaHopDong=? AND h.TrangThai=N'HieuLuc' AND h.NgayBatDau<=EOMONTH(CONVERT(date,'01/'+?,103)) AND h.NgayKetThuc>=CONVERT(date,'01/'+?,103)";
        BigDecimal tienPhong, tienDien = BigDecimal.ZERO, tienNuoc = BigDecimal.ZERO;
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, thangNam);
            ps.setString(2, thangNam);
            ps.setString(3, thangNam);
            ps.setString(4, thangNam);
            ps.setInt(5, maHopDong);
            ps.setString(6, thangNam);
            ps.setString(7, thangNam);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Không tìm thấy hợp đồng hiệu lực trong tháng đã chọn.");
                tienPhong = rs.getBigDecimal("GiaThueThoaThuan");
                Integer dc = (Integer) rs.getObject("DienCu"), dm = (Integer) rs.getObject("DienMoi"), nc = (Integer) rs.getObject("NuocCu"), nm = (Integer) rs.getObject("NuocMoi");
                BigDecimal gd = rs.getBigDecimal("GiaDien"), gn = rs.getBigDecimal("GiaNuoc");
                if (dc == null || dm == null)
                    throw new SQLException("Chưa ghi đủ chỉ số điện cho phòng trong tháng " + thangNam + ".");
                if (nc == null || nm == null)
                    throw new SQLException("Chưa ghi đủ chỉ số nước cho phòng trong tháng " + thangNam + ".");
                if (gd == null || gn == null)
                    throw new SQLException("Chưa khai báo đơn giá điện/nước áp dụng trong tháng " + thangNam + ".");
                tienDien = gd.multiply(BigDecimal.valueOf(dm - dc));
                tienNuoc = gn.multiply(BigDecimal.valueOf(nm - nc));
            }
            BigDecimal tong = tienPhong.add(tienDien).add(tienNuoc);
            String ins = "INSERT INTO HOADON(MaHopDong,ThangNam,TienPhong,TienDien,TienNuoc,TongTien,HanThanhToan,TrangThai) VALUES(?,?,?,?,?,?,?,N'ChuaThanhToan')";
            try (PreparedStatement ps2 = c.prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
                ps2.setInt(1, maHopDong);
                ps2.setString(2, thangNam);
                ps2.setBigDecimal(3, tienPhong);
                ps2.setBigDecimal(4, tienDien);
                ps2.setBigDecimal(5, tienNuoc);
                ps2.setBigDecimal(6, tong);
                ps2.setDate(7, Date.valueOf(hanThanhToan));
                ps2.executeUpdate();
                try (ResultSet keys = ps2.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Không lấy được mã hóa đơn vừa tạo.");
    }
}
