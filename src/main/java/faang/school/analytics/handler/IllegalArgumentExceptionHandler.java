package faang.school.analytics.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class IllegalArgumentExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();

        log.error("Illegal argument {}", e.getMessage(), e);
        error.put("error", e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
