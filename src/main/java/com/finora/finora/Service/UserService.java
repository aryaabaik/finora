package com.finora.finora.Service;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User user) {

        if (userRepository.existsByUsernameAndIdNot(
                user.getUsername(),
                user.getId())) {

            throw new RuntimeException("Username sudah digunakan.");
        }

        if (userRepository.existsByEmailAndIdNot(
                user.getEmail(),
                user.getId())) {

            throw new RuntimeException("Email sudah digunakan.");
        }

        return userRepository.save(user);
    }
}