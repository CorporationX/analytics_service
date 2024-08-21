package validator;

import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.model.Interval;
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
        AnalyticInfoDto analyticInfoDto = AnalyticInfoDto.builder().build();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(analyticInfoDto));
    }

    @Test
    @DisplayName("Test validate when interval is null and from/to are not null")
    public void testValidateWithIntervalNullAndFromToNotNull() {

        AnalyticInfoDto analyticInfoDto = AnalyticInfoDto.builder()
                .from(LocalDateTime.now())
                .to(LocalDateTime.now())
                .build();

        assertDoesNotThrow(() -> validator.validate(analyticInfoDto));
    }

    @Test
    @DisplayName("Test validate when interval is not null and from/to are null")
    public void testValidateWithIntervalNotNullAndFromToNull() {

        AnalyticInfoDto analyticInfoDto = AnalyticInfoDto.builder()
                .interval(Interval.DAY)
                .build();

        assertDoesNotThrow(() -> validator.validate(analyticInfoDto));
    }
}
