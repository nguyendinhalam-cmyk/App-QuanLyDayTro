package com.quanlydaytro.dao;

import com.quanlydaytro.model.HopDong;
import com.quanlydaytro.enums.TrangThaiHopDong;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

/** DAO cho bảng HOPDONG; tên bảng/cột lấy trực tiếp từ database/schema.sql. */
public class HopDongDAO extends AbstractJdbcDAO<HopDong> {
    @Override protected String tableName() { return "HOPDONG"; }
    @Override protected String idColumn() { return "MaHopDong"; }
    @Override protected String[] columns() { return new String[] {"MaPhong", "MaNT", "NgayBatDau", "NgayKetThuc", "GiaThueThoaThuan", "TienCoc", "TrangThai"}; }
    @Override protected Object[] values(HopDong e) { return new Object[] {e.getMaPhong(), e.getMaNguoiThue(), e.getNgayBatDau(), e.getNgayKetThuc(), e.getGiaThueThoaThuan(), e.getTienCoc(), giaTriTrangThai(e.getTrangThai())}; }
    @Override protected HopDong mapRow(ResultSet rs) throws SQLException {
        HopDong e = new HopDong();
        e.setMaHopDong(rs.getInt("MaHopDong"));
        e.setMaPhong(rs.getInt("MaPhong"));
        e.setMaNguoiThue(rs.getInt("MaNT"));
        e.setNgayBatDau(localDate(rs, "NgayBatDau"));
        e.setNgayKetThuc(localDate(rs, "NgayKetThuc"));
        e.setGiaThueThoaThuan(rs.getBigDecimal("GiaThueThoaThuan"));
        e.setTienCoc(rs.getBigDecimal("TienCoc"));
        e.setTrangThai(trangThaiTuCSDL(rs.getString("TrangThai")));
        return e;
    }
    private String giaTriTrangThai(TrangThaiHopDong trangThai) {
        return switch (trangThai) {
            case HIEU_LUC -> "HieuLuc";
            case DA_DONG -> "DaDong";
        };
    }

    private TrangThaiHopDong trangThaiTuCSDL(String value) throws SQLException {
        return switch (value) {
            case "HieuLuc" -> TrangThaiHopDong.HIEU_LUC;
            case "DaDong" -> TrangThaiHopDong.DA_DONG;
            default -> throw new SQLException("TrangThai HOPDONG không hợp lệ trong CSDL: " + value);
        };
    }

}
