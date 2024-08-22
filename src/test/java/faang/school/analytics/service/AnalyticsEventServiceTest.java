package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.filter.AnalyticsEventIntervalFilter;
import faang.school.analytics.filter.AnalyticsEventPeriodFilter;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventIntervalFilter analyticsEventIntervalFilter;
    @Mock
    private AnalyticsEventPeriodFilter analyticsEventPeriodFilter;

    private AnalyticsEvent analyticsEvent;
    private AnalyticsEventDto analyticsEventDto;
    private Stream<AnalyticsEvent> analyticsEventStream;
    private long receiverId;
    private EventType eventType;

    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;

    @BeforeEach
    public void setUp() {
        receiverId = 1L;
        from = LocalDateTime.now();
        to = LocalDateTime.now();
        interval = Interval.DAY;
        eventType = EventType.FOLLOWER;
        analyticsEvent = AnalyticsEvent.builder().build();
        analyticsEventStream = Stream.of(analyticsEvent);
        analyticsEventDto = AnalyticsEventDto.builder().build();
    }

    @Test
    @DisplayName("testing saveEvent method execution")
    void testSaveEvent() {
        when(analyticsEventMapper.toEntity(analyticsEventDto)).thenReturn(analyticsEvent);
        analyticsEventService.saveEvent(analyticsEventDto);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Nested
    @DisplayName("testing getAnalytics")
    class testGetAnalytics {

        @Test
        @DisplayName("testing getAnalytics method with interval filter")
        void testGetAnalyticsWithIntervalFilter() {
            when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                    .thenReturn(analyticsEventStream);
            when(analyticsEventIntervalFilter.filter(analyticsEventStream, interval)).thenReturn(analyticsEventStream);
            when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

            List<AnalyticsEventDto> analyticsEvents =
                    analyticsEventService.getAnalytics(receiverId, eventType, interval, null, null);

            verify(analyticsEventRepository, times(1))
                    .findByReceiverIdAndEventType(eq(receiverId), any());
            verify(analyticsEventIntervalFilter, times(1)).filter(analyticsEventStream, interval);
            verify(analyticsEventMapper, times(1)).toDto(analyticsEvent);
            assertNotNull(analyticsEvents);
            assertIterableEquals(List.of(analyticsEventDto), analyticsEvents);
        }

        @Test
        @DisplayName("testing getAnalytics method with time bounds filter")
        void testGetAnalyticsWithTimeBoundsFilter() {
            when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                    .thenReturn(analyticsEventStream);
            when(analyticsEventPeriodFilter.filter(analyticsEventStream, from, to)).thenReturn(analyticsEventStream);
            when(analyticsEventMapper.toDto(analyticsEvent)).thenReturn(analyticsEventDto);

            List<AnalyticsEventDto> analyticsEvents = analyticsEventService.getAnalytics(receiverId, eventType,
                    null, from, to);

            verify(analyticsEventRepository, times(1))
                    .findByReceiverIdAndEventType(receiverId, eventType);
            verify(analyticsEventPeriodFilter, times(1)).filter(analyticsEventStream, from, to);
            verify(analyticsEventMapper, times(1)).toDto(analyticsEvent);
            assertNotNull(analyticsEvents);
            assertIterableEquals(List.of(analyticsEventDto), analyticsEvents);
        }
    }
}