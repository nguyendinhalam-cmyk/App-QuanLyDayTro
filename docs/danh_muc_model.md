# Danh mục Model – bảng SQL

| Bảng SQL | Model Java | Khóa chính |
|---|---|---|
| `CHUTRO` | `ChuTro` | `MaChuTro` |
| `DAYTRO` | `DayTro` | `MaDay` |
| `PHONG` | `Phong` | `MaPhong` |
| `NGUOITHUE` | `NguoiThue` | `MaNT` |
| `HOPDONG` | `HopDong` | `MaHopDong` |
| `BANGGIADIEN` | `GiaDien` | `MaGiaDien` |
| `BANGGIANUOC` | `GiaNuoc` | `MaGiaNuoc` |
| `CHISODIEN` | `ChiSoDien` | `MaCSD` |
| `CHISONUOC` | `ChiSoNuoc` | `MaCSN` |
| `HOADON` | `HoaDon` | `MaHoaDon` |
| `THANHTOAN` | `ThanhToan` | `MaThanhToan` |
| `NHANVIEN` | `NhanVien` | `MaNV` |
| `NHANVIEN_DAYTRO` | `NhanVienDayTro` | `MaNV + MaDay (ghép)` |
| `VAITRO` | `VaiTro` | `MaVaiTro` |
| `TAIKHOAN` | `TaiKhoan` | `MaTaiKhoan` |


## Enum chuẩn dùng chung

| Enum | Giá trị | Ánh xạ schema hiện tại |
|---|---|---|
| `TrangThaiPhong` | `TRONG`, `DANG_THUE`, `DANG_SUA` | `PHONG.TrangThai`: `Trong`, `DangThue`, `DangSua` |
| `TrangThaiHopDong` | `HIEU_LUC`, `DA_DONG` | `HOPDONG.TrangThai`: `HieuLuc`, `DaDong` |
| `TrangThaiHoaDon` | `CHUA_THANH_TOAN`, `DANG_THANH_TOAN`, `HOAN_TAT` | `HOADON.TrangThai`: `ChuaThanhToan`, `DangThanhToan`, `HoanTat` |

Các enum nằm trong package `com.quanlydaytro.enums`. Chỉ được tạo enum cho giá trị cố định đã được xác nhận trong schema hoặc được nhóm thống nhất cho một yêu cầu cụ thể. Hiện tại không có `TrangThaiTaiKhoan` vì `TAIKHOAN` chưa có cột trạng thái, và không có `LoaiThanhToan` vì `THANHTOAN` chưa có cột hình thức thanh toán.
