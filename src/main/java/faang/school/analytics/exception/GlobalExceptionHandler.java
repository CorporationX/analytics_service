package faang.school.analytics.exception;

import faang.school.analytics.exception.exceptions.AnalyticsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AnalyticsException.class)
    public ResponseEntity<String> handleAnalyticsRequestException(AnalyticsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
