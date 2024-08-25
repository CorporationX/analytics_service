package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class RedisAbstractMessageListener<T> {
    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final AnalyticsEventService analyticsEventService;

    protected T handleEvent(Class<T> clazz, @NonNull Message message) {
        try {
            return objectMapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            log.error("Failed to deserialize event", e);
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}
