package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    @DisplayName("Save Analytics Event")
    public void testSaveAnalyticsEvent() {
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();

        analyticsEventService.save(analyticsEvent);

        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }


    private long receiverId;
    private EventType eventTypeUserFollower;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
    private Stream<AnalyticsEvent> analyticsEventsStream;
    private List<AnalyticEventDto> analyticsEventDtosList;
    private LocalDateTime currentDateTime;

    @BeforeEach
    public void setup() {

        currentDateTime = LocalDateTime.now();

        receiverId = 1L;
        eventTypeUserFollower = EventType.USER_FOLLOWER;
        interval = Interval.WEEK;
        from = currentDateTime.minusDays(8);
        to = currentDateTime;

        AnalyticsEvent firstAnalyticEvent = AnalyticsEvent.builder()
                .id(1L)
                .eventType(eventTypeUserFollower)
                .receivedAt(currentDateTime.minusMinutes(1))
                .build();
        AnalyticsEvent secondAnalyticEvent = AnalyticsEvent.builder()
                .id(2L)
                .eventType(eventTypeUserFollower)
                .receivedAt(currentDateTime.minusDays(3))
                .build();
        AnalyticsEvent thirdAnalyticEvent = AnalyticsEvent.builder()
                .id(3L)
                .eventType(eventTypeUserFollower)
                .receivedAt(currentDateTime.minusDays(1))
                .build();

        analyticsEventsStream = Stream.of(firstAnalyticEvent, secondAnalyticEvent, thirdAnalyticEvent);

        AnalyticEventDto firstAnalyticEventDto = AnalyticEventDto.builder()
                .id(1L)
                .eventType(eventTypeUserFollower)
                .receivedAt(currentDateTime.minusMinutes(1))
                .build();
        AnalyticEventDto secondAnalyticEventDto = AnalyticEventDto.builder()
                .id(2L)
                .eventType(eventTypeUserFollower)
                .receivedAt(currentDateTime.minusDays(3))
                .build();
        AnalyticEventDto thirdAnalyticEventDto = AnalyticEventDto.builder()
                .id(3L)
                .eventType(eventTypeUserFollower)
                .receivedAt(currentDateTime.minusDays(1))
                .build();
        analyticsEventDtosList = List.of(firstAnalyticEventDto, thirdAnalyticEventDto, secondAnalyticEventDto);
    }

    @Test
    @DisplayName("Get analytics events by interval")
    public void testGetAnalyticsEventsByInterval() {

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventTypeUserFollower))
                .thenReturn(analyticsEventsStream);

        List<AnalyticEventDto> actualAnalyticEventDto =
                analyticsEventService.getAnalytics(receiverId, eventTypeUserFollower, interval, from, to);

        assertEquals(analyticsEventDtosList, actualAnalyticEventDto);
    }

    @Test
    @DisplayName("Get analytics events by interval null")
    public void testGetAnalyticsEventsByIntervalNull() {

        when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventTypeUserFollower))
                .thenReturn(analyticsEventsStream);

        List<AnalyticEventDto> actualAnalyticEventDto =
                analyticsEventService.getAnalytics(receiverId, eventTypeUserFollower, null, from, to);

        assertEquals(analyticsEventDtosList, actualAnalyticEventDto);
    }
}
