package com.genetics.adn.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerError extends RuntimeException {

    private static final String MENSAJE_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.toString();

    public InternalServerError(Exception ex) {
        super(MENSAJE_ERROR, ex);
    }

    public InternalServerError(String mensaje) {
        super(mensaje);
    }

    public InternalServerError(String s, Throwable throwable) {
        super(s, throwable);
    }
}
