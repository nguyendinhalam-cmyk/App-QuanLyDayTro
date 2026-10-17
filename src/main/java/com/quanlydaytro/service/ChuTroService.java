package com.quanlydaytro.service;

import com.quanlydaytro.dao.ChuTroDAO;
import com.quanlydaytro.model.ChuTro;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module ChuTro. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class ChuTroService {
    private final ChuTroDAO dao;
    public ChuTroService() { this(new ChuTroDAO()); }
    public ChuTroService(ChuTroDAO dao) { this.dao = dao; }
    public List<ChuTro> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<ChuTro> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(ChuTro entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, ChuTro entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
