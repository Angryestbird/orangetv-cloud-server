package com.orangetv.cloud.videostore.handler;

import com.orangetv.cloud.videostore.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Response<Object> handleException(RuntimeException e) {
        if (e instanceof AccessDeniedException) {
            throw e;
        }
        log.error("Business exception occurred.", e);
        return Response.fail(e.getMessage());
    }
}
