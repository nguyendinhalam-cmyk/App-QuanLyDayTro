# Tích hợp Java DAO/Service với Stored Procedure, Function và Transaction

## 1. Ánh xạ chức năng

| Chức năng Java | Đối tượng SQL Server được gọi | Kết quả |
|---|---|---|
| `HopDongService.them` → `HopDongDAO.taoHopDongBangTransaction` | `TX_TaoHopDong` | Nhận `MaHopDong`; tạo hợp đồng và cập nhật trạng thái phòng trong cùng transaction. |
| `HoaDonService.lapHoaDon` → `HoaDonDAO.lapHoaDon` | `SP_LapHoaDon` | Nhận `MaHoaDon`; thủ tục gọi các function tính tiền điện/nước và tổng hóa đơn. |
| `HoaDonService.tinhTienDuKien` → `HoaDonDAO.tinhTienDuKien` | `FN_TinhTienHoaDon` | Trả về tổng tiền dự kiến theo hợp đồng, phòng và kỳ. |
| `ThanhToanService.ghiNhan` → `ThanhToanDAO.ghiNhanVaCapNhatTrangThai` | `TX_ThanhToanHoaDon` | Nhận `MaThanhToan`; kiểm tra công nợ và ghi nhận trong transaction. |
| `ThanhToanService.daThanhToan` | `FN_TongTienDaThanhToan` | Tính tổng các lần thanh toán của hóa đơn. |
| `ThanhToanService.conNo` | `FN_TinhTienConLai` | Tính số tiền còn lại của hóa đơn. |
| `ChuyenPhongService.danhSachPhongTrong` → `ChuyenPhongDAO.danhSachPhongTrong` | `SP_TimPhongTrong` | Lấy danh sách phòng trống; DAO loại phòng hiện tại khỏi danh sách. |
| `ChuyenPhongService.chuyenPhong` → `ChuyenPhongDAO.chuyenPhong` | `TX_ChuyenPhong` | Chuyển phòng, đóng hợp đồng cũ và tạo hợp đồng mới trong transaction. |
| `TraPhongService.quyetToan` → `TraPhongDAO.quyetToan` | `TX_TraPhong` | Kiểm tra công nợ, đóng hợp đồng và trả phòng trong transaction. |
| `BaoCaoService.doanhThuTheoDay` → `BaoCaoDAO.layDoanhThuTheoDay` | `SP_ThongKeDoanhThu` | Báo cáo số hóa đơn, tổng hóa đơn và thực thu theo dãy trọ. |
| `BaoCaoService.doanhThu` → `BaoCaoDAO.layDoanhThu` | `FN_TongTienDaThanhToan`, `FN_TinhTienConLai` | Báo cáo theo tháng như giao diện hiện tại. |

`FN_TinhTienDien` và `FN_TinhTienNuoc` được gọi từ `SP_LapHoaDon` và `TX_LapHoaDon`; `FN_TinhTienHoaDon` được gọi trực tiếp từ Java và được sử dụng trong logic SQL tính hóa đơn.

## 2. Thay đổi chính

- Tạo hợp đồng không còn chèn trực tiếp bằng `AbstractJdbcDAO.insert`; nó gọi `TX_TaoHopDong`.
- Danh sách phòng trống dùng `SP_TimPhongTrong`.
- Ghi nhận thanh toán dùng `TX_ThanhToanHoaDon`, không tự chèn thanh toán ở Java.
- Trả phòng dùng `TX_TraPhong`, không mở transaction thủ công rồi tự cập nhật hai bảng từ Java.
- Báo cáo tháng dùng các function của CSDL; đồng thời có API Service/DAO gọi trực tiếp `SP_ThongKeDoanhThu` cho báo cáo theo dãy.
- Giữ nguyên chữ ký phương thức mà các màn hình hiện tại đang gọi; không thay đổi JFrame/JTable.

## 3. Thứ tự triển khai SQL

Chạy các script trên đúng database `QL_NHA_TRO` theo thứ tự:

1. `schema.sql`
2. `functions.sql`
3. `triggers.sql`
4. `procedures.sql`
5. `transactions.sql`
6. `indexes.sql`
7. `security.sql` (nếu đang cấu hình tài khoản/role theo script này)
8. `test-data.sql` hoặc `insert-data.sql` phù hợp với dữ liệu hiện có.

Không chạy script dữ liệu mẫu trên database có dữ liệu thật nếu chưa sao lưu và kiểm tra khóa trùng.

## 4. Kiểm thử đã thực hiện và giới hạn

- Đã biên dịch toàn bộ mã nguồn Java bằng `javac -encoding UTF-8`; biên dịch thành công.
- Chưa thể chạy kiểm thử tích hợp thật với SQL Server trong môi trường tạo gói này vì không có kết nối đến instance SQL Server của người dùng. Cần chạy các script SQL trên máy cài SQL Server và kiểm thử các luồng tạo hợp đồng, lập hóa đơn, thanh toán, trả phòng, chuyển phòng.
- `TX_TraPhong` hiện kiểm tra công nợ không vượt tiền cọc và hoàn tất việc trả phòng. Schema hiện tại chưa có bảng lưu bút toán khấu trừ/hoàn cọc; vì vậy khoản khấu trừ và ghi chú truyền từ màn hình mới được kiểm tra ở tầng nghiệp vụ, chưa được lưu thành lịch sử quyết toán riêng. Nếu yêu cầu đồ án cần quản lý lịch sử cọc, nên bổ sung bảng quyết toán cọc trước khi xem nghiệp vụ này là hoàn chỉnh về kế toán.
