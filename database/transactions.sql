--TRANSACTION

---Tx 1 - Tạo hợp đồng + cập nhật trạng thái phòng
CREATE OR ALTER PROCEDURE TX_TaoHopDong
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
        BEGIN TRAN;

        -- 1. Kiểm tra phòng
        IF NOT EXISTS (SELECT 1 FROM PHONG WHERE MaPhong = @MaPhong)
        BEGIN
            THROW 50001, N'Phòng không tồn tại.', 1;
        END;

        -- 2. Kiểm tra trạng thái phòng
        IF EXISTS (SELECT 1 FROM PHONG WHERE MaPhong = @MaPhong AND TrangThai <> N'Trong')
        BEGIN
            THROW 50002, N'Phòng hiện không trống.', 1;
        END;

        -- 3. Kiểm tra người thuê
        IF NOT EXISTS (SELECT 1 FROM NGUOITHUE WHERE MaNT = @MaNT)
        BEGIN
            THROW 50003, N'Người thuê không tồn tại.', 1;
        END;

        -- 4. Kiểm tra ngày
        IF @NgayKetThuc <= @NgayBatDau
        BEGIN
            THROW 50004, N'Ngày kết thúc phải sau ngày bắt đầu.', 1;
        END;

        -- 5. Kiểm tra giá thuê
        IF @GiaThueThoaThuan <= 0
        BEGIN
            THROW 50005, N'Giá thuê phải lớn hơn 0.', 1;
        END;

        -- 6. Tiền cọc = 1 tháng tiền thuê
        IF @TienCoc <> @GiaThueThoaThuan
        BEGIN
            THROW 50006, N'Tiền cọc phải bằng một tháng tiền thuê.', 1;
        END;

        -- 7. Tạo hợp đồng
        INSERT INTO HOPDONG(MaPhong,MaNT,NgayBatDau,NgayKetThuc,GiaThueThoaThuan,TienCoc,TrangThai)
        VALUES(@MaPhong,@MaNT,@NgayBatDau,@NgayKetThuc,@GiaThueThoaThuan,@TienCoc,N'HieuLuc');

        DECLARE @MaHopDong INT = SCOPE_IDENTITY();

        -- 8. Cập nhật phòng
        UPDATE PHONG
        SET TrangThai = N'DangThue',
            SoNguoiO = 1
        WHERE MaPhong = @MaPhong;

        -- 9. Commit
        COMMIT;

        SELECT
            @MaHopDong AS MaHopDong,
            @MaPhong AS MaPhong,
            N'Tạo hợp đồng thành công.' AS ThongBao;

    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
            ROLLBACK;
        THROW;

    END CATCH
END;
GO

