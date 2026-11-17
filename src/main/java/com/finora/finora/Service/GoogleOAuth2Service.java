package com.finora.finora.Service;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Transactional
public class GoogleOAuth2Service {

    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuth2Service.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public GoogleOAuth2Service(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create new user from Google OAuth2 registration
     */
    public User createUserFromGoogle(String email, String fullName, String pictureUrl) {
        logger.info("Creating new user from Google OAuth2 - email present: {}", email != null);

        // Generate unique username from email
        String baseUsername = generateUsernameFromEmail(email);
        String username = ensureUniqueUsername(baseUsername);

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setFullName(fullName != null && !fullName.trim().isEmpty() ? fullName : username);
        user.setFoto(validGooglePictureUrl(pictureUrl) ? pictureUrl : null);

        // Generate secure random password and hash it
        // Google users won't use this password - it's only for database constraint
        String randomPassword = generateSecureRandomPassword();
        user.setPasswordHash(passwordEncoder.encode(randomPassword));

        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        logger.info("User created successfully - username: {}", savedUser.getUsername());

        return savedUser;
    }

    public User syncGooglePicture(User user, String pictureUrl) {
        String googlePicture = validGooglePictureUrl(pictureUrl) ? pictureUrl : null;
        boolean currentlyGooglePicture = validGooglePictureUrl(user.getFoto());
        boolean hasCustomPicture = user.getFoto() != null
                && !user.getFoto().isBlank()
                && !currentlyGooglePicture;

        if (hasCustomPicture) {
            return user;
        }

        if (googlePicture != null) {
            user.setFoto(googlePicture);
        } else if (currentlyGooglePicture) {
            user.setFoto(null);
        } else {
            return user;
        }

        return userRepository.save(user);
    }

    private boolean validGooglePictureUrl(String pictureUrl) {
        if (pictureUrl == null || pictureUrl.isBlank()) {
            return false;
        }

        try {
            URI uri = URI.create(pictureUrl);
            return "https".equalsIgnoreCase(uri.getScheme()) && uri.getHost() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Generate username from email (part before @)
     */
    private String generateUsernameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "user" + System.currentTimeMillis();
        }

        String localPart = email.substring(0, email.indexOf("@"));
        // Remove non-alphanumeric characters
        String cleaned = localPart.replaceAll("[^a-zA-Z0-9]", "");

        if (cleaned.isEmpty()) {
            return "user" + System.currentTimeMillis();
        }

        // Limit to 20 characters
        return cleaned.length() > 20 ? cleaned.substring(0, 20) : cleaned;
    }

    /**
     * Ensure username is unique by appending number if needed
     */
    private String ensureUniqueUsername(String baseUsername) {
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;

            if (counter > 1000) {
                // Fallback to timestamp-based username
                username = baseUsername + System.currentTimeMillis();
                break;
            }
        }

        return username;
    }

    /**
     * Generate secure random password (32 characters)
     * This is only used for database constraint, not for actual login
     */
    private String generateSecureRandomPassword() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Check if user exists by email
     */
    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    /**
     * Find user by email
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }
}
