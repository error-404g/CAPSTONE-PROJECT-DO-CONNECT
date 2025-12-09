package com.example.geh.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.geh.service.ProductService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

/* GlobalExceptionHandler is the centralized class to define all the custom exception as well define the generic exception 
 * which will be application globally and for Swagger With Global Exception Handler 
 * remember to change the version of Swagger OpenApi to 2.8.6*/ 
@ControllerAdvice
public class GlobalExceptionHandler {

	//Logger object is used to display the messages on console (warn , error , info) // concept of  Core java itself 
	private static final Logger Logger = LoggerFactory.getLogger(ProductService.class);
    
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex , HttpServletRequest request) {
        Logger.warn("Resource not found : {} - " , request.getRequestURI(),ex.getMessage());
        
    
       
        return new ResponseEntity<>("Resource Not Found", HttpStatus.NOT_FOUND);
    }


    // You can also handle generic Exception if needed
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex , HttpServletRequest request) {
       
    	Logger.error("Unhandled exception : {} - " , request.getRequestURI(),ex.getMessage());
        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}