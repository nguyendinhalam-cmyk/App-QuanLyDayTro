# QuanLyDayTro
Bộ khung Java Swing + JDBC + SQL Server Express cho đề tài **Hệ thống quản lý dãy trọ**.

## Công nghệ
- Java 17, Maven, Java Swing, JDBC Driver for SQL Server.
- SQL Server Express (ví dụ instance `SQLEXPRESS`).

## Các tầng
- `model`: mô hình dữ liệu.
- `dao`: truy cập dữ liệu bằng JDBC và gọi stored procedure.
- `service`: xử lý quy tắc nghiệp vụ.
- `ui`: giao diện Swing, chia theo nghiệp vụ bằng tên tiếng Việt không dấu.
- `dto`: đối tượng truyền dữ liệu; `util`: tiện ích dùng chung.
- `database`: nơi đặt script SQL của nhóm; `docs`: tài liệu.

## Bắt đầu
1. Mở project dưới dạng Maven project trong IntelliJ IDEA hoặc Eclipse.
2. Sao chép `src/main/resources/application.properties.example` thành `application.properties`.
3. Cập nhật cấu hình kết nối SQL Server.
4. Đưa script SQL thật của nhóm vào `database/` và chạy trên database hiện có.
5. Kiểm tra tên bảng/cột trong DAO khớp schema thực tế, sau đó chạy `com.quanlydaytro.Main`.

**Lưu ý:** Đây là bộ khung ban đầu, không phải ứng dụng đã hoàn thiện. Các script SQL hiện tại là placeholder để tránh ghi đè database thật của nhóm. Không đưa mật khẩu thật lên Git.
