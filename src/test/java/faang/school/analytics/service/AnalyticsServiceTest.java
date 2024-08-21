package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.filter.AnalyticsEventIntervalFilter;
import faang.school.analytics.filter.AnalyticsEventPeriodFilter;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    private AnalyticsEventMapperImpl analyticsEventMapperImpl;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private AnalyticsEventDto analyticsEventDto;
    private AnalyticsEvent analyticsEvent;
    private long receiverId;
    private int eventType;
    private List<AnalyticsEvent> analyticsEventsAll;

    private String intervalStr;
    private String fromStr;
    private String toStr;

    @BeforeEach
    public void setUp() {
        receiverId = 1L;
        fromStr = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        toStr = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        intervalStr = "DAY";
        eventType = 1;
        analyticsEventsAll = List.of(
                AnalyticsEvent.builder()
                        .id(1L)
                        .receivedAt(LocalDateTime.now())
                        .build(),
                AnalyticsEvent.builder()
                        .id(2L)
                        .receivedAt(LocalDateTime.now().minusDays(10))
                        .build()
        );

        analyticsEventDto = AnalyticsEventDto.builder().build();
        analyticsEvent = AnalyticsEvent.builder().build();
        analyticsEventMapperImpl = new AnalyticsEventMapperImpl();
        AnalyticsEventIntervalFilter analyticsEventIntervalFilter = new AnalyticsEventIntervalFilter();
        AnalyticsEventPeriodFilter analyticsEventPeriodFilter = new AnalyticsEventPeriodFilter();
        analyticsService = AnalyticsService.builder()
                .analyticsEventMapper(analyticsEventMapperImpl)
                .analyticsEventRepository(analyticsEventRepository)
                .analyticsEventIntervalFilter(analyticsEventIntervalFilter)
                .analyticsEventPeriodFilter(analyticsEventPeriodFilter)
                .build();
    }

    @Test
    @DisplayName("testing saveEvent method execution")
    void testSaveEvent() {
        analyticsService.saveEvent(analyticsEventDto);
        verify(analyticsEventRepository, times(1)).save(analyticsEvent);
    }

    @Nested
    @DisplayName("testing getAnalytics")
    class testGetAnalytics {

        @Test
        @DisplayName("testing getAnalytics method with interval filter")
        void testGetAnalyticsWithIntervalFilter() {
            when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, EventType.of(eventType)))
                    .thenReturn(analyticsEventsAll.stream());
            List<AnalyticsEventDto> analyticsEvents =
                    analyticsService.getAnalytics(receiverId, eventType, intervalStr, null, null);
            assertEquals(1, analyticsEvents.size());
            assertEquals(analyticsEventMapperImpl.toDto(analyticsEventsAll.get(0)), analyticsEvents.get(0));
        }

        @Test
        @DisplayName("testing getAnalytics method with time bounds filter")
        void testGetAnalyticsWithTimeBoundsFilter() {
            when(analyticsEventRepository.findByReceiverIdAndEventType(receiverId, EventType.of(eventType)))
                    .thenReturn(analyticsEventsAll.stream());
            List<AnalyticsEventDto> analyticsEvents =
                    analyticsService.getAnalytics(receiverId, eventType,
                            null, fromStr, toStr);
            assertEquals(1, analyticsEvents.size());
            assertEquals(analyticsEventMapperImpl.toDto(analyticsEventsAll.get(0)), analyticsEvents.get(0));
        }
    }
}