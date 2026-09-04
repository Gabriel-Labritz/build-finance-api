package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos.EmailVerificationResponseDto;
import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos.ResendVerificationRequestDto;
import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class EmailVerificationService {
    private final UserRepository userRepository;
    private final EmailVerificationTokenService emailVerificationTokenService;

    public EmailVerificationService(UserRepository userRepository, EmailVerificationTokenService emailVerificationTokenService) {
        this.userRepository = userRepository;
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    public EmailVerificationResponseDto resendVerificationEmail(ResendVerificationRequestDto resendVerificationRequestDto) {
        String email = resendVerificationRequestDto.email().trim().toLowerCase(Locale.ROOT);

        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            if(!user.isVerified()) {
                emailVerificationTokenService.updateEmailVerificationToken(user);
            }
        }

        return new EmailVerificationResponseDto("Se houver uma conta pendente de verificação para este email, um novo email de verificação será enviado.");
    }
}
