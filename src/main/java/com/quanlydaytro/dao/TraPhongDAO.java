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
        if (maHopDong <= 0) throw new SQLException("Mã hợp đồng không hợp lệ.");
        if (tienKhauTruCoc == null || tienKhauTruCoc.signum() < 0)
            throw new SQLException("Tiền khấu trừ cọc không hợp lệ.");

        // Kiểm tra khoản khấu trừ theo tiền cọc trước khi gọi transaction phía SQL Server.
        BigDecimal tienCoc;
        try (Connection c = moKetNoi();
             PreparedStatement ps = c.prepareStatement("SELECT TienCoc FROM HOPDONG WHERE MaHopDong=? AND TrangThai=N'HieuLuc'")) {
            ps.setInt(1, maHopDong);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("Hợp đồng không tồn tại hoặc đã kết thúc.");
                tienCoc = rs.getBigDecimal("TienCoc");
            }
        }
        if (tienKhauTruCoc.compareTo(tienCoc) > 0)
            throw new SQLException("Tiền khấu trừ không được lớn hơn tiền cọc.");

        // TX_TraPhong tự kiểm tra công nợ, khóa hợp đồng và cập nhật HOPDONG/PHONG trong cùng transaction.
        String sql = "{call dbo.TX_TraPhong(?)}";
        try (Connection c = moKetNoi(); CallableStatement cs = c.prepareCall(sql)) {
            cs.setInt(1, maHopDong);
            boolean hasResult = cs.execute();
            boolean confirmed = false;
            while (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs != null && rs.next() && hasColumn(rs, "MaHopDong")) confirmed = true;
                }
                hasResult = cs.getMoreResults();
            }
            if (!confirmed) throw new SQLException("Transaction trả phòng không xác nhận kết quả.");
        } catch (SQLException ex) {
            throw new SQLException("Không thể quyết toán trả phòng: " + ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), ex);
        }
        // Lưu ý: schema hiện tại chưa có bảng lưu chi tiết khấu trừ/hoàn cọc; ghiChu và khoản khấu trừ
        // chỉ được kiểm tra ở tầng nghiệp vụ, không được ghi thành một bút toán riêng.
    }

    private boolean hasColumn(ResultSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
