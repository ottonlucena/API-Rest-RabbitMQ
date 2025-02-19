package com.application.dataService.SpringBootRest.data.exception;

import com.application.dataService.SpringBootRest.data.exception.dto.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleProductNotFound(ProductNotFoundException exception){
        logger.error("Product not found exception: ", exception);
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ErrorMessage> handleProductOufStock(ProductOutOfStockException exception){
        logger.error("Product out of stock exception: ", exception);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralExceptions(Exception exception) {
        logger.error("Unexpected error occurred: ", exception);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                new RuntimeException("An unexpected error occurred"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                new RuntimeException("Validation error: " + errorMessage));
    }

    //Metodo privado para construir respuesta de errorMessage
    private ResponseEntity<ErrorMessage> buildErrorResponse(HttpStatus httpStatus, Exception exception){
        ErrorMessage error = ErrorMessage.builder()
                .status(httpStatus)
                .message(exception.getMessage())
                .dateTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(httpStatus).body(error);
    }

}
