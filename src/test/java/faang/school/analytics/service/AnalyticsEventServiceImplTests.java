package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.enums.EventType;
import faang.school.analytics.exceptions.InvalidRequestException;
import faang.school.analytics.mapper.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.Interval;
import faang.school.analytics.parser.AnalyticsRequestParser;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

import static faang.school.analytics.constants.Constants.EVENT_NULL_EXCEPTION;
import static faang.school.analytics.constants.Constants.EVENT_TYPE_NULL_EXCEPTION;
import static faang.school.analytics.constants.Constants.FROM_OR_TO_NULL_EXCEPTION;
import static faang.school.analytics.constants.Constants.MISSING_DATE_PARAMS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTests {
    @Mock
    private AnalyticsEventRepository eventRepository;

    @Mock
    private AnalyticsRequestParser parser;

    @Spy
    private AnalyticsEventMapperImpl eventMapper = new AnalyticsEventMapperImpl();

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    private final long receiverId = 123L;
    private final EventType eventType = EventType.PROJECT_VIEW;
    private final String eventTypeRaw = "PROJECT_VIEW";
    private final String startRaw = "2025-01-01T00:00:00";
    private final String endRaw = "2025-12-31T23:59:59";
    private final LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
    private final LocalDateTime end = LocalDateTime.of(2025, 12, 31, 23, 59);

    private final AnalyticsEvent eventLastHour = AnalyticsEvent.builder()
            .receivedAt(LocalDateTime.now().minusHours(1))
            .build();
    private final AnalyticsEvent eventLastDay = AnalyticsEvent.builder()
            .receivedAt(LocalDateTime.now().minusDays(1))
            .build();
    private final AnalyticsEvent lastWeekEvent = AnalyticsEvent.builder()
            .receivedAt(LocalDateTime.now().minusDays(2))
            .build();
    private final AnalyticsEvent eventLastMonth = AnalyticsEvent.builder()
            .receivedAt(LocalDateTime.now().minusDays(15))
            .build();

    private AnalyticsEvent within;
    private AnalyticsEvent before;
    private AnalyticsEvent after;

    @BeforeEach
    void setUp() {
        within = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.of(2025, 4, 18, 16, 0))
                .actorId(1L)
                .build();

        before = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(start.minusDays(5))
                .actorId(2L)
                .build();

        after = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(end.plusDays(5))
                .actorId(3L)
                .build();
    }

    @Test
    public void testGetParseAnalytics_returnsOnlyEventsWithinRange() {
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseDate(startRaw)).thenReturn(start);
        when(parser.parseDate(endRaw)).thenReturn(end);

        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(within, before, after));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(receiverId, eventTypeRaw, null, startRaw,
                endRaw);

        assertThat(result).containsExactly(within);
    }

    @Test
    public void testGetParseAnalytics_returnsEmptyListWhenNoEventsInRange() {
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseDate(startRaw)).thenReturn(start);
        when(parser.parseDate(endRaw)).thenReturn(end);

        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(Stream.of(before, after));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(receiverId, eventTypeRaw, null, startRaw,
                endRaw);

        assertThat(result).isEmpty();
    }

    @Test
    public void testGetParseAnalytics_returnsMultipleEventsWithinRange() {
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseDate(startRaw)).thenReturn(start);
        when(parser.parseDate(endRaw)).thenReturn(end);

        AnalyticsEvent another = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(LocalDateTime.of(2025, 3, 15, 10, 0))
                .actorId(4L)
                .build();

        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(within, another, before, after));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(receiverId, eventTypeRaw, null, startRaw,
                endRaw);

        assertThat(result).containsExactlyInAnyOrder(within, another);
    }

    @Test
    public void testGetParseAnalytics_throwsWhenNoIntervalAndNoDates() {
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);

        assertThatThrownBy(() ->
                analyticsEventService.getParseAnalytics(receiverId, eventTypeRaw, null, null, null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(MISSING_DATE_PARAMS);
    }

    @Test
    public void testGetParseAnalytics_withInterval() {
        String intervalRaw = "LAST_MONTH";
        Interval interval = Interval.LAST_MONTH;

        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseInterval(intervalRaw)).thenReturn(interval);

        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime intervalStart = interval.getStartDate(zoneId);
        LocalDateTime intervalEnd = interval.getEndDate(zoneId);

        AnalyticsEvent event = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(intervalStart.plusHours(2))
                .actorId(1L)
                .build();

        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType)).thenReturn(Stream.of(event));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(receiverId, eventTypeRaw, intervalRaw, null, null);

        assertThat(result).containsExactly(event);
    }

    @Test
    public void saveEvent_throwsWhenEventIsNull() {
        assertThatThrownBy(() -> analyticsEventService.saveEvent(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EVENT_NULL_EXCEPTION);
    }

    @Test
    public void saveEvent_successfullySavesEvent() {
        AnalyticsEvent event = new AnalyticsEvent();
        analyticsEventService.saveEvent(event);
        verify(eventRepository).save(event);
    }

    @Test
    public void testGetAnalytics_throwsWhenEventTypeIsNull() {
        assertThatThrownBy(() -> analyticsEventService.getAnalytics(receiverId, null, null, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EVENT_TYPE_NULL_EXCEPTION);
    }

    @Test
    public void testGetAnalytics_throwsWhenFromOrToIsNull() {
        assertThatThrownBy(() ->
                analyticsEventService.getAnalytics(receiverId, eventType, null, null, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FROM_OR_TO_NULL_EXCEPTION);
    }

    @Test
    public void getAnalytics_filtersByLastHourInterval() {
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();
        AnalyticsEvent eventNow = AnalyticsEvent.builder().receivedAt(LocalDateTime.now()).build();
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventNow, eventLastDay));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, EventType.FOLLOWER, Interval.LAST_HOUR, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt).containsExactly(eventNow.getReceivedAt());
    }

    @Test
    public void getAnalytics_filtersByLastMonthInterval() {
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        AnalyticsEvent eventThreeMonthAgo = AnalyticsEvent.builder()
                .receivedAt(LocalDateTime.now().minusMonths(3))
                .build();

        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventThreeMonthAgo, eventLastMonth));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(1, EventType.FOLLOWER, Interval.LAST_MONTH, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt).containsExactly(eventLastMonth.getReceivedAt());
    }
}
