package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.dto.analyticsEvent.AnalyticsEventRequestDto;
import faang.school.analytics.dto.interval.IntervalDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.mapper.IntervalMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private IntervalMapper intervalMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent event;
    private AnalyticsEventRequestDto eventRequestDto;
    private AnalyticsEventDto eventDto;
    private IntervalDto intervalDto;
    private Interval interval;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.ACHIEVEMENT_RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();

        eventRequestDto = AnalyticsEventRequestDto.builder()
                .receiverId(1L)
                .actorId(1L)
                .build();

        eventDto = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(1L)
                .actorId(1L)
                .eventType(EventType.ACHIEVEMENT_RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();

        intervalDto = IntervalDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .build();

        interval = Interval.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .build();
    }

    @Test
    void testSaveEvent() {
        analyticsEventService.saveEvent(event);
        verify(analyticsEventRepository, times(1)).save(event);
    }

    @Test
    void testCreateEvent() {
        when(analyticsEventMapper.toEntity(any(AnalyticsEventRequestDto.class))).thenReturn(event);
        when(analyticsEventMapper.toDto(any(AnalyticsEvent.class))).thenReturn(eventDto);

        AnalyticsEventDto result = analyticsEventService.createEvent(eventRequestDto, EventType.ACHIEVEMENT_RECEIVED);

        assertNotNull(result);
        verify(analyticsEventRepository, times(1)).save(event);
    }

    @Test
    void testDeleteEvent() {
        analyticsEventService.deleteEvent(1L);
        verify(analyticsEventRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAnalytics() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(Stream.of(event));
        when(analyticsEventMapper.toDto(anyList())).thenReturn(List.of(eventDto));
        when(intervalMapper.toEntity(any(IntervalDto.class))).thenReturn(interval);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                1L, intervalDto, EventType.ACHIEVEMENT_RECEIVED, LocalDateTime.now(), LocalDateTime.now());

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetAnalyticsNoEventsFound() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(Stream.empty());

        assertThrows(EntityNotFoundException.class, () -> analyticsEventService.getAnalytics(1L, intervalDto, EventType.ACHIEVEMENT_RECEIVED, LocalDateTime.now(), LocalDateTime.now()));
    }
}