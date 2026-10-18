# Hướng dẫn cài đặt và chạy chương trình – Quản lý dãy trọ

## 1. Giới thiệu

Đây là ứng dụng Java desktop quản lý dãy trọ, sử dụng Java Swing cho giao diện, JDBC để truy cập SQL Server và Maven để quản lý thư viện. Cơ sở dữ liệu mặc định có tên `QL_NHA_TRO`.

## 2. Yêu cầu cài đặt

- JDK 17 trở lên (khuyến nghị JDK 17).
- Apache Maven 3.8 trở lên.
- Microsoft SQL Server (ví dụ SQL Server Express) và SQL Server Management Studio (SSMS).
- Git nếu cần tải mã nguồn và theo dõi lịch sử thay đổi.

## 3. Tạo cơ sở dữ liệu

Trong thư mục `database/` có các file SQL để tạo cấu trúc, đối tượng và dữ liệu mẫu. Mở SSMS, kết nối đúng SQL Server instance rồi chạy các file theo thứ tự dưới đây:

1. `schema.sql` – tạo database `QL_NHA_TRO` và các bảng.
2. `functions.sql` – tạo các hàm SQL.
3. `views.sql` – tạo các view.
4. `procedures.sql` – tạo stored procedure.
5. `triggers.sql` – tạo trigger.
6. `indexes.sql` – tạo chỉ mục.
7. `security.sql` – thiết lập phần bảo mật trong script (nếu áp dụng cho môi trường của bạn).
8. `insert-data.sql` – thêm dữ liệu mẫu.

Chạy từng file trong SSMS và kiểm tra thông báo lỗi trước khi chạy file tiếp theo. Một số script tạo đối tượng có thể báo lỗi nếu chạy lại trên database đã có các đối tượng đó. Nếu muốn làm lại từ đầu, hãy sao lưu dữ liệu trước; chỉ xóa/tạo lại database khi chắc chắn không cần giữ dữ liệu cũ.

`test-data.sql` và `transactions.sql` là các script phục vụ kiểm thử/minh họa. Không chạy tùy tiện trên dữ liệu thật vì chúng có thể thêm, cập nhật hoặc thao tác dữ liệu. Chỉ chạy khi đã đọc nội dung và hiểu tác động.

## 4. Cấu hình kết nối SQL Server

1. Trong `src/main/resources/`, sao chép `application.properties.example` thành `application.properties`.
2. Mở `application.properties`, chỉnh URL, tên đăng nhập và mật khẩu đúng với SQL Server trên máy của bạn. Ví dụ:

```properties
db.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=QL_NHA_TRO;encrypt=true;trustServerCertificate=true
db.username=sa
db.password=MAT_KHAU_SQL_SERVER_CUA_BAN
```

- Nếu dùng instance mặc định, thay `localhost\\SQLEXPRESS` bằng máy chủ/instance thực tế, ví dụ `localhost`.
- Nếu dùng SQL Server Authentication, bật chế độ xác thực phù hợp và dùng tài khoản có quyền truy cập database.
- Nếu dùng Windows Authentication, cần cấu hình JDBC tương ứng với môi trường của bạn; chỉ để trống username không tự động bảo đảm Windows Authentication hoạt động.
- Không đưa mật khẩu thật lên GitHub/GitLab. File `application.properties` nên được giữ riêng trên máy; chỉ commit file mẫu không chứa thông tin bí mật.

Lưu ý: file cấu hình mẫu trong project có thể chứa nhiều dòng cấu hình cũ. Hãy giữ lại **một bộ** `db.url`, `db.username`, `db.password` đúng với máy của bạn để tránh nhầm lẫn. Database do `schema.sql` tạo tên là `QL_NHA_TRO`.

## 5. Biên dịch và chạy chương trình

Mở Terminal/Command Prompt tại thư mục gốc project (nơi có `pom.xml`) và chạy:

```bash
mvn clean compile
```

