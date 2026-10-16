--SQL TESTING - HỆ THỐNG QUẢN LÝ DÃY TRỌ


/* =========================================================
   I. TEST VIEW
   ========================================================= */

-- TC01: Danh sách phòng
SELECT *
FROM VW_DanhSachPhong;
GO

-- TC02: Phòng đang thuê
SELECT *
FROM VW_PhongDangThue;
GO

-- TC03: Phòng trống
SELECT *
FROM VW_PhongTrong;
GO

-- TC04: Danh sách hóa đơn
SELECT *
FROM VW_HoaDon;
GO

-- TC05: Hóa đơn chưa thanh toán
SELECT *
FROM VW_HoaDonChuaThanhToan;
GO

SELECT
    MaHoaDon,
    TongTien,
    DaThanhToan,
    ConLai
FROM VW_HoaDonChuaThanhToan
WHERE ConLai > 0;
GO

-- TC06: Doanh thu
SELECT *
FROM VW_DoanhThu
ORDER BY TongDoanhThu DESC;
GO

-- TC07: Hợp đồng sắp hết hạn
SELECT *
FROM VW_HopDongSapHetHan
ORDER BY SoNgayConLai;
GO


/* =========================================================
   II. TEST FUNCTION
   ========================================================= */

-- TC08: Tính tiền điện
SELECT
    dbo.FN_TinhTienDien(1, '09/2026') AS TienDien;
GO

SELECT
    MaPhong,
    ThangNam,
    ChiSoCu,
    ChiSoMoi,
    dbo.FN_TinhTienDien(MaPhong, ThangNam) AS TienDien
FROM CHISODIEN;
GO


-- TC09: Tính tiền nước
SELECT
    dbo.FN_TinhTienNuoc(1, '09/2026') AS TienNuoc;
GO

SELECT
    MaPhong,
    ThangNam,
    ChiSoCu,
    ChiSoMoi,
    dbo.FN_TinhTienNuoc(MaPhong, ThangNam) AS TienNuoc
FROM CHISONUOC;
GO


-- TC10: Tính tổng tiền hóa đơn
SELECT
    HD.MaHoaDon,
    H.MaHopDong,
    HD.TongTien,
    dbo.FN_TinhTienHoaDon(
        H.MaHopDong,
        H.MaPhong,
        HD.ThangNam
    ) AS TongTienTinhLai
FROM HOADON HD INNER JOIN HOPDONG H ON HD.MaHopDong = H.MaHopDong;
GO

-- Kiểm tra chênh lệch
SELECT
    HD.MaHoaDon,
    HD.TongTien,
    dbo.FN_TinhTienHoaDon(
        H.MaHopDong,
        H.MaPhong,
        HD.ThangNam
    ) AS TongTienTinhLai,
    HD.TongTien -
    dbo.FN_TinhTienHoaDon(
        H.MaHopDong,
        H.MaPhong,
        HD.ThangNam
    ) AS ChenhLech
FROM HOADON HD INNER JOIN HOPDONG H ON HD.MaHopDong = H.MaHopDong;
GO


-- TC11: Tổng tiền đã thanh toán
SELECT
    MaHoaDon,
    TongTien,
    dbo.FN_TongTienDaThanhToan(MaHoaDon) AS DaThanhToan
FROM HOADON;
GO


-- TC12: Tiền còn lại
SELECT
    MaHoaDon,
    TongTien,
    dbo.FN_TongTienDaThanhToan(MaHoaDon) AS DaThanhToan,
    dbo.FN_TinhTienConLai(MaHoaDon) AS ConLai
FROM HOADON;
GO


/* =========================================================
   III. TEST STORED PROCEDURE
   ========================================================= */

-- TC13: Tìm phòng trống
EXEC SP_TimPhongTrong;
GO

-- Tìm theo dãy
EXEC SP_TimPhongTrong
    @MaDay = 1;
GO

-- Tìm theo tên phòng
EXEC SP_TimPhongTrong
    @TenPhong = 'P101';
GO


-- TC14: Tạo hợp đồng thành công
-- Chỉ chạy khi MaPhong = 1 đang ở trạng thái 'Trong'
EXEC SP_TaoHopDong
    @MaPhong = 1,
    @MaNT = 1,
    @NgayBatDau = '2026-10-06',
    @NgayKetThuc = '2027-10-06',
    @GiaThueThoaThuan = 3000000,
    @TienCoc = 3000000;
GO

SELECT *
FROM HOPDONG
WHERE MaPhong = 1;
GO

SELECT
    MaPhong,
    TrangThai,
    SoNguoiO
FROM PHONG
WHERE MaPhong = 1;
GO


