package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens;

import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos.EmailVerificationResponseDto;
import com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos.ResendVerificationRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class EmailVerificationController {
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(
            EmailVerificationTokenService emailVerificationTokenService,
            EmailVerificationService emailVerificationService) {
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/verify-account")
    public ResponseEntity<EmailVerificationResponseDto> verifyAccount(@RequestParam(value = "token", required = true) String token) {
        return ResponseEntity.ok().body(emailVerificationTokenService.verifyEmailVerificationToken(token));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<EmailVerificationResponseDto> resendVerificationEmail(
            @RequestBody @Valid ResendVerificationRequestDto resendVerificationRequestDto) {
        return ResponseEntity.ok().body(emailVerificationService.resendVerificationEmail(resendVerificationRequestDto));
    }
}
