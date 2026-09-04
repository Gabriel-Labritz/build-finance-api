package com.gabriellabritz.build_finance_api.domain.authentication.login;

import com.gabriellabritz.build_finance_api.domain.authentication.login.dtos.request.LoginRequestDto;
import com.gabriellabritz.build_finance_api.domain.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;

    public LoginService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void loginUser(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.email().trim().toLowerCase(Locale.ROOT);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, loginRequestDto.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        User user = (User) authenticate.getPrincipal();
    }
}
