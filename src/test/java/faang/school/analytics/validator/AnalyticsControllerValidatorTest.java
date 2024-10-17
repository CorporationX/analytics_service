package faang.school.analytics.validator;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.exception.DataValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerValidatorTest {
    @InjectMocks
    private AnalyticsControllerValidator validator;
    private String eventTypeString;
    private Integer eventTypeInteger;
    private String intervalString;
    private Integer intervalInteger;
    private LocalDateTimeInput startDate;
    private LocalDateTimeInput endDate;

    @BeforeEach
    public void init() {
        eventTypeString = "POST_PUBLISHED";
        eventTypeInteger = 3;

        intervalString = "WEEK";
        intervalInteger = 1;

        startDate = new LocalDateTimeInput();
        startDate.setDateTime(LocalDateTime.MIN);
        endDate = new LocalDateTimeInput();
        endDate.setDateTime(LocalDateTime.MAX);
    }

    @Test
    void validateRequestParam_whenBadEnums() {
        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(" ", null, intervalString,
                        null, null, null));

        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(null, 20, intervalString,
                        null, null, null));

        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(eventTypeString, 20, " ",
                        null, null, null));

        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(eventTypeString, 20, null,
                        40, null, null));
    }

    @Test
    void validateRequestParam_whenNulls() {
        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(null, null, intervalString,
                        null, null, null));

        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(null, 1, null,
                        null, null, null));

        LocalDateTimeInput localDateTimeInput = new LocalDateTimeInput();


        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(eventTypeString, null, null,
                        null, null, endDate));

        Assertions.assertThrows(DataValidationException.class,
                () -> validator.validateRequestParams(eventTypeString, null, null,
                        null, localDateTimeInput, endDate));
    }
}
