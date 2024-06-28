package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    public LikeEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(LikeEvent.class, objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    protected void saveEvent(LikeEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.POST_LIKE);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}