package faang.school.analytics.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.service.analytics.AnalyticsEventService;

public class TestEventListener extends AbstractEventListener<Object> {

    public TestEventListener(ObjectMapper objectMapper,
                             AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
    }
}