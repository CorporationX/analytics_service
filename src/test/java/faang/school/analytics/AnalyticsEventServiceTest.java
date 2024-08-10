package faang.school.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    public void saveEvent_ShouldReturnSavedEventDto() {
        AnalyticsEvent event = new AnalyticsEvent();
        AnalyticsEventDto eventDto = new AnalyticsEventDto();
        when(analyticsEventRepository.save(any(AnalyticsEvent.class))).thenReturn(event);
        when(analyticsEventMapper.analyticsEventToAnalyticsEventDto(event)).thenReturn(eventDto);

        AnalyticsEventDto result = analyticsEventService.saveEvent(event);

        assertEquals(eventDto, result);
        verify(analyticsEventRepository).save(event);
        verify(analyticsEventMapper).analyticsEventToAnalyticsEventDto(event);
    }

    @Test
    public void getAnalytics_ShouldReturnFilteredAnalyticsEventDtos() {
        long receiverId = 1L;
        LocalDateTime startDateTime = new LocalDateTime(2024, 8, 1, 12, 0);
        LocalDateTime endDateTime = new LocalDateTime(2024, 8, 10, 12, 0);
        DateTime start = startDateTime.toDateTime();
        DateTime end = endDateTime.toDateTime();

        Interval interval = new Interval(start, end);
        EventType eventType = EventType.PROFILE_VIEW;

        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();

        AnalyticsEvent event1 = new AnalyticsEvent();
        event1.setReceivedAt(LocalDateTime.now().minusHours(1));
        AnalyticsEvent event2 = new AnalyticsEvent();
        event2.setReceivedAt(LocalDateTime.now().minusHours(2));
        AnalyticsEvent event3 = new AnalyticsEvent();
        event3.setReceivedAt(LocalDateTime.now().minusDays(2));

        Stream<AnalyticsEvent> eventStream = Stream.of(event1, event2, event3);
        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(eventStream);

        List<AnalyticsEventDto> eventDtos = List.of(new AnalyticsEventDto(), new AnalyticsEventDto());
        when(analyticsEventMapper.analyticsEventListToAnalyticsEventDtoList(any())).thenReturn(eventDtos);

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

        assertEquals(2, result.size());
        verify(analyticsEventRepository).findByReceiverIdAndEventType(receiverId, eventType);
        verify(analyticsEventMapper).analyticsEventListToAnalyticsEventDtoList(any());
    }
}
