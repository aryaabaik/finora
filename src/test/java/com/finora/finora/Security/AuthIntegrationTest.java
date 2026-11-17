package com.finora.finora.Security;

import com.finora.finora.Model.JenisKategori;
import com.finora.finora.Model.Kategori;
import com.finora.finora.Model.User;
import com.finora.finora.Repository.KategoriRepository;
import com.finora.finora.Repository.UserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter springSecurityFilterChain;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KategoriRepository kategoriRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
        jdbcTemplate.update("DELETE FROM persistent_logins");
        kategoriRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String extractCsrfToken(MvcResult result) throws Exception {
        String html = result.getResponse().getContentAsString();
        String marker = "name=\"_csrf\" value=\"";
        int idx = html.indexOf(marker);
        if (idx == -1) {
            throw new IllegalStateException("CSRF token not found in HTML");
        }
        int start = idx + marker.length();
        int end = html.indexOf('"', start);
        return html.substring(start, end);
    }

    private User createSavedUser(String username, String email, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Test
    void manualRegisterAndDuplicateEmailPrevention() throws Exception {
        MvcResult regPage = mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andReturn();
        String csrf = extractCsrfToken(regPage);

        mockMvc.perform(post("/register")
                        .session((MockHttpSession) regPage.getRequest().getSession())
                        .param("_csrf", csrf)
                        .param("fullName", "Budi Santoso")
                        .param("username", "budi")
                        .param("email", "budi@example.com")
                        .param("passwordHash", "secret123")
                        .param("confirmPassword", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        assertThat(userRepository.findByEmailIgnoreCase("budi@example.com")).isPresent();

        // Attempt duplicate registration with different email casing
        MvcResult regPage2 = mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andReturn();
        String csrf2 = extractCsrfToken(regPage2);

        MvcResult dupResult = mockMvc.perform(post("/register")
                        .session((MockHttpSession) regPage2.getRequest().getSession())
                        .param("_csrf", csrf2)
                        .param("fullName", "Budi Dua")
                        .param("username", "budi2")
                        .param("email", "BUDI@EXAMPLE.COM")
                        .param("passwordHash", "secret123")
                        .param("confirmPassword", "secret123"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(dupResult.getResponse().getContentAsString()).contains("Email sudah digunakan.");
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void csrfProtectionRejectsPostWithoutToken() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "pass"))
                .andExpect(status().isForbidden());
    }

    @Test
    void manualLoginRememberMePersistsAcrossBrowserRestartAndLogoutClearsAll() throws Exception {
        createSavedUser("andi", "andi@example.com", "password123");

        MvcResult loginPage = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andReturn();
        String csrf = extractCsrfToken(loginPage);
        MockHttpSession loginSession = (MockHttpSession) loginPage.getRequest().getSession();

        // Login with Remember Me over HTTPS
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .session(loginSession)
                        .secure(true)
                        .param("_csrf", csrf)
                        .param("username", "andi")
                        .param("password", "password123")
                        .param("remember-me", "on"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andReturn();

        Cookie rememberMeCookie = loginResult.getResponse().getCookie("remember-me");
        assertThat(rememberMeCookie).isNotNull();
        assertThat(rememberMeCookie.isHttpOnly()).isTrue();
        assertThat(rememberMeCookie.getSecure()).isTrue();
        assertThat(rememberMeCookie.getAttribute("SameSite")).isEqualTo("Lax");
        assertThat(rememberMeCookie.getMaxAge()).isEqualTo(14 * 24 * 60 * 60);

        Integer tokenCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM persistent_logins WHERE username = ?",
                Integer.class, "andi");
        assertThat(tokenCount).isEqualTo(1);

        // Simulate browser close & reopen: new empty session, only remember-me cookie
        MvcResult dashboardResult = mockMvc.perform(get("/dashboard")
                        .cookie(rememberMeCookie)
                        .secure(true))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession newSession = (MockHttpSession) dashboardResult.getRequest().getSession(false);
        assertThat(newSession).isNotNull();
        assertThat(newSession.getAttribute("user")).isNotNull();

        // Rotated remember-me cookie should be returned
        Cookie rotatedCookie = dashboardResult.getResponse().getCookie("remember-me");
        Cookie activeCookie = rotatedCookie != null ? rotatedCookie : rememberMeCookie;

        // Logout with CSRF
        String dashHtml = dashboardResult.getResponse().getContentAsString();
        String logoutCsrf = extractCsrfFromHtml(dashHtml);

        MvcResult logoutResult = mockMvc.perform(post("/logout")
                        .session(newSession)
                        .cookie(activeCookie)
                        .secure(true)
                        .param("_csrf", logoutCsrf))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"))
                .andReturn();

        Cookie clearedRememberMe = logoutResult.getResponse().getCookie("remember-me");
        assertThat(clearedRememberMe).isNotNull();
        assertThat(clearedRememberMe.getMaxAge()).isZero();

        Integer tokenCountAfterLogout = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM persistent_logins WHERE username = ?",
                Integer.class, "andi");
        assertThat(tokenCountAfterLogout).isZero();

        // Using old remember-me cookie after logout must redirect to /login
        mockMvc.perform(get("/dashboard").cookie(activeCookie))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void googleOAuth2AuthorizationRedirectsUseStandardRedirectUriAndClientId() throws Exception {
        MvcResult loginAuth = mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        String loginRedirect = loginAuth.getResponse().getRedirectedUrl();
        assertThat(loginRedirect).contains("https://accounts.google.com/o/oauth2/v2/auth");
        assertThat(loginRedirect).contains("client_id=test-google-client-id");
        assertThat(loginRedirect).contains("response_type=code");
        assertThat(loginRedirect).contains("redirect_uri=http://localhost/login/oauth2/code/google");

        MvcResult regAuth = mockMvc.perform(get("/oauth2/authorization/google-register"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        String regRedirect = regAuth.getResponse().getRedirectedUrl();
        assertThat(regRedirect).contains("https://accounts.google.com/o/oauth2/v2/auth");
        assertThat(regRedirect).contains("client_id=test-google-client-id");
        assertThat(regRedirect).contains("response_type=code");
        assertThat(regRedirect).contains("redirect_uri=http://localhost/login/oauth2/code/google");
    }

    @Test
    void sessionExpirationRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void userCannotAccessOrDeleteAnotherUsersKategori() throws Exception {
        User owner = createSavedUser("owner", "owner@example.com", "password123");
        User attacker = createSavedUser("attacker", "attacker@example.com", "password123");

        Kategori k = new Kategori();
        k.setNama("Rahasia");
        k.setJenis(JenisKategori.PEMASUKAN);
        k.setUser(owner);
        Kategori savedKategori = kategoriRepository.save(k);

        MvcResult loginPage = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andReturn();
        String csrf = extractCsrfToken(loginPage);
        MockHttpSession session = (MockHttpSession) loginPage.getRequest().getSession();

        mockMvc.perform(post("/login")
                        .session(session)
                        .param("_csrf", csrf)
                        .param("username", "attacker")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        // Re-fetch CSRF token after login session fixation migration
        MvcResult dash = mockMvc.perform(get("/dashboard").session(session))
                .andExpect(status().isOk())
                .andReturn();
        String postLoginCsrf = extractCsrfFromHtml(dash.getResponse().getContentAsString());

        Exception deleteEx = null;
        try {
            mockMvc.perform(delete("/kategori/" + savedKategori.getId())
                    .session(session)
                    .header("X-CSRF-TOKEN", postLoginCsrf));
        } catch (Exception e) {
            deleteEx = e;
        }
        assertThat(deleteEx).isNotNull();
        assertThat(kategoriRepository.findById(savedKategori.getId())).isPresent();
    }

    private String extractCsrfFromHtml(String html) {
        String marker = "id=\"finora-csrf-token\"";
        int idx = html.indexOf(marker);
        if (idx == -1) {
            throw new IllegalStateException("finora-csrf-token not found in HTML");
        }
        int valIdx = html.indexOf("value=\"", idx);
        int start = valIdx + "value=\"".length();
        int end = html.indexOf('"', start);
        return html.substring(start, end);
    }
}
