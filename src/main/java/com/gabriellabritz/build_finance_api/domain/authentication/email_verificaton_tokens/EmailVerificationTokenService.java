package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos.EmailVerificationResponseDto;
import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.infra.crypto.TokenHasher;
import com.gabriellabritz.build_finance_api.infra.email.EmailService;
import com.gabriellabritz.build_finance_api.infra.exceptions.business.email_verification_tokens.InvalidVerificationTokenException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailVerificationTokenService {
    private final TokenHasher tokenHasher;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailService emailService;

    public EmailVerificationTokenService(
            TokenHasher tokenHasher,
            EmailVerificationTokenRepository emailVerificationTokenRepository,
            EmailService emailService
    ) {
        this.tokenHasher = tokenHasher;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.emailService = emailService;
    }

    public void createEmailVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        byte[] tokenHash = tokenHasher.hash(token);

        emailVerificationTokenRepository.save(new EmailVerificationToken(tokenHash, user));
        emailService.sendVerificationEmail(user.getName(), user.getEmail(), token);
    }

    public EmailVerificationResponseDto verifyEmailVerificationToken(String token) {
        byte[] tokenHash = tokenHasher.hash(token);

        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidVerificationTokenException("Token de verificação inválido"));

        verificationToken.validate();
        verificationToken.getUser().verify();
        emailVerificationTokenRepository.delete(verificationToken);

        return new EmailVerificationResponseDto("Sua conta foi verificada com sucesso! Faça seu login e começe a utilizar a plataforma.");
    }
}
