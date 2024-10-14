package faang.school.analytics.validator;

import faang.school.analytics.model.enums.Interval;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
class AnalyticControllerValidatorTest {

    private final AnalyticControllerValidator validator = new AnalyticControllerValidator();

    @Test
    void validateIntervalAndDatesUnsuccessful_intervalAndDatesNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateIntervalAndDates(null, null, null));
        Assertions.assertEquals("Interval and dates cannot be null at the same time.", exception.getMessage());
    }

    @Test
    void validateIntervalAndDatesUnsuccessful_intervalAndOneDateNull() {
        LocalDateTime endDate = LocalDateTime.now();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateIntervalAndDates(null, null, endDate.toString()));
        Assertions.assertEquals("Interval and dates cannot be null at the same time.", exception.getMessage());
    }

    @Test
    void validateIntervalAndDatesSuccess_IntervalExists() {
        Interval interval = Interval.DAY;

        validator.validateIntervalAndDates(interval.toString(), null, null);

        assertDoesNotThrow(() -> validator.validateIntervalAndDates(interval.toString(), null, null));
    }

    @Test
    void validateIntervalAndDatesSuccess_StartEndDatesExists() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(5);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);

        validator.validateIntervalAndDates(null, startDate.toString(), endDate.toString());

        assertDoesNotThrow(() -> validator.validateIntervalAndDates(null, startDate.toString(), endDate.toString()));
    }
}