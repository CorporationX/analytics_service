package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    @Autowired
    public FollowerEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(FollowerEvent.class, objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    protected void saveEvent(FollowerEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toFollowerEntity(event);
        analyticsEvent.setEventType(EventType.FOLLOWER);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}

