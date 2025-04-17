package faang.school.analytics.controller;

import faang.school.analytics.exception.DataValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private ResponseEntity<String> response;

    @DisplayName("Обработка DataValidationException: должен возвращать статус BAD_REQUEST и сообщение об ошибке валидации")
    @Test
    public void givenDataValidationExceptionWhenGlobalExceptionHandelThenBadRequest() {
        DataValidationException exception = new DataValidationException("Invalid data");

        response = globalExceptionHandler.handleDataValidationException(exception);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Ошибка валидации данных: Invalid data", response.getBody());
    }

    @DisplayName("Обработка общего Exception: должен возвращать статус INTERNAL_SERVER_ERROR и сообщение о внутренней ошибке")
    @Test
    public void givenExceptionWhenGlobalExceptionHandelThenInternalServerError() {
        Exception exception = new Exception("Internal server error");

        response = globalExceptionHandler.handleException(exception);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Произошла внутренняя ошибка: Internal server error", response.getBody());
    }
}
