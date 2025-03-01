package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository repository;

    @Spy
    private AnalyticsEventMapper mapper = Mappers.getMapper(AnalyticsEventMapper.class);

    @InjectMocks
    private AnalyticsEventService service;

    private AnalyticsEvent event;
    private AnalyticsEventDto eventDto;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.POST_VIEW)
                .receivedAt(LocalDateTime.now())
                .build();

        eventDto = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.POST_VIEW)
                .receivedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void saveEvent_ShouldSaveSuccessfully() {
        when(mapper.toEntity(eventDto)).thenReturn(event);
        service.saveEvent(eventDto);
        verify(repository, times(1)).save(event);
    }

    @Test
    void getAnalytics_ShouldReturnEvents() {
        when(repository.findByReceiverIdAndEventTypeAndReceivedAtBetween(anyLong(), any(), any(), any()))
                .thenReturn(List.of(event));
        when(mapper.toDto(event)).thenReturn(eventDto);

        List<AnalyticsEventDto> result = service.getAnalytics(2L, EventType.POST_VIEW,
                null, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        assertEquals(1, result.size());
    }

    @Test
    void getAnalytics_ShouldReturnEmptyListWhenNoEventsFound() {
        when(repository.findByReceiverIdAndEventTypeAndReceivedAtBetween(anyLong(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        List<AnalyticsEventDto> result = service.getAnalytics(2L, EventType.POST_VIEW,
                null, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        assertTrue(result.isEmpty());
    }

    @Test
    void getAnalytics_ShouldUseIntervalIfProvided() {
        when(repository.findByReceiverIdAndEventTypeAndReceivedAtBetween(anyLong(), any(), any(), any()))
                .thenReturn(List.of(event));
        when(mapper.toDto(event)).thenReturn(eventDto);

        List<AnalyticsEventDto> result = service.getAnalytics(2L, EventType.POST_VIEW, Interval.TODAY, null, null);
        assertEquals(1, result.size());
    }
}
