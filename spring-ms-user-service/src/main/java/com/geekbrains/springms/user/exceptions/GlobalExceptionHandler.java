package com.geekbrains.springms.user.exceptions;

import com.geekbrains.springms.api.AppError;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> responseStatusExceptionHandler (ResponseStatusException ex) {
        return new ResponseEntity<>(
                new AppError(String.valueOf(ex.getStatusCode().value()), ex.getMessage()), ex.getStatusCode()
        );
    }

}
