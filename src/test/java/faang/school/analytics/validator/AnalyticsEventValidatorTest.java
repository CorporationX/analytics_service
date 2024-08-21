package faang.school.analytics.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsEventValidatorTest {

    private AnalyticsEventValidator analyticsEventValidator;

    @BeforeEach
    public void setUp() {
        analyticsEventValidator = new AnalyticsEventValidator();
    }

    @Test
    @DisplayName("testing validate time bounds presence with all null values")
    void testValidateTimeBoundsPresenceWithAllNullValues() {
        assertThrows(IllegalArgumentException.class,
                () -> analyticsEventValidator.validateTimeBoundsPresence(null, null, null));
    }

    @Test
    @DisplayName("testing validate time bounds presence with interval null value")
    void testValidateTimeBoundsPresenceWithIntervalNullValue() {
        assertDoesNotThrow(() -> analyticsEventValidator
                .validateTimeBoundsPresence(null, "from", "to"));
    }

    @Test
    @DisplayName("testing validate time bounds presence with from and to null values")
    void testValidateTimeBoundsPresenceWithFromAndToNullValues() {
        assertDoesNotThrow(() -> analyticsEventValidator
                .validateTimeBoundsPresence("interval", null, null));
    }
}