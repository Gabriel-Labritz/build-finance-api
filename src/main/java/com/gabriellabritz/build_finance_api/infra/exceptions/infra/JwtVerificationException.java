package com.gabriellabritz.build_finance_api.infra.exceptions.infra;

import com.gabriellabritz.build_finance_api.infra.exceptions.business.BusinessException;
import org.springframework.http.HttpStatus;

public class JwtVerificationException extends BusinessException {
    public JwtVerificationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
