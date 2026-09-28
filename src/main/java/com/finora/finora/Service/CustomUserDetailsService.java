package com.finora.finora.Service;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if (login == null || login.trim().isEmpty()) {
            throw new UsernameNotFoundException("Username is required");
        }

        // Cari berdasarkan username (exact match dulu)
        User user = userRepository.findByUsername(login.trim());

        // Kalau tidak ketemu, coba berdasarkan email
        if (user == null) {
            user = userRepository.findByUsernameOrEmailIgnoreCase(login.trim()).orElse(null);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User tidak ditemukan: " + login);
        }

        // password disimpan di kolom "password" yang di-map ke field passwordHash
        String hashedPassword = user.getPasswordHash();
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new UsernameNotFoundException("Password belum diset untuk user: " + login);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(hashedPassword)
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .roles("USER")
                .build();
    }
}
