package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.AnalyticsRequest;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private AnalyticsValidator analyticsValidator;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void shouldSave() {
        AnalyticsEvent event = AnalyticsEvent.builder().build();
        AnalyticsEventDto dto = analyticsEventMapper.toDto(event);

        when(analyticsEventRepository.save(event)).thenReturn(event);

        assertEquals(analyticsEventService.saveEvent(event), dto);
    }

    @Test
    void testGetAnalytics() {
        long receiverId = 1L;
        EventType type = EventType.PROJECT_VIEW;
        Interval interval = Interval.DAY;

        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        AnalyticsRequest request = AnalyticsRequest.builder()
                .receiverId(receiverId)
                .type(type)
                .interval(interval)
                .from(from).to(to)
                .build();

        AnalyticsEvent event1 = AnalyticsEvent.builder().build();
        AnalyticsEvent event2 = AnalyticsEvent.builder().build();

        event1.setReceivedAt(LocalDateTime.now().minusHours(1));
        event2.setReceivedAt(LocalDateTime.now().minusHours(2));

        List<AnalyticsEvent> events = new ArrayList<>(List.of(event1, event2));
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, type)).thenReturn(events.stream());

        AnalyticsEventDto eventDto1 = analyticsEventMapper.toDto(event1);
        AnalyticsEventDto eventDto2 = analyticsEventMapper.toDto(event2);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(request);

        assertEquals(eventDto1, result.get(0));
        assertEquals(eventDto2, result.get(1));
        verify(analyticsValidator, times(1)).validate(request);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(receiverId, type);
    }


}
