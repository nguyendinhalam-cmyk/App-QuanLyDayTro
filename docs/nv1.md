# Nghiệp vụ 1 – Quản lý tài khoản, đăng nhập và phân quyền

## Chức năng
- Đăng nhập bằng tên đăng nhập và mật khẩu.
- Băm mật khẩu bằng PBKDF2-HMAC-SHA256 có salt riêng.
- Quản lý tài khoản: xem danh sách (không hiển thị hash), thêm, sửa, xóa, làm mới.
- Mỗi nhân viên chỉ có một tài khoản theo UNIQUE `TAIKHOAN.MaNV`; tên đăng nhập là duy nhất.
- Danh mục vai trò và nhân viên lấy trực tiếp từ SQL Server, không tạo enum vai trò.
- Phiên đăng nhập giữ mã tài khoản, nhân viên, vai trò và họ tên.
- Menu quản lý tài khoản chỉ hiển thị cho vai trò `Admin`; Service cũng kiểm tra quyền ở mọi thao tác quản lý tài khoản, không chỉ dựa vào việc ẩn menu.
- Không cho xóa tài khoản đang đăng nhập hoặc tự đổi vai trò của tài khoản đang đăng nhập.
- Không hiển thị giá trị hash mật khẩu trong bảng tài khoản.

## Chạy thử
1. Chạy `database/schema.sql` để tạo cấu trúc database.
2. Chạy các file dữ liệu theo thứ tự phù hợp của bộ SQL; `database/insert-data.sql` hiện dùng hash PBKDF2 cho tài khoản mẫu.
3. Mở `application.properties` nếu cần cấu hình kết nối; mặc định kết nối `localhost\SQLEXPRESS`, database `QL_NHA_TRO`, Windows Authentication.
4. Chạy `com.quanlydaytro.Main`.

## Tài khoản kiểm thử khi đã nạp đúng dữ liệu mẫu
- `admin` / `Admin123!`
- `manager` / `Manager123!`
- `staff1` / `Staff123!`
- `report` / `Report123!`

## Lưu ý
Nếu database đã được tạo và `insert-data.sql` đã chạy trước đây, chỉnh sửa file SQL không tự thay đổi các tài khoản đã có. Với môi trường bài tập, có thể tạo lại database theo quy trình của nhóm rồi nạp lại dữ liệu; không xóa database có dữ liệu cần giữ. Hoặc cập nhật cột `MatKhau` của các tài khoản hiện có bằng các giá trị hash trong `insert-data.sql`.

Trong phạm vi nghiệp vụ 1, chỉ `Admin` được quản lý tài khoản; quyền truy cập đã được kiểm tra ở cả giao diện và tầng Service. Ma trận quyền cho các nghiệp vụ 2–14 sẽ được chốt khi triển khai các nghiệp vụ đó.
