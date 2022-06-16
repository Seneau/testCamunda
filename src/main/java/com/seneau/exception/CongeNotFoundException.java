package com.seneau.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CongeNotFoundException extends RuntimeException {

    public CongeNotFoundException(String message) {
        super(message);
    }
}
