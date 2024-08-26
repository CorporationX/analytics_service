package faang.school.analytics.redisListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class EventListener<T> implements MessageListener {

    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final Class<T> clazz;

    public abstract AnalyticsEvent toAnalyticEvent(T followerEventDto);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            T followerEventDto = objectMapper.readValue(message.getBody(), clazz);
            AnalyticsEvent analyticEvent = toAnalyticEvent(followerEventDto);

            analyticsEventService.save(analyticEvent);
        } catch (IOException e) {
            log.error("Error while processing message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
