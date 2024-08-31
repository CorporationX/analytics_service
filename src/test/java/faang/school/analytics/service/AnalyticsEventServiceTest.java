package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsFilterDto;
import faang.school.analytics.filter.AnalyticsFilter;
import faang.school.analytics.filter.impl.AnalyticsDateRangeFilter;
import faang.school.analytics.filter.impl.AnalyticsIntervalFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Spy
    private AnalyticsIntervalFilter intervalFilter;

    @Spy
    private AnalyticsEventMapper mapper;

    @Spy
    private AnalyticsDateRangeFilter dateRangeFilter;

    @Mock
    private AnalyticsEventRepository repository;

    private AnalyticsEventService service;
    private AnalyticsEvent event;
    private AnalyticsFilterDto filterDto;

    @BeforeEach
    public void setUp() {
        List<AnalyticsFilter> filterList = List.of(intervalFilter, dateRangeFilter);
        service = new AnalyticsEventService(repository, mapper, filterList);

        event = new AnalyticsEvent();
        filterDto = new AnalyticsFilterDto();
        filterDto.setReceiverId(1L);
        filterDto.setEventType(EventType.PROJECT_VIEW);
    }

    @Test
    public void testSaveEvent() {
        service.saveEvent(event);
        verify(repository, times(1)).save(event);
    }

    @Test
    public void testSaveEvent_withNull() {
        assertThrows(RuntimeException.class, () -> service.saveEvent(null));
    }

    @Test
    public void testGetAnalytics_withHourlyInterval() {
        filterDto.setInterval(Interval.HOURLY);
        List<AnalyticsEvent> eventList = getIntervalList();
        AnalyticsEventDto expectedEventDto = mapper.toDto(eventList.get(0));
        when(repository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(eventList.stream());

        List<AnalyticsEventDto> result = service.getAnalytics(filterDto);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expectedEventDto, result.get(0));
    }

    @Test
    public void testGetAnalytics_withDateRange() {
        filterDto.setFrom(LocalDateTime.now().minusHours(1));
        filterDto.setTo(LocalDateTime.now().plusHours(1));
        List<AnalyticsEvent> eventList = getIntervalList();
        AnalyticsEventDto expectedEventDto = mapper.toDto(eventList.get(0));
        when(repository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(eventList.stream());

        List<AnalyticsEventDto> result = service.getAnalytics(filterDto);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(expectedEventDto, result.get(0));
    }

    @Test
    public void testGetAnalytics_withEmptyParametrs() {
        assertThrows(IllegalArgumentException.class, () -> service.getAnalytics(filterDto));
    }

    private List<AnalyticsEvent> getIntervalList() {
        AnalyticsEvent eventFirst = AnalyticsEvent.builder().receivedAt(LocalDateTime.now()).build();
        AnalyticsEvent eventSecond = AnalyticsEvent.builder().receivedAt(LocalDateTime.now().minusDays(2)).build();
        return List.of(eventFirst, eventSecond);
    }
}
