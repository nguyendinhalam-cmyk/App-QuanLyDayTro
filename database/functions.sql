-- FUNCTION 1: Tinh tien dien theo bang gia co hieu luc trong thang hoa don
CREATE OR ALTER FUNCTION dbo.FN_TinhTienDien (@MaPhong INT, @ThangNam VARCHAR(7))
RETURNS DECIMAL(12,2)
AS
BEGIN
    DECLARE @Ky DATE = TRY_CONVERT(DATE, '01/' + @ThangNam, 103);
    DECLARE @ChiSoCu INT, @ChiSoMoi INT, @DonGia DECIMAL(10,2);
    IF @Ky IS NULL RETURN 0;

    SELECT @ChiSoCu = ChiSoCu, @ChiSoMoi = ChiSoMoi
    FROM dbo.CHISODIEN WHERE MaPhong = @MaPhong AND ThangNam = @ThangNam;

    SELECT TOP (1) @DonGia = DonGia
    FROM dbo.BANGGIADIEN
    WHERE NgayApDung <= EOMONTH(@Ky)
    ORDER BY NgayApDung DESC;

    RETURN CASE WHEN @ChiSoCu IS NULL OR @ChiSoMoi IS NULL OR @DonGia IS NULL
                THEN 0 ELSE CONVERT(DECIMAL(12,2), (@ChiSoMoi - @ChiSoCu) * @DonGia) END;
END;
GO

-- FUNCTION 2: Tinh tien nuoc theo bang gia co hieu luc trong thang hoa don
CREATE OR ALTER FUNCTION dbo.FN_TinhTienNuoc (@MaPhong INT, @ThangNam VARCHAR(7))
RETURNS DECIMAL(12,2)
AS
BEGIN
    DECLARE @Ky DATE = TRY_CONVERT(DATE, '01/' + @ThangNam, 103);
    DECLARE @ChiSoCu INT, @ChiSoMoi INT, @DonGia DECIMAL(10,2);
    IF @Ky IS NULL RETURN 0;

    SELECT @ChiSoCu = ChiSoCu, @ChiSoMoi = ChiSoMoi
    FROM dbo.CHISONUOC WHERE MaPhong = @MaPhong AND ThangNam = @ThangNam;

    SELECT TOP (1) @DonGia = DonGia
    FROM dbo.BANGGIANUOC
    WHERE NgayApDung <= EOMONTH(@Ky)
    ORDER BY NgayApDung DESC;

    RETURN CASE WHEN @ChiSoCu IS NULL OR @ChiSoMoi IS NULL OR @DonGia IS NULL
                THEN 0 ELSE CONVERT(DECIMAL(12,2), (@ChiSoMoi - @ChiSoCu) * @DonGia) END;
END;
GO

---FN 3 - Tính tổng tiền hóa đơn
---Tổng tiền = Tiền phòng + Tiền điện + Tiền nước
CREATE OR ALTER FUNCTION FN_TinhTienHoaDon (@MaHopDong INT,@MaPhong INT,@ThangNam VARCHAR(7))
RETURNS DECIMAL(12,2)
AS
BEGIN
    DECLARE @TienPhong DECIMAL(12,2);
    DECLARE @TienDien DECIMAL(12,2);
    DECLARE @TienNuoc DECIMAL(12,2);

    SELECT
        @TienPhong = GiaThueThoaThuan
    FROM HOPDONG
    WHERE MaHopDong = @MaHopDong;

    SET @TienDien = dbo.FN_TinhTienDien(@MaPhong, @ThangNam);
    SET @TienNuoc = dbo.FN_TinhTienNuoc(@MaPhong, @ThangNam);
    RETURN ISNULL(@TienPhong, 0) + ISNULL(@TienDien, 0) + ISNULL(@TienNuoc, 0);
END;
GO

---FN 4 - Tính tổng tiền đã thanh toán (Tính tổng tất cả các lần khách đã thanh toán cho một hóa đơn)
/* -----------------------------------
    Hóa đơn 4

    Thanh toán lần 1: 2.000.000
    Thanh toán lần 2: 2.022.000

    → Tổng đã thanh toán = 4.022.000
------------------------------------*/
CREATE OR ALTER FUNCTION FN_TongTienDaThanhToan (@MaHoaDon INT)
RETURNS DECIMAL(12,2)
AS
BEGIN
    DECLARE @TongTien DECIMAL(12,2);

    SELECT
        @TongTien = ISNULL(SUM(SoTienTra), 0)
    FROM THANHTOAN
    WHERE MaHoaDon = @MaHoaDon;

    RETURN @TongTien;
END;
GO

---FN 5 - Tính tiền còn lại
---Công thức: Còn lại = Tổng hóa đơn - Tổng đã thanh toán
CREATE OR ALTER FUNCTION FN_TinhTienConLai (@MaHoaDon INT)
RETURNS DECIMAL(12,2)
AS
BEGIN
    DECLARE @TongTien DECIMAL(12,2);
    DECLARE @DaThanhToan DECIMAL(12,2);

    SELECT
        @TongTien = TongTien
    FROM HOADON
    WHERE MaHoaDon = @MaHoaDon;

    SET @DaThanhToan = dbo.FN_TongTienDaThanhToan(@MaHoaDon);
    RETURN ISNULL(@TongTien, 0) - ISNULL(@DaThanhToan, 0);
END;
GO
