package com.example.challenge.api.aspect;

import com.example.challenge.exceptions.APIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
@Slf4j
public class RestControllerAdvice {
    @ExceptionHandler(APIException.class)
    public Object apiRequestExceptionHandler(Exception e) {
        log.debug("apiRequestExceptionHandler() : for {}", e.getMessage());
        return "error";
    }
}