-- TC15: Tạo hợp đồng với phòng đã thuê
EXEC SP_TaoHopDong
    @MaPhong = 1,
    @MaNT = 2,
    @NgayBatDau = '2026-10-06',
    @NgayKetThuc = '2027-10-06',
    @GiaThueThoaThuan = 3000000,
    @TienCoc = 3000000;
GO


-- TC16: Tiền cọc không hợp lệ
EXEC SP_TaoHopDong
    @MaPhong = 2,
    @MaNT = 1,
    @NgayBatDau = '2026-10-06',
    @NgayKetThuc = '2027-10-06',
    @GiaThueThoaThuan = 3000000,
    @TienCoc = 1000000;
GO


/* =========================================================
   IV. TEST LẬP HÓA ĐƠN
   ========================================================= */

-- TC17: Lập hóa đơn
EXEC SP_LapHoaDon
    @MaHopDong = 1,
    @ThangNam = '09/2026',
    @HanThanhToan = '2026-10-15';
GO

SELECT *
FROM HOADON
WHERE MaHopDong = 1 AND ThangNam = '09/2026';
GO


-- TC18: Lập hóa đơn trùng
EXEC SP_LapHoaDon
    @MaHopDong = 1,
    @ThangNam = '09/2026',
    @HanThanhToan = '2026-10-15';
GO


/* =========================================================
   V. TEST THANH TOÁN
   ========================================================= */

-- TC19: Thanh toán một phần
EXEC SP_ThanhToanHoaDon
    @MaHoaDon = 1,
    @SoTienTra = 1000000,
    @GhiChu = N'Thanh toán lần 1';
GO

SELECT *
FROM HOADON
WHERE MaHoaDon = 1;
GO

SELECT *
FROM THANHTOAN
WHERE MaHoaDon = 1;
GO


-- TC20: Thanh toán vượt quá số tiền còn lại
EXEC SP_ThanhToanHoaDon
    @MaHoaDon = 1,
    @SoTienTra = 999999999,
    @GhiChu = N'Test thanh toán vượt';
GO


/* =========================================================
   VI. TEST TRANSACTION + ROLLBACK
   ========================================================= */

-- TC21: Rollback khi tạo hợp đồng lỗi
EXEC TX_TaoHopDong
    @MaPhong = 3,
    @MaNT = 1,
    @NgayBatDau = '2026-10-06',
    @NgayKetThuc = '2027-10-06',
    @GiaThueThoaThuan = 3000000,
    @TienCoc = 1000000;
GO

-- Kiểm tra phòng
SELECT
    MaPhong,
    TrangThai,
    SoNguoiO
FROM PHONG
WHERE MaPhong = 3;
GO

-- Kiểm tra hợp đồng
SELECT *
FROM HOPDONG
WHERE MaPhong = 3 AND NgayBatDau = '2026-10-06';
GO


-- TC22: Transaction thanh toán
EXEC TX_ThanhToanHoaDon
    @MaHoaDon = 1,
    @SoTienTra = 500000,
    @GhiChu = N'Test TX thanh toán';
GO

SELECT
    HD.MaHoaDon,
    HD.TongTien,
    HD.TrangThai,
    dbo.FN_TongTienDaThanhToan(HD.MaHoaDon) AS DaThanhToan,
    dbo.FN_TinhTienConLai(HD.MaHoaDon) AS ConLai
FROM HOADON HD
WHERE HD.MaHoaDon = 1;
GO


/* =========================================================
   VII. TEST CHUYỂN PHÒNG
   ========================================================= */

-- TC23: Chuyển phòng thành công
-- MaPhongMoi phải là phòng đang trống

EXEC TX_ChuyenPhong
    @MaHopDongCu = 1,
    @MaPhongMoi = 2,
    @NgayBatDauMoi = '2026-10-06',
    @NgayKetThucMoi = '2027-10-06',
    @GiaThueMoi = 3000000,
    @TienCocMoi = 3000000;
GO

SELECT
    MaHopDong,
    MaPhong,
    MaNT,
    TrangThai
FROM HOPDONG
WHERE MaNT = 1
ORDER BY MaHopDong;
GO

SELECT
    MaPhong,
    TrangThai,
    SoNguoiO
FROM PHONG
WHERE MaPhong IN (1, 2);
GO


-- TC24: Chuyển sang phòng đã thuê -> ROLLBACK
EXEC TX_ChuyenPhong
    @MaHopDongCu = 1,
    @MaPhongMoi = 5,
    @NgayBatDauMoi = '2026-10-06',
    @NgayKetThucMoi = '2027-10-06',
    @GiaThueMoi = 3000000,
    @TienCocMoi = 3000000;
GO

