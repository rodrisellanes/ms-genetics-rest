package com.genetics.adn.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenMutantException extends RuntimeException {

    private static final String MENSAJE_ERROR = HttpStatus.FORBIDDEN.toString();

    public ForbiddenMutantException() {
        super(MENSAJE_ERROR);
    }

    public ForbiddenMutantException(String mensaje) {
        super(mensaje);
    }

    public ForbiddenMutantException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
