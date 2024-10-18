package faang.school.analytics.service;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {
    private static final EventType EVENT_TYPE = EventType.POST_PUBLISHED;
    private static final Interval INTERVAL = Interval.WEEK;
    @InjectMocks
    private AnalyticsServiceImpl service;
    @Mock
    private AnalyticsEventRepository repository;

    private long receiverId;
    private String eventTypeString;
    private Integer eventTypeInteger;
    private String intervalString;
    private Integer intervalInteger;
    private LocalDateTimeInput startDate;
    private LocalDateTimeInput endDate;
    private List<AnalyticsEvent> events;


    @BeforeEach
    public void init() {
        receiverId = 1L;

        eventTypeString = "POST_PUBLISHED";
        eventTypeInteger = 3;

        intervalString = "WEEK";
        intervalInteger = 1;

        startDate = new LocalDateTimeInput();
        startDate.setDateTime(LocalDateTime.MIN);
        endDate = new LocalDateTimeInput();
        endDate.setDateTime(LocalDateTime.MAX);

        events = List.of(
                new AnalyticsEvent(1L, receiverId, 1L, EVENT_TYPE, LocalDateTime.now()),
                new AnalyticsEvent(2L, receiverId, 1L, EVENT_TYPE, LocalDateTime.now().minusMonths(2)),
                new AnalyticsEvent(2L, receiverId, 1L, EVENT_TYPE, LocalDateTime.now().minusDays(2))
        );

        Mockito.lenient().when(repository.getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween
                (anyLong(), any(), any(), any()))
                .thenReturn(List.of(events.get(0), events.get(1), events.get(2)));
    }

    @Test
    void getAnalytics_whenStringEventTypeAndInterval() {
        List<AnalyticsEvent> actual = service.getAnalytics(receiverId, eventTypeString, null, intervalString,
                null, null, null);

        Assertions.assertEquals(List.of(events.get(0), events.get(1), events.get(2)),
                actual);

        Mockito.verify(repository, Mockito.times(1))
                .getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween(anyLong(), any(),
                        any(), any());
    }

    @Test
    void getAnalytics_whenIntegerEventTypeAndInterval() {
        List<AnalyticsEvent> actual = service.getAnalytics(receiverId, null, eventTypeInteger, null,
                intervalInteger, null, null);

        Assertions.assertEquals(List.of(events.get(0), events.get(1), events.get(2)),
                actual);

        Mockito.verify(repository, Mockito.times(1))
                .getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween(anyLong(), any(),
                        any(), any());
    }

    @Test
    void getAnalytics_dates() {
        List<AnalyticsEvent> actual = service.getAnalytics(receiverId, null, eventTypeInteger, null,
                null, startDate, endDate);

        Assertions.assertEquals(List.of(events.get(0), events.get(1), events.get(2)),
                actual);

        Mockito.verify(repository, Mockito.times(1))
                .getAnalyticsEventByActorIdAndEventTypeAndReceivedAtBetween(anyLong(), any(),
                        any(), any());
    }

}
