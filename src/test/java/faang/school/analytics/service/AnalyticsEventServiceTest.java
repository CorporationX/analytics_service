package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validation.AnalyticsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private AnalyticsValidator analyticsValidator;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void shouldSave() {
        AnalyticsEventDto dto = AnalyticsEventDto.builder()
                .actorId(1L)
                .receiverId(2L)
                .eventType(EventType.POST_LIKE)
                .build();

        AnalyticsEvent entity = AnalyticsEvent.builder()
                .id(1L)
                .actorId(1L)
                .receiverId(2L)
                .eventType(EventType.POST_LIKE)
                .build();

        when(analyticsEventMapper.toEntity(dto)).thenReturn(entity);
        when(analyticsEventRepository.save(entity)).thenReturn(entity);
        when(analyticsEventMapper.toDto(entity)).thenReturn(dto);

        AnalyticsEventDto result = analyticsEventService.saveEvent(dto);

        assertNotNull(result);
        assertEquals(dto.getActorId(), result.getActorId());
        assertEquals(dto.getReceiverId(), result.getReceiverId());
        assertEquals(dto.getEventType(), result.getEventType());

        verify(analyticsEventMapper).toEntity(dto);
        verify(analyticsEventRepository).save(entity);
        verify(analyticsEventMapper).toDto(entity);
    }

    @Test
    void testGetAnalytics() {
        long receiverId = 1L;

       String type = "PROJECT_VIEW";
       String interval =  "DAY";

        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        AnalyticsEvent event1 = AnalyticsEvent.builder().build();
        AnalyticsEvent event2 = AnalyticsEvent.builder().build();

        event1.setReceivedAt(LocalDateTime.now().minusHours(1));
        event2.setReceivedAt(LocalDateTime.now().minusHours(2));

        List<AnalyticsEvent> events = new ArrayList<>(List.of(event1, event2));
        when(analyticsEventRepository.findByReceiverIdAndEventType(eq(receiverId), any(EventType.class))).thenReturn(events.stream());

        AnalyticsEventDto eventDto1 = analyticsEventMapper.toDto(event1);
        AnalyticsEventDto eventDto2 = analyticsEventMapper.toDto(event2);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics( receiverId, type, interval, from, to);

        assertEquals(eventDto1, result.get(0));
        assertEquals(eventDto2, result.get(1));
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(eq(receiverId), any(EventType.class));
    }


}
