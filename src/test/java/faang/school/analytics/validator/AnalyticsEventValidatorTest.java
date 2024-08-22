package faang.school.analytics.validator;

import faang.school.analytics.exception.IllegalModelException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnalyticsEventValidatorTest {

    private final AnalyticsEventValidator validator = new AnalyticsEventValidator();

    @Test
    public void testValidatePresenceOfIntervalOrDateRage_WithInternal() {
        String interval = "WEEK";
        LocalDateTime from = null;
        LocalDateTime to = null;

        assertDoesNotThrow(() -> validator.validatePresenceOfIntervalOrDateRange(interval, from, to));
    }

    @Test
    public void testValidatePresenceOfIntervalOrDateRange_WithDateRange() {
        String interval = null;
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now();

        assertDoesNotThrow(() -> validator.validatePresenceOfIntervalOrDateRange(interval, from, to));
    }

    @Test
    public void testValidatePresenceOfIntervalOrDateRange_WithMissingIntervalAndDateRange_ShouldThrowException() {
        String interval = null;
        LocalDateTime from = null;
        LocalDateTime to = null;

        assertThrows(IllegalModelException.class, () -> validator.validatePresenceOfIntervalOrDateRange(interval, from, to));
    }

    @Test
    public void testValidatePresenceOfIntervalOrDateRange_WithOnlyFromDate_ShouldThrowException() {
        String interval = null;
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = null;

        assertThrows(IllegalModelException.class, () -> validator.validatePresenceOfIntervalOrDateRange(interval, from, to));
    }

    @Test
    public void testValidatePresenceOfIntervalOrDateRange_WithOnlyToDate_ShouldThrowException() {
        String interval = null;
        LocalDateTime from = null;
        LocalDateTime to = LocalDateTime.now();

        assertThrows(IllegalModelException.class, () -> validator.validatePresenceOfIntervalOrDateRange(interval, from, to));
    }
}
