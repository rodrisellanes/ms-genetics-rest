package com.genetics.adn.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ForbiddenMutantException extends HttpClientErrorException {

    public ForbiddenMutantException() {
        super(HttpStatus.FORBIDDEN);
    }
}
