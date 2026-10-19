/*
    TEST-INDEX-PERFORMANCE.SQL - Minh chứng kiểm thử Index
    Chạy sau schema.sql, views.sql và indexes.sql trong database QL_NHA_TRO.

    Cách làm để có minh chứng trung thực:
    1. Dùng dữ liệu kiểm thử đủ lớn (vài nghìn dòng trở lên sẽ hữu ích hơn dữ liệu mẫu ít dòng).
    2. Bật Include Actual Execution Plan trong SSMS (Ctrl+M).
    3. Chạy từng truy vấn với STATISTICS IO/TIME bật.
    4. Lưu ảnh execution plan và Messages làm minh chứng báo cáo.
    5. So sánh logical reads, CPU time, elapsed time và loại operator.
       Với dữ liệu nhỏ, SQL Server có thể chọn scan; đó không tự động có nghĩa index sai.

    Không có số liệu hiệu năng giả lập trong file này. Hãy ghi lại kết quả đo từ máy nhóm.
*/
SET NOCOUNT ON;
SET STATISTICS IO ON;
SET STATISTICS TIME ON;
GO

PRINT N'TEST 1: Tìm người thuê theo họ tên (IX_NGUOITHUE_HoTen).';
SELECT MaNT, HoTen, CCCD, SoDienThoai
FROM dbo.NGUOITHUE
WHERE HoTen LIKE N'Nguyễn%';
GO

PRINT N'TEST 2: Tra cứu hợp đồng theo phòng và trạng thái (IX_HOPDONG_MaPhong_TrangThai).';
DECLARE @MaPhongTest INT;
SELECT TOP (1) @MaPhongTest = MaPhong FROM dbo.PHONG ORDER BY MaPhong;

SELECT MaHopDong, MaPhong, MaNT, TrangThai, NgayBatDau, NgayKetThuc, GiaThueThoaThuan
FROM dbo.HOPDONG
WHERE MaPhong = @MaPhongTest
  AND TrangThai = N'HieuLuc';
GO

PRINT N'TEST 3: Tính tổng thanh toán theo hóa đơn (IX_THANHTOAN_MaHoaDon).';
DECLARE @MaHoaDonTest INT;
SELECT TOP (1) @MaHoaDonTest = MaHoaDon FROM dbo.HOADON ORDER BY MaHoaDon;

SELECT MaHoaDon, SUM(SoTienTra) AS TongDaThanhToan, MAX(NgayThanhToan) AS LanThanhToanGanNhat
FROM dbo.THANHTOAN
WHERE MaHoaDon = @MaHoaDonTest
GROUP BY MaHoaDon;
GO

PRINT N'TEST 4: Hợp đồng hiệu lực sắp hết hạn (IX_HOPDONG_TrangThai_NgayKetThuc).';
SELECT MaHopDong, MaPhong, MaNT, NgayKetThuc
FROM dbo.HOPDONG
WHERE TrangThai = N'HieuLuc'
  AND NgayKetThuc >= CAST(GETDATE() AS DATE)
  AND NgayKetThuc < DATEADD(DAY, 31, CAST(GETDATE() AS DATE));
GO

PRINT N'TEST 5: Danh sách phòng theo dãy và trạng thái (IX_PHONG_MaDay_TrangThai).';
DECLARE @MaDayTest INT;
SELECT TOP (1) @MaDayTest = MaDay FROM dbo.DAYTRO ORDER BY MaDay;

SELECT MaPhong, TenPhong, GiaThueHienTai, SoNguoiO, TrangThai
FROM dbo.PHONG
WHERE MaDay = @MaDayTest
  AND TrangThai = N'Trong';
GO

SET STATISTICS IO OFF;
SET STATISTICS TIME OFF;
GO

/*
    Mẫu bảng điền kết quả vào báo cáo (không điền số liệu nếu chưa đo):
    | Truy vấn | Trước index: logical reads / CPU / elapsed | Sau index: logical reads / CPU / elapsed | Kết luận |
    |----------|--------------------------------------------|------------------------------------------|----------|
    | Tìm người thuê | ... | ... | ... |
    | Hợp đồng theo phòng | ... | ... | ... |
    | Thanh toán theo hóa đơn | ... | ... | ... |
    | Hợp đồng sắp hết hạn | ... | ... | ... |
    | Phòng theo dãy/trạng thái | ... | ... | ... |
*/
