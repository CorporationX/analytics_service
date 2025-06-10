package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
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
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private AnalyticsEvent event;

    private AnalyticsEventDto eventDto;

    @BeforeEach
    public void setUp() {
        analyticsEventService = new AnalyticsEventServiceImpl(analyticsRepository, analyticsEventMapper);
        event = new AnalyticsEvent();
        event.setEventType(EventType.POST_PUBLISHED);
        event.setReceiverId(1L);
        event.setActorId(2L);
        event.setId(3L);
        event.setReceivedAt(LocalDateTime.now());

        eventDto = new AnalyticsEventDto();
    }

    @Test
    public void testSaveEvent() {
        analyticsEventService.saveEvent(event);

        verify(analyticsRepository, times(1)).save(event);
    }

    @Test
    public void testGetAnalyticsWithInterval() {
        when(analyticsRepository.findByReceiverIdAndEventType(1L, EventType.POST_PUBLISHED))
                .thenReturn(Stream.of(event));

        when(analyticsEventMapper.toDto(event)).thenReturn(eventDto);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                1L, EventType.POST_PUBLISHED, Interval.DAY, LocalDateTime.now().minusDays(2), LocalDateTime.now());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto, result.get(0));
    }

    @Test
    public void testGetAnalyticsWithoutInterval() {
        when(analyticsRepository.findByReceiverIdAndEventType(1L, EventType.POST_PUBLISHED))
                .thenReturn(Stream.of(event));

        when(analyticsEventMapper.toDto(event)).thenReturn(eventDto);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                1L, EventType.POST_PUBLISHED, null, LocalDateTime.now().minusDays(2), LocalDateTime.now());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto, result.get(0));
    }
}