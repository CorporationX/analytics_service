package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static faang.school.analytics.service.AnalyticsEventService.EVENT_NULL_EXCEPTION;
import static faang.school.analytics.service.AnalyticsEventService.EVENT_TYPE_NULL_EXCEPTION;
import static faang.school.analytics.service.AnalyticsEventService.FROM_OR_TO_NULL_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    private final AnalyticsEvent eventLastDay = AnalyticsEvent.builder()
            .receivedAt(LocalDateTime.now().minusDays(0))
            .build();
    private final AnalyticsEvent lastWeek = AnalyticsEvent.builder()
            .receivedAt(LocalDateTime.now().minusDays(2))
            .build();
    private final AnalyticsEvent eventLastMonth = AnalyticsEvent.builder()
            .receivedAt(LocalDateTime.now().minusDays(15))
            .build();

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper = new AnalyticsEventMapperImpl();
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void testSaveEventWithEventNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyticsEventService.saveEvent(null));

        assertEquals(EVENT_NULL_EXCEPTION, exception.getMessage());
    }

    @Test
    void testGetAnalyticsWithEventTypeNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyticsEventService.getAnalytics(1, null, Interval.LAST_DAY,
                        LocalDateTime.now(), LocalDateTime.now()));

        assertEquals(EVENT_TYPE_NULL_EXCEPTION, exception.getMessage());
    }

    @Test
    void testGetAnalyticsWithFromDateNull() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(new AnalyticsEvent()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyticsEventService.getAnalytics(1, EventType.FOLLOWER, null,
                        null, LocalDateTime.now()));

        assertEquals(FROM_OR_TO_NULL_EXCEPTION, exception.getMessage());
    }

    @Test
    void testGetAnalyticsWithFromToNull() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(new AnalyticsEvent()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> analyticsEventService.getAnalytics(1, EventType.FOLLOWER, null,
                        LocalDateTime.now(), null));

        assertEquals(FROM_OR_TO_NULL_EXCEPTION, exception.getMessage());
    }

    @Test
    void testGetAnalyticsWithIntervalLastHour() {
        AnalyticsEvent eventNow = AnalyticsEvent.builder().receivedAt(LocalDateTime.now()).build();

        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventNow, eventLastDay));
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, EventType.FOLLOWER,
                Interval.LAST_HOUR, null, null);

        assertEquals(result.get(0).getReceivedAt(), eventNow.getReceivedAt());
    }

    @Test
    void testGetAnalyticsWithIntervalLastDay() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(lastWeek, eventLastDay));
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, EventType.FOLLOWER,
                Interval.LAST_DAY, null, null);

        assertEquals(result.get(0).getReceivedAt(), eventLastDay.getReceivedAt());
    }

    @Test
    void testGetAnalyticsWithIntervalLastWeek() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(lastWeek, eventLastMonth));
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, EventType.FOLLOWER,
                Interval.LAST_WEEK, null, null);

        assertEquals(result.get(0).getReceivedAt(), lastWeek.getReceivedAt());
    }

    @Test
    void testGetAnalyticsWithIntervalLastMonth() {
        AnalyticsEvent eventThreeMonthAgo = AnalyticsEvent.builder()
                .receivedAt(LocalDateTime.now().minusMonths(3))
                .build();

        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventThreeMonthAgo, eventLastMonth));
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, EventType.FOLLOWER,
                Interval.LAST_MONTH, null, null);

        assertEquals(result.get(0).getReceivedAt(), eventLastMonth.getReceivedAt());
    }

    @Test
    void testGetAnalyticsByDateFromAndToWithIntervalNull() {
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to = LocalDateTime.now().minusDays(1);
        AnalyticsEvent event = AnalyticsEvent.builder()
                .receivedAt(LocalDateTime.now().minusDays(2))
                .build();
        AnalyticsEvent event2 = AnalyticsEvent.builder()
                .receivedAt(LocalDateTime.now().minusDays(5))
                .build();


        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(event, event2));
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, EventType.FOLLOWER,
                null, from, to);

        assertEquals(result.get(0).getReceivedAt(), event.getReceivedAt());
    }
}
