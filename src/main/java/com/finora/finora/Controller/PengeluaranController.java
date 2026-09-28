package com.finora.finora.Controller;

import com.finora.finora.Dto.TemplateItemResponse;
import com.finora.finora.Dto.TemplateResponse;
import com.finora.finora.Model.Pengeluaran;
import com.finora.finora.Model.User;
import com.finora.finora.Service.PengeluaranService;
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
@RequestMapping("/pengeluaran")
public class PengeluaranController {

    private final PengeluaranService pengeluaranService;
    private final TemplateService templateService;

    public PengeluaranController(PengeluaranService pengeluaranService, TemplateService templateService) {
        this.pengeluaranService = pengeluaranService;
        this.templateService = templateService;
    }

    @GetMapping("/page")
    public String indexPage() {
        return "pengeluaran/index";
    }

    @GetMapping("/create")
    public String createPage() {
        return "pengeluaran/create";
    }

    @PostMapping
    @ResponseBody
    public Pengeluaran save(
            @RequestBody Pengeluaran pengeluaran,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Belum login");
        }
        pengeluaran.setUser(user);
        return pengeluaranService.save(pengeluaran);
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
                .filter(template -> template.getJenis() == JenisKategori.PENGELUARAN)
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

        if (template.getJenis() != JenisKategori.PENGELUARAN) {
            throw new RuntimeException("Template bukan template pengeluaran");
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

        return pengeluaranService.getFiltered(user.getId(), page, size, sort, category, start, end);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Pengeluaran getById(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Belum login");
        }
        Pengeluaran pengeluaran = pengeluaranService.getById(id);
        if (!pengeluaran.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Data bukan milik user");
        }
        return pengeluaran;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("Belum login");
        }
        Pengeluaran pengeluaran = pengeluaranService.getById(id);
        if (!pengeluaran.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Data bukan milik user");
        }
        pengeluaranService.deleteById(id);
        return "Pengeluaran berhasil dihapus";
    }
}