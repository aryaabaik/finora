package com.finora.finora.Repository;

import com.finora.finora.Model.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KategoriRepository extends JpaRepository<Kategori, Long> {

    List<Kategori> findByUserId(Long userId);
}