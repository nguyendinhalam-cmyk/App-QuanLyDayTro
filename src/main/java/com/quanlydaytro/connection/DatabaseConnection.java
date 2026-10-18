package com.quanlydaytro.connection;

import com.quanlydaytro.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = AppConfig.get("db.url", "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=QL_NHA_TRO;encrypt=true;trustServerCertificate=true");
        String user = AppConfig.get("db.username", "sa");
        String pass = AppConfig.get("db.password", "123456");
        return user.isBlank() ? DriverManager.getConnection(url) : DriverManager.getConnection(url, user, pass);
    }
}
