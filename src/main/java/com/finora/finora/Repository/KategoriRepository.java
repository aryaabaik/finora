package com.finora.finora.Repository;

import com.finora.finora.Model.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KategoriRepository extends JpaRepository<Kategori, Long> {

    List<Kategori> findByUserId(Long userId);

    @Query("""
            SELECT DISTINCT k
            FROM Kategori k
            LEFT JOIN k.user owner
            WHERE owner.id = :userId
               OR (
                    owner IS NULL
                    AND (
                        EXISTS (
                            SELECT p.id
                            FROM Pemasukan p
                            WHERE p.kategori = k AND p.user.id = :userId
                        )
                        OR EXISTS (
                            SELECT e.id
                            FROM Pengeluaran e
                            WHERE e.kategori = k AND e.user.id = :userId
                        )
                    )
               )
            """)
    List<Kategori> findAvailableByUserId(@Param("userId") Long userId);
}