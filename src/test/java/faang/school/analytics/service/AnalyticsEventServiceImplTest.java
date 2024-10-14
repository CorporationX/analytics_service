package faang.school.analytics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.dto.AnalyticsEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.enums.Interval;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceImplTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper mapper;

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventServiceImpl;

    @Test
    void saveEventTest_shouldCallSaveOnRepository() {
        AnalyticsEvent event = new AnalyticsEvent();
        event.setId(1L);

        analyticsEventServiceImpl.saveEvent(event);

        ArgumentCaptor<AnalyticsEvent> captor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventRepository, times(1)).save(captor.capture());
        assertEquals(event, captor.getValue());
    }

    @Test
    void getAnalyticsTest_success() {

        long receiverId = 1L;
        EventType eventType = EventType.POST_VIEW;
        Interval interval = Interval.DAY;
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to = LocalDateTime.now();

        AnalyticsEvent event1 = new AnalyticsEvent();
        event1.setReceivedAt(LocalDateTime.now().minusHours(2));
        event1.setId(1L);

        AnalyticsEvent event2 = new AnalyticsEvent();
        event2.setReceivedAt(LocalDateTime.now().minusHours(1));
        event2.setId(2L);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event1, event2));

        AnalyticsEventDto eventDto1 = new AnalyticsEventDto(event1.getId(), event1.getReceiverId(), event1.getActorId(), eventType, event1.getReceivedAt());
        AnalyticsEventDto eventDto2 = new AnalyticsEventDto(event2.getId(), event2.getReceiverId(), event2.getActorId(), eventType, event2.getReceivedAt());

        when(mapper.toDto(event1)).thenReturn(eventDto1);
        when(mapper.toDto(event2)).thenReturn(eventDto2);

        List<AnalyticsEventDto> result = analyticsEventServiceImpl.getAnalytics(receiverId, eventType, interval, from, to);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(eventDto2, result.get(0));
        assertEquals(eventDto1, result.get(1));
    }

    @Test
    void getAnalyticsTest_nullInterval() {

        long receiverId = 1L;
        EventType eventType = EventType.POST_VIEW;
        Interval interval = null;
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to = LocalDateTime.now();

        AnalyticsEvent event = new AnalyticsEvent();
        event.setReceivedAt(LocalDateTime.now().minusHours(1));
        event.setId(1L);

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event));

        AnalyticsEventDto eventDto = new AnalyticsEventDto(event.getId(), event.getReceiverId(), event.getActorId(), eventType, event.getReceivedAt());
        when(mapper.toDto(event)).thenReturn(eventDto);

        List<AnalyticsEventDto> result = analyticsEventServiceImpl.getAnalytics(receiverId, eventType, interval, from, to);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(eventDto, result.get(0));
    }
}