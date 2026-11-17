package com.finora.finora.Service;

import com.finora.finora.Model.Kategori;
import com.finora.finora.Repository.KategoriRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public KategoriService(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    @Transactional
    public Kategori save(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    public List<Kategori> getAll() {
        return kategoriRepository.findAll();
    }

    // Ambil kategori milik user tertentu
    public List<Kategori> getByUserId(Long userId) {
        return kategoriRepository.findAvailableByUserId(userId);
    }

    public Kategori getById(Long id) {
        return kategoriRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Kategori not found with id: " + id
                        )
                );
    }

    @Transactional
    public void deleteById(Long id) {
        kategoriRepository.deleteById(id);
    }
}