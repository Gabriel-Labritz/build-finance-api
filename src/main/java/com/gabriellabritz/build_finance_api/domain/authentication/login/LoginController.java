package com.gabriellabritz.build_finance_api.domain.authentication.login;

import com.gabriellabritz.build_finance_api.domain.authentication.login.dtos.request.LoginRequestDto;
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
    public ResponseEntity<Void> signIn(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        loginService.loginUser(loginRequestDto);
        return ResponseEntity.ok().build();
    }
}
