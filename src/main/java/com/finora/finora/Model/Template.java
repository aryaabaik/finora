package com.finora.finora.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;

    @Enumerated(EnumType.STRING)
    private JenisKategori jenis;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
        mappedBy = "template",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<TemplateItem> items = new ArrayList<>();

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

    public List<TemplateItem> getItems() {
        return items;
    }

    public void setItems(List<TemplateItem> items) {
        this.items = items;
    }
}