package com.quanlydaytro.service;

import com.quanlydaytro.dao.NhanVienDAO;
import com.quanlydaytro.model.NhanVien;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module NhanVien. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class NhanVienService {
    private final NhanVienDAO dao;
    public NhanVienService() { this(new NhanVienDAO()); }
    public NhanVienService(NhanVienDAO dao) { this.dao = dao; }
    public List<NhanVien> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<NhanVien> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(NhanVien entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, NhanVien entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
