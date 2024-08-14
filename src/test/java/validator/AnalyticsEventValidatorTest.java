package validator;

import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnalyticsEventValidatorTest {

    private final AnalyticsEventValidator validator = new AnalyticsEventValidator();

    @Test
    @DisplayName("Test validate when interval and from/to are null")
    public void testValidateWithIntervalAndFromToNull() {
        String interval = null;
        LocalDateTime from = null;
        LocalDateTime to = null;

        assertThrows(IllegalArgumentException.class, () -> validator.validate(interval, from, to));
    }

    @Test
    @DisplayName("Test validate when interval is null and from/to are not null")
    public void testValidateWithIntervalNullAndFromToNotNull() {
        String interval = null;
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now();

        assertDoesNotThrow(() -> validator.validate(interval, from, to));
    }

    @Test
    @DisplayName("Test validate when interval is not null and from/to are null")
    public void testValidateWithIntervalNotNullAndFromToNull() {
        String interval = "DAY";
        LocalDateTime from = null;
        LocalDateTime to = null;

        assertDoesNotThrow(() -> validator.validate(interval, from, to));
    }
}
