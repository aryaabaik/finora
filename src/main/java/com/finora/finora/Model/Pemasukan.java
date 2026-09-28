package com.finora.finora.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "pemasukan")
public  class Pemasukan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate tanggal;
    private BigDecimal jumlah;
    private String keterangan;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public BigDecimal getJumlah() {
        return jumlah;
    }

    public void setJumlah(BigDecimal jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }
}