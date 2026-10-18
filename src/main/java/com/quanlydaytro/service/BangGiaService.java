package com.quanlydaytro.service;

/**
 * Điểm truy cập nghiệp vụ chung cho quản lý giá điện và giá nước.
 */
public class BangGiaService {
    private final GiaDienService giaDienService;
    private final GiaNuocService giaNuocService;

    public BangGiaService() {
        this(new GiaDienService(), new GiaNuocService());
    }

    public BangGiaService(GiaDienService dien, GiaNuocService nuoc) {
        this.giaDienService = dien;
        this.giaNuocService = nuoc;
    }

    public GiaDienService getGiaDienService() {
        return giaDienService;
    }

    public GiaNuocService getGiaNuocService() {
        return giaNuocService;
    }
}
