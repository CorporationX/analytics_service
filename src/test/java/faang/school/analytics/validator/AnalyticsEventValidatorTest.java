package faang.school.analytics.validator;

import faang.school.analytics.exception.DataValidationException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class AnalyticsEventValidatorTest {
    private final AnalyticsEventValidator validator = new AnalyticsEventValidator();

    @Test
    public void whenValidateAnalyticsEventParamsThenThrowsException() {
        Assert.assertThrows(DataValidationException.class,
                () -> validator.validateAnalyticsEventParams(null, null, LocalDateTime.now()));
    }
}