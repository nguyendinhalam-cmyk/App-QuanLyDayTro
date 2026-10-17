package com.quanlydaytro.service;

import com.quanlydaytro.dao.HopDongDAO;
import com.quanlydaytro.model.HopDong;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module HopDong. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class HopDongService {
    private final HopDongDAO dao;
    public HopDongService() { this(new HopDongDAO()); }
    public HopDongService(HopDongDAO dao) { this.dao = dao; }
    public List<HopDong> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<HopDong> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(HopDong entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, HopDong entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
