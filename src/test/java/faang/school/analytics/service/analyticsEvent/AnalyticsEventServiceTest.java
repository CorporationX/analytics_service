package faang.school.analytics.service.analyticsEvent;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.mapper.analyticsEvent.AnalyticsEventMapper;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.bouncycastle.est.LimitedSource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    private AnalyticsEventDto analyticsEventDtoFirst;
    private AnalyticsEventDto analyticsEventDtoSecond;
    private AnalyticsEvent analyticsEventFirst;
    private AnalyticsEvent analyticsEventSecond;
    Stream<AnalyticsEvent> analyticsEventList;
    List<AnalyticsEventDto> analyticsEventDtoList;
    List<AnalyticsEvent> expectedList;
    private long receiverId;
    private EventType eventType;
    private Interval interval;


    @BeforeEach
    void init(){
        receiverId = 2L;
        eventType = EventType.FOLLOWER;
        interval = Interval.ALL_TIME;

        analyticsEventDtoFirst = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.FOLLOWER)
                .build();
        analyticsEventDtoSecond = AnalyticsEventDto.builder()
                .id(2L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.FOLLOWER)
                .build();

        analyticsEventFirst = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.now())
                .build();
        analyticsEventSecond = AnalyticsEvent.builder()
                .id(2L)
                .receiverId(2L)
                .actorId(3L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.now())
                .build();

        analyticsEventList = Stream.of(analyticsEventFirst, analyticsEventSecond);
        expectedList = List.of(analyticsEventFirst, analyticsEventSecond);
        analyticsEventDtoList = List.of(analyticsEventDtoFirst, analyticsEventDtoSecond);
    }
    @Test
    @DisplayName("saveEventException")
    void testSaveEventException(){
        Mockito.when(analyticsEventRepository.save(any(AnalyticsEvent.class)))
                .thenThrow(new RuntimeException("exception"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                analyticsEventService.saveEvent(new AnalyticsEvent()));

        assertEquals("exception", exception.getMessage());
    }

    @Test
    @DisplayName("saveEventToDtoException")
    void testSaveEventToDtoException(){
        Mockito.when(analyticsEventRepository.save(any(AnalyticsEvent.class)))
                .thenReturn(analyticsEventFirst);
        Mockito.when(analyticsEventMapper.toDto(any(AnalyticsEvent.class)))
                .thenThrow(new RuntimeException("exception"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                analyticsEventService.saveEvent(new AnalyticsEvent()));

        assertEquals("exception", exception.getMessage());
    }
    @Test
    @DisplayName("saveEventValid")
    void testSaveEventValid(){
        Mockito.when(analyticsEventRepository.save(any(AnalyticsEvent.class)))
                .thenReturn(analyticsEventFirst);
        Mockito.when(analyticsEventMapper.toDto(any(AnalyticsEvent.class)))
                .thenReturn(analyticsEventDtoFirst);

        analyticsEventService.saveEvent(analyticsEventFirst);

        Mockito.verify(analyticsEventRepository, Mockito.times(1))
                .save(any(AnalyticsEvent.class));
        Mockito.verify(analyticsEventMapper, Mockito.times(1))
                .toDto(any(AnalyticsEvent.class));
    }

    @Test
    @DisplayName("getAnalyticsFindAnalyticsEventException")
    void testGetAnalyticsFindAnalyticsEventException(){
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenThrow(new RuntimeException("exception"));
        Exception exception = assertThrows(RuntimeException.class, () ->
                analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null));

        assertEquals("exception", exception.getMessage());
    }
    @Test
    @DisplayName("getAnalyticsSortedAllEvent")
    void testGetAnalyticsSortedAllEvent(){
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(analyticsEventList);
        Mockito.when(analyticsEventMapper.toListDto(anyList())).thenReturn(analyticsEventDtoList);


        List<AnalyticsEventDto> analyticsEventDtos = analyticsEventService
                .getAnalytics(receiverId, eventType, interval, null, null);
        System.out.println(analyticsEventDtos);

        assertEquals(expectedList.size(), analyticsEventDtos.size());

        Mockito.verify(analyticsEventRepository, Mockito.times(1))
                .findByReceiverIdAndEventType(anyLong(), any(EventType.class));
        Mockito.verify(analyticsEventMapper, Mockito.times(1))
                .toListDto(anyList());
    }

    @Test
    @DisplayName("getAnalyticsSortedAllEventAllNull")
    void testGetAnalyticsSortedAllEventAllNull(){
        Mockito.when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                .thenReturn(analyticsEventList);
        Mockito.when(analyticsEventMapper.toListDto(anyList())).thenReturn(analyticsEventDtoList);


        List<AnalyticsEventDto> analyticsEventDtos = analyticsEventService
                .getAnalytics(receiverId, eventType, null, null, null);
        System.out.println(analyticsEventDtos);

        assertEquals(expectedList.size(), analyticsEventDtos.size());

        Mockito.verify(analyticsEventRepository, Mockito.times(1))
                .findByReceiverIdAndEventType(anyLong(), any(EventType.class));
        Mockito.verify(analyticsEventMapper, Mockito.times(1))
                .toListDto(anyList());
    }
}