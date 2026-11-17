
package com.finora.finora.Service;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    public void register(User user) {
        String normalizedEmail = user.getEmail() != null ? user.getEmail().trim() : "";
        String normalizedUsername = user.getUsername() != null ? user.getUsername().trim() : "";

        if (userRepository.findByEmailIgnoreCase(normalizedEmail).isPresent())
            throw new RuntimeException("Email sudah digunakan.");

        if (userRepository.existsByUsername(normalizedUsername))
            throw new RuntimeException("Username sudah digunakan.");

        user.setEmail(normalizedEmail);
        user.setUsername(normalizedUsername);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setEnabled(true);

        userRepository.save(user);
    }

    // CARI USER
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // UPDATE PROFIL
    public User updateUser(User user) {
        if (userRepository.existsByUsernameAndIdNot(
                user.getUsername(), user.getId()))
            throw new RuntimeException("Username sudah digunakan.");

        if (userRepository.existsByEmailAndIdNot(
                user.getEmail(), user.getId()))
            throw new RuntimeException("Email sudah digunakan.");

        return userRepository.save(user);
    }

    // GANTI PASSWORD
    public void updatePassword(User user, String newPassword) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}