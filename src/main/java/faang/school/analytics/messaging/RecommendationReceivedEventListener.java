package faang.school.analytics.messaging;

import faang.school.analytics.dto.RecommendationReceivedEvent;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.util.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendationReceivedEventListener implements MessageListener {
    private final JsonMapper jsonMapper;
    private final AnalyticsEventService analyticsEventService;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        jsonMapper.toObjectFromByte(message.getBody(), RecommendationReceivedEvent.class)
                .ifPresent(str -> analyticsEventService.recommendationReceivedEventSave(str));
        log.info(message + " " + "send");
    }
}
