package faang.school.analytics.listener.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.post.PostViewEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostViewEventListener extends AbstractEventListener<PostViewEvent> {

    public PostViewEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventMapper analyticsEventMapper,
                                 AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostViewEvent.class, event -> {
            AnalyticsEvent analyticsEvent = mapToAnalyticsEvent(event);
            saveEvent(analyticsEvent);
        });
    }

    @Override
    protected EventType getEventType() {
        return EventType.POST_VIEW;
    }
}
