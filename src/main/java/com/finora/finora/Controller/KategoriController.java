package com.finora.finora.Controller;

import com.finora.finora.Model.Kategori;
import com.finora.finora.Model.User;
import com.finora.finora.Service.KategoriService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/kategori")
public class KategoriController {

    private final KategoriService kategoriService;

    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
    }

    @GetMapping("/page")
    public String indexPage() {
        return "kategori/index";
    }

    @PostMapping
    @ResponseBody
    public Kategori save(
            @RequestBody Kategori kategori,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new RuntimeException("Belum login");
        }

        // Hubungkan kategori dengan user yang login
        kategori.setUser(user);

        return kategoriService.save(kategori);
    }

    @GetMapping
    @ResponseBody
    public List<Kategori> getAll(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new RuntimeException("Belum login");
        }

        // Hanya ambil kategori milik user yang login
        return kategoriService.getByUserId(user.getId());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Kategori getById(@PathVariable Long id) {
        return kategoriService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id) {
        kategoriService.deleteById(id);
        return "Kategori berhasil dihapus";
    }
}