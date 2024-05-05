package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    protected ObjectMapper objectMapper;
    protected AnalyticsEventMapper analyticsEventMapper;
    protected AnalyticsEventService analyticsEventService;

    private final Class<T> type;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event = convert(message.getBody());
        workingEvent(event);
        log.info("event processed successfully {}", event);
    }

    private T convert(byte[] body) {
        try {
            return objectMapper.readValue(body, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public abstract void workingEvent(T event);

}