SELECT
    MaHopDong,
    MaPhong,
    TrangThai
FROM HOPDONG
WHERE MaHopDong = 1;
GO


/* =========================================================
   VIII. TEST CONCURRENCY
   =========================================================

   QUAN TRỌNG: Phần này phải chạy bằng 2 cửa sổ SSMS khác nhau.

   SESSION A:
   */

-- SESSION A
EXEC TX_ThuePhong_Concurrency
    @MaPhong = 6,
    @MaNT = 1,
    @NgayBatDau = '2026-10-06',
    @NgayKetThuc = '2027-10-06',
    @GiaThueThoaThuan = 3000000,
    @TienCoc = 3000000,
    @DelaySeconds = 10;
GO


/*
   SESSION B:
   Chạy ngay sau khi chạy SESSION A
   */

-- SESSION B
EXEC TX_ThuePhong_Concurrency
    @MaPhong = 6,
    @MaNT = 2,
    @NgayBatDau = '2026-10-06',
    @NgayKetThuc = '2027-10-06',
    @GiaThueThoaThuan = 3000000,
    @TienCoc = 3000000,
    @DelaySeconds = 0;
GO


-- Kiểm tra kết quả concurrency
SELECT
    MaPhong,
    MaNT,
    TrangThai
FROM HOPDONG
WHERE MaPhong = 6;
GO

SELECT
    MaPhong,
    TrangThai,
    SoNguoiO
FROM PHONG
WHERE MaPhong = 6;
GO


/* =========================================================
   IX. TEST PERFORMANCE / OPTIMIZATION
   ========================================================= */

-- PERF01: Tìm hợp đồng theo phòng + trạng thái

SET STATISTICS IO ON;
SET STATISTICS TIME ON;

SELECT
    MaHopDong,
    MaPhong,
    MaNT,
    NgayBatDau,
    NgayKetThuc,
    GiaThueThoaThuan
FROM HOPDONG
WHERE MaPhong = 1 AND TrangThai = N'HieuLuc';

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
GO


-- PERF02: Tìm hóa đơn theo hợp đồng + tháng

SET STATISTICS IO ON;
SET STATISTICS TIME ON;

SELECT
    MaHoaDon,
    MaHopDong,
    ThangNam,
    TongTien,
    HanThanhToan,
    TrangThai
FROM HOADON
WHERE MaHopDong = 1 AND ThangNam = '09/2026';

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
GO


-- PERF03: Tìm phòng theo dãy + tên phòng

SET STATISTICS IO ON;
SET STATISTICS TIME ON;

SELECT
    MaPhong,
    TenPhong,
    MaDay,
    GiaThueHienTai,
    TrangThai
FROM PHONG
WHERE MaDay = 1
  AND TenPhong = 'P101';

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
GO


/* =========================================================
   X. KIỂM TRA INDEX
   ========================================================= */

SELECT
    OBJECT_NAME(i.object_id) AS TenBang,
    i.name AS TenIndex,
    i.type_desc AS LoaiIndex,
    i.is_unique AS IsUnique,
    i.is_primary_key AS IsPrimaryKey
FROM sys.indexes i
WHERE i.name LIKE 'IX_%'
ORDER BY
    TenBang,
    TenIndex;
GO


/* =====================
   XI. KIỂM TRA TOÀN BỘ
   ===================== */

-- Kiểm tra View
SELECT
    name AS TenView
FROM sys.views
WHERE name LIKE 'VW_%'
ORDER BY name;
GO


-- Kiểm tra Function
SELECT
    name AS TenFunction
FROM sys.objects
WHERE type IN ('FN', 'IF', 'TF') AND name LIKE 'FN_%'
ORDER BY name;
GO


-- Kiểm tra Stored Procedure
SELECT
    name AS TenProcedure
FROM sys.procedures
WHERE name LIKE 'SP_%' OR name LIKE 'TX_%'
ORDER BY name;
GO


-- Kiểm tra Index
SELECT
    OBJECT_NAME(object_id) AS TenBang,
    name AS TenIndex,
    type_desc AS LoaiIndex
FROM sys.indexes
WHERE name LIKE 'IX_%'
ORDER BY
    TenBang,
    TenIndex;
GO


/* =========================================================
   XII. KIỂM TRA DỮ LIỆU SAU TOÀN BỘ TEST
   ========================================================= */

SELECT *
FROM PHONG
ORDER BY MaPhong;
GO

SELECT *
FROM HOPDONG
ORDER BY MaHopDong;
GO

SELECT *
FROM HOADON
ORDER BY MaHoaDon;
GO

SELECT *
FROM THANHTOAN
ORDER BY MaThanhToan;
GO
