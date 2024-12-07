package faang.school.analytics.service.event;

import faang.school.analytics.dto.event.EventDto;
import faang.school.analytics.dto.event.EventRequestDto;
import faang.school.analytics.mapper.event.EventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.dto.event.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.validator.analytic_event.AnalyticEventServiceValidator;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private EventMapperImpl eventMapper;
    @Mock
    private AnalyticEventServiceValidator analyticEventServiceValidator;
    private EventDto eventDto;
    private AnalyticsEvent analyticsEvent;
    private AnalyticsEvent secondAnalyticEvent;

    @BeforeEach
    void setUp() {
        eventDto = new EventDto();

        eventDto.setEventType(EventType.FOLLOWER);
        eventDto.setReceiverId(2);
        eventDto.setActorId(1);

        analyticsEvent = new AnalyticsEvent();
        secondAnalyticEvent = new AnalyticsEvent();
        secondAnalyticEvent.setReceivedAt(LocalDateTime.now().minusDays(1));
        secondAnalyticEvent.setActorId(3);
    }

    @Test
    void testAddNewEventWithNullReceivedAt() {
        when(eventMapper.toEntity(eventDto)).thenReturn(analyticsEvent);

        analyticsEventService.addNewEvent(eventDto);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
        verify(eventMapper, times(1)).toDto(analyticsEvent);
        assertEquals(LocalDateTime.now().toLocalDate(), analyticsEvent.getReceivedAt().toLocalDate());
    }

    @Test
    void testAddNewEventWithReceivedAt() {
        analyticsEvent.setReceivedAt(LocalDateTime.now().minusDays(1));
        when(eventMapper.toEntity(eventDto)).thenReturn(analyticsEvent);

        EventDto result = analyticsEventService.addNewEvent(eventDto);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
        verify(eventMapper, times(1)).toDto(analyticsEvent);
        assertEquals(result.getReceivedAt(), analyticsEvent.getReceivedAt());
    }

    @Test
    void testAddNewEventWithInvalidAnalyticEvent() {
        doThrow(new IllegalArgumentException()).when(analyticEventServiceValidator).checkEntity(analyticsEvent);
        assertThrows(IllegalArgumentException.class,
                () -> analyticsEventService.addNewEvent(analyticsEvent));
    }

    @Test
    void testAddNewEventWithNullReceivedAtWithEvent() {
        analyticsEventService.addNewEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
        verify(eventMapper, times(1)).toDto(analyticsEvent);
        assertEquals(LocalDateTime.now().toLocalDate(), analyticsEvent.getReceivedAt().toLocalDate());
    }

    @Test
    void testAddNewEventWithReceivedAtWithEvent() {
        analyticsEvent.setReceivedAt(LocalDateTime.now().minusDays(1));

        EventDto result = analyticsEventService.addNewEvent(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
        verify(eventMapper, times(1)).toDto(analyticsEvent);
        assertEquals(result.getReceivedAt(), analyticsEvent.getReceivedAt());
    }

//    @Test
//    void testGetEventsDtoWithInvalidEventRequestDto() {
//        doThrow(new IllegalArgumentException())
//                .when(analyticEventServiceValidator).validateInterval(null, null, null);
//        assertThrows(IllegalArgumentException.class,
//                () -> eventService.getEventsDto(1, null, null, null, null));
//    }

    @Test
    void testGetEventsEntityWithInvalidEventRequestDto() {
        doThrow(new IllegalArgumentException())
                .when(analyticEventServiceValidator).validateInterval(null, null, null);
        assertThrows(IllegalArgumentException.class,
                () -> analyticsEventService.getEventsEntity(1, null, null, null, null));
    }

//    @Test
//    void testGetEventDtoWithInterval() {
//        analyticsEvent.setReceivedAt(LocalDateTime.now().minusYears(1));
//
//        Stream<AnalyticsEvent> events = Stream.of(analyticsEvent, secondAnalyticEvent);
//
//        when(analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.FOLLOWER)).thenReturn(events);
//
//        List<EventDto> result = eventService.getEventsDto(1, "FOLLOWER", "WEEK", null, null);
//
//        assertFalse(result.isEmpty());
//        assertEquals(result.get(0).getActorId(), secondAnalyticEvent.getActorId());
//        assertEquals(result.size(), 1);
//
//    }

//    @Test
//    void testGetEventDtoWithLocalDateTime() {
//        analyticsEvent.setReceivedAt(LocalDateTime.now().minusYears(1));
//
//        Stream<AnalyticsEvent> events = Stream.of(analyticsEvent, secondAnalyticEvent);
//
//        when(analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.FOLLOWER)).thenReturn(events);
//
//        List<EventDto> result = eventService.getEventsDto(1, "FOLLOWER", null, "08-10-2024 10:15:30", "04-12-2024 10:15:30");
//
//        assertFalse(result.isEmpty());
//        assertEquals(result.get(0).getActorId(), secondAnalyticEvent.getActorId());
//        assertEquals(result.size(), 1);
//    }

    @Test
    void testGetEventEntityWithInterval() {

        analyticsEvent.setReceivedAt(LocalDateTime.now().minusYears(1));

        Stream<AnalyticsEvent> events = Stream.of(analyticsEvent, secondAnalyticEvent);

        when(analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.FOLLOWER)).thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getEventsEntity(1, EventType.FOLLOWER, Interval.WEEK, null, null);

        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getActorId(), secondAnalyticEvent.getActorId());
        assertEquals(result.size(), 1);

    }

    @Test
    void testGetEventEntityWithLocalDateTime() {
        analyticsEvent.setReceivedAt(LocalDateTime.now().minusYears(1));

        Stream<AnalyticsEvent> events = Stream.of(analyticsEvent, secondAnalyticEvent);

        when(analyticsEventRepository.findByReceiverIdAndEventType(1, EventType.FOLLOWER)).thenReturn(events);

        List<AnalyticsEvent> result = analyticsEventService.getEventsEntity(1, EventType.FOLLOWER, null, LocalDateTime.now().minusWeeks(1), LocalDateTime.now());

        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getActorId(), secondAnalyticEvent.getActorId());
        assertEquals(result.size(), 1);
    }
}