package faang.school.analytics.validation;

import faang.school.analytics.exception.DataValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AnalyticsValidatorTest {

    @InjectMocks
    AnalyticsValidator analyticsValidator;

    @Test
    void validateEventHavePeriod_NullIntervalAndFrom_ThrowsDataValidationException() {
        assertThrows(DataValidationException.class, () -> analyticsValidator.validateEventHavePeriod(null, null, LocalDateTime.now()));
    }

    @Test
    void validateEventHavePeriod_NullIntervalAndTo_ThrowsDataValidationException() {
        assertThrows(DataValidationException.class, () -> analyticsValidator.validateEventHavePeriod(null, LocalDateTime.now(), null));
    }
}
