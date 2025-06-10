package com.ooproject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Transaksi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime tanggal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // private Customer customer;

    private Double total;

    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransaksiMenu> transaksiMenus;

    @Column(name = "alamat", length = 255)
    private String alamat;

    @Column(name = "metode_pembayaran", length = 50)
    private String metodePembayaran;

    // + getter dan setter
    


    // Constructors
    public Transaksi() {}

    public Transaksi(LocalDateTime tanggal, Double total) {
        this.tanggal = tanggal;
        // this.customer = customer;
        this.total = total;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    // public Customer getCustomer() { return customer; }
    // public void setCustomer(Customer customer) { this.customer = customer; }

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