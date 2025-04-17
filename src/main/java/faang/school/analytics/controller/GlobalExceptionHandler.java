package faang.school.analytics.controller;

import faang.school.analytics.exception.DataValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для контроллеров.
 * Этот класс перехватывает исключения, возникающие в контроллерах, и возвращает соответствующие HTTP-ответы.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения типа {@link DataValidationException}.
     * Возвращает HTTP-ответ со статусом 400 (Bad Request) и сообщением об ошибке.
     *
     * @param dataValidationException исключение, которое было выброшено
     * @return ResponseEntity с сообщением об ошибке и статусом 400
     */
    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<String> handleDataValidationException(DataValidationException dataValidationException) {
        log.error("Data validation error: {}", dataValidationException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(String.format("Ошибка валидации данных: %s", dataValidationException.getMessage()));
    }

    /**
     * Обрабатывает все остальные исключения, которые не были перехвачены другими обработчиками.
     * Возвращает HTTP-ответ со статусом 500 (Internal Server Error) и сообщением об ошибке.
     *
     * @param exception исключение, которое было выброшено
     * @return ResponseEntity с сообщением об ошибке и статусом 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        log.error("Internal server error: {}", exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(String.format("Произошла внутренняя ошибка: %s", exception.getMessage()));
    }
}

