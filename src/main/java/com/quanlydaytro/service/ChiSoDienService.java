package com.quanlydaytro.service;

import com.quanlydaytro.dao.ChiSoDienDAO;
import com.quanlydaytro.model.ChiSoDien;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module ChiSoDien. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class ChiSoDienService {
    private final ChiSoDienDAO dao;
    public ChiSoDienService() { this(new ChiSoDienDAO()); }
    public ChiSoDienService(ChiSoDienDAO dao) { this.dao = dao; }
    public List<ChiSoDien> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<ChiSoDien> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(ChiSoDien entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, ChiSoDien entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
