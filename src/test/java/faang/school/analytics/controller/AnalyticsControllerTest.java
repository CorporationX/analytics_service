package faang.school.analytics.controller;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.service.AnalyticsService;
import faang.school.analytics.validator.AnalyticsControllerValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {
    @InjectMocks
    private AnalyticsController controller;

    @Mock
    private AnalyticsControllerValidator validator;
    @Mock
    private AnalyticsService service;

    @Test
    void getAnalytics_whenOk() {
        controller.getAnalytics(1L, "eventType", null,
                "interval", null, new LocalDateTimeInput(), new LocalDateTimeInput());

        Mockito.verify(validator, Mockito.times(1))
                .validateRequestParams("eventType", null,
                        "interval", null, new LocalDateTimeInput(), new LocalDateTimeInput());
        Mockito.verify(service, Mockito.times(1))
                .getAnalytics(1L, "eventType", null,
                        "interval", null, new LocalDateTimeInput(), new LocalDateTimeInput());
    }
}
