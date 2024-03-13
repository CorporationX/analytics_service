package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> {

    @Autowired
    private AnalyticsEventService analyticsService;

    @Autowired
    public FollowerEventListener() {
        super(FollowerEventDto.class);
    }

    @Override
    public void saveEvent(FollowerEventDto event) {
        AnalyticsEvent analyticsEvent = eventMapper.toAnalyticsEvent(event);
        analyticsEvent.setEventType(EventType.FOLLOWER);
        analyticsService.saveEvent(analyticsEvent);
    }
}