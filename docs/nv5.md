# Nghiệp vụ 5 — Quản lý người thuê

## Thành phần
- UI: `ui/quanlynguoithue/NguoiThuePanel.java`
- Service: `service/NguoiThueService.java`
- DAO: `dao/NguoiThueDAO.java`
- Model: `model/NguoiThue.java`
- Bảng dữ liệu: `NGUOITHUE` trong `database/schema.sql`

## Chức năng
- Hiển thị danh sách người thuê.
- Tìm theo mã, họ tên, CCCD, số điện thoại hoặc địa chỉ.
- Thêm, cập nhật và xóa người thuê.
- Xác nhận trước khi xóa; thông báo khi không thể xóa do khóa ngoại/hợp đồng liên quan.

## Kiểm tra dữ liệu
- Họ tên bắt buộc, tối đa 100 ký tự.
- CCCD gồm 9 hoặc 12 chữ số và phải duy nhất.
- Số điện thoại gồm 9–15 chữ số.
- Địa chỉ tùy chọn, tối đa 200 ký tự.
- Các lỗi trùng CCCD và lỗi khóa ngoại được chuyển thành thông báo dễ hiểu.

## Kiểm thử đề xuất
1. Mở màn hình: danh sách tải từ SQL Server.
2. Thêm người thuê với dữ liệu hợp lệ: bản ghi được tạo và có mã tự tăng.
3. Thêm CCCD trùng: bị từ chối.
4. Nhập họ tên rỗng, CCCD sai định dạng hoặc số điện thoại sai định dạng: bị từ chối.
5. Sửa thông tin và giữ nguyên CCCD hiện tại: cập nhật thành công.
6. Tìm theo một phần họ tên/CCCD/số điện thoại/địa chỉ hoặc mã: kết quả được lọc.
7. Xóa người thuê chưa liên kết: thành công.
8. Xóa người thuê đã có hợp đồng: bị từ chối, dữ liệu lịch sử được giữ lại.
