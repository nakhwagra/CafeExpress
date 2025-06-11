package com.ooproject.dto;

import java.time.LocalDateTime;

public class TransaksiDetailDTO {
    private Long transaksiId;
    private LocalDateTime tanggal;
    private Double total;
    private String alamat;
    private String metodePembayaran;
    private String namaMenu;
    private int jumlah;
    private String username;
     private String menuDetails;

    // Constructor
    public TransaksiDetailDTO(Long transaksiId, LocalDateTime tanggal, Double total, String alamat,
                              String metodePembayaran, String namaMenu, int jumlah, String username) {
        this.transaksiId = transaksiId;
        this.tanggal = tanggal;
        this.total = total;
        this.alamat = alamat;
        this.metodePembayaran = metodePembayaran;
        this.namaMenu = namaMenu;
        this.jumlah = jumlah;
        this.username = username;
    }

    // Getters and Setters
    public Long getTransaksiId() {
        return transaksiId;
    }

    public void setTransaksiId(Long transaksiId) {
        this.transaksiId = transaksiId;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}