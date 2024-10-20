package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.LikeEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
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
        try {
            LikeEventDto likeEventDto = objectMapper.readValue(message.getBody(),
                    LikeEventDto.class);
            sendAnalytics(likeEventDto);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
