package com.gabriellabritz.build_finance_api.domain.authentication.login;

import com.gabriellabritz.build_finance_api.domain.authentication.login.dtos.request.LoginRequestDto;
import com.gabriellabritz.build_finance_api.domain.authentication.login.dtos.response.LoginResponseDto;
import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.infra.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.email().trim().toLowerCase(Locale.ROOT);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, loginRequestDto.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        User user = (User) authenticate.getPrincipal();

        String accessToken = jwtService.generateJwtToken(user);

        return new LoginResponseDto(accessToken);
    }
}
