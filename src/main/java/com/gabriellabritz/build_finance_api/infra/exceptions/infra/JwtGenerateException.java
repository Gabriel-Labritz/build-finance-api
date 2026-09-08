package com.gabriellabritz.build_finance_api.infra.exceptions.infra;

public class JwtGenerateException extends RuntimeException {
    public JwtGenerateException(String message) {
        super(message);
    }
}
