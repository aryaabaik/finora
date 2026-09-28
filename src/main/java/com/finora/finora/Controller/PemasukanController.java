package com.finora.finora.Controller;

import com.finora.finora.Dto.TemplateItemResponse;
import com.finora.finora.Dto.TemplateResponse;
import com.finora.finora.Model.Pemasukan;
import com.finora.finora.Model.User;
import com.finora.finora.Service.PemasukanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.finora.finora.Model.JenisKategori;
import com.finora.finora.Model.Template;
import com.finora.finora.Service.TemplateService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pemasukan")
public class PemasukanController {

    private final PemasukanService pemasukanService;
    private final TemplateService templateService;

    public PemasukanController(PemasukanService pemasukanService, TemplateService templateService) {
        this.pemasukanService = pemasukanService;
        this.templateService = templateService;
    }

    // Halaman index
    @GetMapping("/page")
    public String indexPage() {
        return "pemasukan/index";
    }

    // Halaman tambah
    @GetMapping("/create")
    public String createPage() {
        return "pemasukan/create";
    }

    // API simpan
    @PostMapping
    @ResponseBody
    public Pemasukan save(
            @RequestBody Pemasukan pemasukan,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new RuntimeException("Belum login");
        }

        pemasukan.setUser(user);

        return pemasukanService.save(pemasukan);
    }

    @GetMapping("/templates")
    @ResponseBody
    public List<TemplateResponse> getTemplates(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new RuntimeException("Belum login");
        }

        return templateService.getTemplatesByUser(user)
                .stream()
                .filter(template -> template.getJenis() == JenisKategori.PEMASUKAN)
                .map(this::mapToTemplateResponse)
                .collect(Collectors.toList());
    }

    private TemplateResponse mapToTemplateResponse(Template template) {
        List<TemplateItemResponse> itemResponses = template.getItems().stream()
                .map(item -> new TemplateItemResponse(
                        item.getId(),
                        item.getKeterangan(),
                        item.getJumlah(),
                        item.getKategori() != null ? item.getKategori().getNama() : "-"
                ))
                .collect(Collectors.toList());
        return new TemplateResponse(
                template.getId(),
                template.getNama(),
                template.getJenis() != null ? template.getJenis().name() : "-",
                itemResponses
        );
    }

    @PostMapping("/template/use/{templateId}/{itemId}")
    @ResponseBody
    public String gunakanTemplateItem(
            @PathVariable Long templateId,
            @PathVariable Long itemId,
            HttpSession session
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new RuntimeException("Belum login");
        }

        Template template = templateService.getTemplateById(templateId);

        if (template == null) {
            throw new RuntimeException("Template tidak ditemukan");
        }

        if (template.getUser() == null ||
                !template.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Template bukan milik user");
        }

        if (template.getJenis() != JenisKategori.PEMASUKAN) {
            throw new RuntimeException("Template bukan template pemasukan");
        }

        templateService.gunakanTemplateItem(templateId, itemId, user);

        return "Item template berhasil digunakan";
    }

    @GetMapping
    @ResponseBody
    public Map<String, Object> getAll(
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "terbaru") String sort,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Belum login");
        }

        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        return pemasukanService.getFiltered(user.getId(), page, size, sort, category, start, end);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Pemasukan getById(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Belum login");
        }
        Pemasukan pemasukan = pemasukanService.getById(id);
        if (!pemasukan.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Data bukan milik user");
        }
        return pemasukan;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Belum login");
        }
        Pemasukan pemasukan = pemasukanService.getById(id);
        if (!pemasukan.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Data bukan milik user");
        }
        pemasukanService.delete(id);
        return "Pemasukan berhasil dihapus";
    }
}