package faang.school.analytics;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.Interval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAnalytics_withIntervalDay_returnsFilteredAndMappedDtos() {
        // Arrange
        long receiverId = 1L;
        EventType eventType = EventType.PROFILE_VIEW;
        Interval interval = Interval.DAY;
        LocalDateTime past = LocalDateTime.now().minusHours(5);
        LocalDateTime outOfRange = LocalDateTime.now().minusDays(2);

        AnalyticsEvent inRangeEvent = mock(AnalyticsEvent.class);
        when(inRangeEvent.getReceivedAt()).thenReturn(past);

        AnalyticsEvent outOfRangeEvent = mock(AnalyticsEvent.class);
        when(outOfRangeEvent.getReceivedAt()).thenReturn(outOfRange);

        List<AnalyticsEvent> allEvents = Arrays.asList(inRangeEvent, outOfRangeEvent);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(allEvents);

        AnalyticsEventDto dto1 = mock(AnalyticsEventDto.class);
        when(analyticsEventMapper.toDto(inRangeEvent)).thenReturn(dto1);

        // Act
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId,
                eventType,
                interval,
                null,
                null
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(dto1));
        verify(analyticsEventMapper).toDto(inRangeEvent);
        verify(analyticsEventMapper, never()).toDto(outOfRangeEvent);
    }

    @Test
    void getAnalytics_withCustomFromTo_returnsFilteredDtos() {
        // Arrange
        long receiverId = 2L;
        EventType eventType = EventType.PROFILE_VIEW;
        Interval interval = null;
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to = LocalDateTime.now().minusDays(1);

        AnalyticsEvent event1 = mock(AnalyticsEvent.class);
        when(event1.getReceivedAt()).thenReturn(LocalDateTime.now().minusDays(2));
        AnalyticsEvent event2 = mock(AnalyticsEvent.class);
        when(event2.getReceivedAt()).thenReturn(LocalDateTime.now().minusDays(4));

        List<AnalyticsEvent> events = Arrays.asList(event1, event2);
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(events);

        AnalyticsEventDto dto1 = mock(AnalyticsEventDto.class);
        when(analyticsEventMapper.toDto(event1)).thenReturn(dto1);

        // Act
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId,
                eventType,
                interval,
                from,
                to
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(dto1));
        verify(analyticsEventMapper).toDto(event1);
        verify(analyticsEventMapper, never()).toDto(event2);
    }
}

