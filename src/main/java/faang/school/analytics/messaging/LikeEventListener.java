package faang.school.analytics.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.LikeEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    public LikeEventListener(ObjectMapper objectMapper,
                             AnalyticsEventService analyticsEventService,
                             AnalyticsEventMapper analyticsEventMapper
    ) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEvent likeEvent = getEventFromBytes(message.getBody(), LikeEvent.class);
        analyticsEventService.saveEvent(analyticsEventMapper.toEntity(likeEvent));
    }
}
