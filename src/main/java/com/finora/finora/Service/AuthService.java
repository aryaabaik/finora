
package com.finora.finora.Service;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // REGISTER
    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail()))
            throw new RuntimeException("Email sudah digunakan.");

        if (userRepository.existsByUsername(user.getUsername()))
            throw new RuntimeException("Username sudah digunakan.");

        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
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
        user.setPasswordHash(encoder.encode(newPassword));
        userRepository.save(user);
    }
}