package faang.school.analytics.exception.handler;

import faang.school.analytics.exception.BadRequestException;
import faang.school.analytics.exception.DataValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleDataValidationException(DataValidationException e, WebRequest request) {
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .description(request.getDescription(false)).build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequestException(BadRequestException e, WebRequest request) {
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .description(request.getDescription(false)).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .description(request.getDescription(false)).build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleRuntimeException(RuntimeException e, WebRequest request) {
        return ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .description(request.getDescription(false)).build();
    }
}