---Transaction 2 - Ghi chỉ số điện nước + lập hóa đơn
CREATE OR ALTER PROCEDURE TX_LapHoaDon
    @MaPhong INT,
    @MaHopDong INT,
    @ThangNam VARCHAR(7),
    @ChiSoDienMoi INT,
    @ChiSoNuocMoi INT,
    @HanThanhToan DATE
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRAN;

        DECLARE @ChiSoDienCu INT;
        DECLARE @ChiSoNuocCu INT;

        DECLARE @TienPhong DECIMAL(12,2);
        DECLARE @TienDien DECIMAL(12,2);
        DECLARE @TienNuoc DECIMAL(12,2);
        DECLARE @TongTien DECIMAL(12,2);

        -- Kiểm tra hợp đồng
        SELECT
            @TienPhong = GiaThueThoaThuan
        FROM HOPDONG
        WHERE MaHopDong = @MaHopDong AND MaPhong = @MaPhong AND TrangThai = N'HieuLuc';

        IF @TienPhong IS NULL
        BEGIN
            THROW 50010, N'Hợp đồng không tồn tại hoặc không còn hiệu lực.', 1;
        END;

        -- Kiểm tra hóa đơn trùng
        IF EXISTS (SELECT 1 FROM HOADON WHERE MaHopDong = @MaHopDong AND ThangNam = @ThangNam)
        BEGIN
            THROW 50011, N'Hóa đơn tháng này đã tồn tại.', 1;
        END;

        -- Lấy chỉ số cũ
        SELECT
            @ChiSoDienCu = ChiSoMoi
        FROM CHISODIEN
        WHERE MaPhong = @MaPhong
          AND ThangNam = CONVERT(VARCHAR(7),
                                 DATEADD(MONTH, -1,
                                 CONVERT(DATE, '01/' + @ThangNam, 103)),
                                 103);

        SELECT
            @ChiSoNuocCu = ChiSoMoi
        FROM CHISONUOC
        WHERE MaPhong = @MaPhong
          AND ThangNam = CONVERT(VARCHAR(7),
                                 DATEADD(MONTH, -1,
                                 CONVERT(DATE, '01/' + @ThangNam, 103)),
                                 103);

        SET @ChiSoDienCu = ISNULL(@ChiSoDienCu, 0);
        SET @ChiSoNuocCu = ISNULL(@ChiSoNuocCu, 0);

        -- Kiểm tra chỉ số mới
        IF @ChiSoDienMoi < @ChiSoDienCu
        BEGIN
            THROW 50012, N'Chỉ số điện mới không được nhỏ hơn chỉ số cũ.', 1;
        END;

        IF @ChiSoNuocMoi < @ChiSoNuocCu
        BEGIN
            THROW 50013, N'Chỉ số nước mới không được nhỏ hơn chỉ số cũ.', 1;
        END;

        -- Ghi chỉ số điện
        INSERT INTO CHISODIEN(MaPhong,ThangNam,ChiSoCu,ChiSoMoi        )
        VALUES(@MaPhong,@ThangNam,@ChiSoDienCu,@ChiSoDienMoi);

        -- Ghi chỉ số nước
        INSERT INTO CHISONUOC(MaPhong,ThangNam,ChiSoCu,ChiSoMoi)
        VALUES(@MaPhong,@ThangNam,@ChiSoNuocCu,@ChiSoNuocMoi);

        -- Tính tiền
        SET @TienDien = dbo.FN_TinhTienDien(@MaPhong, @ThangNam);
        SET @TienNuoc = dbo.FN_TinhTienNuoc(@MaPhong, @ThangNam);
        SET @TongTien = @TienPhong + @TienDien + @TienNuoc;

        -- Lập hóa đơn
        INSERT INTO HOADON(MaHopDong,ThangNam,TienPhong,TienDien,TienNuoc,TongTien,HanThanhToan,TrangThai)
        VALUES(@MaHopDong,@ThangNam,@TienPhong,@TienDien,@TienNuoc,@TongTien,@HanThanhToan,N'ChuaThanhToan');

        DECLARE @MaHoaDon INT = SCOPE_IDENTITY();

        COMMIT;

        SELECT
            @MaHoaDon AS MaHoaDon,
            @TienPhong AS TienPhong,
            @TienDien AS TienDien,
            @TienNuoc AS TienNuoc,
            @TongTien AS TongTien;

    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
            ROLLBACK;
        THROW;

    END CATCH
END;
GO

