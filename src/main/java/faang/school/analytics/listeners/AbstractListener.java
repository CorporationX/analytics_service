package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    protected final AnalyticsEventMapper analyticsEventMapper;
    private  final AnalyticsEventRepository repository;

    public T readValue(byte[] json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            log.error("Error deserializing JSON messages", e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void save(AnalyticsEvent analyticsEvent) {
        repository.save(analyticsEvent);
        log.info("The event was successfully saved in the analytics database: {}", analyticsEvent);
    }
}
