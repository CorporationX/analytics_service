package faang.school.analytics.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.DataValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(fieldError -> Map.of(
                                fieldError.getField(),
                                String.format("%s. Actual value: %s", fieldError.getDefaultMessage(), fieldError.getRejectedValue())
                        ))
                        .toList()
        );
    }

    @ExceptionHandler(DataValidationException.class)
    public void handleDataValidationException(DataValidationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("message", "Some of fields are incorrect");
            put("details", e.getMessage());
        }}));
        log.error("некоторые из введенных полей введены неверно: ", e);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleRuntimeException(IOException e) {
        log.error("произошла ошибка при работе с потоками ввода-вывода: ", e);
        return ResponseEntity.internalServerError().body("some errors in app. please try again your operation later");
    }
}