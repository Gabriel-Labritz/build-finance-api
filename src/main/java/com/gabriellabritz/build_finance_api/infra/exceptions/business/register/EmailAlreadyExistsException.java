package com.gabriellabritz.build_finance_api.infra.exceptions.business.register;

import com.gabriellabritz.build_finance_api.infra.exceptions.business.BusinessException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super("O email informado já está cadastrado.", HttpStatus.CONFLICT);
    }
}
