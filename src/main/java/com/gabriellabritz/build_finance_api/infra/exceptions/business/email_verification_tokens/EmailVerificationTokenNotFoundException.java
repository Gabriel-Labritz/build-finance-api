package com.gabriellabritz.build_finance_api.infra.exceptions.business.email_verification_tokens;

import com.gabriellabritz.build_finance_api.infra.exceptions.business.BusinessException;
import org.springframework.http.HttpStatus;

public class EmailVerificationTokenNotFoundException extends BusinessException {
    public EmailVerificationTokenNotFoundException() {
        super("Token de verificação não encontrado para o usuário.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
