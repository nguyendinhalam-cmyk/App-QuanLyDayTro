package com.quanlydaytro.service;

import com.quanlydaytro.dao.ChiSoNuocDAO;
import com.quanlydaytro.model.ChiSoNuoc;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module ChiSoNuoc. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class ChiSoNuocService {
    private final ChiSoNuocDAO dao;
    public ChiSoNuocService() { this(new ChiSoNuocDAO()); }
    public ChiSoNuocService(ChiSoNuocDAO dao) { this.dao = dao; }
    public List<ChiSoNuoc> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<ChiSoNuoc> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(ChiSoNuoc entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, ChiSoNuoc entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
