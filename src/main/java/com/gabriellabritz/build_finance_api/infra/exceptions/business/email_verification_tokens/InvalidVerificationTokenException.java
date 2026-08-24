package com.gabriellabritz.build_finance_api.infra.exceptions.business.email_verification_tokens;

import com.gabriellabritz.build_finance_api.infra.exceptions.business.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidVerificationTokenException extends BusinessException {
    public InvalidVerificationTokenException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
