package com.gabriellabritz.build_finance_api.domain.authentication.email_verificaton_tokens.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendVerificationRequestDto(
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "O formato do email informado é inválido.")
        String email
) {
}
