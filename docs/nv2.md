# Nghiệp vụ 2 – Quản lý nhân viên

## Phạm vi
- Xem danh sách nhân viên.
- Tìm kiếm theo mã, họ tên, số điện thoại hoặc chức vụ.
- Thêm, cập nhật và xóa nhân viên.
- Kiểm tra dữ liệu nhập và các ràng buộc liên quan đến tài khoản/phân công dãy trọ.

## Các file triển khai
- `model/NhanVien.java`: mô hình dữ liệu bảng `NHANVIEN`.
- `dao/NhanVienDAO.java`: CRUD nền, tìm kiếm và kiểm tra liên kết dữ liệu.
- `service/NhanVienService.java`: kiểm tra dữ liệu, phân quyền và quy tắc xóa.
- `ui/quanlytaikhoan/NhanVienPanel.java`: giao diện Swing.
- `ui/TrangChinhFrame.java`: thêm mục menu Quản lý nhân viên cho Admin.

## Quy tắc
- Chỉ vai trò `Admin` được sử dụng nghiệp vụ này.
- Họ tên và chức vụ bắt buộc; họ tên tối đa 100 ký tự, chức vụ tối đa 50 ký tự.
- Số điện thoại bắt buộc, dài 8–15 ký tự, chỉ nhận số và các ký tự `+ ( ) . -`.
- Không cho dùng trùng số điện thoại giữa các nhân viên (quy tắc ở tầng Service; schema hiện tại chưa có UNIQUE cho cột này).
- Không cho xóa nhân viên đang có tài khoản hoặc đang được phân công quản lý dãy trọ.
- Truy vấn dùng `PreparedStatement`; tên bảng/cột theo `database/schema.sql`.

## Kiểm thử đề nghị
1. Đăng nhập bằng tài khoản Admin và mở Hệ thống → Quản lý nhân viên.
2. Thêm nhân viên hợp lệ; xác nhận xuất hiện trong bảng.
3. Thử để trống họ tên/chức vụ, nhập số điện thoại sai định dạng và nhập số điện thoại trùng.
4. Sửa một nhân viên rồi kiểm tra dữ liệu sau khi làm mới.
5. Tìm kiếm bằng mã, một phần họ tên, số điện thoại và chức vụ.
6. Xóa nhân viên chưa liên kết; xác nhận xóa thành công.
7. Thử xóa nhân viên đã gắn tài khoản hoặc có phân công dãy trọ; hệ thống phải từ chối và giải thích lý do.
8. Đăng nhập bằng vai trò khác Admin; không được mở menu và Service cũng từ chối truy cập.

## Lưu ý
Bảng `NHANVIEN` trong schema hiện tại không có UNIQUE cho `SoDienThoai`; kiểm tra trùng hiện được thực hiện ở Service. Nếu cần đảm bảo tuyệt đối khi nhiều người thao tác đồng thời, có thể bổ sung UNIQUE constraint sau khi nhóm thống nhất và làm sạch dữ liệu trùng hiện có.
