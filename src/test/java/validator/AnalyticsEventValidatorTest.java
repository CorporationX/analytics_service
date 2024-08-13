package validator;

import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnalyticsEventValidatorTest {

    private final AnalyticsEventValidator validator = new AnalyticsEventValidator();

    @Test
    public void testValidate_IntervalNull_FromNull_ToNull_ThrowsAssertionError() {
        String interval = null;
        LocalDateTime from = null;
        LocalDateTime to = null;

        assertThrows(IllegalArgumentException.class, () -> validator.validate(interval, from, to));
    }

    @Test
    public void testValidate_IntervalNull_FromNotNull_ToNotNull_DoesNotThrowAssertionError() {
        String interval = null;
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now();

        assertDoesNotThrow(() -> validator.validate(interval, from, to));
    }

    @Test
    public void testValidate_IntervalNotNull_FromNull_ToNull_DoesNotThrowAssertionError() {
        String interval = "DAY";
        LocalDateTime from = null;
        LocalDateTime to = null;

        assertDoesNotThrow(() -> validator.validate(interval, from, to));
    }
}
