package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.service.Interval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    private AnalyticsEvent event;
    private AnalyticsEvent secondEvent;
    private AnalyticsEventDto eventDto;
    private AnalyticsEventDto secondEventDto;
    private final EventType eventType = EventType.GOAL_COMPLETED;
    private final long receiverId = 1L;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
    LocalDateTime testTime = LocalDateTime.of(2024, Month.AUGUST, 15, 12, 15);

    @BeforeEach
    void setUp() {
        interval = Interval.WEEK;
        from = testTime.minusWeeks(1);
        to = testTime.plusWeeks(4);

        event = AnalyticsEvent.builder()
                .id(1L)
                .receiverId(receiverId)
                .actorId(1L)
                .eventType(eventType)
                .receivedAt(from.plusWeeks(1))
                .build();

        secondEvent = AnalyticsEvent.builder()
                .id(3L)
                .receiverId(receiverId)
                .actorId(3L)
                .eventType(eventType)
                .receivedAt(from.plusDays(3))
                .build();

        eventDto = AnalyticsEventDto.builder()
                .id(1L)
                .receiverId(receiverId)
                .actorId(1L)
                .eventType(eventType)
                .receivedAt(from.plusWeeks(1))
                .build();

        secondEventDto = AnalyticsEventDto.builder()
                .id(3L)
                .receiverId(4L)
                .actorId(3L)
                .eventType(eventType)
                .receivedAt(from.plusDays(3))
                .build();
    }

    @Test
    void testSaveEvent() {
        when(analyticsEventRepository.save(event)).thenReturn(event);

        analyticsEventService.saveEvent(event);
    }

    @Nested
    @DisplayName("Method: testGetAnalytics")
    class whenGetAnalytics {
        @Test
        void testGetAnalyticsWithNotFoundEvent() {
            when(analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(receiverId, eventType, from, to))
                    .thenReturn(null);

            List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

            Assertions.assertTrue(result.isEmpty());
            verifyNoMoreInteractions(analyticsEventRepository, analyticsEventMapper);
        }

        @Test
        void testGetAnalyticsWhenIntervalIsNull() {
            when(analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(receiverId, eventType, from, to))
                    .thenReturn(Stream.of(event));
            when(analyticsEventMapper.toDto(event)).thenReturn(eventDto);

            List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, null, from, to);

            Assertions.assertEquals(1, result.size());
            Assertions.assertEquals(result, List.of(eventDto));
        }

        @Test
        void testGetAnalyticsWhenIntervalNotNull() {
            when(analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(
                    eq(receiverId), eq(eventType), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(Stream.of(event, secondEvent));
            when(analyticsEventMapper.toDto(event)).thenReturn(eventDto);
            when(analyticsEventMapper.toDto(secondEvent)).thenReturn(secondEventDto);

            List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(receiverId, eventType, interval, from, to);

            Assertions.assertEquals(2, result.size());
            InOrder inOrder = inOrder(analyticsEventRepository, analyticsEventMapper);
            inOrder.verify(analyticsEventRepository).findByReceiverIdAndEventTypeAndReceivedAtBetween(
                    eq(receiverId), eq(eventType), any(LocalDateTime.class), any(LocalDateTime.class));
            inOrder.verify(analyticsEventMapper).toDto(secondEvent);
            inOrder.verify(analyticsEventMapper).toDto(event);
        }
    }
}
