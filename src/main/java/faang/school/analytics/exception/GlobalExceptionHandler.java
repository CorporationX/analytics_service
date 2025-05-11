package faang.school.analytics.exception;

import faang.school.analytics.dto.error.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleExceptionsWithStatusNotFound(Exception ex) {
        return ResponseEntity.status(NOT_FOUND).body(getErrorResponse(ex));
    }

    @ExceptionHandler(AnalyticsConvertingException.class)
    public ResponseEntity<ErrorResponse> handleAnalyticsConvertingException(AnalyticsConvertingException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(getErrorResponse(ex));
    }

    @ExceptionHandler(JsonDeserializationException.class)
    public ResponseEntity<ErrorResponse> handleJsonDeserializationException(JsonDeserializationException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(getErrorResponse(ex));
    }

    private ErrorResponse getErrorResponse(Exception ex) {
        log.error("{}", ex.toString());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }
}
