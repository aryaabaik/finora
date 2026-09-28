package com.finora.finora.Service;

import com.finora.finora.Model.Pemasukan;
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
public class PemasukanService {

    private final PemasukanRepository pemasukanRepository;

    public PemasukanService(PemasukanRepository pemasukanRepository) {
        this.pemasukanRepository = pemasukanRepository;
    }

    public Pemasukan save(Pemasukan pemasukan) {
        return pemasukanRepository.save(pemasukan);
    }

    public List<Pemasukan> getAll() {
        return pemasukanRepository.findAll();
    }

    public List<Pemasukan> getByUserId(Long userId) {
        List<Pemasukan> data = pemasukanRepository.findByUserId(userId);
        data.sort(Comparator.comparing(Pemasukan::getTanggal).reversed());
        return data;
    }

    public Map<String, Object> getFiltered(Long userId, int page, int size, String sort,
                                           Long categoryId, LocalDate startDate, LocalDate endDate) {
        Sort.Direction direction = "terlama".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "tanggal"));

        Page<Pemasukan> result = pemasukanRepository.findByFilters(userId, categoryId, startDate, endDate, pageable);
        BigDecimal totalIncome = pemasukanRepository.sumJumlahByFilters(userId, categoryId, startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("totalElements", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());
        response.put("currentPage", result.getNumber());
        response.put("totalIncome", totalIncome);
        return response;
    }

    public List<Pemasukan> getByUserIdAndTanggalBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return pemasukanRepository.findByUserIdAndTanggalBetween(userId, startDate, endDate);
    }

    public Pemasukan getById(Long id) {
        return pemasukanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pemasukan tidak ditemukan"));
    }

    public void delete(Long id) {
        pemasukanRepository.deleteById(id);
    }
}