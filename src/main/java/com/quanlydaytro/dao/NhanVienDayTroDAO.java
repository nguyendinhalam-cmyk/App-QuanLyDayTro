package com.quanlydaytro.dao;
import com.quanlydaytro.connection.DatabaseConnection;
import com.quanlydaytro.model.NhanVienDayTro;
import java.sql.*; import java.util.*;
/** DAO chuyên biệt cho khóa chính ghép NHANVIEN_DAYTRO. */
public class NhanVienDayTroDAO {
    public List<NhanVienDayTro> findByNhanVien(int maNV) throws SQLException { List<NhanVienDayTro> out=new ArrayList<>(); String sql="SELECT MaNV, MaDay FROM NHANVIEN_DAYTRO WHERE MaNV=? ORDER BY MaDay"; try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,maNV);try(ResultSet r=p.executeQuery()){while(r.next())out.add(new NhanVienDayTro(r.getInt("MaNV"),r.getInt("MaDay")));}} return out; }
    public boolean assign(int maNV,int maDay) throws SQLException { String sql="INSERT INTO NHANVIEN_DAYTRO(MaNV,MaDay) VALUES(?,?)"; try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,maNV);p.setInt(2,maDay);return p.executeUpdate()==1;} }
    public boolean unassign(int maNV,int maDay) throws SQLException {try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM NHANVIEN_DAYTRO WHERE MaNV=? AND MaDay=?")){p.setInt(1,maNV);p.setInt(2,maDay);return p.executeUpdate()==1;}}
}
