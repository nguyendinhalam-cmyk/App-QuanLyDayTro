--VIEW

---VIEW 1 – Danh sách phòng
CREATE OR ALTER VIEW VW_DanhSachPhong
AS
SELECT
    P.MaPhong,
    P.TenPhong,
    D.TenDay,
    D.DiaChi,
    P.GiaThueHienTai,
    P.SoNguoiO,
    P.TrangThai
FROM PHONG P INNER JOIN DAYTRO D ON P.MaDay = D.MaDay;
GO

---VIEW 2 – Phòng đang thuê
CREATE OR ALTER VIEW VW_PhongDangThue
AS
SELECT
    P.MaPhong,
    P.TenPhong,
    D.TenDay,
    NT.MaNT,
    NT.HoTen AS TenNguoiThue,
    NT.CCCD,
    NT.SoDienThoai,
    HD.MaHopDong,
    HD.NgayBatDau,
    HD.NgayKetThuc,
    HD.GiaThueThoaThuan,
    HD.TienCoc
FROM PHONG P
INNER JOIN DAYTRO D ON P.MaDay = D.MaDay
INNER JOIN HOPDONG HD ON P.MaPhong = HD.MaPhong
INNER JOIN NGUOITHUE NT ON HD.MaNT = NT.MaNT
WHERE HD.TrangThai = N'HieuLuc';
GO

---VIEW 3 – Phòng trống
CREATE OR ALTER VIEW VW_PhongTrong
AS
SELECT
    P.MaPhong,
    P.TenPhong,
    D.MaDay,
    D.TenDay,
    D.DiaChi,
    P.GiaThueHienTai
FROM PHONG P INNER JOIN DAYTRO D ON P.MaDay = D.MaDay
WHERE P.TrangThai = N'Trong';
GO

---VIEW 4 – Hóa đơn
CREATE OR ALTER VIEW VW_HoaDon
AS
SELECT
    HD.MaHoaDon,
    HD.MaHopDong,
    HD.ThangNam,
    D.TenDay,
    P.TenPhong,
    NT.HoTen AS TenNguoiThue,
    NT.SoDienThoai,
    HD.TienPhong,
    HD.TienDien,
    HD.TienNuoc,
    HD.TongTien,
    HD.HanThanhToan,
    HD.TrangThai
FROM HOADON HD
INNER JOIN HOPDONG H ON HD.MaHopDong = H.MaHopDong
INNER JOIN PHONG P ON H.MaPhong = P.MaPhong
INNER JOIN DAYTRO D ON P.MaDay = D.MaDay
INNER JOIN NGUOITHUE NT ON H.MaNT = NT.MaNT;
GO

--View 5 - Hóa đơn chưa thanh toán
CREATE OR ALTER VIEW VW_HoaDonChuaThanhToan
AS
SELECT
    HD.MaHoaDon,
    HD.MaHopDong,
    HD.ThangNam,
    D.TenDay,
    P.TenPhong,
    NT.HoTen AS TenNguoiThue,
    HD.TongTien,
    ISNULL(SUM(TT.SoTienTra), 0) AS DaThanhToan,
    HD.TongTien - ISNULL(SUM(TT.SoTienTra), 0) AS ConLai,
    HD.HanThanhToan,
    HD.TrangThai
FROM HOADON HD
INNER JOIN HOPDONG H ON HD.MaHopDong = H.MaHopDong
INNER JOIN PHONG P ON H.MaPhong = P.MaPhong
INNER JOIN DAYTRO D ON P.MaDay = D.MaDay
INNER JOIN NGUOITHUE NT ON H.MaNT = NT.MaNT
LEFT JOIN THANHTOAN TT ON HD.MaHoaDon = TT.MaHoaDon
WHERE HD.TrangThai IN (N'ChuaThanhToan', N'DangThanhToan')
GROUP BY
    HD.MaHoaDon,
    HD.MaHopDong,
    HD.ThangNam,
    D.TenDay,
    P.TenPhong,
    NT.HoTen,
    HD.TongTien,
    HD.HanThanhToan,
    HD.TrangThai;
GO

--View 6 - Doanh thu
CREATE OR ALTER VIEW VW_DoanhThu
AS
SELECT
    D.MaDay,
    D.TenDay,
    COUNT(HD.MaHoaDon) AS SoHoaDon,
    ISNULL(SUM(HD.TongTien), 0) AS TongDoanhThu
FROM DAYTRO D
LEFT JOIN PHONG P ON D.MaDay = P.MaDay
LEFT JOIN HOPDONG H ON P.MaPhong = H.MaPhong
LEFT JOIN HOADON HD ON H.MaHopDong = HD.MaHopDong
GROUP BY
    D.MaDay,
    D.TenDay;
GO

--View 7 - Hợp đồng sắp hết hạn
CREATE OR ALTER VIEW VW_HopDongSapHetHan
AS
SELECT
    HD.MaHopDong,
    P.MaPhong,
    P.TenPhong,
    D.TenDay,
    NT.MaNT,
    NT.HoTen AS TenNguoiThue,
    NT.SoDienThoai,
    HD.NgayBatDau,
    HD.NgayKetThuc,
    DATEDIFF(DAY, CAST(GETDATE() AS DATE), HD.NgayKetThuc)
        AS SoNgayConLai,
    HD.GiaThueThoaThuan,
    HD.TienCoc
FROM HOPDONG HD
INNER JOIN PHONG P ON HD.MaPhong = P.MaPhong
INNER JOIN DAYTRO D ON P.MaDay = D.MaDay
INNER JOIN NGUOITHUE NT ON HD.MaNT = NT.MaNT
WHERE HD.TrangThai = N'HieuLuc'
  AND HD.NgayKetThuc >= CAST(GETDATE() AS DATE)
  AND HD.NgayKetThuc <= DATEADD(DAY, 30, CAST(GETDATE() AS DATE));
GO
