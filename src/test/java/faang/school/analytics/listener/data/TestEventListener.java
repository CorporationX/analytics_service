package faang.school.analytics.listener.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class TestEventListener extends AbstractEventListener<String> {

    public TestEventListener(ObjectMapper objectMapper,
                             AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService, String.class);

    }

    @Override
    protected void handleEvent(String event) {
    }
}