package com.finora.finora.Service;

import com.finora.finora.Model.Kategori;
import com.finora.finora.Repository.KategoriRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KategoriService {

    private final KategoriRepository kategoriRepository;

    public KategoriService(KategoriRepository kategoriRepository) {
        this.kategoriRepository = kategoriRepository;
    }

    public Kategori save(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    public List<Kategori> getAll() {
        return kategoriRepository.findAll();
    }

    // Ambil kategori milik user tertentu
    public List<Kategori> getByUserId(Long userId) {
        return kategoriRepository.findByUserId(userId);
    }

    public Kategori getById(Long id) {
        return kategoriRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Kategori not found with id: " + id
                        )
                );
    }

    public void deleteById(Long id) {
        kategoriRepository.deleteById(id);
    }
}