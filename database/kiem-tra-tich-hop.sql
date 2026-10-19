/* Chạy trên SQL Server sau khi đã triển khai các script schema/functions/procedures/transactions. */
USE QL_NHA_TRO;
GO
SET NOCOUNT ON;

DECLARE @Thieu TABLE (Loai NVARCHAR(30), Ten NVARCHAR(128));
INSERT INTO @Thieu(Loai, Ten)
SELECT N'PROCEDURE', v.Ten
FROM (VALUES
    (N'TX_TaoHopDong'), (N'SP_LapHoaDon'), (N'TX_ThanhToanHoaDon'),
    (N'SP_TimPhongTrong'), (N'SP_ThongKeDoanhThu'), (N'TX_TraPhong'), (N'TX_ChuyenPhong')
) v(Ten)
WHERE OBJECT_ID(N'dbo.' + v.Ten, N'P') IS NULL;

INSERT INTO @Thieu(Loai, Ten)
SELECT N'FUNCTION', v.Ten
FROM (VALUES
    (N'FN_TinhTienDien'), (N'FN_TinhTienNuoc'), (N'FN_TinhTienHoaDon'),
    (N'FN_TongTienDaThanhToan'), (N'FN_TinhTienConLai')
) v(Ten)
WHERE OBJECT_ID(N'dbo.' + v.Ten, N'FN') IS NULL;

IF EXISTS (SELECT 1 FROM @Thieu)
BEGIN
    SELECT Loai, Ten AS DoiTuongChuaDuocTao FROM @Thieu ORDER BY Loai, Ten;
    THROW 51000, N'Thiếu Procedure/Function. Hãy chạy lại các script SQL theo đúng thứ tự.', 1;
END;

SELECT N'OK' AS KetQua, N'Đã tìm thấy các Procedure/Function cần cho luồng tích hợp Java.' AS ThongBao;
GO
