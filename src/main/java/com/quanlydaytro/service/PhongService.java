package com.quanlydaytro.service;

import com.quanlydaytro.dao.PhongDAO;
import com.quanlydaytro.model.Phong;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module Phong. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class PhongService {
    private final PhongDAO dao;
    public PhongService() { this(new PhongDAO()); }
    public PhongService(PhongDAO dao) { this.dao = dao; }
    public List<Phong> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<Phong> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(Phong entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, Phong entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
