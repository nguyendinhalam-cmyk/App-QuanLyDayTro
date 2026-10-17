# Nghiệp vụ 4 – Quản lý phòng trọ

## Chức năng
- Xem danh sách phòng và dãy trọ tương ứng.
- Thêm, cập nhật, xóa phòng.
- Tìm kiếm theo mã phòng, tên phòng, trạng thái, mã/tên dãy trọ.
- Chọn dãy trọ từ danh mục `DAYTRO`.
- Trạng thái phòng: `Trong`, `DangThue`, `DangSua` theo CHECK constraint hiện có.
- Chỉ vai trò Admin và Manager được truy cập; Service cũng kiểm tra quyền.

## Quy tắc kiểm tra
- Tên phòng bắt buộc, tối đa 20 ký tự.
- Giá thuê phải lớn hơn 0.
- Số người ở từ 0 đến 3.
- Tên phòng không được trùng trong cùng một dãy trọ.
- Phòng `Trong` không được có người ở; phòng `DangThue` phải có ít nhất một người ở.
- Không xóa phòng đã có hợp đồng hoặc chỉ số điện/nước; giữ nguyên lịch sử và tránh vi phạm khóa ngoại.

## Kiểm thử thủ công
1. Đăng nhập Admin hoặc Manager, mở Hệ thống → Quản lý phòng.
2. Thử thêm phòng với dãy trọ hợp lệ.
3. Thử giá thuê âm/0, số người ở ngoài 0–3, tên phòng rỗng và tên trùng trong cùng dãy.
4. Thử cập nhật phòng đã chọn và tìm kiếm.
5. Thử xóa phòng chưa có dữ liệu liên quan, sau đó thử xóa phòng đã có hợp đồng/chỉ số.
6. Đăng nhập vai trò Staff/Report và xác nhận menu quản lý phòng không hiển thị.

Không cần sửa `database/schema.sql` cho phần giao diện CRUD này.
