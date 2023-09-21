package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.FollowerEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto>{

    public FollowerEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventMapper analyticsMapper,
                                 AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEventDto event = convertJsonToString(message, FollowerEventDto.class);
        AnalyticsEvent analyticsEvent = super.analyticsMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.FOLLOWER);
        super.analyticsEventService.create(analyticsEvent);
    }
}

