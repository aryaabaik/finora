package com.finora.finora.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "template_items")
public class TemplateItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String keterangan;

    private BigDecimal jumlah;

    @ManyToOne
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public BigDecimal getJumlah() {
        return jumlah;
    }

    public void setJumlah(BigDecimal jumlah) {
        this.jumlah = jumlah;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}
