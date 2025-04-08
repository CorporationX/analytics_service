package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    private final Long id = 1L;
    private final EventType eventType = EventType.POST_LIKE;
    private final Interval interval = Interval.HOUR;
    private final LocalDateTime to = LocalDateTime.now();

    @InjectMocks
    private AnalyticsEventService analyticsService;

    @Spy
    private AnalyticsEventMapper analyticsMapper;

    @Mock
    private AnalyticsEventRepository analyticsRepository;

    @Mock
    private AnalyticsEventFilter analyticsFromDateFilter;

    @Mock
    private AnalyticsEventFilter analyticsToDateFilter;

    @Mock
    private AnalyticsEventFilter analyticsReceiverFilter;

    @Mock
    private AnalyticsEventFilter analyticsTypeFilter;

    @Mock
    private AnalyticsEventFilter analyticsIntervalFilter;

    @BeforeEach
    public void setUp() {
        analyticsService = new AnalyticsEventService(analyticsRepository, analyticsMapper,
                List.of(analyticsFromDateFilter, analyticsToDateFilter, analyticsReceiverFilter,
                        analyticsTypeFilter, analyticsIntervalFilter));
    }

    @Test
    void testPositiveSaveAnalytics() {
        AnalyticsEvent analytics = createAnalytics(id, id, eventType, to);
        AnalyticsEventDto analyticsDto = createAnalyticsDto(id, id, eventType, to);
        when(analyticsMapper.toEntity(analyticsDto)).thenReturn(analytics);

        analyticsService.saveEvent(analyticsDto);

        verify(analyticsRepository, times(1)).save(analytics);
    }

    @Test
    void testPositiveGetAnalytics() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);

        AnalyticsEventFilterDto filter = createAnalyticsFilter(interval, from, to);
        List<AnalyticsEvent> analyticsList = List.of(
                createAnalytics(id, id, eventType, to),
                createAnalytics(id, id, eventType, from)
        );
        List<AnalyticsEventDto> analyticsDtoList = List.of(
                createAnalyticsDto(id, id, eventType, to),
                createAnalyticsDto(id, id, eventType, from)
        );
        when(analyticsRepository.findByReceiverIdAndEventType(id, eventType)).thenReturn(analyticsList.stream());
        when(analyticsMapper.toDtoList(analyticsList)).thenReturn(analyticsDtoList);

        List<AnalyticsEventDto> result = analyticsService.getAnalytics(filter);

        assertEquals(result, analyticsDtoList);
    }

    private AnalyticsEvent createAnalytics(Long receiverId, Long actorId, EventType eventType, LocalDateTime time) {
        return AnalyticsEvent.builder()
                .receiverId(receiverId)
                .actorId(actorId)
                .eventType(eventType)
                .receivedAt(time)
                .build();
    }

    private AnalyticsEventFilterDto createAnalyticsFilter(Interval interval, LocalDateTime from, LocalDateTime to) {
        return AnalyticsEventFilterDto.builder()
                .receiverId(id)
                .eventType(eventType)
                .interval(interval)
                .from(from)
                .to(to)
                .build();
    }

    private AnalyticsEventDto createAnalyticsDto(Long receiverId, Long actorId, EventType eventType, LocalDateTime time) {
        return AnalyticsEventDto.builder()
                .receiverId(receiverId)
                .actorId(actorId)
                .eventType(eventType)
                .receivedAt(time)
                .build();
    }

}
