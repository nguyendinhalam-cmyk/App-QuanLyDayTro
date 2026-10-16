--SP

---SP 1 - Tạo hợp đồng
IF OBJECT_ID('dbo.SP_TaoHopDong', 'P') IS NOT NULL
    DROP PROCEDURE dbo.SP_TaoHopDong;
GO

CREATE PROCEDURE dbo.SP_TaoHopDong
    @MaPhong INT,
    @MaNT INT,
    @NgayBatDau DATE,
    @NgayKetThuc DATE,
    @GiaThueThoaThuan DECIMAL(12,2),
    @TienCoc DECIMAL(12,2)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- 1. Kiểm tra phòng tồn tại
        IF NOT EXISTS (SELECT 1 FROM PHONG WHERE MaPhong = @MaPhong)
        BEGIN
            THROW 50001, N'Phòng không tồn tại.', 1;
        END;

        -- 2. Kiểm tra trạng thái phòng
        IF EXISTS (SELECT 1 FROM PHONG WHERE MaPhong = @MaPhong AND TrangThai <> N'Trong')
        BEGIN
            THROW 50002, N'Phòng hiện không ở trạng thái Trống.', 1;
        END;

        -- 3. Kiểm tra người thuê
        IF NOT EXISTS (SELECT 1 FROM NGUOITHUE WHERE MaNT = @MaNT)
        BEGIN
            THROW 50003, N'Người thuê không tồn tại.', 1;
        END;

        -- 4. Kiểm tra ngày thuê
        IF @NgayKetThuc <= @NgayBatDau
        BEGIN
            THROW 50004, N'Ngày kết thúc phải sau ngày bắt đầu.', 1;
        END;

        -- 5. Kiểm tra giá thuê
        IF @GiaThueThoaThuan <= 0
        BEGIN
            THROW 50005, N'Giá thuê phải lớn hơn 0.', 1;
        END;

        -- 6. Kiểm tra tiền cọc
        IF @TienCoc <> @GiaThueThoaThuan
        BEGIN
            THROW 50006, N'Tiền cọc phải bằng đúng một tháng tiền thuê.', 1;
        END;

        -- 7. Thêm hợp đồng mới
        INSERT INTO HOPDONG(MaPhong,MaNT,NgayBatDau,NgayKetThuc,GiaThueThoaThuan,TienCoc,TrangThai)
        VALUES(@MaPhong,@MaNT,@NgayBatDau,@NgayKetThuc,@GiaThueThoaThuan,@TienCoc,N'HieuLuc');

        -- 8. Cập nhật trạng thái phòng
        UPDATE PHONG
        SET TrangThai = N'DangThue',
            SoNguoiO = 1
        WHERE MaPhong = @MaPhong;

        -- Trả về Mã Hợp Đồng vừa tạo
        SELECT SCOPE_IDENTITY() AS MaHopDong;

    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

---Sp 2 - Lập hóa đơn
IF OBJECT_ID('dbo.SP_LapHoaDon', 'P') IS NOT NULL
    DROP PROCEDURE dbo.SP_LapHoaDon;
GO

