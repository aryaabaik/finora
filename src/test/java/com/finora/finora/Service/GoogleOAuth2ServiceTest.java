package com.finora.finora.Service;

import com.finora.finora.Model.User;
import com.finora.finora.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleOAuth2ServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private GoogleOAuth2Service googleOAuth2Service;

    @BeforeEach
    void setUp() {
        googleOAuth2Service = new GoogleOAuth2Service(userRepository, passwordEncoder);
    }

    @Test
    void syncGooglePictureAddsPictureWhenUserHasNone() {
        User user = new User();
        String pictureUrl = "https://lh3.googleusercontent.com/avatar";
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = googleOAuth2Service.syncGooglePicture(user, pictureUrl);

        assertSame(user, updatedUser);
        assertEquals(pictureUrl, user.getFoto());
        verify(userRepository).save(user);
    }

    @Test
    void syncGooglePicturePreservesUploadedProfilePhoto() {
        User user = new User();
        user.setFoto("local-profile-photo.jpg");

        User updatedUser = googleOAuth2Service.syncGooglePicture(
                user, "https://lh3.googleusercontent.com/avatar");

        assertSame(user, updatedUser);
        assertEquals("local-profile-photo.jpg", user.getFoto());
        verify(userRepository, never()).save(user);
    }

    @Test
    void syncGooglePictureClearsOldGooglePictureWhenGoogleHasNone() {
        User user = new User();
        user.setFoto("https://lh3.googleusercontent.com/old-avatar");
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = googleOAuth2Service.syncGooglePicture(user, null);

        assertSame(user, updatedUser);
        assertNull(user.getFoto());
        verify(userRepository).save(user);
    }
}
