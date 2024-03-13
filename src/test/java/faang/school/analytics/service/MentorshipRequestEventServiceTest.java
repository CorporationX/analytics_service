package faang.school.analytics.service;

import faang.school.analytics.dto.MentorshipRequestedEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MentorshipRequestEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void successSaveEvent() {
        MentorshipRequestedEventDto eventDto = MentorshipRequestedEventDto.builder()
                .requesterId(1L)
                .receiverId(2L)
                .receivedAt(LocalDateTime.now())
                .build();
        AnalyticsEvent event = analyticsEventMapper.toAnalyticsEvent(eventDto);
        analyticsEventService.saveEvent(event);
        verify(analyticsEventRepository, times(1)).save(event);
    }

    @Test
    void getAnalytics() {
        AnalyticsEvent analyticsEvent = AnalyticsEvent.builder()
                .id(1L)
                .actorId(1L)
                .eventType(EventType.MENTORSHIP_REQUEST)
                .receiverId(1L)
                .build();
        List<AnalyticsEvent> analyticsEvents = List.of(analyticsEvent);
        long id = analyticsEvent.getId();
        EventType eventType = analyticsEvent.getEventType();

        when(analyticsEventRepository.findByReceiverIdAndEventType(id, eventType)).thenReturn(analyticsEvents.stream());
        List<AnalyticsEvent> actualAnalyticsEvents = analyticsEventService.getAnalytics(id, eventType);

        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(id, eventType);

        assertEquals(analyticsEvents, actualAnalyticsEvents);
    }
}
