package com.quanlydaytro.service;

import com.quanlydaytro.dao.DayTroDAO;
import com.quanlydaytro.model.DayTro;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module DayTro. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class DayTroService {
    private final DayTroDAO dao;
    public DayTroService() { this(new DayTroDAO()); }
    public DayTroService(DayTroDAO dao) { this.dao = dao; }
    public List<DayTro> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<DayTro> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(DayTro entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, DayTro entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