CREATE PROCEDURE dbo.SP_LapHoaDon
    @MaHopDong INT,
    @ThangNam VARCHAR(7),
    @HanThanhToan DATE
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        DECLARE @MaPhong INT;
        DECLARE @TienPhong DECIMAL(12,2);
        DECLARE @TienDien DECIMAL(12,2);
        DECLARE @TienNuoc DECIMAL(12,2);
        DECLARE @TongTien DECIMAL(12,2);

        -- 1. Lấy thông tin hợp đồng
        SELECT
            @MaPhong = MaPhong,
            @TienPhong = GiaThueThoaThuan
        FROM HOPDONG
        WHERE MaHopDong = @MaHopDong AND TrangThai = N'HieuLuc';

        -- 2. Kiểm tra tính hợp lệ của hợp đồng
        IF @MaPhong IS NULL
        BEGIN
            THROW 50001, N'Hợp đồng không tồn tại hoặc không còn hiệu lực.', 1;
        END;

        -- 3. Kiểm tra xem hóa đơn tháng này đã lập chưa
        IF EXISTS (SELECT 1 FROM HOADON WHERE MaHopDong = @MaHopDong AND ThangNam = @ThangNam)
        BEGIN
            THROW 50002, N'Hóa đơn tháng này đã tồn tại.', 1;
        END;

        -- 4. Tính toán tiền điện, tiền nước từ hàm Scalar Function
        SET @TienDien = ISNULL(dbo.FN_TinhTienDien(@MaPhong, @ThangNam), 0);
        SET @TienNuoc = ISNULL(dbo.FN_TinhTienNuoc(@MaPhong, @ThangNam), 0);

        -- 5. Tính tổng tiền
        SET @TongTien = @TienPhong + @TienDien + @TienNuoc;

        -- 6. Lập hóa đơn mới
        INSERT INTO HOADON(MaHopDong,ThangNam,TienPhong,TienDien,TienNuoc,TongTien,HanThanhToan,TrangThai)
        VALUES(@MaHopDong,@ThangNam,@TienPhong,@TienDien,@TienNuoc,@TongTien,@HanThanhToan,N'ChuaThanhToan');

        -- Trả về thông tin hóa đơn vừa tạo
        SELECT SCOPE_IDENTITY() AS MaHoaDon,
               @TongTien AS TongTien;

    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

---Sp 3 - Thanh toán hóa đơn
IF OBJECT_ID('dbo.SP_ThanhToanHoaDon', 'P') IS NOT NULL
    DROP PROCEDURE dbo.SP_ThanhToanHoaDon;
GO

CREATE OR ALTER PROCEDURE dbo.SP_ThanhToanHoaDon
    @MaHoaDon INT,
    @SoTienTra DECIMAL(12,2),
    @GhiChu NVARCHAR(200) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        DECLARE @TongTien DECIMAL(12,2), @DaThanhToan DECIMAL(12,2), @TongMoi DECIMAL(12,2);

        SELECT @TongTien = TongTien
        FROM HOADON WITH (UPDLOCK, HOLDLOCK)
        WHERE MaHoaDon = @MaHoaDon;
        IF @TongTien IS NULL THROW 50001, N'Hóa đơn không tồn tại.', 1;
        IF @SoTienTra IS NULL OR @SoTienTra <= 0 THROW 50002, N'Số tiền thanh toán phải lớn hơn 0.', 1;

        SELECT @DaThanhToan = ISNULL(SUM(SoTienTra), 0)
        FROM THANHTOAN WHERE MaHoaDon = @MaHoaDon;
        SET @TongMoi = @DaThanhToan + @SoTienTra;
        IF @TongMoi > @TongTien THROW 50003, N'Số tiền thanh toán vượt quá số tiền còn lại của hóa đơn.', 1;

        INSERT INTO THANHTOAN(MaHoaDon, NgayThanhToan, SoTienTra, GhiChu)
        VALUES(@MaHoaDon, GETDATE(), @SoTienTra, @GhiChu);
        -- Trigger TRG_THANHTOAN_CapNhatTrangThai tự cập nhật ChuaThanhToan/DangThanhToan/HoanTat.
        COMMIT TRANSACTION;
        SELECT @TongMoi AS DaThanhToan, @TongTien - @TongMoi AS ConLai;
    END TRY
    BEGIN CATCH
        IF XACT_STATE() <> 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO

---Sp 4 - Tìm phòng trống
IF OBJECT_ID('dbo.SP_TimPhongTrong', 'P') IS NOT NULL
    DROP PROCEDURE dbo.SP_TimPhongTrong;
GO

