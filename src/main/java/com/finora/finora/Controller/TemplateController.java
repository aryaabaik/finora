package com.finora.finora.Controller;

import com.finora.finora.Model.Kategori;
import com.finora.finora.Model.Template;
import com.finora.finora.Model.User;
import com.finora.finora.Service.TemplateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/template")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }


    // =========================
    // DAFTAR TEMPLATE
    // =========================
    @GetMapping
    public String index(
            HttpSession session,
            Model model
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Template> templates =
                templateService.getTemplatesByUser(user);

        model.addAttribute("templates", templates);

        return "template/index";
    }


    // =========================
    // FORM CREATE TEMPLATE
    // =========================
    @GetMapping("/create")
    public String createForm(
            HttpSession session,
            Model model
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "template",
                new Template()
        );

        model.addAttribute(
                "kategoriList",
                templateService.getKategoriByUser(user)
        );

        return "template/create";
    }


    // =========================
    // SIMPAN TEMPLATE
    // =========================
    @PostMapping("/create")
    public String create(
            HttpSession session,
            Template template
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        templateService.createTemplate(
                template,
                user
        );

        return "redirect:/template";
    }


    // =========================
    // GUNAKAN TEMPLATE
    // =========================
    @PostMapping("/use/{id}")
    public String gunakanTemplate(
            @PathVariable Long id,
            HttpSession session
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        templateService.gunakanTemplate(
                id,
                user
        );

        return "redirect:/template";
    }

    // =========================
    // FORM EDIT TEMPLATE
    // =========================
    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Template template = templateService.getTemplateById(id);

        if (template == null) {
            return "redirect:/template";
        }

        if (template.getUser() == null ||
                !template.getUser().getId().equals(user.getId())) {
            return "redirect:/template";
        }

        model.addAttribute("template", template);

        model.addAttribute(
                "kategoriList",
                templateService.getKategoriByUser(user)
        );

        return "template/edit";
    }

    // =========================
    // SIMPAN PERUBAHAN TEMPLATE
    // =========================
    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            HttpSession session,
            Template template
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Template templateLama =
                templateService.getTemplateById(id);

        if (templateLama == null) {
            return "redirect:/template";
        }

        if (templateLama.getUser() == null ||
                !templateLama.getUser().getId().equals(user.getId())) {

            return "redirect:/template";
        }

        templateLama.setNama(template.getNama());
        templateLama.setJenis(template.getJenis());

        templateLama.getItems().clear();

        if (template.getItems() != null) {

            for (var item : template.getItems()) {

                item.setTemplate(templateLama);

                templateLama.getItems().add(item);
            }
        }

        templateService.saveTemplate(templateLama);

        return "redirect:/template";
    }
    
    // =========================
    // HAPUS TEMPLATE
    // =========================
    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            HttpSession session
    ) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Template template =
                templateService.getTemplateById(id);

        if (template == null) {
            return "redirect:/template";
        }

        if (template.getUser() == null ||
                !template.getUser().getId().equals(user.getId())) {

            return "redirect:/template";
        }

        templateService.deleteTemplate(id);

        return "redirect:/template";
    }   
}