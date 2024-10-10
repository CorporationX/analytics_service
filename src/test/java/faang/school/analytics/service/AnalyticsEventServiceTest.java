package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeInterval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private List<AnalyticsEvent> events;

    @BeforeEach
    void setUp() {
        events = List.of(
                AnalyticsEvent.builder()
                        .receiverId(1L)
                        .eventType(EventType.FOLLOWER)
                        .receivedAt(LocalDateTime.of(2024, 10, 1, 10, 0))
                        .build(),

                AnalyticsEvent.builder()
                        .receiverId(1L)
                        .eventType(EventType.FOLLOWER)
                        .receivedAt(LocalDateTime.of(2024, 10, 2, 12, 0))
                        .build(),

                AnalyticsEvent.builder()
                        .receiverId(1L)
                        .eventType(EventType.FOLLOWER)
                        .receivedAt(LocalDateTime.of(2024, 10, 3, 14, 0))
                        .build()
        );
        analyticsEventService.init();
    }

    @Test
    void testSaveEvent_Success() {
        when(analyticsEventRepository.save(events.get(0))).thenReturn(events.get(0));

        AnalyticsEvent savedEvent = analyticsEventService.saveEvent(events.get(0));
        assertThat(savedEvent).usingRecursiveComparison().isEqualTo(events.get(0));

        verify(analyticsEventRepository, times(1)).save(events.get(0));
    }

    @Test
    void testGetAnalytics_withIntervalAll() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        TimeInterval interval = TimeInterval.ALL;

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);
        assertIterableEquals(events, result);

        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(receiverId, eventType);
    }

    @Test
    void testGetAnalytics_withIntervalDay() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        TimeInterval interval = TimeInterval.DAY;

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);

        LocalDateTime dayAgo = LocalDateTime.now().minusDays(1);
        List<AnalyticsEvent> expected = events.stream()
                .filter(event -> event.getReceivedAt().isAfter(dayAgo))
                .toList();

        assertIterableEquals(result, expected);
    }

    @Test
    void testGetAnalytics_withCustomStartAndEnd() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        TimeInterval interval = TimeInterval.DAY;
        LocalDateTime start = LocalDateTime.of(2024, 10, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 10, 2, 23, 59);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, start, end);

        List<AnalyticsEvent> expected = events.stream()
                .filter(event -> event.getReceivedAt().isAfter(start) && event.getReceivedAt().isBefore(end))
                .toList();

        assertIterableEquals(expected, result);
    }

    @Test
    void testGetAnalytics_throwsException() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        TimeInterval interval = TimeInterval.DAY;
        LocalDateTime start = LocalDateTime.of(2024, 10, 3, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 10, 1, 0, 0);

        assertThrows(IllegalArgumentException.class, () ->
                analyticsEventService.getAnalytics(receiverId, eventType, interval, start, end));
    }

    @Test
    void getAnalytics_withOnlyStart() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        TimeInterval interval = TimeInterval.DAY;
        LocalDateTime start = LocalDateTime.of(2024, 10, 2, 0, 0);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, start, null);

        List<AnalyticsEvent> expected = events.stream()
                .filter(event -> event.getReceivedAt().isAfter(start))
                .toList();

        assertIterableEquals(expected, result);
    }

    @Test
    void getAnalytics_withOnlyEnd() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        TimeInterval interval = TimeInterval.DAY;
        LocalDateTime end = LocalDateTime.of(2024, 10, 2, 23, 59);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, null, end);

        List<AnalyticsEvent> expected = events.stream()
                .filter(event -> event.getReceivedAt().isBefore(end))
                .toList();

        assertEquals(expected, result);
    }
}

