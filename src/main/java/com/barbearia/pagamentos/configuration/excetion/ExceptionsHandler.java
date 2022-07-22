package com.barbearia.pagamentos.configuration.excetion;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionsHandler {

    private final ErrorUtils utils;
    private static final String DEFAULT_MSG = "Ocorreu um erro. Entre em contato com suporte";

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({FeignException.class})
    @ResponseBody
    public Erro handleFeignException(FeignException e) {
        return new Erro(utils.extract(e));
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseBody
    public Erro resourceNotFound(ResourceNotFoundException e) {
        if (e.getMessage().length() > 0) {
            return new Erro(e.getMessage());
        } else {
            return new Erro(DEFAULT_MSG);
        }
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public Erro handleErrorServidor(RuntimeException e) {
        return new Erro(DEFAULT_MSG + " Erro: " + e.getMessage());
    }
}
