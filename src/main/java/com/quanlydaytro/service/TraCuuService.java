package com.quanlydaytro.service;

import com.quanlydaytro.dao.TraCuuDAO;

import java.sql.SQLException;
import java.util.List;

/**
 * Lớp nghiệp vụ cho chức năng tra cứu thông tin tổng hợp.
 */
public class TraCuuService {
    private final TraCuuDAO dao = new TraCuuDAO();

    public List<Object[]> traCuu(String loai, String tuKhoa) throws SQLException {
        if (loai == null || loai.isBlank())
            throw new IllegalArgumentException("Vui lòng chọn loại thông tin cần tra cứu.");
        if (tuKhoa != null && tuKhoa.length() > 100)
            throw new IllegalArgumentException("Từ khóa không được vượt quá 100 ký tự.");
        return dao.traCuu(loai, tuKhoa);
    }
}
