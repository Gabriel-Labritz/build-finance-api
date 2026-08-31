package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.infra.crypto.TokenHasher;
import com.gabriellabritz.build_finance_api.infra.email.EmailService;
import com.gabriellabritz.build_finance_api.infra.exceptions.business.email_verification_tokens.EmailVerificationTokenNotFoundException;
import com.gabriellabritz.build_finance_api.infra.exceptions.business.email_verification_tokens.InvalidVerificationTokenException;
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

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationTokenServiceTest {
    @InjectMocks
    private EmailVerificationTokenService emailVerificationTokenService;

    @Mock
    private TokenHasher tokenHasher;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private EmailService emailService;

    private User user;

    @Captor
    private ArgumentCaptor<EmailVerificationToken> emailVerificationTokenCaptor;

    @Captor
    private ArgumentCaptor<String> tokenArgumentCaptor;

    @Mock
    private EmailVerificationToken emailVerificationTokenMock;

    @Captor
    private ArgumentCaptor<byte[]> tokenHashArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.user = new User("Test", "test@test.com", "passowrd-hash");
    }

    @Nested
    class createEmailVerificationToken {
        @Test
        @DisplayName("Deve salvar o token de verificação com o hash gerado.")
        void shouldSaveVerificationTokenWithHash() {
            // Arrange
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);

            when(tokenHasher.hash(anyString())).thenReturn(tokenHash);
            when(emailVerificationTokenRepository.save(any(EmailVerificationToken.class)))
                    .thenAnswer(i -> i.getArgument(0));

            // Act
            emailVerificationTokenService.createEmailVerificationToken(user);

            // Assert
            verify(tokenHasher).hash(anyString());
            verify(emailVerificationTokenRepository).save(emailVerificationTokenCaptor.capture());

            EmailVerificationToken emailVerificationTokenCaptured = emailVerificationTokenCaptor.getValue();

            assertArrayEquals(tokenHash, emailVerificationTokenCaptured.getTokenHash());
            assertEquals(user, emailVerificationTokenCaptured.getUser());
        }

        @Test
        @DisplayName("Deve enviar o token original no email.")
        void shouldSendOriginalTokenInEmail() {
            // Arrange
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);

            when(tokenHasher.hash(anyString())).thenReturn(tokenHash);
            when(emailVerificationTokenRepository.save(any(EmailVerificationToken.class)))
                    .thenAnswer(i -> i.getArgument(0));

            // Act
            emailVerificationTokenService.createEmailVerificationToken(user);

            // Assert
            verify(tokenHasher).hash(tokenArgumentCaptor.capture());
            String originalToken = tokenArgumentCaptor.getValue();

            verify(emailService).sendVerificationEmail(user.getName(), user.getEmail(), originalToken);
        }
    }

    @Nested
    class verifyEmailVerificationToken {
        @Test
        @DisplayName("Deve lançar a exceção InvalidVerificationTokenException quando o token de verificação é inválido.")
        void shouldThrowInvalidVerificationTokenExceptionWhenVerificationTokenIsInvalid() {
            // Arrange
            String token = UUID.randomUUID().toString();
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);

            when(tokenHasher.hash(token)).thenReturn(tokenHash);
            when(emailVerificationTokenRepository.findByTokenHash(tokenHash))
                    .thenReturn(Optional.empty());

            // Act + Asserts
            assertThrows(InvalidVerificationTokenException.class, () -> emailVerificationTokenService.verifyEmailVerificationToken(token));
            verify(emailVerificationTokenRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Deve lançar a exceção InvalidVerificationTokenException quando o token de verificação está expirado.")
        void shouldThrowInvalidVerificationTokenExceptionWhenVerificationTokenIsExpired() {
            // Arrange
            String token = UUID.randomUUID().toString();
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);
            User userMock = mock(User.class);

            when(tokenHasher.hash(token)).thenReturn(tokenHash);
            when(emailVerificationTokenRepository.findByTokenHash(tokenHash))
                    .thenReturn(Optional.of(emailVerificationTokenMock));
            doThrow(new InvalidVerificationTokenException("Token de verificação expirado."))
                    .when(emailVerificationTokenMock)
                    .validate();

            // Act + Asserts
            assertThrows(InvalidVerificationTokenException.class, () -> emailVerificationTokenService.verifyEmailVerificationToken(token));

            verify(emailVerificationTokenMock).validate();
            verify(emailVerificationTokenRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Deve verificar a conta do usuário quando o token de verificação é válido.")
        void shouldVerifyUserAccountWhenVerificationTokenIsValid() {
            // Arrange
            String token = UUID.randomUUID().toString();
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);
            User userMock = mock(User.class);

            when(tokenHasher.hash(token)).thenReturn(tokenHash);
            when(emailVerificationTokenRepository.findByTokenHash(tokenHash))
                    .thenReturn(Optional.of(emailVerificationTokenMock));
            when(emailVerificationTokenMock.getUser()).thenReturn(userMock);

            // Act
            emailVerificationTokenService.verifyEmailVerificationToken(token);

            // Assert
            verify(tokenHasher).hash(token);
            verify(emailVerificationTokenRepository).findByTokenHash(tokenHash);
            verify(emailVerificationTokenMock).validate();
            verify(userMock).verify();
        }

        @Test
        @DisplayName("Deve deletar o token de verificação do usuário quando a conta for verificada.")
        void shouldDeleteTheUserVerificationTokenWhenTheAccountWasVerified() {
            // Arrange
            String token = UUID.randomUUID().toString();
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);
            User userMock = mock(User.class);

            when(tokenHasher.hash(token)).thenReturn(tokenHash);
            when(emailVerificationTokenRepository.findByTokenHash(tokenHash))
                    .thenReturn(Optional.of(emailVerificationTokenMock));
            when(emailVerificationTokenMock.getUser()).thenReturn(userMock);

            // Act
            emailVerificationTokenService.verifyEmailVerificationToken(token);

            // Assert
            verify(emailVerificationTokenRepository).delete(emailVerificationTokenMock);
        }
    }

    @Nested
    class updateEmailVerificationToken {
        @Test
        @DisplayName("Deve lançar EmailVerificationTokenNotFoundException quando o token do usuário não for encontrado.")
        void shouldThrowEmailVerificationTokenNotFoundExceptionWhenUserVerificationTokenNotFound() {
            // Arrange
            when(emailVerificationTokenRepository.findByUserId(user.getId()))
                    .thenReturn(Optional.empty());

            // Act + asserts
            assertThrows(EmailVerificationTokenNotFoundException.class, () -> emailVerificationTokenService.updateEmailVerificationToken(user));

            verify(tokenHasher, never()).hash(any());
            verify(emailVerificationTokenMock, never()).replaceToken(any());
            verify(emailService, never()).sendVerificationEmail(any(), any(), any());
        }

        @Test
        @DisplayName("Deve substituir o hash token antigo pelo hash token novo gerado.")
        void shouldReplaceOldTokenHashWithTheNewlyGeneratedToken() {
            // Arrange
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);

            when(emailVerificationTokenRepository.findByUserId(user.getId()))
                    .thenReturn(Optional.of(emailVerificationTokenMock));
            when(tokenHasher.hash(anyString())).thenReturn(tokenHash);

            // Act
            emailVerificationTokenService.updateEmailVerificationToken(user);

            // Asserts
            verify(tokenHasher).hash(anyString());
            verify(emailVerificationTokenMock).replaceToken(tokenHashArgumentCaptor.capture());

            byte[] tokenHashCaptured = tokenHashArgumentCaptor.getValue();
            assertArrayEquals(tokenHash, tokenHashCaptured);
        }

        @Test
        @DisplayName("Deve enviar o novo token de verificação original gerado para o email.")
        void shouldSendTheNewOriginalVerificationTokenGeneratedInEmail() {
            // Arrange
            byte[] tokenHash = "token-hash".getBytes(StandardCharsets.UTF_8);

            when(emailVerificationTokenRepository.findByUserId(user.getId()))
                    .thenReturn(Optional.of(emailVerificationTokenMock));
            when(tokenHasher.hash(anyString())).thenReturn(tokenHash);

            // Act
            emailVerificationTokenService.updateEmailVerificationToken(user);

            // Asserts
            verify(tokenHasher).hash(tokenArgumentCaptor.capture());
            String originalToken = tokenArgumentCaptor.getValue();

            verify(emailService).sendVerificationEmail(user.getName(), user.getEmail(), originalToken);
        }
    }
}