package com.quanlydaytro.dao;

import com.quanlydaytro.connection.DatabaseConnection;
import java.sql.*;
import java.util.*;

/** DAO nền: truy vấn JDBC dùng PreparedStatement, không ghép dữ liệu người dùng vào SQL. */
public abstract class AbstractJdbcDAO<T> {
    protected abstract String tableName();
    protected abstract String idColumn();
    protected abstract String[] columns(); // các cột không phải khóa chính identity
    protected abstract Object[] values(T entity);
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    public List<T> findAll() throws SQLException {
        String sql = "SELECT * FROM " + tableName() + " ORDER BY " + (idColumn() == null ? columns()[0] : idColumn());
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<T> result = new ArrayList<>(); while (rs.next()) result.add(mapRow(rs)); return result;
        }
    }
    public Optional<T> findById(int id) throws SQLException {
        if (idColumn() == null) throw new UnsupportedOperationException("Bảng dùng khóa chính ghép, hãy dùng DAO chuyên biệt.");
        String sql = "SELECT * FROM " + tableName() + " WHERE " + idColumn() + " = ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,id); try (ResultSet rs=ps.executeQuery()) { return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty(); }
        }
    }
    public int insert(T entity) throws SQLException {
        StringJoiner names=new StringJoiner(", "), marks=new StringJoiner(", ");
        for (String col:columns()) { names.add(col); marks.add("?"); }
        String sql="INSERT INTO "+tableName()+" ("+names+") VALUES ("+marks+")";
        try (Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps,values(entity)); int n=ps.executeUpdate();
            try(ResultSet keys=ps.getGeneratedKeys()){ if(keys.next()) return keys.getInt(1); }
            return n;
        }
    }
    public boolean update(int id, T entity) throws SQLException {
        if (idColumn()==null) throw new UnsupportedOperationException("Bảng dùng khóa chính ghép, hãy dùng DAO chuyên biệt.");
        StringJoiner set=new StringJoiner(", "); for(String col:columns()) set.add(col+" = ?");
        String sql="UPDATE "+tableName()+" SET "+set+" WHERE "+idColumn()+" = ?";
        try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            Object[] vals=values(entity); bind(ps,vals); ps.setInt(vals.length+1,id); return ps.executeUpdate()>0;
        }
    }
    public boolean delete(int id) throws SQLException {
        if (idColumn()==null) throw new UnsupportedOperationException("Bảng dùng khóa chính ghép, hãy dùng DAO chuyên biệt.");
        try(Connection c=DatabaseConnection.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM "+tableName()+" WHERE "+idColumn()+" = ?")) { ps.setInt(1,id); return ps.executeUpdate()>0; }
    }
    private void bind(PreparedStatement ps,Object[] vals) throws SQLException { for(int i=0;i<vals.length;i++) ps.setObject(i+1,vals[i]); }
    protected static java.time.LocalDate localDate(ResultSet rs,String col) throws SQLException { java.sql.Date d=rs.getDate(col); return d==null?null:d.toLocalDate(); }
    protected static java.time.LocalDateTime localDateTime(ResultSet rs,String col) throws SQLException { Timestamp t=rs.getTimestamp(col); return t==null?null:t.toLocalDateTime(); }
}
