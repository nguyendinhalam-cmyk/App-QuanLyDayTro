package com.quanlydaytro.service;

import com.quanlydaytro.dao.HoaDonDAO;
import com.quanlydaytro.model.HoaDon;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module HoaDon. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class HoaDonService {
    private final HoaDonDAO dao;
    public HoaDonService() { this(new HoaDonDAO()); }
    public HoaDonService(HoaDonDAO dao) { this.dao = dao; }
    public List<HoaDon> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<HoaDon> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(HoaDon entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, HoaDon entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
