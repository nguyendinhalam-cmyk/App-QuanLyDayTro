package com.quanlydaytro.service;

/**
 * Facade cho nghiệp vụ ghi chỉ số điện và nước.
 */
public class ChiSoService {
    private final ChiSoDienService dien = new ChiSoDienService();
    private final ChiSoNuocService nuoc = new ChiSoNuocService();

    public ChiSoDienService getDienService() {
        return dien;
    }

    public ChiSoNuocService getNuocService() {
        return nuoc;
    }
}
