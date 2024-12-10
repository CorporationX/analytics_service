package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener, RedisContainerMessageListener {

    protected final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final Function<T, AnalyticsEvent> conversionFunction;


    public void processEvent(Message message, Class<T> eventType, Consumer<AnalyticsEvent> processingEvent) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);

            AnalyticsEvent analyticsEvent = conversionFunction.apply(event);

            processingEvent.accept(analyticsEvent);
        } catch (IOException e) {
            String exceptionMessage = String.format("Unable to parse event: %s, with message: %s",
                    eventType.getName(), message);
            MappingException mappingException = new MappingException(exceptionMessage, e);
            log.error(exceptionMessage, e);
            throw mappingException;
        }
    }
}
