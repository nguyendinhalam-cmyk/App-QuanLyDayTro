/*
    KIEM THU TRANSACTION CHO QL_NHA_TRO
    Chay tren database test/development, khong chay tren du lieu production.
    Script chi tao cac loi co chu dich; cac thu tuc phai rollback va khong de lai thay doi.

    Can co du lieu mau va it nhat:
      - 1 hoa don chua thanh toan het;
      - 1 hop dong dang HieuLuc.
*/
SET NOCOUNT ON;

DECLARE @MaHoaDonTest INT;
DECLARE @MaHopDongTest INT;
DECLARE @MaPhongHienTai INT;
DECLARE @NgayBatDauCu DATE;
DECLARE @NgayChuyenTest DATE;
DECLARE @NgayKetThucTest DATE;
DECLARE @SoTienVuot DECIMAL(12,2);
DECLARE @SoDongThanhToanTruoc INT;
DECLARE @SoDongThanhToanSau INT;
DECLARE @TrangThaiHDTruoc NVARCHAR(20);
DECLARE @TrangThaiHDsau NVARCHAR(20);
DECLARE @ThongBao NVARCHAR(4000);

SELECT TOP (1)
    @MaHoaDonTest = HD.MaHoaDon,
    @SoTienVuot = HD.TongTien + 1,
    @TrangThaiHDTruoc = HD.TrangThai
FROM dbo.HOADON HD
WHERE HD.TongTien > ISNULL((SELECT SUM(TT.SoTienTra) FROM dbo.THANHTOAN TT WHERE TT.MaHoaDon = HD.MaHoaDon), 0)
ORDER BY HD.MaHoaDon;

IF @MaHoaDonTest IS NULL
    THROW 51000, N'Khong co hoa don con no de kiem thu. Hay nap du lieu mau truoc.', 1;

SELECT @SoDongThanhToanTruoc = COUNT(*) FROM dbo.THANHTOAN WHERE MaHoaDon = @MaHoaDonTest;

PRINT N'TEST 1: TX_ThanhToanHoaDon tu choi thanh toan vuot cong no va khong de lai du lieu.';
BEGIN TRY
    EXEC dbo.TX_ThanhToanHoaDon
        @MaHoaDon = @MaHoaDonTest,
        @SoTienTra = @SoTienVuot,
        @GhiChu = N'Kiem thu rollback - thanh toan vuot cong no';
    THROW 51001, N'TEST THAT BAI: thu tuc dang chap nhan thanh toan vuot cong no.', 1;
END TRY
BEGIN CATCH
    IF ERROR_NUMBER() = 51001 THROW;
    SET @ThongBao = ERROR_MESSAGE();
    PRINT N'  Da bat loi mong doi: ' + @ThongBao;
END CATCH;

SELECT @SoDongThanhToanSau = COUNT(*) FROM dbo.THANHTOAN WHERE MaHoaDon = @MaHoaDonTest;
SELECT @TrangThaiHDsau = TrangThai FROM dbo.HOADON WHERE MaHoaDon = @MaHoaDonTest;
IF @SoDongThanhToanSau <> @SoDongThanhToanTruoc OR @TrangThaiHDsau <> @TrangThaiHDTruoc
    THROW 51002, N'TEST THAT BAI: du lieu da thay doi sau khi giao dich thanh toan loi.', 1;
PRINT N'  PASS: khong phat sinh thanh toan va trang thai hoa don khong doi.';

SELECT TOP (1) @MaHopDongTest = MaHopDong, @MaPhongHienTai = MaPhong, @NgayBatDauCu = NgayBatDau
FROM dbo.HOPDONG
WHERE TrangThai = N'HieuLuc'
ORDER BY MaHopDong;

IF @MaHopDongTest IS NULL
    THROW 51003, N'Khong co hop dong HieuLuc de kiem thu TX_ChuyenPhong.', 1;
SET @NgayChuyenTest = DATEADD(DAY, 1, @NgayBatDauCu);
SET @NgayKetThucTest = DATEADD(YEAR, 1, @NgayChuyenTest);

PRINT N'TEST 2: TX_ChuyenPhong tu choi chuyen sang chinh phong hien tai va giu nguyen du lieu.';
BEGIN TRY
    EXEC dbo.TX_ChuyenPhong
        @MaHopDongCu = @MaHopDongTest,
        @MaPhongMoi = @MaPhongHienTai,
        @NgayBatDauMoi = @NgayChuyenTest,
        @NgayKetThucMoi = @NgayKetThucTest,
        @GiaThueMoi = 1000000,
        @TienCocMoi = 1000000;
    THROW 51004, N'TEST THAT BAI: thu tuc cho phep chuyen sang chinh phong hien tai.', 1;
END TRY
BEGIN CATCH
    IF ERROR_NUMBER() = 51004 THROW;
    SET @ThongBao = ERROR_MESSAGE();
    PRINT N'  Da bat loi mong doi: ' + @ThongBao;
END CATCH;

IF NOT EXISTS (SELECT 1 FROM dbo.HOPDONG WHERE MaHopDong = @MaHopDongTest AND TrangThai = N'HieuLuc' AND MaPhong = @MaPhongHienTai)
    THROW 51005, N'TEST THAT BAI: hop dong/phong thay doi sau khi chuyen phong loi.', 1;
PRINT N'  PASS: hop dong va phong giu nguyen sau khi giao dich chuyen phong bi tu choi.';

PRINT N'HOAN TAT: cac kiem thu rollback co chu dich deu PASS.';
PRINT N'Ghi chu: kiem thu tranh chap dong thoi can mo 2 ket noi SSMS va thuc hien cung mot hoa don; xem docs/tich-hop-procedure-function-transaction.md.';
GO
