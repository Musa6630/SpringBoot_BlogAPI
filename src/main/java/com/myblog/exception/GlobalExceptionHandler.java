package com.myblog.exception;

import com.myblog.payload.ErrorDetails;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFountException.class)
    public ResponseEntity<ErrorDetails> resourceNotFound(
             ResourceNotFountException ex,
             WebRequest webRequest
    ){
        ErrorDetails err=new ErrorDetails(new Date(), ex.getMessage(),webRequest.getDescription(true));
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);

    }
}
