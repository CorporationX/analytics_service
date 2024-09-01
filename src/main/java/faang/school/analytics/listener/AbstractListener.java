package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;

    protected abstract void handleEvent(Message message) throws IOException;
    protected abstract AnalyticsEvent mapToAnalyticsEvent(T event);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            handleEvent(message);
        } catch (IOException e) {
            throw new RuntimeException(e + "couldn't deserialize message");
        }

    }
}
