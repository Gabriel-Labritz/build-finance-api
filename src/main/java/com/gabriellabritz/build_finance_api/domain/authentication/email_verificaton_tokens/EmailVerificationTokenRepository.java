package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    Optional<EmailVerificationToken> findByTokenHash(byte[] tokenHash);
}
