package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;

    @Spy
    private AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @InjectMocks
    private AnalyticsEventService service;

    @Test
    void saveEvent_shouldSaveEvent() {
        AnalyticsEventDto eventDto = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.POST_VIEW)
                .receivedAt(LocalDateTime.now())
                .build();

        AnalyticsEvent event = new AnalyticsEvent(1L, 1L, 1L, EventType.POST_VIEW, LocalDateTime.now());
        when(mapper.toEntity(eventDto)).thenReturn(event);

        service.saveEvent(eventDto);

        verify(repository, times(1)).save(event);
    }

    @Test
    void getAnalytics_withIntervalToday_shouldReturnCorrectResults() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsEvent event1 = new AnalyticsEvent(1L, 1L, 1L, EventType.POST_VIEW, now.minusHours(2));
        AnalyticsEvent event2 = new AnalyticsEvent(2L, 2L, 2L, EventType.POST_VIEW, now.minusDays(2));

        when(repository.findByReceiverIdAndEventType(1L, EventType.POST_VIEW))
                .thenReturn(Stream.of(event1, event2));

        List<AnalyticsEventDto> result = service.getAnalytics(1L, EventType.POST_VIEW, Interval.TODAY, null, null);

        assertEquals(1, result.size());
        assertTrue(result.get(0).receivedAt().isAfter(now.minusDays(1)));
    }

    @Test
    void getAnalytics_withIntervalLastWeek_shouldReturnCorrectResults() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsEvent event1 = new AnalyticsEvent(1L, 1L, 1L, EventType.POST_VIEW, now.minusDays(5));
        AnalyticsEvent event2 = new AnalyticsEvent(2L, 2L, 2L, EventType.POST_VIEW, now.minusDays(10));

        when(repository.findByReceiverIdAndEventType(1L, EventType.POST_VIEW))
                .thenReturn(Stream.of(event1, event2));

        List<AnalyticsEventDto> result = service.getAnalytics(1L, EventType.POST_VIEW, Interval.LAST_WEEK, null, null);

        assertEquals(1, result.size());
        assertTrue(result.get(0).receivedAt().isAfter(now.minusWeeks(1)));
    }

    @Test
    void getAnalytics_withIntervalLastMonth_shouldReturnCorrectResults() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsEvent event1 = new AnalyticsEvent(1L, 1L, 1L, EventType.POST_VIEW, now.minusDays(25));
        AnalyticsEvent event2 = new AnalyticsEvent(2L, 2L, 2L, EventType.POST_VIEW, now.minusMonths(2));

        when(repository.findByReceiverIdAndEventType(1L, EventType.POST_VIEW))
                .thenReturn(Stream.of(event1, event2));

        List<AnalyticsEventDto> result = service.getAnalytics(1L, EventType.POST_VIEW, Interval.LAST_MONTH, null, null);

        assertEquals(1, result.size());
        assertTrue(result.get(0).receivedAt().isAfter(now.minusMonths(1)));
    }

    @Test
    void getAnalytics_withoutInterval_withDateFilter_shouldReturnCorrectResults() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsEvent event1 = new AnalyticsEvent(1L, 1L, 1L, EventType.POST_VIEW, now.minusHours(2));
        AnalyticsEvent event2 = new AnalyticsEvent(2L, 2L, 2L, EventType.POST_VIEW, now.minusDays(2));

        when(repository.findByReceiverIdAndEventType(1L, EventType.POST_VIEW))
                .thenReturn(Stream.of(event1, event2));

        LocalDateTime from = now.minusHours(3);
        LocalDateTime to = now.plusHours(1);

        List<AnalyticsEventDto> result = service.getAnalytics(1L, EventType.POST_VIEW, null, from, to);

        assertEquals(1, result.size());
        assertTrue(result.get(0).receivedAt().isAfter(from));
        assertTrue(result.get(0).receivedAt().isBefore(to));
    }

    @Test
    void getAnalytics_withNoMatchingEvents_shouldReturnEmptyList() {
        when(repository.findByReceiverIdAndEventType(1L, EventType.POST_VIEW))
                .thenReturn(Stream.empty());

        List<AnalyticsEventDto> result = service.getAnalytics(1L, EventType.POST_VIEW, Interval.TODAY, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAnalytics_withSorting_shouldReturnSortedResults() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsEvent event1 = new AnalyticsEvent(1L, 1L, 1L, EventType.POST_VIEW, now.minusHours(2));
        AnalyticsEvent event2 = new AnalyticsEvent(2L, 2L, 2L, EventType.POST_VIEW, now.minusHours(1));

        when(repository.findByReceiverIdAndEventType(1L, EventType.POST_VIEW))
                .thenReturn(Stream.of(event1, event2));

        List<AnalyticsEventDto> result = service.getAnalytics(1L, EventType.POST_VIEW, Interval.TODAY, null, null);

        assertEquals(2, result.size());
        assertTrue(result.get(0).receivedAt().isAfter(result.get(1).receivedAt()));
    }
}
