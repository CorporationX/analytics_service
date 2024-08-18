package faang.school.analytics.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.ExceptionMessages;
import faang.school.analytics.exception.event.DataTransformationException;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Abstract base class for event listeners that handle events from Redis topics.
 *
 * @param <T> the type of event this listener handles
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final AnalyticsEventService analyticsEventService;

    /**
     * Serves as an entry point to handle events from Redis topics.
     * Start an implementation of a listener by invoking this method with the specified parameters,
     * then perform the logic of mapping the event to analytics data inside the consumer.
     * Final step is to invoke the {@link #persistAnalyticsData(AnalyticsEvent)} method to save the event for analytics purposes.
     *
     * @param message       the Redis message containing the event data
     * @param type          the class type of the event
     * @param eventConsumer the consumer to process the event
     * @throws IllegalArgumentException if the event cannot be handled
     */
    protected void handleEvent(Message message, Class<T> type, Consumer<T> eventConsumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            log.info("Received event: {}", event);
            eventConsumer.accept(event);
        } catch (IOException e) {
            log.error(ExceptionMessages.INVALID_TRANSFORMATION, e);
            throw new DataTransformationException(ExceptionMessages.INVALID_TRANSFORMATION, e);
        }
    }

    /**
     * Persists the analytics data for the received event.
     *
     * @param analyticsEvent the analytics event to be saved
     *
     * @throws IllegalArgumentException if no notification service is found for the user
     */
    protected void persistAnalyticsData(AnalyticsEvent analyticsEvent) {
        try {
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (Exception e) {
            log.error(ExceptionMessages.FAILED_PERSISTENCE, e);
            throw new PersistenceException(ExceptionMessages.FAILED_PERSISTENCE, e);
        }
    }
}
