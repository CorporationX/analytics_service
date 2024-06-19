package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.joda.time.Interval;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {
    @InjectMocks
    private AnalyticsEventService analyticsEventService;
    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    private List<AnalyticsEvent> analyticsEvents;
    private List<AnalyticsEventDto> analyticsEventDtos;
    private AnalyticsEvent firstEvent;
    private AnalyticsEvent secondEvent;
    private AnalyticsEventDto firstEventDto;
    private AnalyticsEventDto secondEventDto;


    @BeforeEach
    void setUp() {
        LocalDateTime firstEventReceivedAt = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        LocalDateTime secondEventReceivedAt = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
        firstEvent = AnalyticsEvent.builder()
                .eventType(EventType.ACHIEVEMENT_RECEIVED)
                .receivedAt(firstEventReceivedAt)
                .build();
        secondEvent = AnalyticsEvent.builder()
                .eventType(EventType.ACHIEVEMENT_RECEIVED)
                .receivedAt(secondEventReceivedAt)
                .build();

        firstEventDto = AnalyticsEventDto.builder()
                .eventType(EventType.ACHIEVEMENT_RECEIVED)
                .receivedAt(firstEventReceivedAt)
                .build();
        secondEventDto = AnalyticsEventDto.builder()
                .eventType(EventType.ACHIEVEMENT_RECEIVED)
                .receivedAt(secondEventReceivedAt)
                .build();

        analyticsEvents = List.of(secondEvent, firstEvent);
        analyticsEventDtos = List.of(firstEventDto, secondEventDto);
    }

    @Test
    void saveEventTest() {
        ArgumentCaptor<AnalyticsEvent> analyticsEventArgumentCaptor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        AnalyticsEvent event = new AnalyticsEvent();

        analyticsEventService.saveEvent(event);

        verify(analyticsEventRepository).save(analyticsEventArgumentCaptor.capture());
        assertEquals(event, analyticsEventArgumentCaptor.getValue());
    }

    @Nested
    class getAnalyticsPositiveTests {
        @BeforeEach
        void setUp() {
            when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                    .thenReturn(analyticsEvents.stream());
            when(analyticsEventMapper.toDto(firstEvent)).thenReturn(firstEventDto);
            when(analyticsEventMapper.toDto(secondEvent)).thenReturn(secondEventDto);
        }

        @Test
        void getAnalyticsByIntervalTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    Interval.parse("2010-06-30T01:20/3010-06-30T01:20"),
                    null,
                    null);

            assertEquals(analyticsEventDtos, actualResult);
        }

        @Test
        void getAnalyticsByFromAndToTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    null,
                    firstEvent.getReceivedAt(),
                    secondEvent.getReceivedAt());

            assertEquals(analyticsEventDtos, actualResult);
        }

        @Test
        void getAnalyticsWithoutTimeIntervalTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    null,
                    null,
                    null);

            assertEquals(analyticsEventDtos, actualResult);
        }

        @Test
        void getAnalyticsByFromTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    null,
                    firstEvent.getReceivedAt(),
                    null);

            assertEquals(analyticsEventDtos, actualResult);
        }

        @Test
        void getAnalyticsByToTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    null,
                    null,
                    secondEvent.getReceivedAt());

            assertEquals(analyticsEventDtos, actualResult);
        }
    }

    @Nested
    class getAnalyticsNegativeTests {
        @BeforeEach
        void setUp() {
            when(analyticsEventRepository.findByReceiverIdAndEventType(anyLong(), any(EventType.class)))
                    .thenReturn(analyticsEvents.stream());
        }

        @Test
        void getNoAnalyticsForSuchToTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    null,
                    null,
                    secondEvent.getReceivedAt().minus(1, ChronoUnit.YEARS));

            assertEquals(List.of(), actualResult);
        }

        @Test
        void getNoAnalyticsForSuchFromTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    null,
                    firstEvent.getReceivedAt().plus(1, ChronoUnit.YEARS),
                    null);

            assertEquals(List.of(), actualResult);
        }

        @Test
        void getNoAnalyticsForSuchFromAndToTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    null,
                    firstEvent.getReceivedAt().minus(1, ChronoUnit.YEARS),
                    secondEvent.getReceivedAt().minus(1, ChronoUnit.YEARS));

            assertEquals(List.of(), actualResult);
        }

        @Test
        void getNoAnalyticsForSuchIntervalTest() {
            var actualResult = analyticsEventService.getAnalytics(1L,
                    EventType.ACHIEVEMENT_RECEIVED,
                    new Interval("2010-06-30T01:20/P1D"),
                    null,
                    null);

            assertEquals(List.of(), actualResult);
        }
    }
}