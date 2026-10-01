package com.finora.finora.Controller;

import com.finora.finora.Model.User;
import com.finora.finora.Service.LaporanExcelService;
import com.finora.finora.Service.LaporanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import  com.finora.finora.Service.LaporanExcelService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/laporan")
public class LaporanController {

    private final LaporanService laporanService;
    private final com.finora.finora.Config.CurrentUserHelper currentUserHelper;
    private final LaporanExcelService laporanExcelService;

    public LaporanController(
            LaporanService laporanService,com.finora.finora.Config.CurrentUserHelper currentUserHelper,LaporanExcelService laporanExcelService
    ) {
        this.laporanService = laporanService;
        this.currentUserHelper = currentUserHelper;
        this.laporanExcelService = laporanExcelService;
    }

    @GetMapping
    public String laporanPage(
            Model model,
            HttpSession session,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        User user = currentUserHelper.getCurrentUser();
        if (user == null) {
            user = (User) session.getAttribute("user");
        }

        // Cek login
        if (user == null) {
            return "redirect:/login";
        }

        // Default ke bulan sekarang
        YearMonth sekarang = YearMonth.now();
        int currentYear = sekarang.getYear();

        int selectedYear = year != null ? year : currentYear;
        int selectedMonth = month != null ? month : sekarang.getMonthValue();

        int minYear = Math.min(currentYear - 5, selectedYear - 2);
        int maxYear = Math.max(currentYear + 1, selectedYear);
        List<Integer> yearList = new ArrayList<>();
        for (int y = minYear; y <= maxYear; y++) {
            yearList.add(y);
        }

        // Tanggal awal dan akhir bulan
        LocalDate startDate =
                LocalDate.of(selectedYear, selectedMonth, 1);

        LocalDate endDate =
                startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Ambil laporan milik user yang login
        Map<String, Object> reportData =
                laporanService.getLaporan(
                        user.getId(),
                        startDate,
                        endDate
                );

        reportData.put("selectedYear", selectedYear);
        reportData.put("selectedMonth", selectedMonth);

        model.addAttribute("reportData", reportData);
        model.addAttribute("user", user);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("yearList", yearList);
        model.addAttribute("totalPemasukan", reportData.get("totalPemasukan"));
        model.addAttribute("totalPengeluaran", reportData.get("totalPengeluaran"));
        model.addAttribute("saldo", reportData.get("saldo"));
        model.addAttribute("pemasukanList", reportData.get("pemasukan"));
        model.addAttribute("pengeluaranList", reportData.get("pengeluaran"));

        return "reports/index";
    }

    @GetMapping("/export")
    public void exportExcel(
            HttpSession session,
            HttpServletResponse response,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) throws Exception {

        User user = currentUserHelper.getCurrentUser();

        if (user == null) {
            user = (User) session.getAttribute("user");
        }

        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        YearMonth sekarang = YearMonth.now();

        int selectedYear =
                year != null ? year : sekarang.getYear();

        int selectedMonth =
                month != null ? month : sekarang.getMonthValue();

        LocalDate startDate =
                LocalDate.of(selectedYear, selectedMonth, 1);

        LocalDate endDate =
                startDate.withDayOfMonth(
                        startDate.lengthOfMonth()
                );

        Map<String, Object> reportData =
                laporanService.getLaporan(
                        user.getId(),
                        startDate,
                        endDate
                );

        reportData.put("selectedYear", selectedYear);
        reportData.put("selectedMonth", selectedMonth);

        String fileName = String.format(
                "Finora-Laporan-%d-%02d.xlsx",
                selectedYear,
                selectedMonth
        );

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + fileName + "\""
        );

        laporanExcelService.export(
                response.getOutputStream(),
                reportData
        );
    }
}