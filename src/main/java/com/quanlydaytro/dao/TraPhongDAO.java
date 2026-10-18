package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * DAO cho danh sách hợp đồng đang hiệu lực và quyết toán trả phòng.
 */
public class TraPhongDAO {
    protected Connection moKetNoi() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public List<Object[]> danhSachHopDongHieuLuc() throws SQLException {
        String sql = "SELECT hd.MaHopDong, hd.MaPhong, p.TenPhong, hd.MaNT, nt.HoTen, hd.NgayBatDau, hd.NgayKetThuc, hd.TienCoc, " +
                "COALESCE((SELECT SUM(h.TongTien) FROM HOADON h WHERE h.MaHopDong=hd.MaHopDong),0) TongHoaDon, " +
                "COALESCE((SELECT SUM(t.SoTienTra) FROM HOADON h JOIN THANHTOAN t ON t.MaHoaDon=h.MaHoaDon WHERE h.MaHopDong=hd.MaHopDong),0) DaTra " +
                "FROM HOPDONG hd JOIN PHONG p ON p.MaPhong=hd.MaPhong JOIN NGUOITHUE nt ON nt.MaNT=hd.MaNT " +
                "WHERE hd.TrangThai=N'HieuLuc' ORDER BY hd.MaHopDong DESC";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = moKetNoi(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BigDecimal tong = rs.getBigDecimal("TongHoaDon"), daTra = rs.getBigDecimal("DaTra");
                rows.add(new Object[]{rs.getInt("MaHopDong"), rs.getInt("MaPhong"), rs.getString("TenPhong"), rs.getInt("MaNT"), rs.getString("HoTen"), rs.getDate("NgayBatDau"), rs.getDate("NgayKetThuc"), rs.getBigDecimal("TienCoc"), tong, daTra, tong.subtract(daTra).max(BigDecimal.ZERO)});
            }
        }
        return rows;
    }

    public Object[] thongTinHopDong(int maHopDong) throws SQLException {
        String sql = "SELECT hd.MaHopDong,hd.MaPhong,p.TenPhong,hd.MaNT,nt.HoTen,hd.TienCoc,hd.TrangThai FROM HOPDONG hd JOIN PHONG p ON p.MaPhong=hd.MaPhong JOIN NGUOITHUE nt ON nt.MaNT=hd.MaNT WHERE hd.MaHopDong=?";
        try (Connection c = moKetNoi(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maHopDong);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Không tìm thấy hợp đồng.");
                return new Object[]{rs.getInt("MaHopDong"), rs.getInt("MaPhong"), rs.getString("TenPhong"), rs.getInt("MaNT"), rs.getString("HoTen"), rs.getBigDecimal("TienCoc"), rs.getString("TrangThai")};
            }
        }
    }

    /**
     * Chốt trả phòng trong một transaction; không tự xóa hóa đơn hay lịch sử thanh toán.
     */
    public void quyetToan(int maHopDong, BigDecimal tienKhauTruCoc, String ghiChu) throws SQLException {
        if (tienKhauTruCoc == null || tienKhauTruCoc.signum() < 0)
            throw new SQLException("Tiền khấu trừ cọc không hợp lệ.");
        try (Connection c = moKetNoi()) {
            c.setAutoCommit(false);
            try {
                int maPhong;
                BigDecimal tienCoc;
                String trangThai;
                try (PreparedStatement ps = c.prepareStatement("SELECT MaPhong,TienCoc,TrangThai FROM HOPDONG WITH (UPDLOCK,ROWLOCK) WHERE MaHopDong=?")) {
                    ps.setInt(1, maHopDong);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Hợp đồng không tồn tại.");
                        maPhong = rs.getInt("MaPhong");
                        tienCoc = rs.getBigDecimal("TienCoc");
                        trangThai = rs.getString("TrangThai");
                    }
                }
                if (!"HieuLuc".equals(trangThai)) throw new SQLException("Hợp đồng này đã kết thúc trước đó.");
                if (tienKhauTruCoc.compareTo(tienCoc) > 0)
                    throw new SQLException("Tiền khấu trừ không được lớn hơn tiền cọc.");
                String sqlNo = "SELECT COALESCE(SUM(h.TongTien),0)-COALESCE((SELECT SUM(t.SoTienTra) FROM HOADON hx JOIN THANHTOAN t ON t.MaHoaDon=hx.MaHoaDon WHERE hx.MaHopDong=?),0) CongNo FROM HOADON h WHERE h.MaHopDong=?";
                BigDecimal congNo;
                try (PreparedStatement ps = c.prepareStatement(sqlNo)) {
                    ps.setInt(1, maHopDong);
                    ps.setInt(2, maHopDong);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        congNo = rs.getBigDecimal("CongNo");
                    }
                }
                if (congNo.signum() > 0)
                    throw new SQLException("Chưa thể trả phòng: hợp đồng còn công nợ " + congNo + " đồng. Hãy xử lý hóa đơn trước.");
                try (PreparedStatement ps = c.prepareStatement("UPDATE HOPDONG SET TrangThai=N'DaDong' WHERE MaHopDong=? AND TrangThai=N'HieuLuc'")) {
                    ps.setInt(1, maHopDong);
                    if (ps.executeUpdate() != 1) throw new SQLException("Không thể cập nhật trạng thái hợp đồng.");
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE PHONG SET TrangThai=N'Trong',SoNguoiO=0 WHERE MaPhong=? AND TrangThai=N'DangThue'")) {
                    ps.setInt(1, maPhong);
                    if (ps.executeUpdate() != 1)
                        throw new SQLException("Phòng không ở trạng thái ĐangThue; đã hủy quyết toán.");
                }
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                if (ex instanceof SQLException) throw (SQLException) ex;
                throw new SQLException("Lỗi quyết toán trả phòng.", ex);
            } finally {
                c.setAutoCommit(true);
            }
        }
    }
}
