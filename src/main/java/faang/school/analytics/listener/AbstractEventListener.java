package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.DeserializeJSONException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    final AnalyticsEventMapper analyticsMapper;
    final AnalyticsEventService analyticsEventService;

    protected T convertJsonToString(Message message, Class<T> type) {
        T event;
        try {
            event = objectMapper.readValue(message.getBody(), type);
        } catch (IOException e) {
            throw new DeserializeJSONException("Could not deserialize event");
        }
        return event;
    }
}
