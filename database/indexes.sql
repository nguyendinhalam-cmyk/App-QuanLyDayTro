--INDEX

-- Index (MaDay, TenPhong) da duoc tao tu UNIQUE constraint UQ_PHONG_MaDay_TenPhong.
---Index 2 – Người thuê theo họ tên
CREATE NONCLUSTERED INDEX IX_NGUOITHUE_HoTen
ON NGUOITHUE(HoTen);
GO

---Index 3 – Hợp đồng theo phòng + trạng thái
CREATE NONCLUSTERED INDEX IX_HOPDONG_MaPhong_TrangThai
ON HOPDONG(MaPhong, TrangThai)
INCLUDE (MaNT, NgayBatDau, NgayKetThuc, GiaThueThoaThuan);
GO

-- Index (MaHopDong, ThangNam) da duoc tao tu UNIQUE constraint UQ_HOADON_HOPDONG_THANG.
---Index 5 - Thanh toán theo hóa đơn
CREATE NONCLUSTERED INDEX IX_THANHTOAN_MaHoaDon
ON THANHTOAN(MaHoaDon)
INCLUDE (SoTienTra, NgayThanhToan);
GO

---Index 6 - Hợp đồng sắp hết hạn
CREATE NONCLUSTERED INDEX IX_HOPDONG_TrangThai_NgayKetThuc
ON HOPDONG(TrangThai, NgayKetThuc)
INCLUDE (MaPhong, MaNT, NgayBatDau, GiaThueThoaThuan, TienCoc);
GO

---Index 7 - Lọc phòng theo dãy + trạng thái
CREATE NONCLUSTERED INDEX IX_PHONG_MaDay_TrangThai
ON dbo.PHONG(MaDay, TrangThai)
INCLUDE (TenPhong, GiaThueHienTai, SoNguoiO);
Go