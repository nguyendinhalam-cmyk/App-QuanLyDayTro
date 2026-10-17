# Quy ước phát triển thống nhất – QL-DayTro

## 1. Chuẩn dữ liệu
- `database/schema.sql` là nguồn chuẩn duy nhất cho tên bảng, cột, kiểu SQL, khóa chính/ngoại và constraint.
- Database mặc định: `QL_NHA_TRO`. Không tự đổi tên bảng/cột hoặc thêm thuộc tính không có trong schema.
- Khóa identity không truyền vào lệnh INSERT; DAO lấy mã tự sinh bằng generated keys.
- `MaNT` được đặt tên Java là `maNguoiThue`; `MaNV` là `maNhanVien`; `MaCSD` là `maChiSoDien`; `MaCSN` là `maChiSoNuoc`.
- Bảng `NHANVIEN_DAYTRO` dùng khóa chính ghép `(MaNV, MaDay)` và có DAO chuyên biệt.

## 2. Tầng ứng dụng
- `model`: ánh xạ dữ liệu của một bảng. Không chứa SQL hoặc logic giao diện.
- `dto`: dữ liệu phục vụ màn hình/báo cáo/kết quả use case, không thay thế model bảng.
- `dao`: SQL/JDBC, dùng `PreparedStatement`; không hiển thị thông báo Swing.
- `service`: API nghiệp vụ, xác thực đầu vào, quy tắc liên bảng và transaction.
- `ui`: Swing; gọi Service, không gọi JDBC trực tiếp.
- `connection`: cấu hình và tạo kết nối; không commit dữ liệu nghiệp vụ tại đây.

## 3. Quy ước kiểu Java
- `DECIMAL` → `BigDecimal`; `DATE` → `LocalDate`; `DATETIME` → `LocalDateTime`.
- Mã `INT IDENTITY` → `int`; cột `VARCHAR/NVARCHAR` → `String`.
- Trường tháng kỳ `ThangNam` đang là `VARCHAR(7)` trong schema nên giữ `String`, định dạng thống nhất `MM/yyyy`.
- Không dùng `double` cho tiền.

## 4. API DAO/Service nền tảng
Các DAO bảng có khóa identity cung cấp `findAll()`, `findById(int)`, `insert(entity)`, `update(id, entity)`, `delete(id)`. Service cung cấp `layTatCa()`, `timTheoMa(int)`, `them(entity)`, `capNhat(id, entity)`, `xoa(id)`.

Đây là CRUD nền tảng, không có nghĩa mọi thao tác đều được phép trong nghiệp vụ. Các module phải bổ sung kiểm tra nghiệp vụ trước khi ghi dữ liệu. Ví dụ: không xóa phòng đang được hợp đồng tham chiếu; không tạo hai hóa đơn cho cùng hợp đồng/kỳ; thanh toán phải cập nhật trạng thái hóa đơn theo tổng tiền đã trả.

## 5. Transaction
Khi một nghiệp vụ ghi nhiều bảng, Service điều phối transaction trên cùng một Connection (hoặc gọi stored procedure đã có trong database). Không dùng nhiều kết nối độc lập cho các bước cần cùng thành công/thất bại. Trước khi bổ sung logic, kiểm tra `database/procedures.sql`, `triggers.sql`, `functions.sql` và `transactions.sql` để tránh lặp quy tắc.

## 6. Quy trình khi nhận module
1. Đọc schema và quy ước này.
2. Tái sử dụng model/DAO/DTO/Service đã có; không tạo lớp trùng.
3. Nếu cần API mới, thống nhất chữ ký với cả nhóm trước khi code.
4. Thêm kiểm thử cho thành công, dữ liệu sai, khóa ngoại/unique và trường hợp đồng thời nếu có.
5. Không sửa schema nếu chưa được nhóm duyệt.

## 7. Lưu ý bảo mật
Không lưu mật khẩu rõ; dùng cơ chế băm mật khẩu phù hợp. Không ghi mật khẩu vào log hoặc DTO trả về UI. `security.sql`/cấu hình SQL Server cần được thiết lập riêng theo môi trường thực tế.


## Enum dùng chung

- Chỉ dùng ba enum trạng thái hiện được schema hỗ trợ: `TrangThaiPhong`, `TrangThaiHopDong`, `TrangThaiHoaDon`. Hằng enum Java dùng tên tiếng Việt không dấu viết hoa; DAO ánh xạ sang đúng chuỗi lưu trong SQL Server (`Trong`, `HieuLuc`, `ChuaThanhToan`, ...).
- Không tạo `TrangThaiTaiKhoan` vì bảng `TAIKHOAN` chưa có cột trạng thái; không tạo `LoaiThanhToan` vì bảng `THANHTOAN` chưa có cột hình thức thanh toán. Nếu thiết kế CSDL thay đổi sau này, nhóm mới xem xét bổ sung enum tương ứng.
- Enum chỉ liệt kê tập giá trị cố định; quy tắc chuyển trạng thái và kiểm tra điều kiện nghiệp vụ thuộc Service. Việc ánh xạ giữa hằng Java và chuỗi CSDL đặt trong DAO.
