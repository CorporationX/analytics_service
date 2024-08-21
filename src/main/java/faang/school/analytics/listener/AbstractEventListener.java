package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.mapper.AbstractMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AbstractMapper<T> mapper;

    protected T mapToEventDto(Class<T> classOfEvent, Message message) {
        try {
            return objectMapper.readValue(message.getBody(), classOfEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Class<T> getInstance();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        T event = mapToEventDto(getInstance(), message);
        AnalyticsEventDto analyticsEventDto = mapper.toAnalyticsEventDto(event);
        analyticsEventService.saveEvent(analyticsEventDto);
    }
}
