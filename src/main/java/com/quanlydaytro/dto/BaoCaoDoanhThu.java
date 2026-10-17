package com.quanlydaytro.dto;

import java.math.BigDecimal;

/** DTO kết quả thống kê doanh thu theo kỳ; không phải bảng SQL. */
public class BaoCaoDoanhThu {
    private String kyBaoCao;
    private BigDecimal doanhThu;
    public BaoCaoDoanhThu() {}
    public BaoCaoDoanhThu(String kyBaoCao, BigDecimal doanhThu) { this.kyBaoCao=kyBaoCao; this.doanhThu=doanhThu; }
    public String getKyBaoCao(){return kyBaoCao;} public void setKyBaoCao(String v){kyBaoCao=v;}
    public BigDecimal getDoanhThu(){return doanhThu;} public void setDoanhThu(BigDecimal v){doanhThu=v;}
}
