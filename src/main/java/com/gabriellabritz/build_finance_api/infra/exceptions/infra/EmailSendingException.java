package com.gabriellabritz.build_finance_api.infra.exceptions.infra;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(Throwable cause) {
        super("Ocorreu um erro ao enviar o email.", cause);
    }
}
