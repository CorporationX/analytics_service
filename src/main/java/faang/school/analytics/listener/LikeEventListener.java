package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.dto.LikeEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeEventListener extends AbstractRedisListener<LikeEventDto> {

    public LikeEventListener(ObjectMapper objectMapper, AnalyticsEventServiceImpl analyticsEventService) {
        super(objectMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(LikeEventDto.class, message, this::convertToAnalyticsEvent);
    }

    private AnalyticsEvent convertToAnalyticsEvent(LikeEventDto likeEventDto) {
        return AnalyticsEvent.builder()
                .receiverId(likeEventDto.getPostId())
                .actorId(likeEventDto.getUserId())
                .eventType(EventType.POST_LIKE)
                .receivedAt(likeEventDto.getTimestamp())
                .build();
    }
}