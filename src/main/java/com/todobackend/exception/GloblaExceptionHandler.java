package com.todobackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GloblaExceptionHandler {
    @ExceptionHandler({IdNotFoundException.class})
    public ResponseEntity<Object> handleIdNotFoundException(IdNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler({UserNameNotFoundException.class})
    public ResponseEntity<Object> handleUserNameNotFoundException(UserNameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({UserNameExistException.class})
    public ResponseEntity<Object> handleUserNameAlreadyExist(UserNameExistException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

}
