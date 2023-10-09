package faang.school.analytics.listeners;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final List<AnalyticService<T>> analyticServiceList;

    public void save(T event) {
        analyticServiceList.stream()
                .filter(analyticService -> analyticService.supportsEventType(event))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Don't save " + event.getClass() + " in Datasource"))
                .save(event);
    }

    public T convertToJSON(Message message, Class<T> eventType) {
        try {
            return objectMapper.readValue(message.getBody(), eventType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
