package com.gabriellabritz.build_finance_api.domain.authentication.login;

import com.gabriellabritz.build_finance_api.domain.authentication.login.dtos.request.LoginRequestDto;
import com.gabriellabritz.build_finance_api.domain.authentication.login.dtos.response.LoginResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponseDto> signIn(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok().body(loginService.loginUser(loginRequestDto));
    }
}
