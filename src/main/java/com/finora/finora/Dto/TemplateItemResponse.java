package com.finora.finora.Dto;

import java.math.BigDecimal;

public class TemplateItemResponse {
    private Long id;
    private String keterangan;
    private BigDecimal jumlah;
    private String kategori;

    public TemplateItemResponse() {}

    public TemplateItemResponse(Long id, String keterangan, BigDecimal jumlah, String kategori) {
        this.id = id;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.kategori = kategori;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public BigDecimal getJumlah() { return jumlah; }
    public void setJumlah(BigDecimal jumlah) { this.jumlah = jumlah; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
}
