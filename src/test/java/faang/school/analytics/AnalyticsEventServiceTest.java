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
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Evgenii Malkov
 */
@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @InjectMocks
    AnalyticsEventService analyticsEventService;
    @Mock
    AnalyticsEventRepository analyticsEventRepository;
    @Spy
    AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @Test
    void withInterval() {
        long receiverId = 1L;
        EventType eventType = EventType.PROJECT_VIEW;
        LocalDateTime lastHour = LocalDateTime.now().minusHours(1);
        Interval interval = Interval.LAST_DAY;
        LocalDateTime from = null;
        LocalDateTime to = null;

        AnalyticsEvent event1 = new AnalyticsEvent();
        event1.setReceivedAt(LocalDateTime.now().minusHours(1));

        AnalyticsEventDto eventDto1 = new AnalyticsEventDto();

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event1));
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class))).thenReturn(eventDto1);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertTrue(interval.includes(lastHour));
        assertEquals(1, result.size());
        verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
        assertTrue(interval.includes(event1.getReceivedAt()));
        verify(analyticsEventMapper).toDto(event1);
    }

    @Test
    void withDateRange() {
        long receiverId = 1L;
        EventType eventType = EventType.PROJECT_VIEW;
        Interval interval = null;
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        AnalyticsEvent event1 = new AnalyticsEvent();
        event1.setReceivedAt(LocalDateTime.now().minusHours(1));

        AnalyticsEventDto eventDto1 = new AnalyticsEventDto();

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event1));
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class))).thenReturn(eventDto1);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertEquals(1, result.size());
        verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
        verify(analyticsEventMapper).toDto(event1);
    }

    @Test
    void dateRangeOutOfBounds() {
        long receiverId = 1L;
        EventType eventType = EventType.PROJECT_VIEW;
        Interval interval = null;
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().minusHours(2);

        AnalyticsEvent event1 = new AnalyticsEvent();
        event1.setReceivedAt(LocalDateTime.now().minusHours(1));

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event1));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertEquals(0, result.size());
        verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
        verifyNoInteractions(analyticsEventMapper);
    }
}
