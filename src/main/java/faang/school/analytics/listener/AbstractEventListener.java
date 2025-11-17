package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Getter
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final Class<T> eventType;
    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, eventType, this::eventConsumer);
    }

    private void handleEvent(Message message, Class<T> eventType, Consumer<T> eventConsumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            eventConsumer.accept(event);
        } catch (IOException e) {
            log.error("Error while parsing message", e);
            throw new RuntimeException(e);
        }

    }

    protected abstract void eventConsumer(T event);
}
