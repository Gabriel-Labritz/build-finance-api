package com.gabriellabritz.build_finance_api.domain.authentication.register.dtos.requets;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDto(
        @NotBlank(message = "O campo nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome informado deve conter entre 3 a 100 caracteres.")
        String name,

        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "O formato do email informado é inválido.")
        @Size(max = 254, message = "O email deve possuir no máximo 254 caracteres.")
        String email,

        @NotBlank(message = "O campo senha é obrigatório.")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$",
                message = "A senha deve conter no mínimo 8 caracteres, contendo pelo menos, uma letra maiúscula, um número e um caractere especial."
        )
        String password
) {
}