---Transaction 3 - Thanh toán hóa đơn
CREATE OR ALTER PROCEDURE TX_ThanhToanHoaDon
    @MaHoaDon INT,
    @SoTienTra DECIMAL(12,2),
    @GhiChu NVARCHAR(200)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRAN;

        DECLARE @TongTien DECIMAL(12,2);
        DECLARE @DaThanhToan DECIMAL(12,2);
        DECLARE @ConLai DECIMAL(12,2);

        -- Khóa hóa đơn trong transaction
        SELECT
            @TongTien = TongTien
        FROM HOADON WITH (UPDLOCK, HOLDLOCK)
        WHERE MaHoaDon = @MaHoaDon;

        IF @TongTien IS NULL
        BEGIN
            THROW 50020, N'Hóa đơn không tồn tại.', 1;
        END;

        IF @SoTienTra <= 0
        BEGIN
            THROW 50021, N'Số tiền thanh toán phải lớn hơn 0.', 1;
        END;

        -- Tổng đã thanh toán
        SELECT
            @DaThanhToan = ISNULL(SUM(SoTienTra), 0)
        FROM THANHTOAN
        WHERE MaHoaDon = @MaHoaDon;

        SET @ConLai = @TongTien - @DaThanhToan;

        IF @SoTienTra > @ConLai
        BEGIN
            THROW 50022, N'Số tiền thanh toán vượt quá số tiền còn lại.', 1;
        END;

        -- Ghi nhận thanh toán
        INSERT INTO THANHTOAN(MaHoaDon,NgayThanhToan,SoTienTra,GhiChu)
        VALUES(@MaHoaDon,GETDATE(),@SoTienTra,@GhiChu);

        SET @DaThanhToan = @DaThanhToan + @SoTienTra;
        SET @ConLai = @TongTien - @DaThanhToan;

        -- Cập nhật trạng thái
        IF @ConLai = 0
        BEGIN
            UPDATE HOADON
            SET TrangThai = N'HoanTat'
            WHERE MaHoaDon = @MaHoaDon;
        END
        ELSE
        BEGIN
            UPDATE HOADON
            SET TrangThai = N'DangThanhToan'
            WHERE MaHoaDon = @MaHoaDon;
        END;

        COMMIT;

        SELECT
            @MaHoaDon AS MaHoaDon,
            @DaThanhToan AS DaThanhToan,
            @ConLai AS ConLai;

    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
            ROLLBACK;
        THROW;

    END CATCH
END;
GO

---Tx 4 - Trả phòng
CREATE OR ALTER PROCEDURE TX_TraPhong
    @MaHopDong INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRAN;

        DECLARE @MaPhong INT;
        DECLARE @TienCoc DECIMAL(12,2);
        DECLARE @TongNo DECIMAL(12,2);

        -- Khóa hợp đồng
        SELECT
            @MaPhong = MaPhong,
            @TienCoc = TienCoc
        FROM HOPDONG WITH (UPDLOCK, HOLDLOCK)
        WHERE MaHopDong = @MaHopDong AND TrangThai = N'HieuLuc';

        IF @MaPhong IS NULL
        BEGIN
            THROW 50030, N'Hợp đồng không tồn tại hoặc đã đóng.', 1;
        END;

        -- Tính tổng công nợ chưa thanh toán
        SELECT
            @TongNo = ISNULL(SUM(HD.TongTien - ISNULL(TT.DaThanhToan, 0)),0)
        FROM HOADON HD
        OUTER APPLY
        (
            SELECT SUM(SoTienTra) AS DaThanhToan
            FROM THANHTOAN
            WHERE MaHoaDon = HD.MaHoaDon
        ) TT
        WHERE HD.MaHopDong = @MaHopDong AND HD.TrangThai IN (N'ChuaThanhToan', N'DangThanhToan');

        -- Nếu công nợ vượt tiền cọc
        IF @TongNo > @TienCoc
        BEGIN
            THROW 50031, N'Công nợ vượt quá tiền cọc, chưa thể hoàn tất trả phòng.', 1;
        END;

        -- Đóng hợp đồng
        UPDATE HOPDONG
        SET TrangThai = N'DaDong'
        WHERE MaHopDong = @MaHopDong;

        -- Chuyển phòng về trạng thái trống
        UPDATE PHONG
        SET TrangThai = N'Trong',
            SoNguoiO = 0
        WHERE MaPhong = @MaPhong;

        COMMIT;

        SELECT
            @MaHopDong AS MaHopDong,
            @MaPhong AS MaPhong,
            @TienCoc AS TienCoc,
            @TongNo AS TongCongNo,
            @TienCoc - @TongNo AS TienCocConLai,
            N'Trả phòng thành công.' AS ThongBao;

    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
            ROLLBACK;
        THROW;

    END CATCH
