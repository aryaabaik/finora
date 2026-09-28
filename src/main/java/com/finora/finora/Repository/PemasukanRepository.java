package com.finora.finora.Repository;

import com.finora.finora.Model.Pemasukan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PemasukanRepository extends JpaRepository<Pemasukan, Long> {

    List<Pemasukan> findByUserId(Long userId);
    List<Pemasukan> findByUserIdAndTanggalBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT p FROM Pemasukan p WHERE p.user.id = :userId "
         + "AND p.kategori.id = COALESCE(:categoryId, p.kategori.id) "
         + "AND p.tanggal >= COALESCE(:startDate, p.tanggal) "
         + "AND p.tanggal <= COALESCE(:endDate, p.tanggal)")
    Page<Pemasukan> findByFilters(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.jumlah), 0) FROM Pemasukan p WHERE p.user.id = :userId "
         + "AND p.kategori.id = COALESCE(:categoryId, p.kategori.id) "
         + "AND p.tanggal >= COALESCE(:startDate, p.tanggal) "
         + "AND p.tanggal <= COALESCE(:endDate, p.tanggal)")
    BigDecimal sumJumlahByFilters(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}