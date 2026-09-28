package com.finora.finora.Controller;

import com.finora.finora.Model.User;
import com.finora.finora.Service.LaporanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Controller
@RequestMapping("/laporan")
public class LaporanController {

    private final LaporanService laporanService;
    private final com.finora.finora.Config.CurrentUserHelper currentUserHelper;

    public LaporanController(LaporanService laporanService, com.finora.finora.Config.CurrentUserHelper currentUserHelper) {
        this.laporanService = laporanService;
        this.currentUserHelper = currentUserHelper;
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

        int selectedYear = year != null ? year : sekarang.getYear();
        int selectedMonth = month != null ? month : sekarang.getMonthValue();

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

        model.addAttribute("reportData", reportData);
        model.addAttribute("user", user);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("totalPemasukan", reportData.get("totalPemasukan"));
        model.addAttribute("totalPengeluaran", reportData.get("totalPengeluaran"));
        model.addAttribute("saldo", reportData.get("saldo"));
        model.addAttribute("pemasukanList", reportData.get("pemasukan"));
        model.addAttribute("pengeluaranList", reportData.get("pengeluaran"));

        return "reports/index";
    }
}