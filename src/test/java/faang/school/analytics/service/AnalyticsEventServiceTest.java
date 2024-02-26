package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private final long recieverId = 1L;
    private final EventType event = EventType.POST_COMMENT;
    private final LocalDateTime from = LocalDateTime.now().minusDays(1);
    private final LocalDateTime to = LocalDateTime.now();

    @BeforeEach
    void setUp() {

    }


    @Test
    void testSaveEvent() {
        //Arrange
        AnalyticsEvent event = new AnalyticsEvent();
        //Act
        analyticsEventService.saveEvent(event);
        //Assert
        verify(analyticsEventRepository, times(1)).save(event);
    }

    @Test
    void testGetAnalytics_whenIntervalNotNull_thenListFilterByInterval() {
        //Arrange
        Interval interval = Interval.WEEK;
        List<AnalyticsEvent> events = List.of(
                AnalyticsEvent.builder().id(10L).eventType(EventType.POST_COMMENT)
                        .receiverId(recieverId).receivedAt(to.minusDays(1)).build(),
                AnalyticsEvent.builder().id(20L).eventType(EventType.POST_COMMENT)
                        .receiverId(recieverId).receivedAt(to.minusDays(6)).build(),
                AnalyticsEvent.builder().id(30L).eventType(EventType.POST_COMMENT)
                        .receiverId(recieverId).receivedAt(to.minusDays(10)).build()
        );
        when(analyticsEventRepository.findByReceiverIdAndEventType(recieverId, event)).thenReturn(events.stream());

        //Act
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(recieverId, event, interval, from, to);

        //Assert
        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(recieverId, event);
        verify(analyticsEventMapper, times(2)).toDto(any());
        assertEquals(result.size(), 2);

    }

    @Test
    void testGetAnalytics_whenIntervalNull_thenListFilterByFromTo() {
        //Arrange
        Interval interval = null;
        List<AnalyticsEvent> events = List.of(
                AnalyticsEvent.builder().id(10L).eventType(EventType.POST_COMMENT)
                        .receiverId(recieverId).receivedAt(to.minusDays(1)).build(),
                AnalyticsEvent.builder().id(20L).eventType(EventType.POST_COMMENT)
                        .receiverId(recieverId).receivedAt(to.minusDays(6)).build(),
                AnalyticsEvent.builder().id(30L).eventType(EventType.POST_COMMENT)
                        .receiverId(recieverId).receivedAt(to.minusDays(10)).build()
        );
        when(analyticsEventRepository.findByReceiverIdAndEventType(recieverId, event)).thenReturn(events.stream());

        //Act
        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(recieverId, event, interval, from, to);

        //Assert
        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventType(recieverId, event);
        verify(analyticsEventMapper, times(1)).toDto(any());
        assertEquals(result.size(), 1);
    }
}