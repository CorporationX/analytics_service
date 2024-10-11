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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static faang.school.analytics.model.TimeInterval.HOUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testSaveEvent() {
        AnalyticsEvent event = new AnalyticsEvent();
        when(analyticsEventRepository.save(event)).thenReturn(event);

        AnalyticsEvent savedEvent = analyticsEventService.saveEvent(event);

        assertEquals(event, savedEvent);
        verify(analyticsEventRepository, times(1)).save(event);
    }

    @Test
    void testGetAnalytics_WithNonTimeFilter() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        List<AnalyticsEvent> expectedEvents = Arrays.asList(new AnalyticsEvent(), new AnalyticsEvent());

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(expectedEvents);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, null, null, null);

        assertEquals(expectedEvents, result);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(receiverId, eventType);
    }

    @Test
    void testGetAnalytics_WithIntervalFilter() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        TimeInterval interval = HOUR;
        List<AnalyticsEvent> expectedEvents = Arrays.asList(new AnalyticsEvent(), new AnalyticsEvent());

        when(analyticsEventRepository.findEventsBetweenTimes(eq(receiverId), eq(eventType), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedEvents);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);

        assertIterableEquals(expectedEvents, result);
    }

    @Test
    void testGetAnalytics_WithBetweenTimesFilter() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<AnalyticsEvent> expectedEvents = Collections.singletonList(new AnalyticsEvent());

        when(analyticsEventRepository.findEventsBetweenTimes(receiverId, eventType, start, end)).thenReturn(expectedEvents);

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(receiverId, eventType, null, start, end);

        assertIterableEquals(expectedEvents, result);
    }

    @Test
    void testGetAnalytics_ThrowsException_WhenStartIsAfterEnd() {
        long receiverId = 1L;
        EventType eventType = EventType.FOLLOWER;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                analyticsEventService.getAnalytics(receiverId, eventType, null, start, end));
    }
}


