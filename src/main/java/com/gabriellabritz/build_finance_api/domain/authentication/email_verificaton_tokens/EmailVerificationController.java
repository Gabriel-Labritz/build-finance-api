package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos.EmailVerificationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class EmailVerificationController {
    private final EmailVerificationTokenService emailVerificationTokenService;

    public EmailVerificationController(EmailVerificationTokenService emailVerificationTokenService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    @GetMapping("/verify-account")
    public ResponseEntity<EmailVerificationResponseDto> verifyAccount(@RequestParam(value = "token", required = true) String token) {
        return ResponseEntity.ok().body(emailVerificationTokenService.verifyEmailVerificationToken(token));
    }
}
