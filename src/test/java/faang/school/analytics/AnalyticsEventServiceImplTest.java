package faang.school.analytics;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {
    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    private Long anyReciverId;
    private EventType anyEventType;
    private Interval anyInterval;
    private LocalDateTime anyLocalDateTimeFrom;
    private LocalDateTime anyLocalDateTimeTo;


    @BeforeEach
    public void setUp() {
        anyReciverId = 1L;
        anyEventType = EventType.FOLLOWER;
        anyInterval = Interval.DAY;
        anyLocalDateTimeFrom = LocalDateTime.now().minusHours(2);
        anyLocalDateTimeTo = LocalDateTime.now();
    }


    @Test
    public void saveEventSuccessfullySaves() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(any(AnalyticsEvent.class));
    }

    @Test
    public void getAnalyticsReturnsAnalyticsByInterval() {
        analyticsEventService.getAnalytics(anyReciverId, anyEventType, anyInterval, null, null);

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(anyReciverId, anyEventType);
    }

    @Test
    public void getAnalyticsReturnsAnalyticsByPeriod() {
        analyticsEventService.getAnalytics(
                anyReciverId, anyEventType, null, anyLocalDateTimeFrom, anyLocalDateTimeTo);

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(anyReciverId, anyEventType);
    }
}
