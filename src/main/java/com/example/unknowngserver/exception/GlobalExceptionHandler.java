package com.example.unknowngserver.exception;

import com.example.unknowngserver.exception.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArticleException.class)
    public ErrorResponse articleExceptionHandler(ArticleException e) {
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(CommentException.class)
    public ErrorResponse commentExceptionHandler(CommentException e) {
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(ReportException.class)
    public ErrorResponse reportExceptionHandler(ReportException e) {
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }

    @ExceptionHandler(PasswordException.class)
    public ErrorResponse passwordExceptionHandler(PasswordException e) {
        return new ErrorResponse(e.getStatus(), e.getErrorCode(), e.getErrorMessage());
    }
}
