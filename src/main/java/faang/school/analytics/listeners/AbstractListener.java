package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    protected final AnalyticsEventService analyticsEventService;

    public T readValue(byte[] json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            log.error("Error deserializing JSON messages", e);
            throw new RuntimeException(e);
        }
    }
}