Nếu biên dịch thành công, chạy ứng dụng bằng Maven Exec Plugin:

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass="com.quanlydaytro.Main"
```

Nếu máy chưa có Maven, cài Maven hoặc mở project bằng IntelliJ IDEA/Eclipse, chọn JDK 17, tải dependency Maven, sau đó chạy lớp `com.quanlydaytro.Main`.

Nếu gặp lỗi kết nối database, kiểm tra lần lượt: SQL Server service đang chạy; tên server/instance và cổng đúng; database `QL_NHA_TRO` đã được tạo; tài khoản/mật khẩu chính xác; quyền truy cập database; và các giá trị trong `application.properties`.

## 6. Cấu trúc thư mục chính

- `src/main/java/` – mã nguồn Java, gồm các lớp giao diện, service, DAO, cấu hình và tiện ích.
- `src/main/resources/` – tài nguyên/cấu hình ứng dụng.
- `database/` – script SQL tạo cấu trúc database, hàm, view, procedure, trigger, index và dữ liệu mẫu.
- `docs/` – tài liệu mô tả module, quy ước phát triển và test case.
- `pom.xml` – cấu hình Maven và thư viện JDBC cho SQL Server.

## 7. File backup cơ sở dữ liệu là gì?

“File backup CSDL SQL Server (.bak) hoặc script (.sql) tạo lại toàn bộ CSDL” nghĩa là khi nộp project, cần kèm một cách để người khác khôi phục/tạo lại database mà không phải tự thiết kế lại các bảng.

### Cách A – File `.bak`

`.bak` là bản sao lưu do SQL Server tạo ra, thường chứa cấu trúc và dữ liệu tại thời điểm sao lưu. Trong SSMS, chọn database → **Tasks → Back Up…** → Backup type **Full** → chọn đường dẫn lưu file `.bak` → **OK**. Khi khôi phục, dùng **Databases → Restore Database… → Device** và chọn file `.bak`.

### Cách B – Các file `.sql`

`.sql` là các câu lệnh SQL có thể chạy để tạo database, bảng, ràng buộc, đối tượng SQL và/hoặc dữ liệu. Trong project này, thư mục `database/` đã có nhiều script phục vụ việc dựng lại database. Vì vậy, nếu giảng viên chấp nhận script SQL thay cho `.bak`, hãy nộp đầy đủ thư mục này và hướng dẫn thứ tự chạy ở mục 3. Không nên chỉ nộp riêng `insert-data.sql`, vì file đó chỉ thêm dữ liệu và không tự tạo toàn bộ cấu trúc database.

**Lưu ý với bản ZIP hiện tại:** tôi thấy có thư mục `database/` chứa các script `.sql`; không thấy file `.bak` trong danh sách file của gói này. Do đó, hiện có thể dùng bộ script SQL để dựng database theo hướng dẫn, hoặc tự tạo thêm file `.bak` từ database đã chạy thành công nếu yêu cầu nộp bài ưu tiên bản backup.

## 8. Quản lý mã nguồn bằng GitHub/GitLab

Tại thư mục gốc project, có thể dùng các lệnh sau để khởi tạo repository và lưu lịch sử commit:

```bash
git init
git add .
git commit -m "Initial commit - Quan Ly Day Tro"
```

Tạo repository trống trên GitHub/GitLab rồi liên kết và đẩy mã nguồn theo URL repository của bạn:

```bash
git branch -M main
git remote add origin <URL_REPOSITORY_CUA_BAN>
git push -u origin main
```

Trước khi commit, kiểm tra để không đưa mật khẩu database, dữ liệu cá nhân hoặc file backup chứa dữ liệu nhạy cảm lên repository công khai. Nên sử dụng `.gitignore` cho thư mục `target/`, file cấu hình thật `src/main/resources/application.properties` và các file môi trường riêng.

## 9. Khắc phục lỗi thường gặp

- **Login/database connection failed:** kiểm tra cấu hình `application.properties`, SQL Server service, instance và quyền database.
- **Invalid object name / không tìm thấy bảng:** chạy `schema.sql` và các script tạo đối tượng theo thứ tự ở mục 3.
- **Duplicate object / object already exists:** database có thể đã được tạo trước đó; không chạy lại toàn bộ script một cách mù quáng. Sao lưu trước và kiểm tra trạng thái database.
- **Maven không nhận lệnh `mvn`:** cài Maven, thêm Maven vào `PATH`, hoặc dùng Maven tích hợp trong IDE.
- **Sai phiên bản Java:** kiểm tra `java -version`; project cấu hình Maven biên dịch với Java 17.

---

Tài liệu này dựa trên cấu trúc và cấu hình có trong gói mã nguồn. Các thông tin phụ thuộc máy cá nhân như SQL Server instance, tài khoản và mật khẩu phải được thay bằng giá trị thực tế trước khi chạy.
