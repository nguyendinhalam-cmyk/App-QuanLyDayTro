package com.quanlydaytro.service;

import com.quanlydaytro.dao.VaiTroDAO;
import com.quanlydaytro.model.VaiTro;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module VaiTro. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class VaiTroService {
    private final VaiTroDAO dao;
    public VaiTroService() { this(new VaiTroDAO()); }
    public VaiTroService(VaiTroDAO dao) { this.dao = dao; }
    public List<VaiTro> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<VaiTro> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(VaiTro entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, VaiTro entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
