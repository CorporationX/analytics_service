package faang.school.analytics.subscruber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public abstract class RedisAbstractMessageListener<T> implements MessageListener {
    protected final AnalyticsEventMapper mapper;
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            T t = objectMapper.readValue(message.getBody(), clazz);
            analyticsEventService.saveEventEntity(map(t));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    abstract AnalyticsEvent map(T t);
}
