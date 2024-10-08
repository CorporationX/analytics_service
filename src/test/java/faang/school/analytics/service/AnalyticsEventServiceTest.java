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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;



    @BeforeEach
    public void init() {
        analyticsEventService.init();
    }

    @Test
    public void testSaveEvent() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder().build();
        analyticsEventService.saveEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    public void testGetAnalytics() {
        long resId = anyLong();
        EventType type = any(EventType.class);

        analyticsEventService.getAnalytics(resId, type);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(resId, type);
    }

    @Test
    public void testGetAnalyticsByInterval() {
        long resId = 1L;
        EventType type = EventType.FOLLOWER;
        TimeInterval interval = TimeInterval.MINUTE;

        analyticsEventService.getAnalyticsByInterval(resId, type, interval);

        verify(analyticsEventRepository, times(1))
                .findEventsByTime(eq(resId), eq(type), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    public void testAnalyticsBetweenDate() {
        long resId = 1L;
        EventType type = EventType.FOLLOWER;
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        analyticsEventService.getAnalyticsBetweenDates(resId, type, start, end);

        verify(analyticsEventRepository, times(1))
                .findEventsByTime(resId, type, start, end);
    }
}
