package faang.school.analytics.listener;

import faang.school.analytics.event.PostViewEvent;
import faang.school.analytics.mapper.PostViewEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostViewEventListener extends AbstractEventListener<PostViewEvent> {

    private final PostViewEventMapper postViewEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, PostViewEvent.class, event -> {
            AnalyticsEvent analyticsEvent = postViewEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.POST_VIEW);
            analyticsEventService.saveEvent(analyticsEvent);
        });
    }
}
