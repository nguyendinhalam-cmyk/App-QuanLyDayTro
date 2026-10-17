package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;

import java.sql.*;
import java.util.*;

/**
 * Truy vấn danh mục vai trò để nạp JComboBox.
 */
public class VaiTroDanhMucDAO {
    public List<Object[]> layDanhMuc() throws SQLException {
        List<Object[]> ds = new ArrayList<>();
        String sql = "SELECT MaVaiTro,TenVaiTro FROM VAITRO ORDER BY MaVaiTro";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ds.add(new Object[]{rs.getInt(1), rs.getString(2)});
        }
        return ds;
    }
}
