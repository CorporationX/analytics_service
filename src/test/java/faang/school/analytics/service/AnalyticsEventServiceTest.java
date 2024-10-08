package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private static final long RECEIVER_ID = 1L;
    private static final EventType EVENT_TYPE = EventType.POST_VIEW;
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime YESTERDAY = NOW.minusDays(1);
    private static final LocalDateTime SIX_HOURS_AGO = NOW.minusHours(6);

    private AnalyticsEvent event1;
    private AnalyticsEvent event2;

    @BeforeEach
    void setUp() {
        event1 = new AnalyticsEvent(1L, RECEIVER_ID, 2L, EVENT_TYPE, YESTERDAY);
        event2 = new AnalyticsEvent(2L, RECEIVER_ID, 3L, EVENT_TYPE, SIX_HOURS_AGO);
    }

    @Test
    void testSaveEvent() {
        when(analyticsEventRepository.save(event1)).thenReturn(event1);

        AnalyticsEvent result = analyticsEventService.saveEvent(event1);

        verify(analyticsEventRepository, times(1)).save(event1);
        assertEquals(event1, result);
    }

    @Test
    void testGetAnalytics_withInterval() {
        Interval interval = Interval.LAST_DAY;

        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(List.of(event1, event2));

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, interval, null, null);

        assertEquals(2, result.size());
        assertTrue(result.contains(event1));
        assertTrue(result.contains(event2));

        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE);
    }

    @Test
    void testGetAnalytics_withPeriod() {
        LocalDateTime from = NOW.minusDays(2);
        LocalDateTime to = NOW;

        when(analyticsEventRepository.findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE))
                .thenReturn(List.of(event1, event2));

        List<AnalyticsEvent> result = analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, from, to);

        assertEquals(2, result.size());
        assertTrue(result.contains(event1));
        assertTrue(result.contains(event2));

        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(RECEIVER_ID, EVENT_TYPE);
    }
}