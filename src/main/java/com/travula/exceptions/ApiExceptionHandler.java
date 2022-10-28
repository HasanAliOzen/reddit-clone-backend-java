package com.travula.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.ProviderNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> springRedditException(SpringRedditException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> alreadyExistException(AlreadyExistException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> providerNotFoundException(ProviderNotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
}
