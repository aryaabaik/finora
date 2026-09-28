package com.finora.finora.Service;

import com.finora.finora.Model.Pemasukan;
import com.finora.finora.Model.Pengeluaran;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LaporanService {

    private final PemasukanService pemasukanService;
    private final PengeluaranService pengeluaranService;

    public LaporanService(
            PemasukanService pemasukanService,
            PengeluaranService pengeluaranService) {

        this.pemasukanService = pemasukanService;
        this.pengeluaranService = pengeluaranService;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getLaporan(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {

        // Ambil transaksi user
        List<Pemasukan> pemasukan =
                pemasukanService.getByUserIdAndTanggalBetween(
                        userId, startDate, endDate);

        List<Pengeluaran> pengeluaran =
                pengeluaranService.getByUserIdAndTanggalBetween(
                        userId, startDate, endDate);

        pemasukan.sort(Comparator.comparing(Pemasukan::getTanggal));
        pengeluaran.sort(Comparator.comparing(Pengeluaran::getTanggal));

        // Total pemasukan
        BigDecimal totalPemasukan = pemasukan.stream()
                .map(Pemasukan::getJumlah)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total pengeluaran
        BigDecimal totalPengeluaran = pengeluaran.stream()
                .map(Pengeluaran::getJumlah)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Saldo
        BigDecimal saldo =
                totalPemasukan.subtract(totalPengeluaran);

        // Pengeluaran berdasarkan kategori
        Map<String, BigDecimal> pengeluaranKategori =
                pengeluaran.stream()
                        .filter(p -> p.getKategori() != null)
                        .collect(Collectors.groupingBy(
                                p -> p.getKategori().getNama(),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Pengeluaran::getJumlah,
                                        BigDecimal::add
                                )
                        ));

        // Rasio pengeluaran terhadap pemasukan
        double rasio = 0.0;
        if (totalPemasukan.compareTo(BigDecimal.ZERO) > 0) {
            rasio = totalPengeluaran.multiply(BigDecimal.valueOf(100))
                    .divide(totalPemasukan, 1, java.math.RoundingMode.HALF_UP)
                    .doubleValue();
        }

        String rasioFormatted = (rasio == (long) rasio)
                ? String.format("%d", (long) rasio)
                : String.format(java.util.Locale.US, "%.1f", rasio);

        // Breakdown kategori pengeluaran dengan persentase
        List<Map<String, Object>> kategoriBreakdown = new ArrayList<>();
        final BigDecimal finalTotalPengeluaran = totalPengeluaran;
        pengeluaranKategori.forEach((nama, jumlah) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("nama", nama);
            item.put("jumlah", jumlah);
            double pct = 0.0;
            if (finalTotalPengeluaran.compareTo(BigDecimal.ZERO) > 0) {
                pct = jumlah.multiply(BigDecimal.valueOf(100))
                        .divide(finalTotalPengeluaran, 1, java.math.RoundingMode.HALF_UP)
                        .doubleValue();
            }
            item.put("persentase", pct);
            kategoriBreakdown.add(item);
        });
        kategoriBreakdown.sort((a, b) -> ((BigDecimal) b.get("jumlah")).compareTo((BigDecimal) a.get("jumlah")));

        // Pemasukan berdasarkan kategori
        Map<String, BigDecimal> pemasukanKategori =
                pemasukan.stream()
                        .collect(Collectors.groupingBy(
                                p -> p.getKategori() != null ? p.getKategori().getNama() : "Lainnya",
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        Pemasukan::getJumlah,
                                        BigDecimal::add
                                )
                        ));

        List<Map<String, Object>> pemasukanBreakdown = new ArrayList<>();
        final BigDecimal finalTotalPemasukan = totalPemasukan;
        pemasukanKategori.forEach((nama, jumlah) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("nama", nama);
            item.put("jumlah", jumlah);
            double pct = 0.0;
            if (finalTotalPemasukan.compareTo(BigDecimal.ZERO) > 0) {
                pct = jumlah.multiply(BigDecimal.valueOf(100))
                        .divide(finalTotalPemasukan, 1, java.math.RoundingMode.HALF_UP)
                        .doubleValue();
            }
            item.put("persentase", pct);
            pemasukanBreakdown.add(item);
        });
        pemasukanBreakdown.sort((a, b) -> ((BigDecimal) b.get("jumlah")).compareTo((BigDecimal) a.get("jumlah")));

         String[] namaBulan = {
                "Januari",
                "Februari",
                "Maret",
                "April",
                "Mei",
                "Juni",
                "Juli",
                "Agustus",
                "September",
                "Oktober",
                "November",
                "Desember"
        };

        List<String> labels = new ArrayList<>();
        List<BigDecimal> incomeData = new ArrayList<>();
        List<BigDecimal> expenseData = new ArrayList<>();
        List<BigDecimal> balanceData = new ArrayList<>();

        BigDecimal runningBalance = BigDecimal.ZERO;

        int tahun = startDate.getYear();

        for (int bulan = 1; bulan <= 12; bulan++) {

            BigDecimal pemasukanBulan = BigDecimal.ZERO;
            BigDecimal pengeluaranBulan = BigDecimal.ZERO;

            // Hitung pemasukan bulan ini
            for (Pemasukan item : pemasukan) {

                if (item.getTanggal().getYear() == tahun
                        && item.getTanggal().getMonthValue() == bulan) {

                    pemasukanBulan =
                            pemasukanBulan.add(item.getJumlah());
                }
            }

            // Hitung pengeluaran bulan ini
            for (Pengeluaran item : pengeluaran) {

                if (item.getTanggal().getYear() == tahun
                        && item.getTanggal().getMonthValue() == bulan) {

                    pengeluaranBulan =
                            pengeluaranBulan.add(item.getJumlah());
                }
            }

            // Hitung saldo berjalan
            runningBalance = runningBalance
                    .add(pemasukanBulan)
                    .subtract(pengeluaranBulan);

            labels.add(namaBulan[bulan - 1]);
            incomeData.add(pemasukanBulan);
            expenseData.add(pengeluaranBulan);
            balanceData.add(runningBalance);
        }

        // Data chart
        Map<String, Object> chartData = new HashMap<>();

        chartData.put("labels", labels);
        chartData.put("income", incomeData);
        chartData.put("expense", expenseData);
        chartData.put("balance", balanceData);

        Map<String, Object> reportData = new HashMap<>();

        reportData.put("totalPemasukan", totalPemasukan);
        reportData.put("totalPengeluaran", totalPengeluaran);
        reportData.put("saldo", saldo);
        reportData.put("rasio", rasio);
        reportData.put("rasioFormatted", rasioFormatted);
        reportData.put("kategoriBreakdown", kategoriBreakdown);
        reportData.put("pemasukanBreakdown", pemasukanBreakdown);

        reportData.put(
                "pengeluaranKategori",
                pengeluaranKategori
        );

        reportData.put("pemasukan", pemasukan);
        reportData.put("pengeluaran", pengeluaran);

        reportData.put("chartData", chartData);

        return reportData;
    }
}