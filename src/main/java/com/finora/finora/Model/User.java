package com.finora.finora.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "username")
})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    private String email;

    private String username;

    @Column(name = "password")
    private String passwordHash;

    @Column(name = "password_hash")
    private String passwordHashCopy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean enabled;

    private String foto;

    @PrePersist
    @PreUpdate
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (!enabled) {
            enabled = true;
        }
        if (passwordHash != null && (passwordHashCopy == null || !passwordHashCopy.equals(passwordHash))) {
            passwordHashCopy = passwordHash;
        }
    }
}