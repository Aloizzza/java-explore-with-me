package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.error("BAD_REQUEST", e);
        return ErrorResponse.builder()
                .error(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error("NOT_FOUND", e);
        return ErrorResponse.builder()
                .error(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("INTERNAL_SERVER_ERROR", e);
        return ErrorResponse.builder()
                .error(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final ConflictException e) {
        log.error("CONFLICT", e);
        return ErrorResponse.builder()
                .error(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .build();
    }
}