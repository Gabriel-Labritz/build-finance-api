package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos.ResendVerificationRequestDto;
import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {
    @InjectMocks
    private EmailVerificationService emailVerificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailVerificationTokenService emailVerificationTokenService;

    private ResendVerificationRequestDto resendVerificationRequestDto;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        this.resendVerificationRequestDto = new ResendVerificationRequestDto("test@test.com");
    }

    @Nested
    class resendVerificationEmail {
        @Test
        @DisplayName("Deve enviar o email de verificação se o usuário for encontrado e não está verificado.")
        void shouldSendTheEmailVerificationIfUserIsFoundAndIsNotVerified() {
            // Arrange
            when(userRepository.findByEmail(resendVerificationRequestDto.email()))
                    .thenReturn(Optional.of(user));
            when(user.isVerified()).thenReturn(false);

            // Act
            emailVerificationService.resendVerificationEmail(resendVerificationRequestDto);

            // Assert
            verify(emailVerificationTokenService).updateEmailVerificationToken(user);
        }

        @Test
        @DisplayName("Não deve enviar o email de verificação se o usuário não for encontrado.")
        void shouldNotSendTheEmailVerificationIfUserIsNotFound() {
            // Arrange
            when(userRepository.findByEmail(resendVerificationRequestDto.email()))
                    .thenReturn(Optional.empty());

            // Act
            emailVerificationService.resendVerificationEmail(resendVerificationRequestDto);

            // Assert
            verify(emailVerificationTokenService, never()).updateEmailVerificationToken(user);
        }

        @Test
        @DisplayName("Não deve enviar o email de verificação se o usuário já está verificado.")
        void shouldNotSendTheEmailVerificationIfUserIsAlreadyVerified() {
            // Arrange
            when(userRepository.findByEmail(resendVerificationRequestDto.email()))
                    .thenReturn(Optional.of(user));
            when(user.isVerified()).thenReturn(true);

            // Act
            emailVerificationService.resendVerificationEmail(resendVerificationRequestDto);

            // Assert
            verify(emailVerificationTokenService, never()).updateEmailVerificationToken(user);
        }
    }
}