package com.rq.challenge.api.aspect;

import com.rq.challenge.exceptions.RestClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class RestClientExceptionHandler {
    private static final String STATUS_FAILED = "Failed";

    @ExceptionHandler(RestClientException.class)
    public Map<String, Object> handleRestClientExceptions(RestClientException e) {
        log.error("RestControllerAdvice() :: AOP RestClientException Handler");
        Map<String, Object> map = new HashMap<>();
        map.put("status", STATUS_FAILED);
        map.put("message", e.getMessage());
        return map;
    }
}
