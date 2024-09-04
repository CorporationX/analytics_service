package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private static final String CODE_MESSAGE_ERROR = "message.error.readValueException";
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;
    private final AnalyticsEventService service;
    @Getter
    private final String channelName;

    public void processEvent(Message message, Class<T> type, Function<T, AnalyticsEvent> function) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            AnalyticsEvent analyticsEvent = function.apply(event);
            service.saveEvent(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(
                    messageSource.getMessage(CODE_MESSAGE_ERROR, null, Locale.getDefault()));
        }
    }
}