package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.TimeInterval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    private AnalyticsEvent event;
    private long receiverId;
    private EventType eventType;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        event = new AnalyticsEvent();
        receiverId = 1L;
        eventType = EventType.FOLLOWER;
        start = LocalDateTime.now().minusDays(1);
        end = LocalDateTime.now();
    }

    @Test
    @DisplayName("Should save event successfully")
    void testSaveEvent() {
        when(analyticsEventRepository.save(event)).thenReturn(event);

        AnalyticsEvent savedEvent = analyticsEventService.saveEvent(event);

        assertNotNull(savedEvent);
        verify(analyticsEventRepository, times(1)).save(event);
    }

    @Test
    @DisplayName("Should retrieve analytics events within a given time interval")
    void testGetAnalyticsInInterval() {
        TimeInterval interval = TimeInterval.HOUR;

        List<AnalyticsEvent> events = Collections.singletonList(event);

        when(analyticsEventRepository.findEventsBetweenTimes(eq(receiverId), eq(eventType), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getAnalyticsInInterval(receiverId, eventType, interval);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when time interval is null")
    void testGetAnalyticsInIntervalThrowsExceptionWhenIntervalIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                analyticsEventService.getAnalyticsInInterval(receiverId, eventType, null));

        assertEquals("Interval can`t be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should retrieve analytics events between specific start and end times")
    void testGetAnalyticsBetweenTime() {
        List<AnalyticsEvent> events = Collections.singletonList(event);

        when(analyticsEventRepository.findEventsBetweenTimes(receiverId, eventType, start, end))
                .thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getAnalyticsBetweenTime(receiverId, eventType, start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when end time is null")
    void testGetAnalyticsBetweenTimeThrowsExceptionWhenEndIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                analyticsEventService.getAnalyticsBetweenTime(receiverId, eventType, null, null));
    }
}



