package com.finora.finora.Controller;

import com.finora.finora.Model.User;
import com.finora.finora.Service.LaporanService;
import com.finora.finora.Config.CurrentUserHelper;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class DashboardController {

    private final LaporanService laporanService;
    private final CurrentUserHelper currentUserHelper;

    public DashboardController(LaporanService laporanService, CurrentUserHelper currentUserHelper) {
        this.laporanService = laporanService;
        this.currentUserHelper = currentUserHelper;
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, HttpSession session) {
        User user = currentUserHelper.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        session.setAttribute("user", user);

        LocalDate sekarang = LocalDate.now();
        LocalDate awalTahun = LocalDate.of(sekarang.getYear(), 1, 1);
        LocalDate akhirTahun = LocalDate.of(sekarang.getYear(), 12, 31);

        model.addAttribute("laporan", laporanService.getLaporan(user.getId(), awalTahun, akhirTahun));
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("user", user);

        return "dashboard";
    }
}