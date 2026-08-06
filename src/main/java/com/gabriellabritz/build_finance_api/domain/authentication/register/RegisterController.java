package com.gabriellabritz.build_finance_api.domain.authentication.register;

import com.gabriellabritz.build_finance_api.domain.authentication.register.dtos.requets.UserRegisterRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RegisterController {
    @PostMapping("/sign-up")
    public ResponseEntity<String> userRegister(@RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body("Rota de registro do usuário.");
    }
}
