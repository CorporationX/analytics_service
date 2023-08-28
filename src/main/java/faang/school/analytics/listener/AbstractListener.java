package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {
    private final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    protected T readValue(byte[] json, Class<T> classType) {
        try {
            return objectMapper.readValue(json, classType);
        } catch (IOException e) {
            log.error("Failed to deserialize event", e);
            throw new RuntimeException(e);
        }
    }

    protected void save(AnalyticsEvent event) {
        analyticsEventRepository.save(event);
        log.info("Analytics event saved: {}", event);
    }
}