package faang.school.analytics.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public CommentEventListener(AnalyticsEventService analyticsEventService,
                                AnalyticsEventMapper analyticsEventMapper,
                                ObjectMapper objectMapper) {
        super(objectMapper);

        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    public EventType getEventType() {

        return EventType.POST_COMMENT;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, commentEvent -> {
            AnalyticsEvent entity = analyticsEventMapper.toAnalyticsEvent(commentEvent);
            entity.setEventType(getEventType());
            analyticsEventService.saveEvent(entity);
        });
    }
}
