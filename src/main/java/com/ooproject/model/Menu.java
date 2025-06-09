package com.ooproject.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;
    private Double harga;
    private String deskripsi;
    private String gambar;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaksiMenu> transaksiMenus;

    @Enumerated(EnumType.STRING)
    private FavoriteStatus favorite = FavoriteStatus.NO;

    @Enumerated(EnumType.STRING)
    private Kategori kategori;

    // Constructors
    public Menu() {}

    public Menu(String nama, Double harga, String deskripsi, String gambar) {
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public Double getHarga() { return harga; }
    public void setHarga(Double harga) { this.harga = harga; } 

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }

    public List<TransaksiMenu> getTransaksiMenus() {
        return transaksiMenus;
    }

    public void setTransaksiMenus(List<TransaksiMenu> transaksiMenus) {
        this.transaksiMenus = transaksiMenus;
    }

    public FavoriteStatus getFavorite() {
        return favorite;
    }

    public void setFavorite(FavoriteStatus favorite) {
        this.favorite = favorite;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

}