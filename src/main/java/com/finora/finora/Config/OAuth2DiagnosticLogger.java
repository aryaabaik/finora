package com.finora.finora.Config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Diagnostic logger untuk memverifikasi bahwa OAuth2 credentials
 * terbaca dengan benar oleh Spring Boot saat startup.
 *
 * Log ini AMAN: hanya mencetak prefix Client ID (bukan secret).
 * Hapus class ini setelah deployment ke production.
 */
@Component
public class OAuth2DiagnosticLogger {

    private static final Logger log = LoggerFactory.getLogger(OAuth2DiagnosticLogger.class);

    @Value("${spring.security.oauth2.client.registration.google.client-id:__NOT_SET__}")
    private String googleClientId;

    @PostConstruct
    public void logOAuth2Config() {
        if ("__NOT_SET__".equals(googleClientId) || googleClientId == null || googleClientId.isBlank()) {
            log.error("╔══════════════════════════════════════════════════════════╗");
            log.error("║  ❌  GOOGLE_CLIENT_ID TIDAK TERSEDIA!                   ║");
            log.error("║  Spring Boot tidak dapat membaca environment variable.  ║");
            log.error("║  Set GOOGLE_CLIENT_ID dan GOOGLE_CLIENT_SECRET          ║");
            log.error("║  sebelum menjalankan aplikasi.                          ║");
            log.error("║  Gunakan: .\\run-with-oauth.ps1  (isi dulu nilainya)    ║");
            log.error("╚══════════════════════════════════════════════════════════╝");
            return;
        }

        // Hanya cetak prefix aman (tidak ada secret)
        String prefix = googleClientId.length() > 25
                ? googleClientId.substring(0, 25) + "..."
                : googleClientId;
        boolean looksValid = googleClientId.endsWith(".apps.googleusercontent.com");

        log.info("╔══════════════════════════════════════════════════════════╗");
        log.info("║  ✅  Google OAuth2 Credential Check                     ║");
        log.info("║  Client ID prefix : {}  ║", String.format("%-29s", prefix));
        log.info("║  Format valid     : {}                               ║", looksValid ? "YES ✔" : "NO  ✖ — periksa kembali!");
        log.info("║  Secret           : [HIDDEN — {} chars]               ║", "[configured]");
        log.info("║  Redirect URI     : {baseUrl}/login/oauth2/code/google  ║");
        log.info("╚══════════════════════════════════════════════════════════╝");

        if (!looksValid) {
            log.warn("⚠️  Client ID tidak diakhiri '.apps.googleusercontent.com'. Periksa konfigurasi!");
        }
    }
}
