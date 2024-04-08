package faang.school.analytics.service;

import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.dto.analytics.Interval;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    @InjectMocks
    private AnalyticsService analyticsService;

    Stream<AnalyticsEvent> analyticsEvents;

    LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.now().minusHours(1);
        analyticsEvents = Stream.of(AnalyticsEvent.builder()
                        .receiverId(1L)
                        .eventType(EventType.FOLLOWER)
                        .actorId(1L)
                        .receivedAt(localDateTime)
                        .build(),
                AnalyticsEvent.builder()
                        .receiverId(1L)
                        .eventType(EventType.FOLLOWER)
                        .actorId(1L)
                        .receivedAt(LocalDateTime.now().minusDays(2))
                        .build());
    }

    @Test
    void getAnalyticsEvents_ValidArgs() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class))).thenReturn(analyticsEvents);
        List<AnalyticsEventDto> expected = getExpectedAnalyticsEvents();

        List<AnalyticsEventDto> actual = analyticsService.getAnalytics(1L, EventType.FOLLOWER, Interval.DAY);

        assertEquals(expected, actual);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(anyLong(), any(EventType.class));
        verify(analyticsEventMapper, times(1)).toDto(anyList());
    }

    @Test
    void getAnalyticsEvents2_ValidArgs() {
        when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class))).thenReturn(analyticsEvents);
        List<AnalyticsEventDto> expected = getExpectedAnalyticsEvents();

        List<AnalyticsEventDto> actual = analyticsService.getAnalytics(1L, EventType.FOLLOWER, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));

        assertEquals(expected, actual);
        verify(analyticsEventRepository, times(1)).findByReceiverIdAndEventType(anyLong(), any(EventType.class));
        verify(analyticsEventMapper, times(1)).toDto(anyList());
    }

    private List<AnalyticsEventDto> getExpectedAnalyticsEvents() {
        return List.of(AnalyticsEventDto.builder()
                .receiverId(1L)
                .eventType(EventType.FOLLOWER)
                .actorId(1L)
                .receivedAt(localDateTime)
                .build());
    }
}
