package faang.school.analytics.exceptions.controller;

import faang.school.analytics.exception.NotFoundException;
import faang.school.analytics.exception.ValidationException;
import faang.school.analytics.exceptions.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleDateTimeParseException(DateTimeParseException e) {
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .causeMessage(e.getCause().getMessage())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException e) {
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .causeMessage(e.getCause().getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ErrorMessage handleNotFoundException(NotFoundException e) {
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .causeMessage(e.getCause().getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException", e);
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .causeMessage(e.getCause().getMessage())
                .build();
    }

    @ExceptionHandler({ConversionFailedException.class, ValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidation(RuntimeException e) {

        log.error("Caught exception from validation group", e);
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .causeMessage(e.getCause().getMessage())
                .build();
    }
}
