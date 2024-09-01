package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
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
import org.springframework.data.redis.connection.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension .class)
public class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    private AnalyticsEvent analyticsEvent;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @Mock
    private ObjectMapper objectMapper;

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
    private List<AnalyticsEvent> analyticsEvents;
    private List<AnalyticEventDto> analyticsEventDtosList;
    private LocalDateTime currentDateTime;
    private AnalyticInfoDto analyticInfoDto;

    @BeforeEach
    public void setUp(){
        analyticsEvent = new AnalyticsEvent();

        currentDateTime = LocalDateTime.now();

        receiverId = 1L;
        eventTypeUserFollower = EventType.USER_FOLLOWER;
        interval = Interval.WEEK;
        from = currentDateTime.minusDays(8);
        to = currentDateTime;

        analyticInfoDto = AnalyticInfoDto.builder()
                .receiverId(receiverId)
                .eventType(eventTypeUserFollower)
                .interval(interval)
                .from(from)
                .to(to).build();

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

        analyticsEvents = List.of(firstAnalyticEvent, secondAnalyticEvent, thirdAnalyticEvent);

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

        when(analyticsEventRepository.getByDays(any(LocalDateTime.class)))
                .thenReturn(analyticsEvents);

        List<AnalyticEventDto> actualAnalyticEventDto =
                analyticsEventService.getAnalytics(analyticInfoDto);

        assertEquals(analyticsEventDtosList, actualAnalyticEventDto);
    }

    @Test
    void testSaveAnalyticsEvent() {
        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Test
    @DisplayName("Get analytics events by interval null")
    public void testGetAnalyticsEventsByIntervalNull() {

        when(analyticsEventRepository.getBetweenDate(analyticInfoDto.getFrom(), analyticInfoDto.getTo()))
                .thenReturn(analyticsEvents);

        analyticInfoDto.setInterval(null);
        List<AnalyticEventDto> actualAnalyticEventDto =
                analyticsEventService.getAnalytics(analyticInfoDto);

        assertEquals(analyticsEventDtosList, actualAnalyticEventDto);
    }

    @DisplayName("testing saveLikeAnalytics method")
    @Test
    public void testSaveLikeAnalytics() throws IOException {
        Message message = mock(Message.class);
        AnalyticsEvent analyticsEvent = new AnalyticsEvent();
        LikeEvent likeEvent = new LikeEvent();

        when(objectMapper.readValue(message.getBody(), LikeEvent.class)).thenReturn(likeEvent);
        when(analyticsEventMapper.toAnalyticsEventFromLikeEvent(likeEvent)).thenReturn(analyticsEvent);

        analyticsEventService.saveLikeAnalytics(message);


}