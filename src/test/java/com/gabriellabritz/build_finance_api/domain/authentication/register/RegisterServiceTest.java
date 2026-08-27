package com.gabriellabritz.build_finance_api.domain.authentication.register;

import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.EmailVerificationTokenService;
import com.gabriellabritz.build_finance_api.domain.authentication.register.dtos.requets.UserRegisterRequestDto;
import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.domain.user.UserRepository;
import com.gabriellabritz.build_finance_api.infra.exceptions.business.register.EmailAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
    @InjectMocks
    private RegisterService registerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailVerificationTokenService emailVerificationTokenService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private UserRegisterRequestDto dto;

    @BeforeEach
    void setUp() {
        this.dto = new UserRegisterRequestDto("Test", "test@test.com", "Test123!");
    }

    @Nested
    class register {
        @Test
        @DisplayName("Deve lançar a exceção EmailAlreadyExistsException quando já existe o email enviado já está cadastrado.")
        void shouldThrowEmailAlreadyExistsExceptionWhenUserEmailExists() {
            // Arrange
            when(userRepository.existsByEmail(dto.email())).thenReturn(true);

            // Act + Assert
            assertThrows(EmailAlreadyExistsException.class, () -> registerService.register(dto));

            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).save(any());
            verify(emailVerificationTokenService, never()).createEmailVerificationToken(any());
        }

        @Test
        @DisplayName("Deve salvar o usuário com o hash da senha.")
        void shouldSaveUserWithPasswordHash() {
            // Arrange
            String passwordHash = "password-hash";
            when(userRepository.existsByEmail(dto.email())).thenReturn(false);
            when(passwordEncoder.encode(dto.password())).thenReturn(passwordHash);

            // Act
            registerService.register(dto);

            // Assert
            verify(passwordEncoder).encode(dto.password());
            verify(userRepository).save(userCaptor.capture());

            User userCaptured = userCaptor.getValue();
            assertEquals("Test", userCaptured.getName());
            assertEquals("test@test.com", userCaptured.getEmail());
            assertEquals(passwordHash, userCaptured.getPassword());
        }

        @Test
        @DisplayName("Deve criar o token de verificação após o registro.")
        void shouldCreateVerificationTokenAfterRegister() {
            // Arrange
            String passwordHash = "password-hash";

            when(userRepository.existsByEmail(dto.email())).thenReturn(false);
            when(passwordEncoder.encode(dto.password())).thenReturn(passwordHash);
            when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            registerService.register(dto);

            // Assert
            verify(userRepository).save(userCaptor.capture());
            User userCaptured = userCaptor.getValue();

            verify(emailVerificationTokenService).createEmailVerificationToken(userCaptured);
        }
    }
}