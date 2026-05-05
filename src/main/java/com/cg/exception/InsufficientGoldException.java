package com.cg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientGoldException extends RuntimeException {
    public InsufficientGoldException(String message) {
        super(message);
    }
}
