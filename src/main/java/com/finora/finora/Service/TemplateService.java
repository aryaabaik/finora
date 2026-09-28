package com.finora.finora.Service;

import com.finora.finora.Model.Kategori;
import com.finora.finora.Model.Pemasukan;
import com.finora.finora.Model.Pengeluaran;
import com.finora.finora.Model.Template;
import com.finora.finora.Model.TemplateItem;
import com.finora.finora.Model.User;
import com.finora.finora.Repository.KategoriRepository;
import com.finora.finora.Repository.PemasukanRepository;
import com.finora.finora.Repository.PengeluaranRepository;
import com.finora.finora.Repository.TemplateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final KategoriRepository kategoriRepository;
    private final PemasukanRepository pemasukanRepository;
    private final PengeluaranRepository pengeluaranRepository;

    public TemplateService(
            TemplateRepository templateRepository,
            KategoriRepository kategoriRepository,
            PemasukanRepository pemasukanRepository,
            PengeluaranRepository pengeluaranRepository
    ) {
        this.templateRepository = templateRepository;
        this.kategoriRepository = kategoriRepository;
        this.pemasukanRepository = pemasukanRepository;
        this.pengeluaranRepository = pengeluaranRepository;
    }

    // =========================
    // GET TEMPLATE USER
    // =========================
    public List<Template> getTemplatesByUser(User user) {
        return templateRepository.findByUser(user);
    }


    // =========================
    // GET KATEGORI USER
    // =========================
    public List<Kategori> getKategoriByUser(User user) {
        return kategoriRepository.findByUserId(user.getId());
    }


    // =========================
    // GET TEMPLATE BY ID
    // =========================
    public Template getTemplateById(Long id) {
        return templateRepository.findById(id).orElse(null);
    }


    // =========================
    // SAVE TEMPLATE
    // =========================
    public Template saveTemplate(Template template) {
        return templateRepository.save(template);
    }


    // =========================
    // DELETE TEMPLATE
    // =========================
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }


    // =========================
    // CREATE TEMPLATE
    // =========================
    public Template createTemplate(Template template, User user) {

        template.setUser(user);

        if (template.getItems() != null) {

            for (TemplateItem item : template.getItems()) {
                item.setTemplate(template);
            }

        }

        return templateRepository.save(template);
    }


    // =========================
    // GUNAKAN ITEM TEMPLATE
    // =========================
    public void gunakanTemplateItem(Long templateId, Long itemId, User user) {

        Template template = templateRepository.findById(templateId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Template tidak ditemukan")
                );

        // Pastikan template milik user yang sedang login
        if (template.getUser() == null ||
                !template.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Template bukan milik user ini"
            );
        }

        // Cari item dan pastikan item milik template ini
        TemplateItem targetItem = null;
        if (template.getItems() != null) {
            for (TemplateItem item : template.getItems()) {
                if (item.getId() != null && item.getId().equals(itemId)) {
                    targetItem = item;
                    break;
                }
            }
        }

        if (targetItem == null) {
            throw new IllegalArgumentException(
                    "Item template tidak ditemukan atau bukan milik template ini"
            );
        }

        LocalDate tanggalHariIni = LocalDate.now();

        // =========================
        // TEMPLATE PEMASUKAN
        // =========================
        if (template.getJenis().name().equals("PEMASUKAN")) {
            Pemasukan pemasukan = new Pemasukan();
            pemasukan.setTanggal(tanggalHariIni);
            pemasukan.setJumlah(targetItem.getJumlah());
            pemasukan.setKeterangan(targetItem.getKeterangan());
            pemasukan.setKategori(targetItem.getKategori());
            pemasukan.setUser(user);
            pemasukanRepository.save(pemasukan);
        }

        // =========================
        // TEMPLATE PENGELUARAN
        // =========================
        else if (template.getJenis().name().equals("PENGELUARAN")) {
            Pengeluaran pengeluaran = new Pengeluaran();
            pengeluaran.setTanggal(tanggalHariIni);
            pengeluaran.setJumlah(targetItem.getJumlah());
            pengeluaran.setKeterangan(targetItem.getKeterangan());
            pengeluaran.setKategori(targetItem.getKategori());
            pengeluaran.setUser(user);
            pengeluaranRepository.save(pengeluaran);
        }
    }


    // =========================
    // GUNAKAN TEMPLATE
    // =========================
    public void gunakanTemplate(Long templateId, User user) {

        Template template = templateRepository.findById(templateId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Template tidak ditemukan")
                );


        // Pastikan template milik user yang sedang login
        if (template.getUser() == null ||
                !template.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Template bukan milik user ini"
            );
        }


        LocalDate tanggalHariIni = LocalDate.now();


        // =========================
        // TEMPLATE PEMASUKAN
        // =========================
        if (template.getJenis().name().equals("PEMASUKAN")) {

            for (TemplateItem item : template.getItems()) {

                Pemasukan pemasukan = new Pemasukan();

                pemasukan.setTanggal(tanggalHariIni);
                pemasukan.setJumlah(item.getJumlah());
                pemasukan.setKeterangan(item.getKeterangan());
                pemasukan.setKategori(item.getKategori());
                pemasukan.setUser(user);

                pemasukanRepository.save(pemasukan);
            }
        }


        // =========================
        // TEMPLATE PENGELUARAN
        // =========================
        else if (template.getJenis().name().equals("PENGELUARAN")) {

            for (TemplateItem item : template.getItems()) {

                Pengeluaran pengeluaran = new Pengeluaran();

                pengeluaran.setTanggal(tanggalHariIni);
                pengeluaran.setJumlah(item.getJumlah());
                pengeluaran.setKeterangan(item.getKeterangan());
                pengeluaran.setKategori(item.getKategori());
                pengeluaran.setUser(user);

                pengeluaranRepository.save(pengeluaran);
            }
        }
    }
}
