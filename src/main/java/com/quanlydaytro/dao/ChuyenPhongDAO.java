package com.quanlydaytro.dao;
import com.quanlydaytro.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
/** Lớp truy cập dữ liệu. Thêm truy vấn khớp schema SQL Server của nhóm. */
public class ChuyenPhongDAO {
 protected Connection moKetNoi() throws SQLException { return DatabaseConnection.getConnection(); }
}
