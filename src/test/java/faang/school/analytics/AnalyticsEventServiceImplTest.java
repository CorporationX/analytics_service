package faang.school.analytics;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {
    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Captor
    private ArgumentCaptor<List<AnalyticsEvent>> listAnalyticsEventCaptor;

    private Long anyLong;
    private Long anyReciverId;
    private EventType anyEventType;
    private Interval anyInterval;
    private LocalDateTime anyLocalDateTimeFrom;
    private LocalDateTime anyLocalDateTimeTo;
    private LocalDateTime anyLocalDateTimeInsideRequiredPeriod;
    private AnalyticsEvent anyAnalyticsEvent;

    @BeforeEach
    public void setUp() {
        anyLong = 1L;
        anyReciverId = anyLong;
        anyEventType = EventType.FOLLOWER;
        anyInterval = Interval.DAY;
        anyLocalDateTimeFrom = LocalDateTime.now().minusHours(2);
        anyLocalDateTimeTo = LocalDateTime.now();
        anyLocalDateTimeInsideRequiredPeriod = LocalDateTime.now().minusHours(1);
        anyAnalyticsEvent = new AnalyticsEvent(anyLong, anyLong, anyLong, anyEventType, anyLocalDateTimeInsideRequiredPeriod);
    }

    @Test
    void saveLikeEventSuccessful() {
        AnalyticsEvent event = new AnalyticsEvent(
                1L,
                10L,
                20L,
                EventType.POST_LIKE,
                LocalDateTime.now()
        );

        analyticsEventService.saveEvent(event);

        verify(analyticsEventRepository).save(event);
    }

    @Test
    public void saveEventSuccessfullySaves() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        analyticsEventService.saveEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(any(AnalyticsEvent.class));
    }

    @Test
    public void getAnalyticsReturnsAnalyticsByInterval() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyReciverId, anyEventType))
                .thenReturn(Stream.of(anyAnalyticsEvent));
        assertTrue(analyticsEventService.getAnalytics(anyReciverId, anyEventType, anyInterval, null, null)
                .contains(anyAnalyticsEvent));

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(anyReciverId, anyEventType);
    }

    @Test
    public void getAnalyticsReturnsAnalyticsByPeriod() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyReciverId, anyEventType))
                .thenReturn(Stream.of(anyAnalyticsEvent));
        assertTrue(analyticsEventService.getAnalytics(
                anyReciverId,
                anyEventType,
                null,
                anyLocalDateTimeFrom,
                anyLocalDateTimeTo
        ).contains(anyAnalyticsEvent));

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(anyReciverId, anyEventType);
    }
}
