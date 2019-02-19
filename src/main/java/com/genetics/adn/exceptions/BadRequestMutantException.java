package com.genetics.adn.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class BadRequestMutantException extends HttpClientErrorException {

    public BadRequestMutantException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
