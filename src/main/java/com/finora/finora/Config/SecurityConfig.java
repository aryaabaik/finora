
package com.finora.finora.Config;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/", "/login", "/register",
                        "/css/**", "/js/**", "/img/**",
                        "/uploads/**", "/*.css", "/favicon.ico"
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
                        request.getSession().setAttribute("user", user);
                    }

                    response.sendRedirect("/dashboard");
                })
                .failureUrl("/login?error")
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
        );

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}