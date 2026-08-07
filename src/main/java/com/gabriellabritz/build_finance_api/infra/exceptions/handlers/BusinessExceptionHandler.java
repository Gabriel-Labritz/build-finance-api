package com.gabriellabritz.build_finance_api.infra.exceptions.handlers;

import com.gabriellabritz.build_finance_api.infra.exceptions.business.BusinessException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BusinessExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException exception) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(exception.getHttpStatus(), exception.getMessage());

        problemDetail.setTitle("Erro de regras de negócios.");
        return problemDetail;
    }
}
