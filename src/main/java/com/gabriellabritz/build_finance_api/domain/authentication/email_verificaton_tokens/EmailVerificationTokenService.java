package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.infra.crypto.TokenHasher;
import com.gabriellabritz.build_finance_api.infra.email.EmailService;
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
        emailService.sendEmailVerification(user.getName(), user.getEmail(), token);
    }
}
