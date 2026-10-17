package com.quanlydaytro.service;

import com.quanlydaytro.dao.NguoiThueDAO;
import com.quanlydaytro.model.NguoiThue;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** API nền tảng cho module NguoiThue. Quy tắc nghiệp vụ đặc thù sẽ được bổ sung tại đây. */
public class NguoiThueService {
    private final NguoiThueDAO dao;
    public NguoiThueService() { this(new NguoiThueDAO()); }
    public NguoiThueService(NguoiThueDAO dao) { this.dao = dao; }
    public List<NguoiThue> layTatCa() throws SQLException { return dao.findAll(); }
    public Optional<NguoiThue> timTheoMa(int ma) throws SQLException { return dao.findById(ma); }
    public int them(NguoiThue entity) throws SQLException { if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.insert(entity); }
    public boolean capNhat(int ma, NguoiThue entity) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); if(entity == null) throw new IllegalArgumentException("Dữ liệu không được null"); return dao.update(ma,entity); }
    public boolean xoa(int ma) throws SQLException { if(ma <= 0) throw new IllegalArgumentException("Mã phải lớn hơn 0"); return dao.delete(ma); }
}
