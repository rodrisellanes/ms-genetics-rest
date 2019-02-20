package com.genetics.adn.exceptions;

import org.springframework.http.HttpStatus;

public class DataBaseConnectionException extends RuntimeException {

    private static final String MENSAJE_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.toString();

    public DataBaseConnectionException(Exception ex) {
        super(MENSAJE_ERROR, ex);
    }

    public DataBaseConnectionException(String mensaje) {
        super(mensaje);
    }

    public DataBaseConnectionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
