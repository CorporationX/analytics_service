package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;

    protected AnalyticsEvent getAnalyticEvent(Message message, T event) {
        try {
            event = objectMapper.readValue(message.getBody(), (Class<T>) event.getClass());
        } catch (IOException e){

        }
//        return analyticsEventMapper.toEntity(event)
        return null;
    }
}
