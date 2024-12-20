package faang.school.analytics.validator;

import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.validator.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnalyticsEventValidatorTest {
    private AnalyticsEventValidator analyticsEventValidator;
    private LocalDateTime from;
    private LocalDateTime to;

    @BeforeEach
    void setUp() {
        analyticsEventValidator = new AnalyticsEventValidator();
    }

    @Test
    void testValidateDateOrder_ShouldNotThrowException() {
        from = LocalDateTime.now();
        to = from.plusDays(1);

        assertThatCode(() -> analyticsEventValidator.validateDateOrder(from, to))
                .doesNotThrowAnyException();
    }

    @Test
    void testValidateDateOrder_ShouldThrowException() {
        from = LocalDateTime.now();
        to = from.minusDays(1);

        assertThatThrownBy(() -> analyticsEventValidator.validateDateOrder(from, to))
                .isInstanceOf(DataValidationException.class)
                .hasMessageContaining(String.format("'From' %s date must be earlier than 'To' %s date.", from, to));
    }
}