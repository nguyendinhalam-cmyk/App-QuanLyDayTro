package com.quanlydaytro.service;

import com.quanlydaytro.dao.TaiKhoanDAO;
import com.quanlydaytro.model.TaiKhoan;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module TaiKhoan. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class TaiKhoanService {
    private final TaiKhoanDAO dao;
    public TaiKhoanService() { this(new TaiKhoanDAO()); }
    public TaiKhoanService(TaiKhoanDAO dao) { this.dao = dao; }
    public List<TaiKhoan> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<TaiKhoan> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(TaiKhoan entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, TaiKhoan entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
