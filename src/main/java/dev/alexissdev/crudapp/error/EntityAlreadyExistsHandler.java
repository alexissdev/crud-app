package dev.alexissdev.crudapp.error;

import dev.alexissdev.crudapp.error.response.ErrorResponse;
import dev.alexissdev.crudapp.exception.EntityAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntityAlreadyExistsHandler {


    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(EntityAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(null, null, ex.getMessage())
        );
    }
}
