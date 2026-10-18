# Nghiệp vụ 8 — Quản lý chỉ số điện, nước

## Chức năng
- Xem, tìm kiếm, thêm, sửa, xóa chỉ số điện và nước theo phòng/kỳ.
- Tính lượng tiêu thụ = chỉ số mới - chỉ số cũ.
- Kiểm tra kỳ ghi theo MM/yyyy, ngày ghi theo yyyy-MM-dd, chỉ số không âm và chỉ số mới >= chỉ số cũ.
- Kiểm tra phòng tồn tại; nếu có kỳ ghi trước thì chỉ số cũ phải bằng chỉ số mới của kỳ gần nhất.
- CSDL đảm bảo mỗi phòng chỉ có một bản ghi mỗi kỳ qua UNIQUE (MaPhong, ThangNam).

## Kiểm thử
1. Thêm bản ghi hợp lệ cho phòng tồn tại.
2. Thử chỉ số mới nhỏ hơn chỉ số cũ: phải bị từ chối.
3. Thử kỳ không đúng MM/yyyy: phải bị từ chối.
4. Thử trùng phòng/kỳ: SQL Server từ chối do UNIQUE.
5. Khi có kỳ trước, thử chỉ số cũ không khớp chỉ số mới kỳ trước: phải bị từ chối.
6. Tìm kiếm theo mã chỉ số, mã phòng và kỳ; kiểm tra lượng tiêu thụ hiển thị.

## Lưu ý
Các mức giá được quản lý tại nghiệp vụ 7; màn hình chỉ số chưa tính tiền. Không thêm/sửa schema SQL. Xóa chỉ số đã dùng trong hóa đơn cần tuân thủ khóa ngoại và quy trình dữ liệu của hệ thống.
