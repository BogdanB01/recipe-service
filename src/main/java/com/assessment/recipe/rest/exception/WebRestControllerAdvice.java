package com.assessment.recipe.rest.exception;

import com.assessment.recipe.rest.dto.WebError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class WebRestControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public WebError handleNotFoundException(NotFoundException exception, WebRequest webRequest) {
        return createWebError(HttpStatus.NOT_FOUND, exception, webRequest);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public WebError handleBadRequestException(BadRequestException exception, WebRequest webRequest) {
        return createWebError(HttpStatus.BAD_REQUEST, exception, webRequest);
    }

    private WebError createWebError(HttpStatus httpStatus, Exception exception, WebRequest webRequest) {
        return WebError.builder()
                .message(exception.getMessage())
                .statusCode(httpStatus.value())
                .path(((ServletWebRequest) webRequest).getRequest().getRequestURI())
                .timestamp(new Date())
                .build();
    }
}