END;
GO

---Tx 5 - Chuyển phòng
CREATE OR ALTER PROCEDURE TX_ChuyenPhong
    @MaHopDongCu INT,
    @MaPhongMoi INT,
    @NgayBatDauMoi DATE,
    @NgayKetThucMoi DATE,
    @GiaThueMoi DECIMAL(12,2),
    @TienCocMoi DECIMAL(12,2)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRAN;

        DECLARE @MaNT INT;
        DECLARE @MaPhongCu INT;

        -- Lấy hợp đồng hiện tại
        SELECT
            @MaNT = MaNT,
            @MaPhongCu = MaPhong
        FROM HOPDONG WITH (UPDLOCK, HOLDLOCK)
        WHERE MaHopDong = @MaHopDongCu AND TrangThai = N'HieuLuc';

        IF @MaNT IS NULL
        BEGIN
            THROW 50040, N'Hợp đồng hiện tại không tồn tại hoặc đã đóng.', 1;
        END;

        -- Không cho chuyển sang chính phòng hiện tại
        IF @MaPhongCu = @MaPhongMoi
        BEGIN
            THROW 50041, N'Phòng mới phải khác phòng hiện tại.', 1;
        END;

        -- Kiểm tra phòng mới
        IF NOT EXISTS (SELECT 1 FROM PHONG WITH (UPDLOCK, HOLDLOCK) WHERE MaPhong = @MaPhongMoi)
        BEGIN
            THROW 50042, N'Phòng mới không tồn tại.', 1;
        END;

        IF EXISTS (SELECT 1 FROM PHONG WHERE MaPhong = @MaPhongMoi AND TrangThai <> N'Trong')
        BEGIN
            THROW 50043, N'Phòng mới hiện không trống.', 1;
        END;

        -- Kiểm tra ngày
        IF @NgayKetThucMoi <= @NgayBatDauMoi
        BEGIN
            THROW 50044, N'Ngày kết thúc phải sau ngày bắt đầu.', 1;
        END;

        -- Kiểm tra giá
        IF @GiaThueMoi <= 0
        BEGIN
            THROW 50045, N'Giá thuê phải lớn hơn 0.', 1;
        END;

        IF @TienCocMoi <> @GiaThueMoi
        BEGIN
            THROW 50046, N'Tiền cọc phải bằng một tháng tiền thuê.', 1;
        END;

        -- 1. Đóng hợp đồng cũ
        UPDATE HOPDONG
        SET TrangThai = N'DaDong'
        WHERE MaHopDong = @MaHopDongCu;

        -- 2. Phòng cũ trở thành trống
        UPDATE PHONG
        SET TrangThai = N'Trong',
            SoNguoiO = 0
        WHERE MaPhong = @MaPhongCu;

        -- 3. Tạo hợp đồng mới
        INSERT INTO HOPDONG(MaPhong,MaNT,NgayBatDau,NgayKetThuc,GiaThueThoaThuan,TienCoc,TrangThai)
        VALUES(@MaPhongMoi,@MaNT,@NgayBatDauMoi,@NgayKetThucMoi,@GiaThueMoi,@TienCocMoi,N'HieuLuc');

        DECLARE @MaHopDongMoi INT = SCOPE_IDENTITY();

        -- 4. Phòng mới thành đang thuê
        UPDATE PHONG
        SET TrangThai = N'DangThue',
            SoNguoiO = 1
        WHERE MaPhong = @MaPhongMoi;

        COMMIT;

        SELECT
            @MaHopDongCu AS MaHopDongCu,
            @MaHopDongMoi AS MaHopDongMoi,
            @MaPhongCu AS MaPhongCu,
            @MaPhongMoi AS MaPhongMoi,
            N'Chuyển phòng thành công.' AS ThongBao;

    END TRY
    BEGIN CATCH

        IF @@TRANCOUNT > 0
            ROLLBACK;
        THROW;

    END CATCH
END;
GO
