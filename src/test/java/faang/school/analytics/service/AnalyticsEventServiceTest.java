package faang.school.analytics.service;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnalyticsEventServiceImpl Tests")
class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository repository;

    @Spy
    private AnalyticsEventMapperImpl mapper; 

    @InjectMocks
    private AnalyticsEventServiceImpl service;

    private static final long RECEIVER_ID = 123L;
    private static final EventType EVENT_TYPE = EventType.POST_VIEW;
    private static final LocalDateTime NOW = LocalDateTime.now();

    private AnalyticsEvent event;
    private AnalyticsEventDto eventDto;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW)
                .build();

        eventDto = AnalyticsEventDto.builder()
            .receiverId(RECEIVER_ID)
            .eventType(EVENT_TYPE)
            .receivedAt(NOW)
            .build();
    }

    // ---------------- saveEvent() ----------------

    @Test
    @DisplayName("saveEvent(): should map and save event successfully")
    void saveEvent_shouldMapAndSaveSuccessfully() {
        CreateAnalyticsEventDto dto = CreateAnalyticsEventDto.builder()
            .receiverId(RECEIVER_ID)
            .actorId(2L)
            .eventType(EVENT_TYPE)
            .build();

        AnalyticsEvent mappedEvent = mapper.toModel(dto);
        when(repository.save(any(AnalyticsEvent.class))).thenReturn(mappedEvent);

        AnalyticsEventDto result = service.saveEvent(dto);

        verify(mapper, times(1)).toModel(dto);
        verify(repository, times(1)).save(mappedEvent);
        verify(mapper, times(1)).toDto(mappedEvent);
        assertNotNull(result);
        assertEquals(mappedEvent.getReceiverId(), result.receiverId());
        assertEquals(mappedEvent.getEventType(), result.eventType());
        assertEquals(mappedEvent.getReceivedAt(), result.receivedAt());
    }

    // ---------------- getAnalytics() ----------------

    @Test
    @DisplayName("getAnalytics(): should throw exception when eventType is null")
    void getAnalytics_shouldThrowException_whenEventTypeIsNull() {
        assertThrows(DataValidationException.class,
                () -> service.getAnalytics(RECEIVER_ID, null, null, NOW.minusDays(1), NOW));
    }

    @Test
    @DisplayName("getAnalytics(): should throw exception when both 'from' and 'interval' are null")
    void getAnalytics_shouldThrowException_whenFromAndIntervalAreNull() {
        when(repository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(Stream.of(event));

        assertThrows(DataValidationException.class,
                () -> service.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, null, null));
    }

    @Test
    @DisplayName("getAnalytics(): should use 'to = now' if null and interval to calculate 'from'")
    void getAnalytics_shouldUseNowAndInterval() {
        Interval interval = Interval.DAY;

        AnalyticsEvent recentEvent = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW.minusHours(5))
                .build();
        when(repository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(Stream.of(recentEvent));

        List<AnalyticsEventDto> result = service.getAnalytics(RECEIVER_ID, EVENT_TYPE, interval, null, null);

        assertEquals(1, result.size());
        assertEquals(recentEvent.getReceivedAt(), result.get(0).receivedAt());
    }

    @Test
    @DisplayName("getAnalytics(): should filter out events before 'from'")
    void getAnalytics_shouldFilterEventsBeforeFrom() {
        LocalDateTime from = NOW.minusDays(1);
        LocalDateTime to = NOW.plusDays(1);

        AnalyticsEvent inRange = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW)
                .build();

        AnalyticsEvent outOfRange = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW.minusDays(5))
                .build();

        when(repository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(Stream.of(inRange, outOfRange));

        List<AnalyticsEventDto> result = service.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, from, to);

        assertEquals(1, result.size());
        assertEquals(inRange.getReceivedAt(), result.get(0).receivedAt());
    }

    @Test
    @DisplayName("getAnalytics(): should filter out events after 'to'")
    void getAnalytics_shouldFilterEventsAfterTo() {
        LocalDateTime from = NOW.minusDays(1);
        LocalDateTime to = NOW;

        AnalyticsEvent inRange = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW)
                .build();

        AnalyticsEvent outOfRange = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW.plusDays(1))
                .build();

        when(repository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(Stream.of(inRange, outOfRange));

        List<AnalyticsEventDto> result = service.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, from, to);

        assertEquals(1, result.size());
        assertEquals(inRange.getReceivedAt(), result.get(0).receivedAt());
    }

    @Test
    @DisplayName("getAnalytics(): should return multiple events within range")
    void getAnalytics_shouldReturnMultipleEvents() {
        LocalDateTime from = NOW.minusDays(2);
        LocalDateTime to = NOW.plusDays(1);

        AnalyticsEvent e1 = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW.minusHours(1))
                .build();
        AnalyticsEvent e2 = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW)
                .build();

        when(repository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(Stream.of(e1, e2));

        List<AnalyticsEventDto> result = service.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, from, to);

        assertEquals(2, result.size());
        assertEquals(NOW.minusHours(1), result.get(0).receivedAt());
        assertEquals(NOW, result.get(1).receivedAt());
    }

    @Test
    @DisplayName("getAnalytics(): should return empty list if no events in range")
    void getAnalytics_shouldReturnEmptyIfNoEvents() {
        LocalDateTime from = NOW.minusDays(1);
        LocalDateTime to = NOW;

        AnalyticsEvent e1 = AnalyticsEvent.builder()
                .receiverId(RECEIVER_ID)
                .eventType(EVENT_TYPE)
                .receivedAt(NOW.minusDays(5))
                .build();

        when(repository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(Stream.of(e1));

        List<AnalyticsEventDto> result = service.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, from, to);

        assertTrue(result.isEmpty());
    }

}
