package com.finora.finora.Service;

import com.finora.finora.Model.Pengeluaran;
import com.finora.finora.Repository.PengeluaranRepository;
import com.finora.finora.Repository.PemasukanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PengeluaranService {

    private final PengeluaranRepository pengeluaranRepository;
    private final PemasukanRepository pemasukanRepository;

    public PengeluaranService(
            PengeluaranRepository pengeluaranRepository,
            PemasukanRepository pemasukanRepository) {

        this.pengeluaranRepository = pengeluaranRepository;
        this.pemasukanRepository = pemasukanRepository;
    }

    public Pengeluaran save(Pengeluaran pengeluaran) {

        Long userId = pengeluaran.getUser().getId();

        BigDecimal totalPemasukan =
                pemasukanRepository.findByUserId(userId)
                        .stream()
                        .map(p -> p.getJumlah())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPengeluaran =
                pengeluaranRepository.findByUserId(userId)
                        .stream()
                        .map(p -> p.getJumlah())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo =
                totalPemasukan.subtract(totalPengeluaran);

        if (pengeluaran.getJumlah().compareTo(saldo) > 0) {
            throw new RuntimeException("Saldo Anda tidak cukup");
        }

        return pengeluaranRepository.save(pengeluaran);
    }

    public List<Pengeluaran> getAll() {
        return pengeluaranRepository.findAll();
    }

    public List<Pengeluaran> getByUserId(Long userId) {
        List<Pengeluaran> data =
                pengeluaranRepository.findByUserId(userId);

        data.sort(
                Comparator.comparing(Pengeluaran::getTanggal).reversed()
        );

        return data;
    }

    public Map<String, Object> getFiltered(Long userId, int page, int size, String sort,
                                                Long categoryId, LocalDate startDate, LocalDate endDate) {
        Sort.Direction direction = "terlama".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "tanggal"));

        Page<Pengeluaran> result = pengeluaranRepository.findByFilters(userId, categoryId, startDate, endDate, pageable);
        BigDecimal totalIncome = pengeluaranRepository.sumJumlahByFilters(userId, categoryId, startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());
        response.put("currentPage", result.getNumber());
        response.put("totalIncome", totalIncome);
        return response;
    }

    public List<Pengeluaran> getByUserIdAndTanggalBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Pengeluaran> data =
                pengeluaranRepository.findByUserIdAndTanggalBetween(userId, startDate, endDate);

        data.sort(
                Comparator.comparing(Pengeluaran::getTanggal).reversed()
        );

        return data;
    }

    public Pengeluaran getById(Long id) {
        return pengeluaranRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pengeluaran not found with id: " + id
                        )
                );
    }

    public void deleteById(Long id) {
        pengeluaranRepository.deleteById(id);
    }
}