package com.ysma.ppt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RestControllerAdvice 针对controller + json's response场景
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 捕获  定制业务异常
     * @param request 预留坑位 获取apiParam
     */
    @ExceptionHandler(CustomException.class)
    public ErrorResponseEntity customExceptionHandler(HttpServletRequest request,
                                                      final Exception e,
                                                      HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        CustomException exception = (CustomException) e;
        return new ErrorResponseEntity(
                String.valueOf(exception.getErrorCode()),
                exception.getMessage());
    }

    /**
     * 捕获  RuntimeException 异常
     * */
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponseEntity runtimeExceptionHandler(HttpServletRequest request,
                                                       final Exception e,
                                                       HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        RuntimeException exception = (RuntimeException) e;
        return new ErrorResponseEntity(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                exception.getMessage());
    }
}
