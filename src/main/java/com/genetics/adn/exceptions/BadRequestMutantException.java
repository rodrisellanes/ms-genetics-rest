package com.genetics.adn.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class BadRequestMutantException extends RuntimeException {

    private static final String MENSAJE_ERROR = HttpStatus.BAD_REQUEST.toString();

    public BadRequestMutantException() {
        super(MENSAJE_ERROR);
    }

    public BadRequestMutantException(String mensaje) {
        super(mensaje);
    }

    public BadRequestMutantException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
