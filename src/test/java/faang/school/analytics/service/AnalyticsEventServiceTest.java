package faang.school.analytics.service;

import faang.school.analytics.dto.analyticsEvent.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapper analyticsEventMapper = Mappers.getMapper(AnalyticsEventMapper.class);
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent event;

    @BeforeEach
    void setUp() {
        event = AnalyticsEvent.builder().id(1L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.of(2025, 2, 9, 0, 0)).build();
    }

    @Test
    void saveEvent_ShouldSaveAndReturnDto() {
        when(analyticsEventRepository.save(event)).thenReturn(event);
        AnalyticsEventDto expectedDto = analyticsEventMapper.toAnalyticsEventDto(event);

        AnalyticsEventDto result = analyticsEventService.saveEvent(event);

        assertEquals(expectedDto, result);

        verify(analyticsEventRepository).save(event);
    }

    @Test
    void getAnalytics_ShouldFilterAndSortEvents() {
        LocalDateTime from = LocalDateTime.of(2025, 2, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2025, 2, 10, 0, 0);

        AnalyticsEvent event1 = AnalyticsEvent.builder().id(1L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.of(2025, 1, 30, 10, 0)).build();
        AnalyticsEvent event2 = AnalyticsEvent.builder().id(2L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.of(2025, 2, 5, 10, 0)).build();
        AnalyticsEvent event3 = AnalyticsEvent.builder().id(3L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER)
                .receivedAt(LocalDateTime.of(2025, 2, 7, 10, 0)).build();

        when(analyticsEventRepository.findByReceiverIdAndEventType(100L, EventType.FOLLOWER))
                .thenReturn(Stream.of(event3, event2, event1));

        List<AnalyticsEventDto> expectedDtos = Stream.of(event3, event2)
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(100L,
                EventType.FOLLOWER, null, from, to);

        assertEquals(expectedDtos, result);
        verify(analyticsEventRepository).findByReceiverIdAndEventType(100L, EventType.FOLLOWER);
    }

    @Test
    void getAnalytics_ShouldApplyInterval() {
        Interval interval = Interval.WEEK;

        AnalyticsEvent event1 = AnalyticsEvent.builder().id(1L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER).receivedAt(LocalDateTime.now().minusDays(6)).build();
        AnalyticsEvent event2 = AnalyticsEvent.builder().id(2L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER).receivedAt(LocalDateTime.now().minusDays(2)).build();
        AnalyticsEvent event3 = AnalyticsEvent.builder().id(3L).receiverId(100L).actorId(200L)
                .eventType(EventType.FOLLOWER).receivedAt(LocalDateTime.now().minusDays(10)).build();
        List<AnalyticsEventDto> expectedDtos = Stream.of(event2, event1)
                .map(analyticsEventMapper::toAnalyticsEventDto)
                .toList();

        when(analyticsEventRepository.findByReceiverIdAndEventType(100L, EventType.FOLLOWER))
                .thenReturn(Stream.of(event1, event2, event3));


        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(100L,
                EventType.FOLLOWER, interval, null, null);

        assertEquals(expectedDtos, result);
    }
}