package faang.school.analytics.messaging.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.like.LikeEvent;
import faang.school.analytics.mapper.like.LikeEventMapper;
import faang.school.analytics.messaging.listener.AbstractEventListener;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    private final LikeEventMapper likeEventMapper;

    public LikeEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService,
                             LikeEventMapper likeEventMapper) {
        super(objectMapper, analyticsEventService);
        this.likeEventMapper = likeEventMapper;
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, (likeEvent -> {
            var analyticsEvent = likeEventMapper.toAnalyticsEventEntity(likeEvent);
            analyticsEvent.setEventType(EventType.POST_LIKE);
            analyticsEvent.setReceivedAt(LocalDateTime.now());
            persistAnalyticsData(analyticsEvent);
        }));
    }
}
