package com.quanlydaytro.service;

import com.quanlydaytro.dao.GiaDienDAO;
import com.quanlydaytro.model.GiaDien;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module GiaDien. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class GiaDienService {
    private final GiaDienDAO dao;
    public GiaDienService() { this(new GiaDienDAO()); }
    public GiaDienService(GiaDienDAO dao) { this.dao = dao; }
    public List<GiaDien> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<GiaDien> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(GiaDien entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, GiaDien entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
