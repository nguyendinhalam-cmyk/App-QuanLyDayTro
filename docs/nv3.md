# Nghiệp vụ 3 – Quản lý dãy trọ và chủ trọ

## Phạm vi
- Quản lý chủ trọ: danh sách, tìm kiếm, thêm, cập nhật, xóa.
- Quản lý dãy trọ: danh sách, tìm kiếm theo mã/tên/địa chỉ/chủ trọ, thêm, cập nhật, xóa.
- Dãy trọ phải gắn với một chủ trọ tồn tại trong bảng `CHUTRO`.
- Chỉ vai trò `Admin` và `Manager` được mở và thao tác các màn hình này.

## File được cập nhật/bổ sung
- `dao/ChuTroDAO.java`: tìm kiếm, kiểm tra trùng số điện thoại, kiểm tra dãy trọ phụ thuộc.
- `dao/DayTroDAO.java`: tìm kiếm, kiểm tra trùng địa chỉ, kiểm tra phòng/phân công phụ thuộc.
- `service/ChuTroService.java`: kiểm tra quyền, dữ liệu đầu vào, số điện thoại, không cho xóa chủ trọ đang sở hữu dãy trọ.
- `service/DayTroService.java`: kiểm tra quyền, dữ liệu đầu vào, địa chỉ duy nhất, không cho xóa dãy trọ đang có phòng hoặc nhân viên được phân công.
- `ui/quanlydaytro/ChuTroPanel.java`: giao diện CRUD chủ trọ.
- `ui/quanlydaytro/DayTroPanel.java`: giao diện CRUD dãy trọ và chọn chủ trọ.
- `ui/TrangChinhFrame.java`: thêm menu quản lý chủ trọ và dãy trọ cho Admin/Manager.

## Kiểm thử đề xuất
1. Đăng nhập bằng Admin hoặc Manager, mở hai màn hình mới.
2. Thêm chủ trọ hợp lệ; thử bỏ trống họ tên và nhập số điện thoại không hợp lệ.
3. Thử thêm số điện thoại đã tồn tại.
4. Thêm dãy trọ với chủ trọ có sẵn; thử bỏ trống tên/địa chỉ.
5. Thử nhập địa chỉ đã được dùng bởi dãy trọ khác.
6. Thử xóa chủ trọ đang sở hữu dãy trọ: hệ thống phải từ chối.
7. Thử xóa dãy trọ đang có phòng hoặc đang có nhân viên được phân công: hệ thống phải từ chối.
8. Đăng nhập bằng vai trò khác và xác nhận menu không hiển thị hai chức năng này.

## Lưu ý
- Không thay đổi `database/schema.sql`; các cột sử dụng theo schema hiện tại (`CHUTRO.MaChuTro`, `HoTen`, `SoDienThoai`; `DAYTRO.MaDay`, `TenDay`, `DiaChi`, `MaChuTro`).
- Cần có ít nhất một bản ghi `CHUTRO` trước khi tạo dãy trọ.
- Mã nguồn đã được biên dịch bằng `javac` với Java 17. Chưa xác nhận tích hợp với SQL Server trên máy người dùng; cần chạy thử với database thực tế.
