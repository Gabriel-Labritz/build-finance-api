package com.gabriellabritz.build_finance_api.infra.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class AuthenticationExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());

        problemDetail.setTitle("Erro de autorização.");
        return problemDetail;
    }
}
