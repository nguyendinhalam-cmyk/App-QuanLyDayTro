/*
    PHAN QUYEN CSDL - QUAN LY DAY TRO
    Database: QL_NHA_TRO
    SQL Server

    Chay script bang tai khoan co quyen db_owner tren QL_NHA_TRO.
    Script tao 4 database role va cap quyen theo nguyen tac quyen toi thieu.

    LUU Y BAO MAT:
    - Khong luu mat khau SQL Login that trong source code/Git.
    - Phan CREATE LOGIN mau o cuoi file la mau tham khao, can chay rieng
      va thay mat khau manh truoc khi su dung.
    - Ung dung Java hien dang dang nhap qua dbo.TAIKHOAN (tai khoan nghiep vu).
      Database roles ben duoi chi ap dung khi ket noi SQL Server bang cac login
      duoc map vao database user tuong ung.
*/
USE [QL_NHA_TRO];
GO

SET NOCOUNT ON;
GO

/* 1. Tao database roles (chay lai an toan) */
IF DATABASE_PRINCIPAL_ID(N'QLNT_Admin') IS NULL
    CREATE ROLE [QLNT_Admin] AUTHORIZATION [dbo];
GO
IF DATABASE_PRINCIPAL_ID(N'QLNT_Manager') IS NULL
    CREATE ROLE [QLNT_Manager] AUTHORIZATION [dbo];
GO
IF DATABASE_PRINCIPAL_ID(N'QLNT_Staff') IS NULL
    CREATE ROLE [QLNT_Staff] AUTHORIZATION [dbo];
GO
IF DATABASE_PRINCIPAL_ID(N'QLNT_Report') IS NULL
    CREATE ROLE [QLNT_Report] AUTHORIZATION [dbo];
GO

/* 2. ADMIN
   Quyen quan tri CSDL. Chi them tai khoan quan tri duoc uy quyen vao role nay. */
GRANT CONTROL ON DATABASE::[QL_NHA_TRO] TO [QLNT_Admin];
GO

/* 3. MANAGER
   Quan ly du lieu nghiep vu va goi cac thu tuc nghiep vu.
   Khong cap quyen sua/xoa tai khoan va danh muc vai tro truc tiep. */
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.CHUTRO TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.DAYTRO TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.PHONG TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.NGUOITHUE TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.HOPDONG TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.BANGGIADIEN TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.BANGGIANUOC TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.CHISODIEN TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.CHISONUOC TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.HOADON TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.THANHTOAN TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.NHANVIEN TO [QLNT_Manager];
GRANT SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.NHANVIEN_DAYTRO TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.VAITRO TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.TAIKHOAN TO [QLNT_Manager];

GRANT SELECT ON OBJECT::dbo.VW_DanhSachPhong TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.VW_PhongDangThue TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.VW_PhongTrong TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.VW_HoaDon TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.VW_HoaDonChuaThanhToan TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.VW_DoanhThu TO [QLNT_Manager];
GRANT SELECT ON OBJECT::dbo.VW_HopDongSapHetHan TO [QLNT_Manager];

GRANT EXECUTE ON OBJECT::dbo.SP_TaoHopDong TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.SP_LapHoaDon TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.SP_ThanhToanHoaDon TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.SP_TimPhongTrong TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.SP_ThongKeDoanhThu TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.SP_TraPhong TO [QLNT_Manager];

-- Cac thu tuc quan ly giao dich (transactions.sql)
GRANT EXECUTE ON OBJECT::dbo.TX_TaoHopDong TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.TX_LapHoaDon TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.TX_ThanhToanHoaDon TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.TX_TraPhong TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.TX_ChuyenPhong TO [QLNT_Manager];

-- Cho phep su dung cac ham nghiep vu
GRANT EXECUTE ON OBJECT::dbo.FN_TinhTienDien TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.FN_TinhTienNuoc TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.FN_TinhTienHoaDon TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.FN_TongTienDaThanhToan TO [QLNT_Manager];
GRANT EXECUTE ON OBJECT::dbo.FN_TinhTienConLai TO [QLNT_Manager];
GO

/* 4. STAFF
   Nhan vien duoc xem du lieu va thao tac mot so bang nghiep vu.
   Cac nghiep vu quan trong uu tien thuc hien qua stored procedure. */
GRANT SELECT ON OBJECT::dbo.CHUTRO TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.DAYTRO TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.PHONG TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.NGUOITHUE TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.HOPDONG TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.CHISODIEN TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.CHISONUOC TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.HOADON TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.THANHTOAN TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.VW_DanhSachPhong TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.VW_PhongDangThue TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.VW_PhongTrong TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.VW_HoaDon TO [QLNT_Staff];
GRANT SELECT ON OBJECT::dbo.VW_HoaDonChuaThanhToan TO [QLNT_Staff];

GRANT EXECUTE ON OBJECT::dbo.SP_TimPhongTrong TO [QLNT_Staff];
GRANT EXECUTE ON OBJECT::dbo.SP_ThanhToanHoaDon TO [QLNT_Staff];
GRANT EXECUTE ON OBJECT::dbo.TX_ThanhToanHoaDon TO [QLNT_Staff];
GRANT EXECUTE ON OBJECT::dbo.FN_TinhTienHoaDon TO [QLNT_Staff];
GRANT EXECUTE ON OBJECT::dbo.FN_TongTienDaThanhToan TO [QLNT_Staff];
GRANT EXECUTE ON OBJECT::dbo.FN_TinhTienConLai TO [QLNT_Staff];
GO

/* 5. REPORT
   Chi doc cac view bao cao va chay thu tuc thong ke.
   DENY tren bang goc ngan truy cap truc tiep; quyen SELECT tren view van
   co the hoat dong theo ownership chaining neu view va bang cung owner dbo. */
