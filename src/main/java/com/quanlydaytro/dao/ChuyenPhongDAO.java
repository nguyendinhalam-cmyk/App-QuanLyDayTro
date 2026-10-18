package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Truy vấn và giao dịch chuyển phòng theo schema QL_NHA_TRO.
 */
public class ChuyenPhongDAO {
    public List<Object[]> danhSachHopDongHieuLuc() throws SQLException {
        String sql = "SELECT h.MaHopDong, h.MaPhong, p.TenPhong, h.MaNT, n.HoTen, h.NgayBatDau, h.NgayKetThuc, h.GiaThueThoaThuan, h.TienCoc " +
                "FROM HOPDONG h JOIN PHONG p ON p.MaPhong=h.MaPhong JOIN NGUOITHUE n ON n.MaNT=h.MaNT " +
                "WHERE h.TrangThai=N'HieuLuc' ORDER BY h.MaHopDong DESC";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                rows.add(new Object[]{rs.getInt("MaHopDong"), rs.getInt("MaPhong"), rs.getString("TenPhong"), rs.getInt("MaNT"), rs.getString("HoTen"), rs.getDate("NgayBatDau").toLocalDate(), rs.getDate("NgayKetThuc").toLocalDate(), rs.getBigDecimal("GiaThueThoaThuan"), rs.getBigDecimal("TienCoc")});
        }
        return rows;
    }

    public List<Object[]> danhSachPhongTrong(int maPhongHienTai) throws SQLException {
        String sql = "SELECT MaPhong, TenPhong, MaDay, GiaThueHienTai FROM PHONG WHERE TrangThai=N'Trong' AND MaPhong<>? ORDER BY MaDay, TenPhong";
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, maPhongHienTai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    rows.add(new Object[]{rs.getInt("MaPhong"), rs.getString("TenPhong"), rs.getInt("MaDay"), rs.getBigDecimal("GiaThueHienTai")});
            }
        }
        return rows;
    }

    public void chuyenPhong(int maHopDong, int maPhongMoi, LocalDate ngayChuyen, BigDecimal giaThueMoi, BigDecimal tienCocMoi) throws SQLException {
        if (ngayChuyen == null || giaThueMoi == null || giaThueMoi.signum() <= 0 || tienCocMoi == null || tienCocMoi.signum() <= 0)
            throw new IllegalArgumentException("Ngày chuyển, giá thuê và tiền cọc phải hợp lệ.");
        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int maPhongCu, maNT;
                LocalDate ngayBatDau;
                BigDecimal cocCu;
                String lock = "SELECT MaPhong, MaNT, NgayBatDau, TienCoc FROM HOPDONG WITH (UPDLOCK, HOLDLOCK) WHERE MaHopDong=? AND TrangThai=N'HieuLuc'";
                try (PreparedStatement ps = c.prepareStatement(lock)) {
                    ps.setInt(1, maHopDong);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Hợp đồng không tồn tại hoặc đã kết thúc.");
                        maPhongCu = rs.getInt("MaPhong");
                        maNT = rs.getInt("MaNT");
                        ngayBatDau = rs.getDate("NgayBatDau").toLocalDate();
                        cocCu = rs.getBigDecimal("TienCoc");
                    }
                }
                if (maPhongCu == maPhongMoi) throw new SQLException("Phòng mới phải khác phòng hiện tại.");
                if (!ngayChuyen.isAfter(ngayBatDau))
                    throw new SQLException("Ngày chuyển phải sau ngày bắt đầu hợp đồng hiện tại theo ràng buộc cơ sở dữ liệu.");
                try (PreparedStatement ps = c.prepareStatement("SELECT TrangThai FROM PHONG WITH (UPDLOCK,HOLDLOCK) WHERE MaPhong=?")) {
                    ps.setInt(1, maPhongMoi);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || !"Trong".equals(rs.getString(1)))
                            throw new SQLException("Phòng mới không tồn tại hoặc không còn trống.");
                    }
                }
                String debt = "SELECT COUNT(*) FROM HOADON h WHERE h.MaHopDong=? AND h.TrangThai<>N'HoanTat'";
                try (PreparedStatement ps = c.prepareStatement(debt)) {
                    ps.setInt(1, maHopDong);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) > 0)
                            throw new SQLException("Hợp đồng còn hóa đơn chưa thanh toán đủ. Vui lòng quyết toán công nợ trước khi chuyển phòng.");
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE HOPDONG SET NgayKetThuc=?, TrangThai=N'DaDong' WHERE MaHopDong=? AND TrangThai=N'HieuLuc'")) {
                    ps.setDate(1, Date.valueOf(ngayChuyen));
                    ps.setInt(2, maHopDong);
                    if (ps.executeUpdate() != 1) throw new SQLException("Không thể cập nhật hợp đồng cũ.");
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE PHONG SET TrangThai=N'Trong', SoNguoiO=0 WHERE MaPhong=? AND TrangThai=N'DangThue'")) {
                    ps.setInt(1, maPhongCu);
                    if (ps.executeUpdate() != 1)
                        throw new SQLException("Trạng thái phòng cũ không hợp lệ; giao dịch đã hủy.");
                }
                try (PreparedStatement ps = c.prepareStatement("UPDATE PHONG SET TrangThai=N'DangThue', SoNguoiO=1 WHERE MaPhong=? AND TrangThai=N'Trong'")) {
                    ps.setInt(1, maPhongMoi);
                    if (ps.executeUpdate() != 1)
                        throw new SQLException("Phòng mới vừa được người khác đặt; giao dịch đã hủy.");
                }
                String insert = "INSERT INTO HOPDONG(MaPhong,MaNT,NgayBatDau,NgayKetThuc,GiaThueThoaThuan,TienCoc,TrangThai) VALUES(?,?,?,?,?,?,N'HieuLuc')";
                try (PreparedStatement ps = c.prepareStatement(insert)) {
                    ps.setInt(1, maPhongMoi);
                    ps.setInt(2, maNT);
                    ps.setDate(3, Date.valueOf(ngayChuyen));
                    ps.setDate(4, Date.valueOf(ngayChuyen.plusYears(1)));
                    ps.setBigDecimal(5, giaThueMoi);
                    ps.setBigDecimal(6, tienCocMoi);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (Exception ex) {
                try {
                    c.rollback();
                } catch (SQLException rollbackEx) {
                    ex.addSuppressed(rollbackEx);
                }
                if (ex instanceof SQLException se) throw se;
                if (ex instanceof RuntimeException re) throw re;
                throw new SQLException("Không thể chuyển phòng.", ex);
            } finally {
                try {
                    c.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
