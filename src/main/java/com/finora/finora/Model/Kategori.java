package com.finora.finora.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "kategori")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nama;
    @Enumerated(EnumType.STRING)
    private JenisKategori jenis;

    // Kategori milik user tertentu
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public JenisKategori getJenis() {
        return jenis;
    }

    public void setJenis(JenisKategori jenis) {
        this.jenis = jenis;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}