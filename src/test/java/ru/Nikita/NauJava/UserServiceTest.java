package ru.Nikita.NauJava;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.UserRepository;
import ru.Nikita.NauJava.transaction.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private String testEmail;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setEmail(testEmail);
        testUser.setPasswordHash("hash123");
        testUser.setFullName("Test User");
        testUser.setRole("USER");
    }

    @Nested
    @DisplayName("getUserByEmail() Tests")
    class GetUserByEmailTests {

        @Test
        @DisplayName("Should return user when user exists with the given email")
        void shouldReturnUser_WhenUserExists() {
            when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

            Optional<UserEntity> result = userService.getUserByEmail(testEmail);

            assertThat(result)
                    .isPresent()
                    .contains(testUser);
            assertThat(result.get().getEmail()).isEqualTo(testEmail);
            assertThat(result.get().getFullName()).isEqualTo("Test User");

            verify(userRepository, times(1)).findByEmail(testEmail);
        }

        @Test
        void shouldReturnEmpty_WhenUserDoesNotExist() {
            when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

            Optional<UserEntity> result = userService.getUserByEmail("nonexistent@example.com");

            assertThat(result).isEmpty();

            verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
        }

        @Test
        void shouldThrowException_WhenEmailIsNull() {
            when(userRepository.findByEmail(null)).thenThrow(new IllegalArgumentException("Email cannot be null"));

            assertThrows(IllegalArgumentException.class, () -> {
                userService.getUserByEmail(null);
            });

            verify(userRepository, times(1)).findByEmail(null);
        }

        @Test
        void shouldReturnEmpty_WhenEmailIsEmpty() {
            when(userRepository.findByEmail("")).thenReturn(Optional.empty());

            Optional<UserEntity> result = userService.getUserByEmail("");

            assertThat(result).isEmpty();
            verify(userRepository, times(1)).findByEmail("");
        }

    }
}
