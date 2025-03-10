package faang.school.analytics.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEventDto;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEventDto> {

    public LikeEventListener(AnalyticsEventService analyticsEventService,
                             AnalyticsEventMapper analyticsEventMapper,
                             ObjectMapper objectMapper,
                             @Value("${spring.data.redis.channels.channel-like-event}") String channel) {
        super(objectMapper, analyticsEventService, analyticsEventMapper, channel);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEventDto.class, commentEvent -> {
            analyticsEventService.saveEvent(analyticsEventMapper.toLikeEventEntity(commentEvent));
        });
    }
}
