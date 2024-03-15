package com.example.bookstore.exception;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.SemanticException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("errorCode", ex.getHttpStatus());
        logger.info(body.toString());
        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

    @ExceptionHandler(SemanticException.class)
    public ResponseEntity<Object> semanticException(SemanticException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        var message = ex.getMessage().split("' of '")[0] + "'!";
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("errorCode", HttpStatus.BAD_REQUEST.value());
        logger.info(body.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getLocalizedMessage());
        body.put("errorCode", HttpStatus.BAD_REQUEST.value());
        logger.info(body.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getLocalizedMessage().contains("JSON parse error") ? "JSON parse error" : ex.getLocalizedMessage());
        body.put("errorCode", HttpStatus.BAD_REQUEST.value());
        logger.info(body.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> httpEntityNotFoundException(HttpMessageNotReadableException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getLocalizedMessage());
        body.put("errorCode", HttpStatus.BAD_REQUEST.value());
        logger.info(body.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(body);
    }

}