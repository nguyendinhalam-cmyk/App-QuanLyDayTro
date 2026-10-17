package com.quanlydaytro.service;

import com.quanlydaytro.dao.GiaNuocDAO;
import com.quanlydaytro.model.GiaNuoc;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module GiaNuoc. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class GiaNuocService {
    private final GiaNuocDAO dao;
    public GiaNuocService() { this(new GiaNuocDAO()); }
    public GiaNuocService(GiaNuocDAO dao) { this.dao = dao; }
    public List<GiaNuoc> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<GiaNuoc> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(GiaNuoc entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, GiaNuoc entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
