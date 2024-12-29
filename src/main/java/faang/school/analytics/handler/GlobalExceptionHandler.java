package faang.school.analytics.handler;

import faang.school.analytics.exception.DataValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleDataValidationException(DataValidationException e) {
        log.error("IllegalStateException", e);
        return new ResponseEntity<>("Internal server error", HttpStatus.BAD_REQUEST);
    }
}
