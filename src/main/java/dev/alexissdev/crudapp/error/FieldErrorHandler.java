package dev.alexissdev.crudapp.error;

import dev.alexissdev.crudapp.error.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class FieldErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add(new ErrorResponse(
                fieldError.getField(),
                (String) fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()
        )));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
