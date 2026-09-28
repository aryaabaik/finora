package com.finora.finora.Dto;

import java.math.BigDecimal;
import java.util.List;

public class TemplateResponse {
    private Long id;
    private String nama;
    private String jenis;
    private List<TemplateItemResponse> items;
    private BigDecimal totalJumlah;

    public TemplateResponse() {}

    public TemplateResponse(Long id, String nama, String jenis, List<TemplateItemResponse> items) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.items = items;
        if (items != null) {
            this.totalJumlah = items.stream()
                    .map(TemplateItemResponse::getJumlah)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalJumlah = BigDecimal.ZERO;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public List<TemplateItemResponse> getItems() { return items; }
    public void setItems(List<TemplateItemResponse> items) { this.items = items; }
    public BigDecimal getTotalJumlah() { return totalJumlah; }
    public void setTotalJumlah(BigDecimal totalJumlah) { this.totalJumlah = totalJumlah; }
}
