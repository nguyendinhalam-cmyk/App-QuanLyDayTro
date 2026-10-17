package com.quanlydaytro.dao;
import com.quanlydaytro.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class ChuyenPhongDAO {
    protected Connection moKetNoi() throws SQLException { return DatabaseConnection.getConnection(); }
}
