package com.gabriellabritz.build_finance_api.infra.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Um ou mais campos possuem valores inválidos");
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        problemDetail.setTitle("Erro de validação");
        problemDetail.setProperty("errors_fields", errors);

        return problemDetail;
    }
}
