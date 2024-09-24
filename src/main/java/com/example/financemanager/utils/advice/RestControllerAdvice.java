package com.example.financemanager.utils.advice;

import com.example.financemanager.utils.model.Error;
import com.example.financemanager.utils.model.ResponseEnvelope;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
@Component
public class RestControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        var error = new Error();
        error.setStatusCode(HttpStatus.NOT_FOUND);
        error.setMessages(List.of(exception.getMessage()));
        return new ResponseEntity<>(new ResponseEnvelope<>(null, error, null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var error = new Error();
        error.setStatusCode(HttpStatus.BAD_REQUEST);

        var errorMessages = exception.getBindingResult().getFieldErrors().stream().filter(Objects::nonNull)
                .map(m -> m.getField() + " " + m.getDefaultMessage()).toList();
        error.setMessages(errorMessages);
        return new ResponseEntity<>(new ResponseEnvelope<>(null, error, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        var error = new Error();
        error.setStatusCode(HttpStatus.BAD_REQUEST);
        error.setMessages(List.of(exception.getMessage()));
        return new ResponseEntity<>(new ResponseEnvelope<>(null, error, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<Object> handleCallNotPermittedException(CallNotPermittedException exception) {
        var error = new Error();
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessages(List.of("External service is unavailable"));
        return new ResponseEntity<>(new ResponseEnvelope<>(null, error, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception exception) {
        var error = new Error();
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessages(List.of(exception.getMessage()));
        return new ResponseEntity<>(new ResponseEnvelope<>(null, error, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
