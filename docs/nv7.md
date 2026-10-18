# Nghiệp vụ 7 — Quản lý giá điện, nước

## Phạm vi
- Quản lý riêng lịch sử đơn giá điện (`BANGGIADIEN`) và đơn giá nước (`BANGGIANUOC`).
- Xem danh sách, tìm kiếm theo mã/đơn giá/ngày áp dụng, thêm, cập nhật, xóa và làm mới.
- Không thay đổi schema SQL hoặc tên cột hiện tại.

## Quy tắc nghiệp vụ
1. Đơn giá bắt buộc và phải lớn hơn 0.
2. Đơn giá tối đa 2 chữ số thập phân, tương thích `DECIMAL(10,2)`.
3. Ngày áp dụng bắt buộc, nhập theo `yyyy-MM-dd`.
4. Trong mỗi bảng, ngày áp dụng là duy nhất; cùng ngày có thể tồn tại một mức giá điện và một mức giá nước.
5. DAO kiểm tra ngày áp dụng trùng trước khi thêm/cập nhật; unique constraint trong SQL Server vẫn là lớp bảo vệ cuối cùng.
6. Nên giữ lại mức giá lịch sử đã dùng để tính/đối chiếu hóa đơn, tránh xóa dữ liệu lịch sử không cần thiết.

## Thành phần
- `model/GiaDien.java`, `model/GiaNuoc.java`: model giữ nguyên.
- `dao/GiaDienDAO.java`, `dao/GiaNuocDAO.java`: truy vấn tìm kiếm và kiểm tra ngày áp dụng.
- `service/GiaDienService.java`, `service/GiaNuocService.java`: kiểm tra dữ liệu và xử lý nghiệp vụ.
- `service/BangGiaService.java`: điểm truy cập chung cho hai service.
- `ui/quanlydiennuoc/BangGiaPanel.java`: giao diện hai tab giá điện/giá nước.

## Các ca kiểm thử
1. Thêm giá hợp lệ, ví dụ `3500.00`, ngày `2026-10-01` — thành công.
2. Nhập giá bằng 0 hoặc âm — bị từ chối.
3. Nhập giá có hơn 2 chữ số thập phân có ý nghĩa — bị từ chối.
4. Nhập ngày sai định dạng — giao diện báo lỗi, không ghi dữ liệu.
5. Thêm hai mức giá điện cùng ngày — lần thứ hai bị từ chối.
6. Thêm giá điện và giá nước cùng ngày — được phép vì hai bảng độc lập.
7. Sửa một mức giá nhưng giữ nguyên ngày của chính nó — được phép.
8. Sửa sang ngày đã được một bản ghi khác trong cùng bảng sử dụng — bị từ chối.
9. Tìm kiếm theo mã, đơn giá và ngày ISO — trả về các bản ghi khớp.
10. Xóa bản ghi — yêu cầu xác nhận trước khi thực hiện.

## Lưu ý
Cần kiểm thử CRUD với SQL Server của nhóm. Môi trường đóng gói không kết nối tới database thật nên không thể xác nhận dữ liệu runtime.
