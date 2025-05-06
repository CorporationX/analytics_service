package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentEvent;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceImplTest {
    @Mock
    private AnalyticsEventRepository eventRepository;

    @Mock
    private AnalyticsRequestParser parser;

    @Spy
    private AnalyticsEventMapperImpl eventMapper = new AnalyticsEventMapperImpl();

    @InjectMocks
    private AnalyticsEventServiceImpl analyticsEventService;

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    private Clock fixedClock;
    private final long receiverId = 123L;
    private final String eventTypeRaw = "PROJECT_VIEW";
    private final EventType eventType = EventType.PROJECT_VIEW;
    private final String startRaw = "2025-01-01T00:00:00";
    private final String endRaw = "2025-12-31T23:59:59";
    private final LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
    private final LocalDateTime end = LocalDateTime.of(2025, 12, 31, 23, 59);
    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 5, 2, 22, 0);

    private AnalyticsEvent eventLastHour;
    private AnalyticsEvent eventLastDay;
    private AnalyticsEvent eventLastWeek;
    private AnalyticsEvent eventLastMonth;

    private AnalyticsEvent within;
    private AnalyticsEvent before;
    private AnalyticsEvent after;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        analyticsEventService = new AnalyticsEventServiceImpl(eventMapper, eventRepository, parser, fixedClock);

        eventLastHour = AnalyticsEvent.builder().receivedAt(fixedNow.minusMinutes(59)).build();
        eventLastDay = AnalyticsEvent.builder().receivedAt(fixedNow.minusHours(23)).build();
        eventLastWeek = AnalyticsEvent.builder().receivedAt(fixedNow.minusDays(5)).build();
        eventLastMonth = AnalyticsEvent.builder().receivedAt(fixedNow.minusDays(20)).build();


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
    public void testSaveCommentEvent() {
        CommentEvent commentEvent = new CommentEvent();

        analyticsEventService.saveCommentEvent(commentEvent);

        verify(eventRepository, times(1)).save(analyticsEventCaptor.capture());
        assertEquals(EventType.POST_COMMENT, analyticsEventCaptor.getValue().getEventType());
    }

    @Test
    public void testSaveEventThrowsWhenNull() {
        assertThatThrownBy(() -> analyticsEventService.saveEvent(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EVENT_NULL_EXCEPTION);
    }

    @Test
    public void testSaveEventSuccessfully() {
        AnalyticsEvent event = new AnalyticsEvent();
        analyticsEventService.saveEvent(event);
        verify(eventRepository).save(event);
    }

    @Test
    public void testGetParseAnalytics_returnsOnlyEventsWithinRange() {
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseDate(startRaw)).thenReturn(start);
        when(parser.parseDate(endRaw)).thenReturn(end);
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(within, before, after));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(
                receiverId, eventTypeRaw, null, startRaw, endRaw);

        assertThat(result).containsExactly(within);
    }

    @Test
    public void testGetParseAnalytics_returnsEmptyListWhenNoEventsInRange() {
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseDate(startRaw)).thenReturn(start);
        when(parser.parseDate(endRaw)).thenReturn(end);
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(before, after));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(
                receiverId, eventTypeRaw, null, startRaw, endRaw);

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

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(
                receiverId, eventTypeRaw, null, startRaw, endRaw);

        assertThat(result).containsExactlyInAnyOrder(within, another);
    }

    @Test
    public void testGetParseAnalytics_throwsWhenNoIntervalAndNoDates() {
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        assertThatThrownBy(() -> analyticsEventService.getParseAnalytics(
                receiverId, eventTypeRaw, null, null, null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(MISSING_DATE_PARAMS);
    }

    @Test
    public void testGetParseAnalytics_withInterval() {
        String intervalRaw = "LAST_MONTH";
        Interval interval = Interval.LAST_MONTH;
        when(parser.parseEventType(eventTypeRaw)).thenReturn(eventType);
        when(parser.parseInterval(intervalRaw)).thenReturn(interval);

        LocalDateTime intervalStart = interval.getStartDate(fixedClock);
        LocalDateTime intervalEnd = interval.getEndDate(fixedClock);

        AnalyticsEvent event = AnalyticsEvent.builder()
                .receiverId(receiverId)
                .eventType(eventType)
                .receivedAt(intervalStart.plusHours(2))
                .actorId(1L)
                .build();
        when(eventRepository.findByReceiverIdAndEventType(receiverId, eventType))
                .thenReturn(Stream.of(event));

        List<AnalyticsEvent> result = analyticsEventService.getParseAnalytics(
                receiverId, eventTypeRaw, intervalRaw, null, null);
        assertThat(result).containsExactly(event);
    }

    @Test
    public void testGetAnalytics_throwsWhenEventTypeIsNull() {
        assertThatThrownBy(() -> analyticsEventService.getAnalytics(
                receiverId, null, null, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EVENT_TYPE_NULL_EXCEPTION);
    }

    @Test
    public void testGetAnalytics_throwsWhenFromDateNull() {
        assertThatThrownBy(() -> analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, null, null, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FROM_OR_TO_NULL_EXCEPTION);
    }

    @Test
    public void testGetAnalytics_throwsWhenToDateNull() {
        assertThatThrownBy(() -> analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, null, LocalDateTime.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FROM_OR_TO_NULL_EXCEPTION);
    }

    @Test
    public void testGetAnalytics_filtersByLastHourInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastHour, eventLastDay));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.LAST_HOUR, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastHour.getReceivedAt());
    }

    @Test
    public void testGetAnalytics_filtersByLastDayInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastHour, eventLastDay));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.YESTERDAY, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastHour.getReceivedAt(), eventLastDay.getReceivedAt());
    }

    @Test
    public void testGetAnalytics_filtersByLastWeekInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastWeek, eventLastMonth));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.LAST_WEEK, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastWeek.getReceivedAt());
    }

    @Test
    public void testGetAnalytics_filtersByLastMonthInterval() {
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventLastMonth, eventLastWeek));
        when(eventMapper.toAnalyticsEventDtoList(any())).thenCallRealMethod();

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, Interval.LAST_MONTH, null, null);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventLastWeek.getReceivedAt(), eventLastMonth.getReceivedAt());
    }

    @Test
    public void testGetAnalyticsByDateFromAndToWithIntervalNull() {
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to = LocalDateTime.now().minusDays(1);
        AnalyticsEvent eventInRange = AnalyticsEvent.builder()
                .receivedAt(LocalDateTime.now().minusDays(2))
                .build();
        AnalyticsEvent eventOutOfRange = AnalyticsEvent.builder()
                .receivedAt(LocalDateTime.now().minusDays(5))
                .build();
        when(eventRepository.findByReceiverIdAndEventType(anyLong(), any()))
                .thenReturn(Stream.of(eventInRange, eventOutOfRange));

        List<AnalyticsEventDto> result = analyticsEventService.getAnalytics(
                receiverId, EventType.FOLLOWER, null, from, to);
        assertThat(result).extracting(AnalyticsEventDto::getReceivedAt)
                .containsExactly(eventInRange.getReceivedAt());
    }
}
