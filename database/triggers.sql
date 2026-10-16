---TRIGGER

--TRIGGER 1 — Một phòng chỉ có 1 hợp đồng hiệu lực
CREATE TRIGGER TRG_HOPDONG_MotHopDongHieuLuc
ON HOPDONG
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS
    (
        SELECT MaPhong
        FROM HOPDONG
        WHERE TrangThai = N'HieuLuc'
        GROUP BY MaPhong
        HAVING COUNT(*) > 1
    )
    BEGIN
        RAISERROR(N'Lỗi: Một phòng chỉ được có tối đa một hợp đồng đang hiệu lực.', 16, 1);

        ROLLBACK TRANSACTION;
        RETURN;
    END
END;
GO

--TRIGGER 2 — Tiền cọc phải bằng tiền thuê
CREATE TRIGGER TRG_HOPDONG_TienCoc
ON HOPDONG
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS
    (
        SELECT 1
        FROM inserted
        WHERE TienCoc <> GiaThueThoaThuan
    )
    BEGIN
        RAISERROR (N'Lỗi: Tiền cọc phải bằng đúng một tháng tiền thuê.', 16, 1);

        ROLLBACK TRANSACTION;
        RETURN;
    END
END;
GO

--TRIGGER 3 — Kiểm tra chỉ số điện
CREATE TRIGGER TRG_CHISODIEN_KiemTra
ON CHISODIEN
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS
    (
        SELECT 1
        FROM inserted
        WHERE ChiSoMoi < ChiSoCu
    )
    BEGIN
        RAISERROR (N'Lỗi: Chỉ số điện mới không được nhỏ hơn chỉ số cũ.', 16, 1);

        ROLLBACK TRANSACTION;
        RETURN;
    END
END;
GO

--TRIGGER 4 — Kiểm tra chỉ số nước
CREATE TRIGGER TRG_CHISONUOC_KiemTra
ON CHISONUOC
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS
    (
        SELECT 1
        FROM inserted
        WHERE ChiSoMoi < ChiSoCu
    )
    BEGIN
        RAISERROR (N'Lỗi: Chỉ số nước mới không được nhỏ hơn chỉ số cũ.', 16, 1);

        ROLLBACK TRANSACTION;
        RETURN;
    END
END;
GO

--TRIGGER 5 — Không được thanh toán vượt quá hóa đơn
CREATE TRIGGER TRG_THANHTOAN_KiemTraSoTien
ON THANHTOAN
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS
    (
        SELECT 1
        FROM THANHTOAN TT
        INNER JOIN HOADON HD
            ON TT.MaHoaDon = HD.MaHoaDon
        WHERE TT.MaHoaDon IN
        (
            SELECT DISTINCT MaHoaDon
            FROM inserted
        )
        GROUP BY TT.MaHoaDon, HD.TongTien
        HAVING SUM(TT.SoTienTra) > HD.TongTien
    )
    BEGIN
        RAISERROR (N'Lỗi: Tổng các lần thanh toán vượt quá tổng tiền hóa đơn.', 16, 1);

        ROLLBACK TRANSACTION;
        RETURN;
    END
END;
GO

--TRIGGER 6 — Tự động cập nhật trạng thái hóa đơn
CREATE TRIGGER TRG_THANHTOAN_CapNhatTrangThai
ON THANHTOAN
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE HD
    SET TrangThai =
        CASE
            WHEN ISNULL(TT.TongDaTra, 0) = 0
                THEN N'ChuaThanhToan'

            WHEN ISNULL(TT.TongDaTra, 0) < HD.TongTien
                THEN N'DangThanhToan'

            WHEN ISNULL(TT.TongDaTra, 0) >= HD.TongTien
                THEN N'HoanTat'
        END
    FROM HOADON HD
    LEFT JOIN
    (
        SELECT
            MaHoaDon,
            SUM(SoTienTra) AS TongDaTra
        FROM THANHTOAN
        GROUP BY MaHoaDon
    ) TT
        ON HD.MaHoaDon = TT.MaHoaDon
    WHERE HD.MaHoaDon IN
    (
        SELECT MaHoaDon FROM inserted
        UNION
        SELECT MaHoaDon FROM deleted
    );
END;
GO

--TRIGGER 7 — Hóa đơn phải thuộc hợp đồng hiệu lực
CREATE TRIGGER TRG_HOADON_KiemTraHopDong
ON HOADON
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS
    (
        SELECT 1
        FROM inserted I
        INNER JOIN HOPDONG HD
            ON I.MaHopDong = HD.MaHopDong
        WHERE HD.TrangThai <> N'HieuLuc'
    )
    BEGIN
        RAISERROR
        (
            N'Lỗi: Hóa đơn phải thuộc hợp đồng đang hiệu lực.',
            16,
            1
        );

        ROLLBACK TRANSACTION;
        RETURN;
    END
END;
GO
