--Chủ trọ
INSERT INTO CHUTRO (HoTen, SoDienThoai)
VALUES
(N'Nguyễn Văn An', '0901000001'),
(N'Trần Thị Bình', '0901000002');
GO

--Dãy trọ
INSERT INTO DAYTRO (TenDay, DiaChi, MaChuTro)
VALUES
(N'Dãy A', N'123 Nguyễn Trãi, Quận 5, TP.HCM', 1),
(N'Dãy B', N'456 Lê Văn Sỹ, Quận 3, TP.HCM', 1),
(N'Dãy C', N'789 Phan Văn Trị, Bình Thạnh, TP.HCM', 2);
GO

--Phòng
INSERT INTO PHONG (TenPhong, GiaThueHienTai, SoNguoiO, TrangThai, MaDay)
VALUES
('A01', 3500000, 2, N'DangThue', 1),
('A02', 3200000, 0, N'Trong', 1),
('A03', 3000000, 0, N'DangSua', 1),

('B01', 4000000, 2, N'DangThue', 2),
('B02', 3800000, 0, N'Trong', 2),

('C01', 4500000, 1, N'DangThue', 3),
('C02', 4200000, 0, N'Trong', 3);
GO

--Người thuê
INSERT INTO NGUOITHUE (HoTen, CCCD, SoDienThoai, DiaChiThuongTru)
VALUES
(N'Lê Hoàng Nam', '079201000001', '0911111111',
 N'TP. Hồ Chí Minh'),

(N'Phạm Minh Tuấn', '079201000002', '0922222222',
 N'Đồng Nai'),

(N'Nguyễn Thị Lan', '079201000003', '0933333333',
 N'Bình Dương'),

(N'Trần Văn Hùng', '079201000004', '0944444444',
 N'Long An');
GO

--Bảng giá điện/nước
INSERT INTO BANGGIADIEN (DonGia, NgayApDung)
VALUES
(2500, '2026-01-01'),
(3000, '2026-07-01');
GO

INSERT INTO BANGGIANUOC (DonGia, NgayApDung)
VALUES
(15000, '2026-01-01'),
(18000, '2026-07-01');
GO

--Hợp đồng
INSERT INTO HOPDONG (MaPhong, MaNT, NgayBatDau, NgayKetThuc, GiaThueThoaThuan, TienCoc, TrangThai)
VALUES
(1, 1, '2026-01-01', '2026-12-31',
 3500000, 3500000, N'HieuLuc'),

(4, 2, '2026-03-01', '2027-02-28',
 4000000, 4000000, N'HieuLuc'),

(6, 3, '2026-05-01', '2027-04-30',
 4500000, 4500000, N'HieuLuc');
GO

--Chỉ số điện
INSERT INTO CHISODIEN (MaPhong, ThangNam, ChiSoCu, ChiSoMoi, NgayGhi)
VALUES
(1, '08/2026', 1000, 1120, '2026-08-30'),
(1, '09/2026', 1120, 1250, '2026-09-30'),

(4, '08/2026', 2000, 2130, '2026-08-30'),
(4, '09/2026', 2130, 2270, '2026-09-30'),

(6, '08/2026', 3000, 3080, '2026-08-30'),
(6, '09/2026', 3080, 3160, '2026-09-30');
GO

--Chỉ số nước
INSERT INTO CHISONUOC (MaPhong, ThangNam, ChiSoCu, ChiSoMoi, NgayGhi)
VALUES
(1, '08/2026', 100, 108, '2026-08-30'),
(1, '09/2026', 108, 117, '2026-09-30'),

(4, '08/2026', 200, 210, '2026-08-30'),
(4, '09/2026', 210, 221, '2026-09-30'),

(6, '08/2026', 300, 307, '2026-08-30'),
(6, '09/2026', 307, 315, '2026-09-30');
GO

--Hóa đơn
INSERT INTO HOADON (MaHopDong, ThangNam, TienPhong, TienDien, TienNuoc, TongTien, HanThanhToan, TrangThai)
VALUES
(
    1,
    '09/2026',
    3500000,
    390000,
    162000,
    4052000,
    '2026-10-05',
    N'DangThanhToan'
),

(
    2,
    '09/2026',
    4000000,
    420000,
    198000,
    4618000,
    '2026-10-05',
    N'HoanTat'
),

(
    3,
    '09/2026',
    4500000,
    240000,
    144000,
    4884000,
    '2026-10-05',
    N'ChuaThanhToan'
);
GO

-- Lich su thanh toan mau
-- Hoa don 1: thanh toan mot phan -> DangThanhToan (2.000.000 / 4.052.000)
-- Hoa don 2: thanh toan 2 dot, tong bang hoa don -> HoanTat (4.618.000 / 4.618.000)
-- Hoa don 3: chua thanh toan -> ChuaThanhToan
-- Neu trigger da duoc tao truoc khi chay file nay, trigger se tu dong dong bo TrangThai.
INSERT INTO THANHTOAN (MaHoaDon, NgayThanhToan, SoTienTra, GhiChu)
VALUES
(1, '2026-10-01T09:15:00', 1200000, N'Tien mat - dot 1'),
(1, '2026-10-02T18:30:00', 800000, N'Chuyen khoan - dot 2'),
(2, '2026-10-01T10:00:00', 2000000, N'Chuyen khoan - dot 1'),
(2, '2026-10-03T14:20:00', 2618000, N'Tien mat - dot 2');
GO

--Nhân viên
INSERT INTO NHANVIEN (HoTen, SoDienThoai, ChucVu)
VALUES
(N'Nguyễn Văn Quản', '0905000001', N'Quản lý'),
(N'Lê Minh Staff', '0905000002', N'Nhân viên'),
(N'Trần Quốc Staff', '0905000003', N'Nhân viên'),
(N'Phạm Báo Cáo', '0905000004', N'Báo cáo');
GO

--Phân công
INSERT INTO NHANVIEN_DAYTRO (MaNV, MaDay)
VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 2),
(4, 3);
GO

--Vai trò
INSERT INTO VAITRO (TenVaiTro)
VALUES
(N'Admin'),
(N'Manager'),
(N'Staff'),
(N'Report');
GO

--Tài khoản
INSERT INTO TAIKHOAN
(TenDangNhap,MatKhau,MaNV,MaVaiTro)
VALUES
('admin', 'PBKDF2$210000$FuIcJIITP48mmoLktVPPmg$HUYf0/Iw46n7yiDOLG+L6tJk0UpdwNV2XlsduefICRs', 1, 1),
('manager', 'PBKDF2$210000$DV/oKVitCoPFWFOPxTCl/A$cO/cCFEJSw6YDzWA4Mfgs/WtsPxnAwJRNwjCVyWErHM', 2, 2),
('staff1', 'PBKDF2$210000$4dkIa6WZy2+kQ9KsyzyBvw$YUpKC/+8Gz2L/bhKgdZgqejXkBVsCE7rR9s+3YVEKic', 3, 3),
('report', 'PBKDF2$210000$xc6E2rIGD8AjtAyHDf60YA$A7rcq0gRIW39VJkAzOHdvqdy9vtA25gaMu9vmJ5hUT0', 4, 4);
GO