package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticEventDto;
import faang.school.analytics.dto.AnalyticInfoDto;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventFilterDto;
import faang.school.analytics.filter.AnalyticsEventIntervalFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.LikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
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
import java.util.stream.Stream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    private AnalyticsEventIntervalFilter analyticsEventIntervalFilter;

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

    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventDto analyticsEventDto;
    private Stream<AnalyticsEvent> analyticsEventStream;
    private long receiverId;
    private EventType eventType;
    private AnalyticsEventFilterDto analyticsEventFilterDto;

        @BeforeEach
        public void setup() {

            currentDateTime = LocalDateTime.now();

            receiverId = 1L;
            eventType = EventType.FOLLOWER;
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

//            analyticsEvent = AnalyticsEvent.builder().build();
//            analyticsEventStream = Stream.of(analyticsEvent);
//            analyticsEventDto = AnalyticsEventDto.builder().build();
//            analyticsEventFilterDto = AnalyticsEventFilterDto
//                    .builder()
//                    .receiverId(receiverId)
//                    .eventType(eventType)
//                    .build();
//
//            analyticsEventService = AnalyticsEventService.builder()
//                    .analyticsEventMapper(analyticsEventMapper)
//                    .analyticsEventRepository(analyticsEventRepository)
//                    .analyticsEventFilters(List.of(analyticsEventIntervalFilter))
//                    .objectMapper(objectMapper)
//                    .build();

        }

//    @Test
//    @DisplayName("testing saveEvent method execution")
//    void testSaveEvent() {
//        analyticsEventService.saveEvent(analyticsEvent);
//        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
//    }
//
//    @Test
//    @DisplayName("testing getAnalytics method with interval filter")
//    void testGetAnalyticsWithIntervalFilter() {
//        when(analyticsEventRepository.findByReceiverIdAndEventTypeOrderByReceiverIdDesc(receiverId, eventType))
//                .thenReturn(analyticsEventStream);
//        when(analyticsEventIntervalFilter.filter(analyticsEventStream, analyticsEventFilterDto))
//                .thenReturn(analyticsEventStream);
//        when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);
//
//        List<AnalyticsEventDto> analyticsEvents =
//                analyticsEventService.getAnalytics(analyticsEventFilterDto);
//
//        verify(analyticsEventRepository, times(1))
//                .findByReceiverIdAndEventTypeOrderByReceiverIdDesc(receiverId, eventType);
//        verify(analyticsEventIntervalFilter, times(1))
//                .filter(analyticsEventStream, analyticsEventFilterDto);
//        verify(analyticsEventMapper, times(1)).toDto(analyticsEvent);
//        assertNotNull(analyticsEvents);
//        assertIterableEquals(List.of(analyticsEventDto), analyticsEvents);
//    }

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

            verify(analyticsEventRepository, times(1)).save(analyticsEvent);
        }
}