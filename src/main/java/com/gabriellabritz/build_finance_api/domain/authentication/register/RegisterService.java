package com.gabriellabritz.build_finance_api.domain.authentication.register;

import com.gabriellabritz.build_finance_api.domain.authentication.register.dtos.requets.UserRegisterRequestDto;
import com.gabriellabritz.build_finance_api.domain.authentication.register.dtos.responses.UserRegisterResponseDto;
import com.gabriellabritz.build_finance_api.domain.user.User;
import com.gabriellabritz.build_finance_api.domain.user.UserRepository;
import com.gabriellabritz.build_finance_api.infra.exceptions.business.register.EmailAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) {
        String email = userRegisterRequestDto.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        String passwordHash = passwordEncoder.encode(userRegisterRequestDto.password());
        userRepository.save(new User(userRegisterRequestDto.name(), email, passwordHash));

        return new UserRegisterResponseDto("Cadastro realizado com sucesso! Verifique seu email para ativar a conta.");
    }
}
