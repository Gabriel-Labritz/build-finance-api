package com.gabriellabritz.build_finance_api.domain.authentication.register;

import com.gabriellabritz.build_finance_api.domain.authentication.register.dtos.requets.UserRegisterRequestDto;
import com.gabriellabritz.build_finance_api.domain.authentication.register.dtos.responses.UserRegisterResponseDto;
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
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserRegisterResponseDto> userRegister(@RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerService.register(userRegisterRequestDto));
    }
}
