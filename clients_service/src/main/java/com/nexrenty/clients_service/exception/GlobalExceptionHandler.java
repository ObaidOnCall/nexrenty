package com.nexrenty.clients_service.exception;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nexrenty.clients_service.dtos.ErrorResponse;
import com.nexrenty.clients_service.dtos.ErrorResponse.ErrorDetail;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice  
public class GlobalExceptionHandler {

    private static final  String MESSAGE = "message" ;
    private static final  String STATUS = "status" ;
    private static final  String ERRORS = "errors" ;
    

    @ExceptionHandler(UnableToProccessIteamException.class)
    public ResponseEntity<ErrorResponse> handleUnableToProccessIteamException(UnableToProccessIteamException ex) {
        log.error("UnableToProccessIteamException: {}", ex.getMessage());

        ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail();
        errorDetail.setField("general"); // Default field
        errorDetail.setMessage(ex.getMessage());

        // Create the error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage("Processing failed");
        errorResponse.setErrors(List.of(errorDetail));


        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Map<String, Object>>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: {}", ex.getMessage());
        
        // Extract detailed information about the JSON parse error
        String errorMessage = "Invalid JSON format";
        
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, false);
        response.put(MESSAGE, errorMessage);

        Map<String, Map<String, Object>> error = new HashMap<>();
        error.put("error", response);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(false); // Indicates the request failed
        errorResponse.setMessage("Validation failed"); // General error message

        List<ErrorDetail> validationErrors = ex.getConstraintViolations().stream()
        .map(violation -> {
            // Create an ErrorDetail object for each validation error
            ErrorDetail errorDetail = new ErrorDetail();

            // Extract the field name from the property path
            errorDetail.setField(extractSimpleFieldName(violation.getPropertyPath().toString()));

            // Add the validation error message
            errorDetail.setMessage(violation.getMessage());

            return errorDetail;
        })
        .toList();

        errorResponse.setErrors(validationErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }






    /**private methods */

    private String extractSimpleFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
}