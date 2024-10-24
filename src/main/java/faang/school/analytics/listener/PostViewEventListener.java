package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostViewEventListener extends AbstractEventListener<PostViewEventDto> {
    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventMapper analyticsEventMapper,
                                 AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PostViewEventDto event = handleEvent(message, PostViewEventDto.class);
        log.debug("Received post view event: {}", event);
        sendAnalytics(event);
    }

    @Override
    protected EventType getEventType() {
        return EventType.POST_VIEW;
    }
}
