package faang.school.analytics.exception;

import faang.school.analytics.dto.error.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleExceptionsWithStatusNotFound(Exception ex) {
        return getErrorResponse(ex, NOT_FOUND);
    }

    @ExceptionHandler({
            AnalyticsConvertingException.class
    })
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleExceptionsWithStatusBadRequest(Exception ex) {
        return getErrorResponse(ex, BAD_REQUEST);
    }

    private ErrorResponse getErrorResponse(Exception ex, HttpStatus status) {
        log.error("{}", ex.toString());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(status.value())
                .statusName(status.name())
                .build();
    }
}
