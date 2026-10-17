package com.quanlydaytro.dto;

/** Kết quả đăng nhập trả về UI; tuyệt đối không chứa mật khẩu. */
public class KetQuaDangNhap {
    private final boolean thanhCong;
    private final String thongBao;
    private final int maTaiKhoan;
    private final String tenDangNhap;
    private final int maNhanVien;
    private final int maVaiTro;
    private final String tenVaiTro;
    public KetQuaDangNhap(boolean thanhCong, String thongBao, int maTaiKhoan) {
        this(thanhCong, thongBao, maTaiKhoan, null, 0, 0, null);
    }
    public KetQuaDangNhap(boolean thanhCong, String thongBao, int maTaiKhoan, String tenDangNhap, int maNhanVien, int maVaiTro, String tenVaiTro) {
        this.thanhCong=thanhCong; this.thongBao=thongBao; this.maTaiKhoan=maTaiKhoan; this.tenDangNhap=tenDangNhap;
        this.maNhanVien=maNhanVien; this.maVaiTro=maVaiTro; this.tenVaiTro=tenVaiTro;
    }
    public boolean isThanhCong(){return thanhCong;} public String getThongBao(){return thongBao;}
    public int getMaTaiKhoan(){return maTaiKhoan;} public String getTenDangNhap(){return tenDangNhap;}
    public int getMaNhanVien(){return maNhanVien;} public int getMaVaiTro(){return maVaiTro;} public String getTenVaiTro(){return tenVaiTro;}
}
