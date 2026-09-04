package com.gabriellabritz.build_finance_api.domain.authentication.login.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "O formato do email informado é inválido.")
        String email,

        @NotBlank(message = "O campo senha é obrigatório.")
        String password
) {
}
