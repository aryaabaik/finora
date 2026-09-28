
package com.finora.finora.Controller;

import com.finora.finora.Config.CurrentUserHelper;
import com.finora.finora.Model.User;
import com.finora.finora.Service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.*;
import java.util.UUID;

@Controller
public class AuthController {
    private final AuthService authService;
    private final CurrentUserHelper currentUserHelper;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService a, CurrentUserHelper c, PasswordEncoder p) {
        authService = a;
        currentUserHelper = c;
        passwordEncoder = p;
    }

    // LOGIN
    @GetMapping("/login")
    public String loginPage(Model model,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String registered) {
        if (error != null) model.addAttribute("error", "Username atau kata sandi salah.");
        if (logout != null) model.addAttribute("message", "Anda telah berhasil keluar dari sesi.");
        if (registered != null) model.addAttribute("success", "Pendaftaran berhasil! Silakan masuk menggunakan akun Anda.");
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password, HttpSession session) {
        User user = authService.findByUsername(username);

        if (user == null || !user.isEnabled()
                || !passwordEncoder.matches(password, user.getPasswordHash()))
            return "redirect:/login?error";

        session.setAttribute("user", user);
        return "redirect:/dashboard";
    }

    // REGISTER
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user,
            @RequestParam(required = false) String confirmPassword, Model model) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty())
                throw new RuntimeException("Username wajib diisi.");

            if (user.getEmail() == null || user.getEmail().trim().isEmpty())
                throw new RuntimeException("Alamat email wajib diisi.");

            if (user.getPasswordHash() == null || user.getPasswordHash().length() < 6)
                throw new RuntimeException("Kata sandi minimal harus 6 karakter.");

            if (confirmPassword != null && !confirmPassword.equals(user.getPasswordHash()))
                throw new RuntimeException("Konfirmasi kata sandi tidak cocok.");

            if (user.getFullName() == null || user.getFullName().trim().isEmpty())
                user.setFullName(user.getUsername());

            authService.register(user);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "auth/register";
        }
    }

    // AMBIL USER YANG LOGIN
    private User getUser(HttpSession session) {
        User user = currentUserHelper.getCurrentUser();
        return user != null ? user : (User) session.getAttribute("user");
    }

    // SETTINGS
    @GetMapping({"/auth/settings", "/settings"})
    public String settings(Model model, HttpSession session) {
        User user = getUser(session);
        if (user == null) return "redirect:/login";

        User fresh = authService.findByUsername(user.getUsername());
        if (fresh == null) fresh = user;

        session.setAttribute("user", fresh);
        model.addAttribute("user", fresh);
        model.addAttribute("userName", fresh.getUsername());
        return "auth/settings";
    }

    // UPDATE PROFIL
    @PostMapping("/auth/settings/profile")
    public String updateProfile(@RequestParam String fullName,
            @RequestParam String email, HttpSession session,
            RedirectAttributes redirect) {
        User user = getUser(session);
        if (user == null) return "redirect:/login";

        try {
            User fresh = authService.findByUsername(user.getUsername());
            fresh.setFullName(fullName.trim());
            fresh.setEmail(email.trim());

            session.setAttribute("user", authService.updateUser(fresh));
            redirect.addFlashAttribute("success", "Profil berhasil diperbarui!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Gagal memperbarui profil: " + e.getMessage());
        }
        return "redirect:/auth/settings";
    }

    // UPDATE FOTO
    @PostMapping("/auth/settings/photo")
    public String updatePhoto(@RequestParam("photo") MultipartFile photo,
            HttpSession session, RedirectAttributes redirect) {
        User user = getUser(session);
        if (user == null) return "redirect:/login";

        try {
            if (photo.isEmpty())
                throw new RuntimeException("Silakan pilih file gambar terlebih dahulu.");

            String type = photo.getContentType();
            if (type == null || !type.startsWith("image/"))
                throw new RuntimeException("File harus berupa gambar (JPG, PNG, WebP, dll).");

            Path folder = Paths.get("uploads/profile");
            Files.createDirectories(folder);

            String original = photo.getOriginalFilename();
            String clean = original == null ? "avatar.jpg"
                    : original.replaceAll("[^a-zA-Z0-9._-]", "_");

            String fileName = UUID.randomUUID() + "_" + clean;
            Files.copy(photo.getInputStream(), folder.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            User fresh = authService.findByUsername(user.getUsername());
            fresh.setFoto(fileName);

            session.setAttribute("user", authService.updateUser(fresh));
            redirect.addFlashAttribute("successPhoto", "Foto profil berhasil diperbarui!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorPhoto", "Gagal mengunggah foto: " + e.getMessage());
        }
        return "redirect:/auth/settings";
    }

    // GANTI PASSWORD
    @PostMapping("/auth/settings/password")
    public String updatePassword(@RequestParam String oldPassword,
            @RequestParam String newPassword, @RequestParam String confirmPassword,
            HttpSession session, RedirectAttributes redirect) {
        User user = getUser(session);
        if (user == null) return "redirect:/login";

        try {
            if (!newPassword.equals(confirmPassword))
                throw new RuntimeException("Konfirmasi password baru tidak cocok.");

            if (newPassword.length() < 6)
                throw new RuntimeException("Password baru minimal 6 karakter.");

            User fresh = authService.findByUsername(user.getUsername());
            if (!passwordEncoder.matches(oldPassword, fresh.getPasswordHash()))
                throw new RuntimeException("Password saat ini salah.");

            authService.updatePassword(fresh, newPassword);
            redirect.addFlashAttribute("successPassword", "Password berhasil diubah!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorPassword", e.getMessage());
        }
        return "redirect:/auth/settings";
    }
}