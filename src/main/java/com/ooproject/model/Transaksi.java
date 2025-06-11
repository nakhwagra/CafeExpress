package com.ooproject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transaksi") // Pastikan nama tabelnya 'transaksi' di DB
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime tanggal;

    // ✅ Ganti dari 'Customer customer' menjadi 'User user'
    @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY disarankan
    @JoinColumn(name = "user_id") // Pastikan nama kolom di DB Anda memang 'user_id'
    private User user; // <-- Ini harus menunjuk ke model User Anda

    private Double total;

    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaksiMenu> transaksiMenus;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "metode_pembayaran", length = 50)
    private String metodePembayaran;

    // Constructors
    public Transaksi() {}

    public Transaksi(LocalDateTime tanggal, Double total) {
        this.tanggal = tanggal;
        this.total = total;
    }

    // Getters & Setters untuk semua properti
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    // ✅ Tambahkan getter dan setter untuk User
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public List<TransaksiMenu> getTransaksiMenus() {
        return transaksiMenus;
    }

    public void setTransaksiMenus(List<TransaksiMenu> transaksiMenus) {
        this.transaksiMenus = transaksiMenus;
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
}