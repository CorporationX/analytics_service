package faang.school.analytics.controller.advices;

import faang.school.analytics.domain.dto.error.ErrorMessageDto;
import faang.school.analytics.exception.DataValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DataValidationControllerAdvice {

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<ErrorMessageDto> handle(DataValidationException e) {
        log.warn("Data validation exception: {}", e.getMessage());

        ErrorMessageDto message = ErrorMessageDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(message);
    }

}