CREATE PROCEDURE dbo.SP_TimPhongTrong
    @MaDay INT = NULL,
    @TenPhong NVARCHAR(50) = NULL
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT
            P.MaPhong,
            P.TenPhong,
            D.MaDay,
            D.TenDay,
            D.DiaChi,
            P.GiaThueHienTai
        FROM PHONG P
        INNER JOIN DAYTRO D ON P.MaDay = D.MaDay
        WHERE P.TrangThai = N'Trong'
          AND (@MaDay IS NULL OR P.MaDay = @MaDay)
          AND (@TenPhong IS NULL OR P.TenPhong LIKE N'%' + @TenPhong + N'%')
        ORDER BY
            D.TenDay,
            P.TenPhong;

    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

---Sp 5 - Thống kê doanh thu
IF OBJECT_ID('dbo.SP_ThongKeDoanhThu', 'P') IS NOT NULL
    DROP PROCEDURE dbo.SP_ThongKeDoanhThu;
GO

CREATE PROCEDURE dbo.SP_ThongKeDoanhThu
    @TuNgay DATE,
    @DenNgay DATE,
    @MaDay INT = NULL
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- 1. Kiểm tra khoảng thời gian hợp lệ
        IF @TuNgay > @DenNgay
        BEGIN
            THROW 50001, N'Từ ngày phải nhỏ hơn hoặc bằng đến ngày.', 1;
        END;

        -- 2. Truy vấn thống kê doanh thu theo dãy trọ
        SELECT
            D.MaDay,
            D.TenDay,
            COUNT(DISTINCT HD.MaHoaDon) AS SoHoaDon,
            ISNULL(SUM(HD.TongTien), 0) AS TongTienHoaDon,
            ISNULL(SUM(TT.TongDaThanhToan), 0) AS TongDoanhThuThucThu
        FROM HOADON HD
        INNER JOIN HOPDONG H ON HD.MaHopDong = H.MaHopDong
        INNER JOIN PHONG P ON H.MaPhong = P.MaPhong
        INNER JOIN DAYTRO D ON P.MaDay = D.MaDay
        LEFT JOIN (SELECT MaHoaDon, SUM(SoTienTra) AS TongDaThanhToan FROM THANHTOAN GROUP BY MaHoaDon) TT ON HD.MaHoaDon = TT.MaHoaDon
        WHERE HD.HanThanhToan BETWEEN @TuNgay AND @DenNgay AND (@MaDay IS NULL OR D.MaDay = @MaDay)
        GROUP BY
            D.MaDay,
            D.TenDay
        ORDER BY
            D.TenDay;

    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO

---Sp 6 - Trả phòng
IF OBJECT_ID('dbo.SP_TraPhong', 'P') IS NOT NULL
    DROP PROCEDURE dbo.SP_TraPhong;
GO

CREATE PROCEDURE dbo.SP_TraPhong
    @MaHopDong INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        DECLARE @MaPhong INT;

        -- 1. Lấy thông tin phòng từ hợp đồng đang có hiệu lực
        SELECT @MaPhong = MaPhong
        FROM HOPDONG
        WHERE MaHopDong = @MaHopDong AND TrangThai = N'HieuLuc';

        -- 2. Kiểm tra tính hợp lệ của hợp đồng
        IF @MaPhong IS NULL
        BEGIN
            THROW 50001, N'Hợp đồng không tồn tại hoặc đã đóng.', 1;
        END;

        -- 3. Đóng hợp đồng
        UPDATE HOPDONG
        SET TrangThai = N'DaDong'
        WHERE MaHopDong = @MaHopDong;

        -- 4. Đưa phòng về trạng thái Trống và reset số người ở
        UPDATE PHONG
        SET TrangThai = N'Trong',SoNguoiO = 0
        WHERE MaPhong = @MaPhong;

        -- 5. Trả về kết quả thông báo
        SELECT
            @MaHopDong AS MaHopDong,
            @MaPhong AS MaPhong,
            N'Trả phòng thành công' AS ThongBao;

    END TRY
    BEGIN CATCH
        THROW;
    END CATCH
END;
GO
