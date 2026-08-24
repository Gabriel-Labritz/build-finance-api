package com.gabriellabritz.build_finance_api.infra.exceptions.infra;

public class LoadTemplateException extends RuntimeException {
    public LoadTemplateException(Throwable cause) {
        super("Erro ao carregar template", cause);
    }
}
