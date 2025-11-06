package faang.school.analytics.validation;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventValidatorTest {
    private static final long INCORRECT_ID = 0;
    private static final long DEFAULT_ID = 1;
    private static final long SECOND_ID = 2;
    private static final long CORRECT_ID = 10;
    private static final long SECOND_CORRECT_ID = 20;
    private static final LocalDateTime DEFAULT_NULL = null;
    private static final LocalDateTime CURRENT_DATE_TIME = LocalDateTime.now();

    @InjectMocks
    private AnalyticsEventValidator validator;

    private final long incorrectId = INCORRECT_ID;
    private final long receiverId = DEFAULT_ID;
    private final long actorId = SECOND_ID;
    private final LocalDateTime nullValue = DEFAULT_NULL;
    private final LocalDateTime currentDateTime = CURRENT_DATE_TIME;
    private final long correctReceiverId = CORRECT_ID;
    private final long correctActorId = SECOND_CORRECT_ID;


    AnalyticsEvent incorrectReceiverIdEvent = AnalyticsEvent.builder()
            .receiverId(incorrectId)
            .actorId(receiverId)
            .eventType(EventType.FOLLOWER)
            .receivedAt(currentDateTime)
            .build();

    AnalyticsEvent incorrectActorIdEvent = AnalyticsEvent.builder()
            .receiverId(receiverId)
            .actorId(incorrectId)
            .eventType(EventType.POST_LIKE)
            .receivedAt(currentDateTime)
            .build();

    AnalyticsEvent incorrectEventTypeEvent = AnalyticsEvent.builder()
            .receiverId(receiverId)
            .actorId(actorId)
            .receivedAt(currentDateTime)
            .build();

    AnalyticsEvent incorrectReceivedAtEvent = AnalyticsEvent.builder()
            .receiverId(receiverId)
            .actorId(actorId)
            .eventType(EventType.POST_VIEW)
            .receivedAt(currentDateTime.plusDays(1))
            .build();

    AnalyticsEvent correctEvent = AnalyticsEvent.builder()
            .receiverId(correctReceiverId)
            .actorId(correctActorId)
            .eventType(EventType.PROFILE_VIEW)
            .receivedAt(currentDateTime)
            .build();

    // ✅ saveEvent validations
    @Test
    void testExceptionEventIsNullWhenEventSaved() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateEventForSave(null));
    }

    @Test
    void testExceptionReceiverIdIncorrectWhenEventSaved() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateEventForSave(incorrectReceiverIdEvent));
    }

    @Test
    void testExceptionActorIdIncorrectWhenEventSaved() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateEventForSave(incorrectActorIdEvent));
    }

    @Test
    void testExceptionEventTypeIsNullWhenEventSaved() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateEventForSave(incorrectEventTypeEvent));
    }

    @Test
    void testExceptionFutureReceivedAtWhenEventSaved() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateEventForSave(incorrectReceivedAtEvent));
    }

    @Test
    void testSuccessfullyEventSaved() {
        assertDoesNotThrow(() -> validator.validateEventForSave(correctEvent));
    }

    // ✅ getAnalytics validations
    @Test
    void testExceptionReceiverIdIncorrectWhenEventsGet() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateGetAnalyticsEventParams(
                        incorrectId,
                        EventType.POST_VIEW,
                        nullValue,
                        nullValue,
                        true));
    }

    @Test
    void testExceptionEventTypeIsNullWhenEventsGet() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateGetAnalyticsEventParams(
                        receiverId,
                        null,
                        nullValue,
                        nullValue,
                        true));
    }

    @Test
    void testExceptionIntervalNotUsedDatesMissingWhenEventsGet() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateGetAnalyticsEventParams(
                        receiverId,
                        EventType.POST_VIEW,
                        nullValue,
                        nullValue,
                        false));
    }

    @Test
    void testExceptionFromAfterToWhenEventsGet() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateGetAnalyticsEventParams(
                        receiverId,
                        EventType.POST_VIEW,
                        currentDateTime,
                        currentDateTime.minusDays(1),
                        false
                ));
    }

    @Test
    void testSuccessfullyEventsGetWithInterval() {
        assertDoesNotThrow(() ->
                validator.validateGetAnalyticsEventParams(
                        receiverId,
                        EventType.POST_VIEW,
                        nullValue,
                        nullValue,
                        true
                ));
    }

    @Test
    void testSuccessfullyEventsGetWithDates() {
        assertDoesNotThrow(() ->
                validator.validateGetAnalyticsEventParams(
                        receiverId,
                        EventType.POST_VIEW,
                        currentDateTime.minusDays(2),
                        currentDateTime,
                        false
                ));
    }
}
