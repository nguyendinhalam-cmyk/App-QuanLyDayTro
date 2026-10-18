# Nghiệp vụ 9 – Quản lý hóa đơn

## Chức năng
- Tra cứu hóa đơn theo mã hóa đơn, mã hợp đồng, tháng/năm hoặc trạng thái.
- Lập hóa đơn tháng cho hợp đồng đang hiệu lực.
- Tính tiền phòng theo `HOPDONG.GiaThueThoaThuan`.
- Tính tiền điện/nước từ mức tiêu thụ trong `CHISODIEN`, `CHISONUOC` nhân với đơn giá gần nhất có `NgayApDung` không vượt quá ngày cuối tháng lập hóa đơn.
- Lưu hóa đơn vào `HOADON` với trạng thái `ChuaThanhToan`; ràng buộc unique của CSDL ngăn tạo trùng hóa đơn cho cùng hợp đồng/tháng.

## File đã cập nhật
- `src/main/java/com/quanlydaytro/dao/HoaDonDAO.java`
- `src/main/java/com/quanlydaytro/service/HoaDonService.java`
- `src/main/java/com/quanlydaytro/ui/quanlyhoadon/HoaDonPanel.java`
- `src/main/java/com/quanlydaytro/ui/TrangChinhFrame.java`

## Điều kiện lập hóa đơn
1. Hợp đồng tồn tại, đang hiệu lực và có thời gian giao với tháng cần lập.
2. Đã ghi chỉ số điện và nước của phòng trong tháng đó.
3. Có đơn giá điện và nước áp dụng trong tháng.
4. Nhập tháng theo `MM/yyyy`, hạn thanh toán theo `yyyy-MM-dd`.
5. Chưa tồn tại hóa đơn cho cùng hợp đồng và tháng.

## Kiểm thử đề xuất trên SQL Server
- Lập hóa đơn với hợp đồng hợp lệ, đủ chỉ số và đơn giá; đối chiếu phép tính tổng tiền.
- Thử thiếu chỉ số điện hoặc nước; hệ thống phải báo lỗi và không tạo hóa đơn.
- Thử thiếu đơn giá áp dụng; hệ thống phải báo lỗi.
- Lập trùng hợp đồng/tháng; CSDL phải từ chối do `UQ_HOADON_HOPDONG_THANG`.
- Thử hợp đồng không hiệu lực hoặc không giao với tháng hóa đơn; hệ thống phải từ chối.
- Tìm kiếm theo mã hóa đơn, mã hợp đồng, tháng/năm và trạng thái.

## Lưu ý
Đã biên dịch toàn bộ mã nguồn Java thành công. Chưa thực hiện kiểm thử kết nối trực tiếp với SQL Server trong môi trường này. Chức năng hiện tập trung vào lập/tra cứu hóa đơn; luồng thanh toán nhiều lần và quyết toán trả phòng thuộc nghiệp vụ 10–11.
