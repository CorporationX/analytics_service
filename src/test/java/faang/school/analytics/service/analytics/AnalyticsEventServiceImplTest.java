package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
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

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private AnalyticsEvent event1;

    private AnalyticsEventDto eventDto;

    @BeforeEach
    public void setUp() {
        analyticsEventService = new AnalyticsEventServiceImpl(analyticsRepository, analyticsEventMapper);
        event1 = AnalyticsEvent.builder()
                .id(3L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.POST_PUBLISHED)
                .receivedAt(LocalDateTime.now().minusMinutes(1))
                .build();
        eventDto = new AnalyticsEventDto();
    }

    @Test
    public void testSaveEvent() {
        when(analyticsRepository.save(event1)).thenReturn(event1);

        analyticsEventService.saveEvent(event1);

        verify(analyticsRepository, times(1)).save(event1);
    }

    @Test
    public void testGetAnalytics_WithInterval_Filtered() {
        AnalyticsEvent event2 = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.POST_PUBLISHED)
                .receivedAt(LocalDateTime.now().minusWeeks(2))
                .build();
        when(analyticsRepository.findByReceiverIdAndEventType(1L, EventType.POST_PUBLISHED))
                .thenReturn(Stream.of(event1, event2));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                1L, EventType.POST_PUBLISHED, Interval.DAY, LocalDateTime.now().minusDays(2), LocalDateTime.now());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto.getClass(), result.get(0).getClass());
        assertEquals(EventType.POST_PUBLISHED, result.get(0).getEventType());
    }

    @Test
    public void testGetAnalytics_WithoutInterval_Sorted() {
        AnalyticsEvent event2 = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.POST_PUBLISHED)
                .receivedAt(LocalDateTime.now().minusDays(2))
                .build();

        AnalyticsEvent event3 = AnalyticsEvent.builder()
                .id(2L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.POST_PUBLISHED)
                .receivedAt(LocalDateTime.now().minusDays(1))
                .build();

        when(analyticsRepository.findByReceiverIdAndEventType(1L, EventType.POST_PUBLISHED))
                .thenReturn(Stream.of(event2, event3, event1));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                1L, EventType.POST_PUBLISHED, null, LocalDateTime.now().minusDays(4), LocalDateTime.now());

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(eventDto.getClass(), result.get(0).getClass());
        assertEquals(3L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(1L, result.get(2).getId());
    }
}