GRANT SELECT ON OBJECT::dbo.VW_DanhSachPhong TO [QLNT_Report];
GRANT SELECT ON OBJECT::dbo.VW_PhongDangThue TO [QLNT_Report];
GRANT SELECT ON OBJECT::dbo.VW_PhongTrong TO [QLNT_Report];
GRANT SELECT ON OBJECT::dbo.VW_HoaDon TO [QLNT_Report];
GRANT SELECT ON OBJECT::dbo.VW_HoaDonChuaThanhToan TO [QLNT_Report];
GRANT SELECT ON OBJECT::dbo.VW_DoanhThu TO [QLNT_Report];
GRANT SELECT ON OBJECT::dbo.VW_HopDongSapHetHan TO [QLNT_Report];
GRANT EXECUTE ON OBJECT::dbo.SP_ThongKeDoanhThu TO [QLNT_Report];

DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.TAIKHOAN TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.VAITRO TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.CHUTRO TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.DAYTRO TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.PHONG TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.NGUOITHUE TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.HOPDONG TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.BANGGIADIEN TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.BANGGIANUOC TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.CHISODIEN TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.CHISONUOC TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.HOADON TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.THANHTOAN TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.NHANVIEN TO [QLNT_Report];
DENY SELECT, INSERT, UPDATE, DELETE ON OBJECT::dbo.NHANVIEN_DAYTRO TO [QLNT_Report];
GO

/* 6. Vi du REVOKE:
   Staff khong duoc xoa chi so dien truc tiep. REVOKE xoa cap quyen DELETE
   neu truoc do da duoc cap; role Staff khong duoc cap DELETE trong script nay.
   Dong lenh duoi day minh hoa cach thu hoi quyen, khong tao quyen moi. */
REVOKE DELETE ON OBJECT::dbo.CHISODIEN FROM [QLNT_Staff];
GO

/* 7. Map cac SQL Login da duoc DBA tao san vao database user/role.
   Neu login chua ton tai, script bo qua va in huong dan; khong tao mat khau
   mac dinh trong source code. */
DECLARE @LoginRole TABLE
(
    LoginName SYSNAME NOT NULL,
    RoleName SYSNAME NOT NULL
);
INSERT INTO @LoginRole (LoginName, RoleName)
VALUES
    (N'QLNT_AdminLogin',   N'QLNT_Admin'),
    (N'QLNT_ManagerLogin', N'QLNT_Manager'),
    (N'QLNT_StaffLogin',   N'QLNT_Staff'),
    (N'QLNT_ReportLogin',  N'QLNT_Report');

DECLARE @LoginName SYSNAME, @RoleName SYSNAME, @Sql NVARCHAR(MAX);
DECLARE curLogin CURSOR LOCAL FAST_FORWARD FOR
    SELECT LoginName, RoleName FROM @LoginRole;
OPEN curLogin;
FETCH NEXT FROM curLogin INTO @LoginName, @RoleName;

WHILE @@FETCH_STATUS = 0
BEGIN
    IF SUSER_ID(@LoginName) IS NOT NULL
    BEGIN
        IF DATABASE_PRINCIPAL_ID(@LoginName) IS NULL
        BEGIN
            SET @Sql = N'CREATE USER ' + QUOTENAME(@LoginName)
                     + N' FOR LOGIN ' + QUOTENAME(@LoginName) + N';';
            EXEC sys.sp_executesql @Sql;
        END;

        SET @Sql = N'ALTER ROLE ' + QUOTENAME(@RoleName)
                 + N' ADD MEMBER ' + QUOTENAME(@LoginName) + N';';
        EXEC sys.sp_executesql @Sql;

        PRINT N'Da map login ' + @LoginName + N' vao role ' + @RoleName + N'.';
    END
    ELSE
    BEGIN
        PRINT N'Chua co SQL Login ' + @LoginName
            + N'. Hay tao login bang mat khau manh va chay lai script.';
    END;

    FETCH NEXT FROM curLogin INTO @LoginName, @RoleName;
END;

CLOSE curLogin;
DEALLOCATE curLogin;
GO

/* 8. Kiem tra nhanh cac role va quyen da cap */
SELECT
    RoleName = r.name,
    MemberName = m.name
FROM sys.database_role_members rm
JOIN sys.database_principals r ON rm.role_principal_id = r.principal_id
JOIN sys.database_principals m ON rm.member_principal_id = m.principal_id
WHERE r.name IN (N'QLNT_Admin', N'QLNT_Manager', N'QLNT_Staff', N'QLNT_Report')
ORDER BY r.name, m.name;
GO

/*
    TAO SQL LOGIN (THUC HIEN RIENG TREN INSTANCE, KHONG COMMIT MAT KHAU):
    Chay trong master sau khi thay <MAT_KHAU_MANH> bang mat khau rieng:
      CREATE LOGIN [QLNT_AdminLogin]   WITH PASSWORD = '<MAT_KHAU_MANH>', CHECK_POLICY = ON;
      CREATE LOGIN [QLNT_ManagerLogin] WITH PASSWORD = '<MAT_KHAU_MANH>', CHECK_POLICY = ON;
      CREATE LOGIN [QLNT_StaffLogin]   WITH PASSWORD = '<MAT_KHAU_MANH>', CHECK_POLICY = ON;
      CREATE LOGIN [QLNT_ReportLogin]  WITH PASSWORD = '<MAT_KHAU_MANH>', CHECK_POLICY = ON;

    Sau khi tao login, chay lai file nay de tao database user va gan role.
    Khong dung chung SQL Login quan tri cho tat ca nguoi dung ung dung.
*/
