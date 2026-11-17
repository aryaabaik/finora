package com.finora.finora.Config;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;
import com.finora.finora.Service.CustomUserDetailsService;
import com.finora.finora.Service.GoogleOAuth2Service;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    public static final int REMEMBER_ME_VALIDITY_SECONDS = 14 * 24 * 60 * 60; // 14 days

    private final UserRepository userRepository;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final CustomUserDetailsService customUserDetailsService;
    private final CurrentUserHelper currentUserHelper;
    private final DataSource dataSource;
    private final String rememberMeKey;

    public SecurityConfig(
            UserRepository userRepository,
            GoogleOAuth2Service googleOAuth2Service,
            CustomUserDetailsService customUserDetailsService,
            CurrentUserHelper currentUserHelper,
            DataSource dataSource,
            @Value("${REMEMBER_ME_KEY:}") String rememberMeKey) {
        this.userRepository = userRepository;
        this.googleOAuth2Service = googleOAuth2Service;
        this.customUserDetailsService = customUserDetailsService;
        this.currentUserHelper = currentUserHelper;
        this.dataSource = dataSource;
        this.rememberMeKey = (rememberMeKey != null && !rememberMeKey.isBlank())
                ? rememberMeKey
                : UUID.randomUUID().toString();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS persistent_logins (" +
                        "username varchar(255) not null, " +
                        "series varchar(64) primary key, " +
                        "token varchar(64) not null, " +
                        "last_used timestamp not null)"
        );

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(
            PersistentTokenRepository persistentTokenRepository) {
        PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices(
                rememberMeKey,
                customUserDetailsService,
                persistentTokenRepository) {
            @Override
            protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
                String cookieValue = encodeCookie(tokens);
                Cookie cookie = new Cookie(getCookieName(), cookieValue);
                cookie.setMaxAge(maxAge);
                cookie.setPath(resolveCookiePath(request));
                cookie.setHttpOnly(true);
                cookie.setSecure(request.isSecure());
                cookie.setAttribute("SameSite", "Lax");
                response.addCookie(cookie);
            }

            @Override
            protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
                Cookie cookie = new Cookie(getCookieName(), null);
                cookie.setMaxAge(0);
                cookie.setPath(resolveCookiePath(request));
                cookie.setHttpOnly(true);
                cookie.setSecure(request.isSecure());
                cookie.setAttribute("SameSite", "Lax");
                response.addCookie(cookie);
            }

            private String resolveCookiePath(HttpServletRequest request) {
                String contextPath = request.getContextPath();
                return (contextPath != null && !contextPath.isEmpty()) ? contextPath : "/";
            }
        };
        services.setParameter("remember-me");
        services.setCookieName("remember-me");
        services.setTokenValiditySeconds(REMEMBER_ME_VALIDITY_SECONDS);
        return services;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            PersistentTokenBasedRememberMeServices rememberMeServices) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/", "/login", "/register",
                        "/css/**", "/js/**", "/img/**",
                        "/uploads/**", "/*.css", "/favicon.ico",
                        "/oauth2/**", "/login/oauth2/**"
                ).permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    String username = authentication.getName();
                    User user = userRepository.findByUsername(username);
                    if (user == null) {
                        user = userRepository
                                .findByUsernameOrEmailIgnoreCase(username)
                                .orElse(null);
                    }
                    if (user != null) {
                        request.getSession(true).setAttribute("user", user);
                    }
                    response.sendRedirect("/dashboard");
                })
                .failureUrl("/login?error")
                .permitAll()
        );

        http.oauth2Login(oauth -> oauth
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    try {
                        logger.info("=== Google LOGIN Handler Started ===");

                        String registrationId = null;
                        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                            registrationId = oauthToken.getAuthorizedClientRegistrationId();
                        }
                        logger.info("OAuth2 Registration ID: {}", registrationId);

                        OAuth2User googleUser = (OAuth2User) authentication.getPrincipal();
                        String email = googleUser.getAttribute("email");
                        Object emailVerifiedObj = googleUser.getAttribute("email_verified");
                        String fullName = googleUser.getAttribute("name");
                        String pictureUrl = googleUser.getAttribute("picture");

                        Boolean emailVerified = null;
                        if (emailVerifiedObj instanceof Boolean) {
                            emailVerified = (Boolean) emailVerifiedObj;
                        } else if (emailVerifiedObj instanceof String) {
                            emailVerified = Boolean.parseBoolean((String) emailVerifiedObj);
                        }

                        if (email == null || email.trim().isEmpty()) {
                            logger.warn("OAuth2 failed - Email is null or empty");
                            clearAuthenticationContext(request);
                            response.sendRedirect("/login?error=google");
                            return;
                        }

                        if (!Boolean.TRUE.equals(emailVerified)) {
                            logger.warn("OAuth2 failed - Email not verified");
                            clearAuthenticationContext(request);
                            response.sendRedirect("/login?error=google");
                            return;
                        }

                        User resolvedUser;
                        if ("google-register".equals(registrationId)) {
                            logger.info("Google REGISTER flow - Email: present");
                            User existingUser = googleOAuth2Service.findUserByEmail(email);
                            if (existingUser != null) {
                                logger.info("Google Register - User already exists, logging in");
                                resolvedUser = googleOAuth2Service.syncGooglePicture(existingUser, pictureUrl);
                            } else {
                                logger.info("Google Register - Creating new user");
                                resolvedUser = googleOAuth2Service.createUserFromGoogle(email, fullName, pictureUrl);
                                logger.info("Google Register success - User created: {}", resolvedUser.getUsername());
                            }
                        } else {
                            logger.info("Google LOGIN flow - Email: present");
                            User user = googleOAuth2Service.findUserByEmail(email);
                            if (user == null) {
                                logger.info("Google Login - Email not registered, redirecting to register");
                                clearAuthenticationContext(request);
                                HttpSession session = request.getSession(true);
                                session.setAttribute("googleEmail", email);
                                session.setAttribute("googleName", fullName);
                                session.setAttribute("googleMessage",
                                        "Akun dengan email Google Anda belum terdaftar. Silakan lanjutkan pendaftaran atau gunakan tombol Daftar dengan Google.");
                                response.sendRedirect("/register");
                                return;
                            }
                            logger.info("Google Login success - User found: {}", user.getUsername());
                            resolvedUser = googleOAuth2Service.syncGooglePicture(user, pictureUrl);
                        }

                        request.getSession(true).setAttribute("user", resolvedUser);

                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(resolvedUser.getUsername());
                        Authentication rememberMeAuth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        rememberMeServices.loginSuccess(request, response, rememberMeAuth);

                        response.sendRedirect("/dashboard");
                    } catch (Exception e) {
                        logger.error("Exception in OAuth2 handler: {}", e.getMessage(), e);
                        clearAuthenticationContext(request);
                        response.sendRedirect("/login?error=google");
                    }
                })
                .failureHandler((request, response, exception) -> {
                    logger.error("=== OAuth2 Failure Handler ===");
                    logger.error("Exception: {}", exception.getMessage());
                    response.sendRedirect("/login?error=google");
                })
        );

        http.rememberMe(remember -> remember
                .rememberMeServices(rememberMeServices)
                .key(rememberMeKey)
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .addLogoutHandler(rememberMeServices)
                .permitAll()
        );

        http.addFilterAfter(sessionUserSyncFilter(), AnonymousAuthenticationFilter.class);

        return http.build();
    }

    private OncePerRequestFilter sessionUserSyncFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                    HttpSession session = request.getSession(false);
                    if (session == null || session.getAttribute("user") == null) {
                        User user = currentUserHelper.getCurrentUser();
                        if (user != null) {
                            request.getSession(true).setAttribute("user", user);
                        }
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private void clearAuthenticationContext(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            session.removeAttribute("user");
        }
    }
}
