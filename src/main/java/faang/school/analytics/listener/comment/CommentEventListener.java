package faang.school.analytics.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, commentEvent -> {
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEventEntity(commentEvent));
        });
    }
}
