# Nghiệp vụ 6 — Quản lý hợp đồng

## Chức năng
- Lập hợp đồng, cập nhật thông tin hợp đồng, tra cứu danh sách/lịch sử và kết thúc hợp đồng.
- Tìm kiếm theo mã hợp đồng, mã/tên phòng, mã người thuê, họ tên hoặc trạng thái.
- Xóa chỉ cho phép khi hợp đồng chưa phát sinh hóa đơn.

## Quy tắc
- Phòng và người thuê phải tồn tại trong CSDL.
- Ngày kết thúc phải sau ngày bắt đầu.
- Giá thuê thỏa thuận và tiền cọc phải lớn hơn 0 theo constraint hiện có.
- Trạng thái CSDL dùng `HieuLuc` hoặc `DaDong`.
- Hợp đồng hiệu lực không được chồng lấn thời gian với hợp đồng hiệu lực khác của cùng phòng.
- Không xóa hợp đồng đã có hóa đơn để bảo toàn lịch sử.

## Kiểm thử thủ công
1. Lập hợp đồng với phòng/người thuê hợp lệ và ngày hợp lệ.
2. Thử ngày kết thúc trước hoặc bằng ngày bắt đầu: phải bị từ chối.
3. Thử giá thuê hoặc tiền cọc bằng 0/âm: phải bị từ chối.
4. Lập hợp đồng hiệu lực trùng khoảng ngày với hợp đồng cùng phòng: phải bị từ chối.
5. Tìm kiếm theo mã, tên phòng, họ tên người thuê.
6. Kết thúc hợp đồng và xác nhận trạng thái chuyển thành `DaDong`.
7. Thử xóa hợp đồng đã có hóa đơn: phải bị từ chối.
