package com.quanlydaytro.dao;

import com.quanlydaytro.model.HoaDon;
import com.quanlydaytro.enums.TrangThaiHoaDon;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

/** DAO cho bảng HOADON; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class HoaDonDAO extends AbstractJdbcDAO<HoaDon> {
    @Override protected String tableName() { return "HOADON"; }
    @Override protected String idColumn() { return "MaHoaDon"; }
    @Override protected String[] columns() { return new String[] {"MaHopDong", "ThangNam", "TienPhong", "TienDien", "TienNuoc", "TongTien", "HanThanhToan", "TrangThai"}; }
    @Override protected Object[] values(HoaDon e) { return new Object[] {e.getMaHopDong(), e.getThangNam(), e.getTienPhong(), e.getTienDien(), e.getTienNuoc(), e.getTongTien(), e.getHanThanhToan(), giaTriTrangThai(e.getTrangThai())}; }
    @Override protected HoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon e = new HoaDon();
        e.setMaHoaDon(rs.getInt("MaHoaDon"));
        e.setMaHopDong(rs.getInt("MaHopDong"));
        e.setThangNam(rs.getString("ThangNam"));
        e.setTienPhong(rs.getBigDecimal("TienPhong"));
        e.setTienDien(rs.getBigDecimal("TienDien"));
        e.setTienNuoc(rs.getBigDecimal("TienNuoc"));
        e.setTongTien(rs.getBigDecimal("TongTien"));
        e.setHanThanhToan(localDate(rs, "HanThanhToan"));
        e.setTrangThai(trangThaiTuCSDL(rs.getString("TrangThai")));
        return e;
    }
    private String giaTriTrangThai(TrangThaiHoaDon trangThai) {
        return switch (trangThai) {
            case CHUA_THANH_TOAN -> "ChuaThanhToan";
            case DANG_THANH_TOAN -> "DangThanhToan";
            case HOAN_TAT -> "HoanTat";
        };
    }

    private TrangThaiHoaDon trangThaiTuCSDL(String value) throws SQLException {
        return switch (value) {
            case "ChuaThanhToan" -> TrangThaiHoaDon.CHUA_THANH_TOAN;
            case "DangThanhToan" -> TrangThaiHoaDon.DANG_THANH_TOAN;
            case "HoanTat" -> TrangThaiHoaDon.HOAN_TAT;
            default -> throw new SQLException("TrangThai HOADON không hợp lệ trong CSDL: " + value);
        };
    }

}
