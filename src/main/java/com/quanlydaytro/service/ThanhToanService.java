package com.quanlydaytro.service;

import com.quanlydaytro.dao.ThanhToanDAO;
import com.quanlydaytro.model.ThanhToan;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module ThanhToan. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class ThanhToanService {
    private final ThanhToanDAO dao;
    public ThanhToanService() { this(new ThanhToanDAO()); }
    public ThanhToanService(ThanhToanDAO dao) { this.dao = dao; }
    public List<ThanhToan> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<ThanhToan> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(ThanhToan entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, ThanhToan entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
