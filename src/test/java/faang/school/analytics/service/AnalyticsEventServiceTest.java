package faang.school.analytics.service;

import faang.school.analytics.dto.AnalylticsEventDto;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;

    @Mock
    private AnalyticsEventMapper mapper;

    @InjectMocks
    private AnalyticsEventService service;

    private AnalyticsEvent event;
    private AnalylticsEventDto eventDto;
    private Stream<AnalyticsEvent> streamList;
    private List<AnalyticsEvent> emptyList;
    private List<AnalyticsEvent> analyticsList;

    @BeforeEach
    public void setUp() {
        event = new AnalyticsEvent();
        event.setReceivedAt(LocalDateTime.now());
        eventDto = new AnalylticsEventDto();
        analyticsList = List.of(event);
        streamList = analyticsList.stream();
        emptyList = new ArrayList<>();
    }

    @Test
    public void testSaveEvent() {
        service.saveEvent(event);
        verify(repository, times(1)).save(event);
    }

    @Test
    public void testGetAnalytics_withHourlyPeriod() {
        when(repository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(streamList);
        when(mapper.toDto(any(AnalyticsEvent.class))).thenReturn(eventDto);

        List<AnalylticsEventDto> result = service.getAnalytics(1L,
                EventType.PROJECT_VIEW,
                Interval.HOURLY,
                null,
                null);

        assertEquals(1, result.size());
        assertEquals(eventDto, result.get(0));
    }

    @Test
    public void testGetAnalytics_withInerval() {
        when(repository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(streamList);
        when(mapper.toDto(any(AnalyticsEvent.class))).thenReturn(eventDto);

        LocalDateTime from = LocalDateTime.now().minusDays(2);
        LocalDateTime to = LocalDateTime.now().plusDays(2);

        List<AnalylticsEventDto> result = service.getAnalytics(1L,
                EventType.PROJECT_VIEW,
                null,
                from,
                to);

        assertEquals(1, result.size());
        assertEquals(eventDto, result.get(0));
    }

    @Test
    public void testGetAnalytics_withEmptyResult() {
        when(repository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(emptyList.stream());

        LocalDateTime from = LocalDateTime.now().minusDays(2);
        LocalDateTime to = LocalDateTime.now().plusDays(2);

        List<AnalylticsEventDto> result = service.getAnalytics(1L,
                EventType.PROJECT_VIEW,
                null,
                from,
                to);

        assertEquals(0, result.size());
    }
}
