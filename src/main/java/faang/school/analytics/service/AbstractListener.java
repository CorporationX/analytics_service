package faang.school.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    protected abstract T handleEvent(Message message) throws IOException, ExecutionException, InterruptedException;
    protected abstract AnalyticsEvent mapToAnalyticsEvent(T event);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event;
        try {
            event = handleEvent(message);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e + "couldn't deserialize message");
        }
        AnalyticsEvent analyticsEvent = mapToAnalyticsEvent(event);
        analyticsEventService.save(analyticsEvent);
    }
}
