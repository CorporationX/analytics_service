package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.LikeEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component("postLikeEventListener")
public class PostLikeEventListener extends AbstractEventListener<LikeEventDto> {

    public PostLikeEventListener(ObjectMapper objectMapper,
                                 AnalyticsEventMapper analyticsEventMapper,
                                 AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    protected EventType getEventType() {
        return EventType.POST_LIKE;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEventDto likeEventDto = handleEvent(message, LikeEventDto.class);
        log.debug("Received event: {}", likeEventDto);
        sendAnalytics(likeEventDto);
        log.debug("Sent event: {}", likeEventDto);
    }
}
