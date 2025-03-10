package faang.school.analytics.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    public CommentEventListener(AnalyticsEventService analyticsEventService,
                                AnalyticsEventMapper analyticsEventMapper,
                                ObjectMapper objectMapper,
                                @Value("${spring.data.redis.channels.channel-comment}") String channel) {
        super(objectMapper, analyticsEventService, analyticsEventMapper, channel);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, commentEvent -> {
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEventEntity(commentEvent));
        });
    }
}
