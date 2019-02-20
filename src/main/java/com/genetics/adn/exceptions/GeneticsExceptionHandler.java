package com.genetics.adn.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GeneticsExceptionHandler {

    @ExceptionHandler(ForbiddenMutantException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handle(ForbiddenMutantException ex) {
        return String.format("ERROR CODIGO(%s): Codigo ADN humano", ex.getMessage());
    }

    @ExceptionHandler(BadRequestMutantException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handle(BadRequestMutantException ex) {
        return String.format("ERROR CODIGO(%s): Secuencia de ADN invalida", ex.getMessage());
    }

    @ExceptionHandler(DataBaseConnectionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handle(DataBaseConnectionException ex) {
        return String.format("ERROR CODIGO(%s): conexion con la Base de Datos interrumpida", ex.getMessage());
    }

}
