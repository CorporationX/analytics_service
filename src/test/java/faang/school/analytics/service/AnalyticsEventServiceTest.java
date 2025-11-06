package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validation.AnalyticsEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    private final static long FIRST_RECEIVER_ID = 10L;
    private final static long FIRST_EVENT_ID = 1L;
    private final static long SECOND_EVENT_ID = 2L;
    private final static long FIRST_ACTOR_ID = 20L;
    private final static long SECOND_ACTOR_ID = 21L;
    private final static long INTERVAL_HOUR = 1L;
    private final static long INTERVAL_MINUTES = 10L;
    private final static LocalDateTime CURRENT_DATE_TIME = LocalDateTime.now();
    private final static LocalDateTime NULL_DATE_TIME = null;
    private final static int DAYS = 2;

    @InjectMocks
    private AnalyticsEventServiceImpl service;

    @Mock
    private AnalyticsEventRepository repository;

    @Spy
    private AnalyticsEventMapper mapper;

    @Mock
    private AnalyticsEventValidator validator;

    private AnalyticsEvent event1;
    private AnalyticsEvent event2;
    private AnalyticsEvent savedEvent;
    private AnalyticsEventDto firstAnalyticsEventDto;
    private AnalyticsEventDto secondAnalyticsEventDto;

    private final long firstReceiverId = FIRST_RECEIVER_ID;
    private final long firstEventId = FIRST_EVENT_ID;
    private final long secondEventId = SECOND_EVENT_ID;
    private final long firstActorId = FIRST_ACTOR_ID;
    private final long secondActorId = SECOND_ACTOR_ID;
    private final LocalDateTime currentDateTime = CURRENT_DATE_TIME;
    private final long intervalHour = INTERVAL_HOUR;
    private final long intervalMinutes = INTERVAL_MINUTES;
    private final Interval lastDay = Interval.LAST_DAY;
    private final LocalDateTime nullDateTime = NULL_DATE_TIME;
    LocalDateTime from = currentDateTime.minusDays(DAYS);
    LocalDateTime to = currentDateTime;

    @BeforeEach
    void setup() {
        event1 = AnalyticsEvent.builder()
                .id(firstEventId)
                .receiverId(firstReceiverId)
                .actorId(firstActorId)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(currentDateTime.minusHours(intervalHour))
                .build();
        event2 = AnalyticsEvent.builder()
                .id(secondEventId)
                .receiverId(firstReceiverId)
                .actorId(secondActorId)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(currentDateTime.minusMinutes(intervalMinutes))
                .build();
        savedEvent = AnalyticsEvent.builder()
                .id(firstEventId)
                .receiverId(firstReceiverId)
                .actorId(firstActorId)
                .eventType(EventType.PROFILE_VIEW)
                .receivedAt(currentDateTime.minusHours(intervalHour))
                .build();
        firstAnalyticsEventDto =
                AnalyticsEventDto.builder()
                        .id(firstEventId)
                        .receiverId(firstReceiverId)
                        .actorId(firstActorId)
                        .eventType(EventType.PROFILE_VIEW)
                        .receivedAt(event1.getReceivedAt())
                        .build();
        secondAnalyticsEventDto =
                AnalyticsEventDto.builder()
                        .id(secondEventId)
                        .receiverId(firstReceiverId)
                        .actorId(secondActorId)
                        .eventType(EventType.PROFILE_VIEW)
                        .receivedAt(event2.getReceivedAt())
                        .build();
    }

    @Test
    void testSuccessfullySaveEvent() {
        doNothing().when(validator).validateEventForSave(event1);
        when(repository.save(event1)).thenReturn(savedEvent);
        when(mapper.toAnalyticsEventDto(savedEvent)).thenReturn(firstAnalyticsEventDto);

        AnalyticsEventDto result = service.saveEvent(event1);

        assertNotNull(result);
        assertEquals(firstAnalyticsEventDto, result);
        verify(validator, times(1))
                .validateEventForSave(event1);
        verify(repository, times(1))
                .save(event1);
        verify(mapper, times(1))
                .toAnalyticsEventDto(savedEvent);
    }

    @Test
    void testFailValidationWhenEventSaved() {
        doThrow(new IllegalArgumentException("Validation error"))
                .when(validator)
                .validateEventForSave(event1);

        assertThrows(IllegalArgumentException.class, () ->
                service.saveEvent(event1));

        verify(repository, never()).save(any());
        verify(mapper, never()).toAnalyticsEventDto(any());
    }

    @Test
    void testAnalyticsGetWithInterval() {
        when(repository.findByReceiverIdAndEventType(firstReceiverId, EventType.PROFILE_VIEW))
                .thenReturn(Stream.of(event1, event2));
        when(mapper.toAnalyticsEventDto(any()))
                .thenReturn(secondAnalyticsEventDto,
                        firstAnalyticsEventDto);

        List<AnalyticsEventDto> result = service.getAnalytics(
                firstReceiverId,
                EventType.PROFILE_VIEW,
                lastDay,
                nullDateTime,
                nullDateTime
        );

        verify(validator, times(1))
                .validateGetAnalyticsEventParams(firstReceiverId,
                        EventType.PROFILE_VIEW,
                        nullDateTime,
                        nullDateTime,
                        true);
        verify(repository, times(1))
                .findByReceiverIdAndEventType(firstReceiverId, EventType.PROFILE_VIEW);
        assertEquals(2, result.size());
        assertTrue(result.get(0).receivedAt().isAfter(result.get(1).receivedAt()));
    }

    @Test
    void testAnalyticsGetWithDateRange() {
        when(repository.findByReceiverIdAndEventType(10L, EventType.PROFILE_VIEW))
                .thenReturn(Stream.of(event1, event2));
        when(mapper.toAnalyticsEventDto(any())).thenReturn(secondAnalyticsEventDto, firstAnalyticsEventDto);

        List<AnalyticsEventDto> result = service.getAnalytics(
                firstReceiverId,
                EventType.PROFILE_VIEW,
                null,
                from,
                to
        );

        verify(validator).validateGetAnalyticsEventParams(firstReceiverId,
                EventType.PROFILE_VIEW,
                from,
                to,
                false);
        assertEquals(2, result.size());
    }

    @Test
    void testValidationExceptionWhenAnalyticsListGet() {
        doThrow(new IllegalArgumentException("Invalid params"))
                .when(validator)
                .validateGetAnalyticsEventParams(firstReceiverId,
                        EventType.POST_VIEW,
                        nullDateTime,
                        nullDateTime,
                        false);

        assertThrows(IllegalArgumentException.class,
                () -> service.getAnalytics(firstReceiverId,
                        EventType.POST_VIEW,
                        null,
                        nullDateTime,
                        nullDateTime));

        verify(repository, never()).findByReceiverIdAndEventType(anyLong(), any());
    }

    @Test
    void testEmptyResultAnalyticsEventsGet() {
        when(repository.findByReceiverIdAndEventType(firstReceiverId,
                EventType.POST_LIKE))
                .thenReturn(Stream.empty());

        List<AnalyticsEventDto> result = service.getAnalytics(
                firstReceiverId,
                EventType.POST_LIKE,
                Interval.LAST_DAY,
                nullDateTime,
                nullDateTime);

        assertTrue(result.isEmpty());
    }
}
